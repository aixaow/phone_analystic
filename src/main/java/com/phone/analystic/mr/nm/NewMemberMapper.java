package com.phone.analystic.mr.nm;

import com.phone.Util.JdbcUtil;
import com.phone.Util.MemberUtil;
import com.phone.analystic.modle.StatsCommonDimension;
import com.phone.analystic.modle.StatsUserDimension;
import com.phone.analystic.modle.base.BrowserDimension;
import com.phone.analystic.modle.base.DateDimension;
import com.phone.analystic.modle.base.KpiDimension;
import com.phone.analystic.modle.base.PlatformDimension;
import com.phone.analystic.modle.value.map.TimeOutputValue;
import com.phone.common.DateEnum;
import com.phone.common.KpiType;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;

/**
 * @author axiao
 * @date Create 20:47 2018/9/26 0026
 * @description:
 */
public class NewMemberMapper extends Mapper<LongWritable,Text,StatsUserDimension,TimeOutputValue> {
    private static final Logger logger = Logger.getLogger(NewMemberMapper.class);
    private StatsUserDimension k = new StatsUserDimension();
    private TimeOutputValue v = new TimeOutputValue();

    private KpiDimension newMemberKpi = new KpiDimension(KpiType.NEW_MEMBER.kpiName);
    private KpiDimension newBrowserMemberKpi = new KpiDimension(KpiType.BROWSER_NEW_MEMBER.kpiName);
    private Connection conn = null;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        //如果程序中间报错导致数据没有完全添加成功，为了避免数据出错，要先进行一次清空
        //为了删除member_info中的数据
        MemberUtil.deleteByDay(context.getConfiguration());
        conn = JdbcUtil.getConn();
    }

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
        String memberId = fields[4];
        String browserName = fields[24];
        String browserVersion = fields[25];

        if (StringUtils.isEmpty(serverTime) || StringUtils.isEmpty(memberId)) {
            logger.info("serverTime & memberId is null serverTime:" + serverTime + ".memberId" + memberId);
            return;
        }

        //判断是不是为新增会员
        if(!MemberUtil.isNewMember(memberId,conn,context.getConfiguration())){
            logger.info("该会员是一个老会员.memberId:"+memberId);
            return;
        }

        //构造输出的key
        long stime = Long.valueOf(serverTime);
        PlatformDimension platformDimension = PlatformDimension.getInstance(platform);
        DateDimension dateDimension = DateDimension.buildDate(stime, DateEnum.DAY);
        StatsCommonDimension statsCommonDimension = this.k.getStatsCommonDimension();
        //为StatsCommonDimension设值
        statsCommonDimension.setDateDimension(dateDimension);
        statsCommonDimension.setPlatformDimension(platformDimension);

        //用户模块新增用户
        //设置默认的浏览器对象(因为新增用户指标并不需要浏览器维度，所以赋值为空)
        BrowserDimension defaultBrowserDimension = new BrowserDimension("", "");
        statsCommonDimension.setKpiDimension(newMemberKpi);
        this.k.setBrowserDimension(defaultBrowserDimension);
        this.k.setStatsCommonDimension(statsCommonDimension);
        this.v.setId(memberId);
        this.v.setTime(stime);
        context.write(this.k, this.v);//输出

        //浏览器模块新增用户
        statsCommonDimension.setKpiDimension(newBrowserMemberKpi);
        BrowserDimension browserDimension = new BrowserDimension(browserName, browserVersion);
        this.k.setBrowserDimension(browserDimension);
        this.k.setStatsCommonDimension(statsCommonDimension);
        context.write(this.k, this.v);//输出
    }
}
