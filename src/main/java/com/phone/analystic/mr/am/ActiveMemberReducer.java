package com.phone.analystic.mr.am;

import com.phone.analystic.modle.StatsUserDimension;
import com.phone.analystic.modle.value.map.TimeOutputValue;
import com.phone.analystic.modle.value.reduce.OutputWritable;
import com.phone.analystic.mr.nu.NewUserReducer;
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
 * @date Create 19:38 2018/9/26 0026
 * @description:
 */
public class ActiveMemberReducer extends Reducer<StatsUserDimension,TimeOutputValue,
        StatsUserDimension,OutputWritable> {
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
        System.out.println("-----------");
        for (TimeOutputValue tv: values) {
            this.unique.add(tv.getId());
            System.out.println(tv.getId());
        }
        //构造输出的v
        //首先判断传过来的kpi指标名称是否是活跃会员的指标名称
        this.v.setKpi(KpiType.valueOfKpiName(key.getStatsCommonDimension().getKpiDimension().getKpiName()));

        this.map.put(new IntWritable(-1),new IntWritable(this.unique.size()));
        this.v.setValue(this.map);
        //输出
        context.write(key,this.v);
        this.unique.clear();//清空操作

    }
}

