package com.phone.analystic.mr.pv;

import com.phone.analystic.modle.StatsUserDimension;
import com.phone.analystic.modle.base.KpiDimension;
import com.phone.analystic.modle.value.map.TimeOutputValue;
import com.phone.analystic.mr.nu.NewUserMapper;
import com.phone.common.KpiType;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * @author axiao
 * @date Create 16:32 2018/10/10 0010
 * @description:
 */
public class PVMapper extends Mapper<LongWritable,Text,StatsUserDimension,TimeOutputValue> {
    private static final Logger logger = Logger.getLogger(NewUserMapper.class);
    private StatsUserDimension k = new StatsUserDimension();
    private TimeOutputValue v = new TimeOutputValue();
    private KpiDimension pvKpi = new KpiDimension(KpiType.PAGEVIEW.kpiName);
    private KpiDimension browserPVKpi = new KpiDimension(KpiType.BROWSER_PAGEVIEW.kpiName);

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        if(StringUtils.isEmpty(line)){
            return;
        }
        //拆分
        String[] fields = line.split("\u0001");
        //获取事件的类型
        String en = fields[2];
        //访问时间
        String serverTime = fields[1];
        String url = fields[10];
        //平台信息
        String platform = fields[13];
        //浏览器信息
        String browserName = fields[24];
        String browserVersion = fields[25];

    }
}
