package com.phone.analystic.mr.au;

import com.phone.analystic.modle.StatsBaseDimension;
import com.phone.analystic.modle.StatsUserDimension;
import com.phone.analystic.modle.value.StatsOutpuValue;
import com.phone.analystic.modle.value.reduce.OutputWritable;
import com.phone.analystic.mr.IOutputWritter;
import com.phone.analystic.mr.service.IDimension;
import com.phone.common.GlobalConstants;
import com.phone.common.KpiType;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;

/**
 * @author axiao
 * @date Create 17:11 2018/9/26 0026
 * @description:
 */
public class ActiveUserOutputWritter implements IOutputWritter {
    private static final Logger logger = Logger.getLogger(ActiveUserOutputWritter.class);
    @Override
    //这里通过key和value给ps语句赋值
    public void output(Configuration conf, StatsBaseDimension key, StatsOutpuValue value, PreparedStatement ps, IDimension iDimension) {

        try {
            StatsUserDimension k = (StatsUserDimension) key;
            OutputWritable v = (OutputWritable) value;

            int i = 0;

            switch (v.getKpi()){
                case ACTIVE_USER:
                case BROWSER_ACTIVE_USER:
                    //获取活跃用户的值
                    int newUser = ((IntWritable)(v.getValue().get(new IntWritable(-1)))).get();

                    ps.setInt(++i,iDimension.getDiemnsionIdByObject(k.getStatsCommonDimension().getDateDimension()));
                    ps.setInt(++i,iDimension.getDiemnsionIdByObject(k.getStatsCommonDimension().getPlatformDimension()));
                    if(v.getKpi().equals(KpiType.BROWSER_ACTIVE_USER)){
                        ps.setInt(++i,iDimension.getDiemnsionIdByObject(k.getBrowserDimension()));
                    }
                    ps.setInt(++i,newUser);
                    //注意这里需要在runner类里面进行赋值
                    ps.setString(++i,conf.get(GlobalConstants.RUNNING_DATE));
                    ps.setInt(++i,newUser);
                case HOURLY_ACTIVE_USER:
                    ps.setInt(++i,iDimension.getDiemnsionIdByObject(k.getStatsCommonDimension().getDateDimension()));
                    ps.setInt(++i,iDimension.getDiemnsionIdByObject(k.getStatsCommonDimension().getPlatformDimension()));
                    ps.setInt(++i,iDimension.getDiemnsionIdByObject(k.getStatsCommonDimension().getKpiDimension()));

                    for (int j = 0;j<24;j++){
                        ps.setInt(++i,((IntWritable)(v.getValue().get(new IntWritable(j)))).get());
                    }
                    ps.setString(++i,conf.get(GlobalConstants.RUNNING_DATE));
                    for (int j = 0;j<24;j++){
                        ps.setInt(++i,((IntWritable)(v.getValue().get(new IntWritable(j)))).get());
                    }
                    break;
                default:break;
            }


            ps.addBatch();//添加到批处理中，批量执行SQL语句
        } catch (Exception e) {
            logger.warn("给ps赋值失败！！！",e);
        }
    }
}
