package com.phone.analystic.mr.nu;

import com.phone.analystic.modle.StatsBaseDimension;
import com.phone.analystic.modle.StatsUserDimension;
import com.phone.analystic.modle.value.StatsOutpuValue;
import com.phone.analystic.modle.value.reduce.OutputWritable;
import com.phone.analystic.mr.IOutputWritter;
import com.phone.analystic.mr.service.IDimension;
import com.phone.common.GlobalConstants;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;

import java.sql.PreparedStatement;

/**
 * @author axiao
 * @date Create 16:49 2018/9/25 0025
 * @description:  浏览器模块下的新增用户
 */
public class BrowserNewUserOutputWritter implements IOutputWritter {
    @Override
    public void output(Configuration conf, StatsBaseDimension key,
                      StatsOutpuValue value, PreparedStatement ps,
                      IDimension iDimension) {
        try {
            StatsUserDimension k = (StatsUserDimension) key;
            OutputWritable v = (OutputWritable) value;
            //获取新增用户的值
            int newUser = ((IntWritable)(v.getValue().get(new IntWritable(-1)))).get();

            int i = 0;
            ps.setInt(++i,iDimension.getDiemnsionIdByObject(k.getStatsCommonDimension().getDateDimension()));
            ps.setInt(++i,iDimension.getDiemnsionIdByObject(k.getStatsCommonDimension().getPlatformDimension()));
            ps.setInt(++i,iDimension.getDiemnsionIdByObject(k.getBrowserDimension()));
            ps.setInt(++i,newUser);
            ps.setString(++i,conf.get(GlobalConstants.RUNNING_DATE));
            ps.setInt(++i,newUser);

            ps.addBatch(); //添加到批处理中
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
