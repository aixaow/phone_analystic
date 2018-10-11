package com.phone.analystic.mr.am;

import com.phone.analystic.modle.StatsCommonDimension;
import com.phone.analystic.modle.StatsUserDimension;
import com.phone.analystic.modle.base.BrowserDimension;
import com.phone.analystic.modle.base.DateDimension;
import com.phone.analystic.modle.base.KpiDimension;
import com.phone.analystic.modle.base.PlatformDimension;
import com.phone.analystic.modle.value.map.TimeOutputValue;
import com.phone.analystic.mr.nu.NewUserMapper;
import com.phone.common.Constants;
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
 * @date Create 19:14 2018/9/26 0026
 * @description: 活跃会员统计是通过统计登录网站的用户数量(去重)
 *
 *
 * 活跃会员(active_member)计算规则：
 * 计算当天(确定时间维度信息)的pageview事件的数据中memberid的去重个数。
 * (这里之所以选择pageview事件，是可能会存在一种可能：
 * 某个会员在当天没有进行任何操作，但是他订单支付成功的操作在今天在被触发，
 * 这样在所有数据中就会出现一个java_server平台产生的订单支付成功事件，包含会员id)。
 *
 * 最终数据保存：stats_user和stats_device_browser。
 * 涉及到的列(除了维度列和created列外)：active_members。
 * 涉及到其他表有dimension_platform、dimension_date、dimension_browser。
 *
 */
public class AcitveMemberMapper extends Mapper<LongWritable,Text,StatsUserDimension,TimeOutputValue> {
    private static final Logger logger = Logger.getLogger(NewUserMapper.class);
    private StatsUserDimension k = new StatsUserDimension();
    private TimeOutputValue v = new TimeOutputValue();
    private KpiDimension activeMemberKpi = new KpiDimension(KpiType.ACTIVE_MEMBER.kpiName);
    private KpiDimension activeBrowserMemberKpi = new KpiDimension(KpiType.BROWSER_ACTIVE_MEMBER.kpiName);

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
        //时间类型不为空，并且是pv事件
        if(StringUtils.isNotEmpty(en) && en.equals(Constants.EventEnum.PAGEVIEW.alias)){
            //获取想要的字段
            //访问时间
            String serverTime = fields[1];
            //平台信息
            String platform = fields[13];
            String memberId = fields[4];
            //浏览器信息
            String browserName = fields[24];
            String browserVersion = fields[25];

            if(StringUtils.isEmpty(serverTime) || StringUtils.isEmpty(memberId)){
                logger.info("serverTime & uuid is null.serverTime:"+serverTime+". uuid:"+memberId);
                return;
            }

            //构造输出的key
            long stime = Long.valueOf(serverTime);
            //获取平台对象实例
            PlatformDimension platformDimension = PlatformDimension.getInstance(platform);
            //根据指标类型  返回DateDimension--日新增用户信息
            DateDimension dateDimension = DateDimension.buildDate(stime, DateEnum.DAY);
            //获取浏览器对象实例
            BrowserDimension browserDimension = BrowserDimension.getInstance(browserName,browserVersion);

            //获取公共维度对象实例，公共维度---时间、平台、kpi
            StatsCommonDimension statsCommonDimension = this.k.getStatsCommonDimension();

            //为statsCommonDimension设置
            statsCommonDimension.setDateDimension(dateDimension);
            statsCommonDimension.setPlatformDimension(platformDimension);
            //指标类型为新增用户
            statsCommonDimension.setKpiDimension(activeMemberKpi);

            //设置默认的浏览器对象
            BrowserDimension defaultBrowserDimension = new BrowserDimension("","");
            this.k.setBrowserDimension(defaultBrowserDimension);
            this.k.setStatsCommonDimension(statsCommonDimension);

            //构建输出的value
            this.v.setId(memberId);
            //输出
//            context.write(this.k,this.v);


            //以下输出的数据用于计算浏览器模块下的新增用户（map端可以设置多个输出）
            statsCommonDimension.setKpiDimension(activeBrowserMemberKpi);
            this.k.setBrowserDimension(browserDimension);
            this.k.setStatsCommonDimension(statsCommonDimension);

            context.write(this.k,this.v);

        }
    }
}

