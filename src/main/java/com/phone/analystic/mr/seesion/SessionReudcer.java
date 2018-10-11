package com.phone.analystic.mr.seesion;

import com.phone.analystic.modle.StatsUserDimension;
import com.phone.analystic.modle.value.map.TimeOutputValue;
import com.phone.analystic.modle.value.reduce.OutputWritable;
import com.phone.common.GlobalConstants;
import com.phone.common.KpiType;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.*;

/**
 * @author axiao
 * @date Create 8:30 2018/9/28 0028
 * @description:
 */
public class SessionReudcer extends Reducer<StatsUserDimension, TimeOutputValue,
        StatsUserDimension, OutputWritable> {
    private static final Logger logger = Logger.getLogger(SessionReudcer.class);
    private OutputWritable v = new OutputWritable();
    private Map<String, List<Long>> map = new HashMap<String, List<Long>>();

    @Override
    protected void reduce(StatsUserDimension key, Iterable<TimeOutputValue> values, Context context) throws IOException, InterruptedException {

        logger.info("reducer=============="+key.toString());
        //清空map
        this.map.clear();
        //循环map阶段传过来的value
        for (TimeOutputValue tv : values) {
            String sessionId = tv.getId();
            long serverTime = tv.getTime();
            //存储时间
            if (map.containsKey(tv.getId())) {
                List<Long> li = map.get(sessionId);
                li.add(serverTime);
                map.put(sessionId, li);
            } else {
                List<Long> li = new ArrayList<Long>();
                li.add(serverTime);
                map.put(sessionId, li);
            }
        }

        //构建输出的value
        MapWritable mapWritable = new MapWritable();
        mapWritable.put(new IntWritable(-1), new IntWritable(this.map.size()));
        //session的时长
        int sessionLength = 0;
        for (Map.Entry<String, List<Long>> en : map.entrySet()) {
            //确保至少有两个会话时间（开始结束）
            if (en.getValue().size() >= 2) {
                //按照时间长短升序排列
                Collections.sort(en.getValue());
                //会话长度等于最后一个会话的时间-最初会话时间
                sessionLength += (en.getValue().get(en.getValue().size() - 1) - en.getValue().get(0));
            }
        }


        if (sessionLength > 0 && sessionLength <= GlobalConstants.DAY_OF_MILLSECOND) {
            //不足一秒算一秒
            if (sessionLength % 1000 == 0) {
                sessionLength = sessionLength / 1000;
            } else {
                sessionLength = sessionLength / 1000 + 1;
            }
        }
        mapWritable.put(new IntWritable(-2), new IntWritable(sessionLength));
        this.v.setValue(mapWritable);
        //还需要设置kpi--从key中传过来的kpi
        this.v.setKpi(KpiType.valueOfKpiName(key.getStatsCommonDimension().getKpiDimension().getKpiName()));
        //输出即可
        context.write(key, this.v);
    }
}
