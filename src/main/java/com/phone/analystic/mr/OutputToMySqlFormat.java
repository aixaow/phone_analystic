package com.phone.analystic.mr;

import com.phone.Util.JdbcUtil;
import com.phone.analystic.modle.StatsBaseDimension;
import com.phone.analystic.modle.value.reduce.OutputWritable;
import com.phone.analystic.mr.nm.NewMemberRunner;
import com.phone.analystic.mr.service.IDimension;
import com.phone.analystic.mr.service.impl.IDimensionImpl;
import com.phone.common.KpiType;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.output.FileOutputCommitter;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author axiao
 * @date Create 11:14 2018/9/22 0022
 * @description: 将结果输出到mysql的自定义类
 */
public class OutputToMySqlFormat extends OutputFormat<StatsBaseDimension,OutputWritable> {
    private static final Logger logger = Logger.getLogger(NewMemberRunner.class);
    /**
     * 获取输出记录--核心方法
     *
     * @param taskAttemptContext
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public RecordWriter<StatsBaseDimension, OutputWritable> getRecordWriter(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        Connection conn = JdbcUtil.getConn();
        Configuration conf = taskAttemptContext.getConfiguration();
        IDimension iDimension = new IDimensionImpl();
        return new OutputToMysqlRecordWritter(conf, conn, iDimension);
    }

    @Override
    public void checkOutputSpecs(JobContext jobContext) throws IOException, InterruptedException {
        //检测输出空间--不需要输出到磁盘，所以这里不用检测了
    }

    @Override
    public OutputCommitter getOutputCommitter(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
       //我们的输出没有path，
        return new FileOutputCommitter(null, taskAttemptContext);
    }


    /**
     * 用于封装写出记录到mysql的信息
     */
    public static class OutputToMysqlRecordWritter extends RecordWriter<StatsBaseDimension, OutputWritable> {
        Configuration conf = null;
        Connection conn = null;
        IDimension iDimension = null;
        /**
         * 存储kpi-ps
         */
        private Map<KpiType, PreparedStatement> map = new HashMap<KpiType, PreparedStatement>();
        /**
         * 存储kpi-对应的输出sql
         */
        private Map<KpiType, Integer> batch = new HashMap<KpiType, Integer>();

        public OutputToMysqlRecordWritter(Configuration conf, Connection conn, IDimension iDimension) {
            this.conf = conf;
            this.conn = conn;
            this.iDimension = iDimension;
        }

        /**
         * 写--执行一次reduce，执行一次write
         * @param key
         * @param value
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        public void write(StatsBaseDimension key, OutputWritable value) throws IOException, InterruptedException {
            //获取kpi
            KpiType kpi = value.getKpi();
            PreparedStatement ps = null;
            try {
                //获取ps
                if (map.containsKey(kpi)) {
                    ps = map.get(kpi);
                } else {
                    ps = conn.prepareStatement(conf.get(kpi.kpiName));
                    //将新增加的ps存储到map中
                    map.put(kpi, ps);
                }
                //有问题
                int count = 1;
                this.batch.put(kpi, count);
                count++;

                //为ps赋值准备--对应的是output_writter.xml
                String calssName = conf.get("writter_" + kpi.kpiName);
                //com.phone.analystic.mr.nu.NewUserOutputWritter
                //将包名+类名转换成类
                Class<?> classz = Class.forName(calssName);
                IOutputWritter writter = (IOutputWritter) classz.newInstance();
                //调用IOutputWritter中的output方法
                writter.output(conf, key, value, ps, iDimension);

                //对赋值好的ps进行执行
                //有50个ps执行
                if (batch.size() % 50 == 0) {
                    //批量执行
                    ps.executeBatch();
                    //提交批处理执行
//                    this.conn.commit();
                    //将执行完的ps移除掉
                    batch.remove(kpi);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        /**
         * @param taskAttemptContext
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        public void close(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
            try {
                for (Map.Entry<KpiType, PreparedStatement> en : map.entrySet()) {
                    //将剩余的ps进行执行--也就是上面的批处理完之后，数据不足50条了，上面不执行了，在这里进行处理
                    en.getValue().executeBatch();
                    //已经设置自动提交了  这里不用再执行了 否则就会报错
//                    this.conn.commit();
                }
            } catch (SQLException e) {
                logger.warn("close异常",e);
            } finally {
                for (Map.Entry<KpiType, PreparedStatement> en : map.entrySet()) {
                    //关闭所有能关闭的资源
                    JdbcUtil.close(conn, en.getValue(), null);
                }
            }
        }
    }
}

