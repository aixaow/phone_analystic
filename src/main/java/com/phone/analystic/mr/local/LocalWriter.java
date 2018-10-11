package com.phone.analystic.mr.local;

import com.phone.analystic.modle.StatsBaseDimension;
import com.phone.analystic.modle.StatsLocationDimension;
import com.phone.analystic.modle.value.StatsOutpuValue;
import com.phone.analystic.modle.value.reduce.LocationReduceOutput;
import com.phone.analystic.mr.IOutputWritter;
import com.phone.analystic.mr.service.IDimension;
import com.phone.common.GlobalConstants;
import org.apache.hadoop.conf.Configuration;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author axiao
 * @date Create 8:20 2018/9/28 0028
 * @description:
 */
public class LocalWriter implements IOutputWritter {
    @Override
    public void output(Configuration conf, StatsBaseDimension key, StatsOutpuValue value, PreparedStatement ps, IDimension iDimension) {
        try {
            StatsLocationDimension statsLocationDimension  = (StatsLocationDimension) key;
            LocationReduceOutput locationReduceOutput = (LocationReduceOutput) value;
            //为ps赋值
            int i = 0;
            ps.setInt(++i,iDimension.getDiemnsionIdByObject(statsLocationDimension.getStatsCommonDimension().getDateDimension()));
            ps.setInt(++i,iDimension.getDiemnsionIdByObject(statsLocationDimension.getStatsCommonDimension().getPlatformDimension()));
            ps.setInt(++i,iDimension.getDiemnsionIdByObject(statsLocationDimension.getLocationDimension()));
            ps.setInt(++i,locationReduceOutput.getAus());
            ps.setInt(++i,locationReduceOutput.getSessions());
            ps.setInt(++i,locationReduceOutput.getBounce_sessions());
            ps.setString(++i,conf.get(GlobalConstants.RUNNING_DATE));
            ps.setInt(++i,locationReduceOutput.getAus());
            ps.setInt(++i,locationReduceOutput.getSessions());
            ps.setInt(++i,locationReduceOutput.getBounce_sessions());

            //添加到批处理中
            ps.addBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
