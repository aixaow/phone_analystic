package com.phone.analystic.mr.seesion;

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

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author axiao
 * @date Create 8:31 2018/9/28 0028
 * @description:
 */
public class SessionOutputWritter implements IOutputWritter {

    @Override
    public void output(Configuration conf, StatsBaseDimension key, StatsOutpuValue value, PreparedStatement ps, IDimension iDimension) {
        try {
            StatsUserDimension statsUserDimension = (StatsUserDimension) key;
            OutputWritable v = (OutputWritable) value;
            int sessions = ((IntWritable) ((OutputWritable) value).
                    getValue().get(new IntWritable(-1))).get();
            int sessionsLength = ((IntWritable) ((OutputWritable) value).
                    getValue().get(new IntWritable(-2))).get();

            //为ps赋值
            int i = 0;
            ps.setInt(++i, iDimension.getDiemnsionIdByObject(statsUserDimension.getStatsCommonDimension().getDateDimension()));
            ps.setInt(++i, iDimension.getDiemnsionIdByObject(statsUserDimension.getStatsCommonDimension().getPlatformDimension()));
            if (v.getKpi().equals(KpiType.BROWSER_SESSION)) {
                ps.setInt(++i, iDimension.getDiemnsionIdByObject(statsUserDimension.getBrowserDimension()));
            }
            ps.setInt(++i, sessions);
            ps.setInt(++i, sessionsLength);
            ps.setString(++i, conf.get(GlobalConstants.RUNNING_DATE));
            ps.setInt(++i, sessions);
            ps.setInt(++i, sessionsLength);

            //添加到批处理中
            ps.addBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

