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
 * @date Create 11:13 2018/9/22 0022
 * @description: 继承为每一个kpi的最终结果赋值的接口
 */
public class NewUserOutputWritter implements IOutputWritter {
    @Override
    public void ouput(Configuration conf, StatsBaseDimension key,
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
            ps.setInt(++i,newUser);
            ps.setString(++i,conf.get(GlobalConstants.RUNNING_DATE));
            ps.setInt(++i,newUser);

            ps.addBatch(); //添加到批处理中
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
