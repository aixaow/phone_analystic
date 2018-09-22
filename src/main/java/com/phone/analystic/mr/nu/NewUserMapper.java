package com.phone.analystic.mr.nu;

import com.phone.analystic.modle.StatsCommonDimension;
import com.phone.analystic.modle.StatsUserDimension;
import com.phone.analystic.modle.base.BrowserDimension;
import com.phone.analystic.modle.base.DateDimension;
import com.phone.analystic.modle.base.KpiDimension;
import com.phone.analystic.modle.base.PlatformDimension;
import com.phone.analystic.modle.value.map.TimeOutputValue;
import com.phone.common.Constants;
import com.phone.common.DateEnum;
import com.phone.common.KpiType;
import com.phone.etl.mr.EtlToHdfsMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * @author axiao
 * @date Create 22:45 2018/9/19 0019
 * @description:
 */
public class NewUserMapper extends Mapper<LongWritable,Text,StatsUserDimension,TimeOutputValue> {
    private static final Logger logger = Logger.getLogger(NewUserMapper.class);
    private StatsUserDimension k = new StatsUserDimension();
    private TimeOutputValue v = new TimeOutputValue();
    private KpiDimension newUserKpi = new KpiDimension(KpiType.NEW_USER.kpiName);
    private KpiDimension browserNewUserKpi = new KpiDimension(KpiType.BROWSER_NEW_USER.kpiName);


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
        //时间类型不为空，并且是新增用户事件--e_l
        if(StringUtils.isNotEmpty(en) && en.equals(Constants.EventEnum.LANUCH.alias)){
            //获取想要的字段
            //访问时间
            String serverTime = fields[1];
            //平台信息
            String platform = fields[13];
            String uuid = fields[3];

            if(StringUtils.isEmpty(serverTime) || StringUtils.isEmpty(uuid)){
                logger.info("serverTime & uuid is null.serverTime:"+serverTime+". uuid:"+uuid);
                return;
            }

            //构造输出的key
            long stime = Long.valueOf(serverTime);
            //获取平台对象实例
            PlatformDimension platformDimension = PlatformDimension.getInstance(platform);
            //根据指标类型  返回DateDimension--日新增用户信息
            DateDimension dateDimension = DateDimension.buildDate(stime, DateEnum.DAY);
            //获取公共维度对象实例，公共维度---时间、平台、kpi
            StatsCommonDimension statsCommonDimension = this.k.getStatsCommonDimension();

            //为statsCommonDimension设置
            statsCommonDimension.setDateDimension(dateDimension);
            statsCommonDimension.setPlatformDimension(platformDimension);
            //指标类型为新增用户
            statsCommonDimension.setKpiDimension(newUserKpi);

            //设置默认的浏览器对象
            BrowserDimension defaultBrowserDimension = new BrowserDimension("","");
            this.k.setBrowserDimension(defaultBrowserDimension);
            this.k.setStatsCommonDimension(statsCommonDimension);

            //构建输出的value
            this.v.setId(uuid);
            //输出
            context.write(this.k,this.v);

        }
    }
}