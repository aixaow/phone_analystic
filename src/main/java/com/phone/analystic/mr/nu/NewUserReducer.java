package com.phone.analystic.mr.nu;

import com.phone.analystic.modle.StatsUserDimension;
import com.phone.analystic.modle.value.map.TimeOutputValue;
import com.phone.analystic.modle.value.reduce.OutputWritable;
import com.phone.common.KpiType;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author axiao
 * @date Create 18:02 2018/9/20 0020
 * @description:
 */
public class NewUserReducer extends Reducer<StatsUserDimension,TimeOutputValue,
        StatsUserDimension,OutputWritable> {

    //2018-09-20 ios IE 8.0 new_user 3688
    //2018-09-20 ios IE 9.0 new_user 12
    //2018-09-20 ios IE 8.0 houryly_user 12
    //2018-09-20 ios IE 9.0 houryly_user 500


    private static final Logger logger = Logger.getLogger(NewUserReducer.class);
    private OutputWritable v = new OutputWritable();
    /**
     * 用于去重uuid -- set一般用来去重
     */
    private Set unique = new HashSet();
    private MapWritable map = new MapWritable();

    @Override
    protected void reduce(StatsUserDimension key, Iterable<TimeOutputValue> values, Context context) throws IOException, InterruptedException {
        //清空map--reduce是一个key对应执行一次reduce，不清空，会包含上一次reduce的执行结果，统计就会出错
        map.clear();
        //循环--去重
        for (TimeOutputValue tv: values) {
            this.unique.add(tv.getId());
        }
        //构造输出的v
//        this.v.setKpi(KpiType.valueOfKpiName(key.getStatsCommonDimension().getKpiDimension().getKpiName()));
        //首先判断传过来的kpi指标名称是否是新增用户的指标名称
        if(key.getStatsCommonDimension().getKpiDimension().getKpiName().equals(KpiType.NEW_USER.kpiName)){
            this.v.setKpi(KpiType.NEW_USER);
        }
        //将map的key都设置为-1，只是一个标志，代表给sql赋值的时候，知道这是新增用户的信息
        this.map.put(new IntWritable(-1),new IntWritable(this.unique.size()));
        this.v.setValue(this.map);
        //输出
        context.write(key,this.v);
    }
}



