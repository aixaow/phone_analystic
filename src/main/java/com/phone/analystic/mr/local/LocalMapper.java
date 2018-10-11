package com.phone.analystic.mr.local;

import com.phone.analystic.modle.StatsCommonDimension;
import com.phone.analystic.modle.StatsLocationDimension;
import com.phone.analystic.modle.base.DateDimension;
import com.phone.analystic.modle.base.KpiDimension;
import com.phone.analystic.modle.base.LocationDimension;
import com.phone.analystic.modle.base.PlatformDimension;
import com.phone.analystic.modle.value.map.LocationOutputValue;
import com.phone.common.DateEnum;
import com.phone.common.KpiType;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * @author axiao
 * @date Create 8:13 2018/9/28 0028
 * @description:
 */
public class LocalMapper extends Mapper<LongWritable,Text,StatsLocationDimension,LocationOutputValue> {

    private static final Logger logger = Logger.getLogger(LocalMapper.class);

    private StatsLocationDimension k = new StatsLocationDimension();
    private LocationOutputValue v = new LocationOutputValue();
    private KpiDimension localKpi = new KpiDimension(KpiType.LOCAL.kpiName);
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        if(StringUtils.isEmpty(line)){
            return ;
        }

        //拆分
        String[] fields = line.split("\u0001");
        //en是事件名称
        String en = fields[2];
        //获取想要的字段
        String serverTime = fields[1];
        String platform = fields[13];
        String uuid = fields[3];
        String sid = fields[5];
        String country = fields[28];
        String province = fields[29];
        String city = fields[30];

        if(StringUtils.isEmpty(serverTime)){
            logger.info("serverTime & uuid is null serverTime:"+serverTime+".uuid"+uuid);
            return;
        }


        //构造输出的value
        long longOfServerTime = Long.valueOf(serverTime);
        this.v.setUid(uuid);
        this.v.setSid(sid);

        //构造输出的key
        long stime = Long.valueOf(serverTime);
        PlatformDimension platformDimension = PlatformDimension.getInstance(platform);
        DateDimension dateDimension = DateDimension.buildDate(stime, DateEnum.DAY);
        LocationDimension locationDimension = LocationDimension.getInstance(country,province,city);
        StatsCommonDimension statsCommonDimension = this.k.getStatsCommonDimension();
        //为StatsCommonDimension设值
        statsCommonDimension.setDateDimension(dateDimension);
        statsCommonDimension.setPlatformDimension(platformDimension);

        statsCommonDimension.setKpiDimension(localKpi);
        this.k.setLocationDimension(locationDimension);
        this.k.setStatsCommonDimension(statsCommonDimension);

        context.write(this.k,this.v);//输出
    }
}
