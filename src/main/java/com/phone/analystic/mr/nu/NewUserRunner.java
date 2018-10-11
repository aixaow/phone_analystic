package com.phone.analystic.mr.nu;

import com.phone.Util.JdbcUtil;
import com.phone.Util.TimeUtil;
import com.phone.analystic.modle.StatsUserDimension;
import com.phone.analystic.modle.base.DateDimension;
import com.phone.analystic.modle.value.map.TimeOutputValue;
import com.phone.analystic.modle.value.reduce.OutputWritable;
import com.phone.analystic.mr.OutputToMySqlFormat;
import com.phone.analystic.mr.service.IDimension;
import com.phone.analystic.mr.service.impl.IDimensionImpl;
import com.phone.common.DateEnum;
import com.phone.common.GlobalConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
;

/**
 * @author axiao
 * @date Create 11:14 2018/9/22 0022
 * @description:
 *
truncate dimension_browser;
truncate dimension_currency_type;
truncate dimension_date;
truncate dimension_event;
truncate dimension_inbound;
truncate dimension_kpi;
truncate dimension_location;
truncate dimension_os;
truncate dimension_payment_type;
truncate dimension_platform;
truncate event_info;
truncate order_info;
truncate stats_device_browser;
truncate stats_device_location;
truncate stats_event;
truncate stats_hourly;
truncate stats_inbound;
truncate stats_order;
truncate stats_user;
truncate stats_view_depth;
truncate member_info;
 */
public class NewUserRunner implements Tool {

    private static final Logger logger = Logger.getLogger(NewUserRunner.class);
    private Configuration conf = new Configuration();


    public static void main(String[] args) {
        try {
            ToolRunner.run(new Configuration(),new NewUserRunner(),args);
        } catch (Exception e) {
            logger.warn("执行new-user异常",e);
        }
    }

    @Override
    public void setConf(Configuration conf) {
        conf.addResource("output_mapping.xml");
        conf.addResource("output_writter.xml");
        conf.addResource("other_mapping.xml");

        this.conf = conf;
    }

    @Override
    public Configuration getConf() {
        return this.conf;
    }

    @Override
    public int run(String[] args) throws Exception {
        Configuration conf = this.getConf();
        //设置输入参数的时间
        this.handleArgs(conf,args);

        Job job = Job.getInstance(conf, "newUser to mysql");

        //设置运行的类
        job.setJarByClass(NewUserRunner.class);

        //设置map和输出
        job.setMapperClass(NewUserMapper.class);
        job.setMapOutputKeyClass(StatsUserDimension.class);
        job.setMapOutputValueClass(TimeOutputValue.class);

        //设置reduce和输出
        job.setReducerClass(NewUserReducer.class);
        job.setMapOutputKeyClass(StatsUserDimension.class);
        job.setOutputValueClass(OutputWritable.class);
        job.setNumReduceTasks(1);
        //输入文件设置
        handleInputOutput(job);

        //设置redcuce的输出格式类
        job.setOutputFormatClass(OutputToMySqlFormat.class);

//        return job.waitForCompletion(true) ? 1 : 0;
        if(job.waitForCompletion(true)){
            this.computeTotalNewUser(job);
            return 0;
        } else {
            return 1;
        }

    }


    /**
     * 计算新增的总用户
     * 1、获取运行日期当天和前一天的时间维度，并获取其对应的时间维度id，判断id是否大于0。
     * 2、根据时间维度的id获取前天的总用户和当天的新增用户。
     * 3、更新新增总用户
     * @param job
     */
    private void computeTotalNewUser(Job job) {

        /**
         *  1、根据运行当天获取日期
         *  2、获取日期和前一天的对应的时间维度
         *  3、根据时间维度获取对应的时间维度ID
         *  4、根据前天的时间维度Id获取前天的新增总用户，根据当天的时间维度Id获取当天的新增用户
         *  5、更新当天的新增总用户
         *  6、同一维度前一天？？
         */

        String date = job.getConfiguration().get(GlobalConstants.RUNNING_DATE);
        //获取当前的时间戳和前一天的时间戳
        long nowday = TimeUtil.parseString2Long(date);
        long yesterday = nowday - GlobalConstants.DAY_OF_MILLSECOND;

        //获取时间维度对象
        DateDimension nowDateDiemnsion = DateDimension.buildDate(nowday,DateEnum.DAY);
        DateDimension yesterdayDateDiemnsion = DateDimension.buildDate(yesterday,DateEnum.DAY);

        IDimension iDimension = new IDimensionImpl();
        //获取时间维度Id
        int nowDateDimensionId = -1;
        int yesterdayDateDimensionId = -1;


        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            nowDateDimensionId = iDimension.getDiemnsionIdByObject(nowDateDiemnsion);
            yesterdayDateDimensionId = iDimension.getDiemnsionIdByObject(yesterdayDateDiemnsion);

            conn = JdbcUtil.getConn();
            Map<String,Integer> map = new HashMap<String,Integer>();

            //浏览器模块--`stats_device_browser`------------------------------------------------------

            //开始判断维度Id是否正确
            if(nowDateDimensionId > 0){
                ps = conn.prepareStatement(conf.get("other_new_total_browser_user_now_sql"));
                ps.setInt(1,nowDateDimensionId);
                rs = ps.executeQuery();
                while (rs.next()) {
                    int platformId = rs.getInt("platform_dimension_id");
                    int browserId = rs.getInt("browser_dimension_id");
                    int newUsers = rs.getInt("new_install_users");
                    map.put(platformId+"_"+browserId,newUsers);
                }
            }

            //查询前一天的新增总用户
            if(yesterdayDateDimensionId > 0){
                ps = conn.prepareStatement(conf.get("other_new_total_browser_user_yesterday_sql"));
                ps.setInt(1,yesterdayDateDimensionId);
                rs = ps.executeQuery();
                while (rs.next()) {
                    int platformId = rs.getInt("platform_dimension_id");
                    int browserId = rs.getInt("browser_dimension_id");
                    int newTotalUsers = rs.getInt("total_install_users");
                    String key = platformId +"_"+browserId;
                    if(map.containsKey(key)){
                        newTotalUsers += map.get(key);
                    }
                    //存储
                    map.put(key,newTotalUsers);
                }
            }

            //更新
            if(map.size() > 0){
                for (Map.Entry<String,Integer> en:map.entrySet()){
                    ps = conn.prepareStatement(conf.get("other_new_total_browser_user_update_sql"));
                    //赋值
                    String[] fields = en.getKey().split("_");
                    ps.setInt(1,nowDateDimensionId);
                    //platformId
                    ps.setInt(2,Integer.parseInt(fields[0]));
                    //browserId
                    ps.setInt(3,Integer.parseInt(fields[1]));
                    //newUsers
                    ps.setInt(4,en.getValue());
                    ps.setString(5,conf.get(GlobalConstants.RUNNING_DATE));
                    //newTotalUsers
                    ps.setInt(6,en.getValue());
                    //执行更新
                    ps.execute();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JdbcUtil.close(conn,ps,rs);
        }

    }


    /**
     *解析获取的时间
     * @param conf
     * @param args
     */
    private void handleArgs(Configuration conf, String[] args) {
        String date = null;
        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("-d")) {
                    if (i + 1 <= args.length - 1) {
                        date = args[i + 1];
                        break;
                    }
                }
            }
            if (StringUtils.isEmpty(date)) {
                throw new RuntimeException("获取时间参数异常");
            } else {
                conf.set(GlobalConstants.RUNNING_DATE, date);
            }
        }
    }


    /**
     * 设置输入输出文件
     * @param job
     */
    private void handleInputOutput(Job job) {
        Configuration conf = job.getConfiguration();
        String[] strings = conf.get(GlobalConstants.RUNNING_DATE).split("-");
        String m = strings[1];
        String d = strings[2];
        try {
            FileSystem fs = FileSystem.get(conf);
            Path inpath = new Path("/ods/" + m + "/" + d);

            if (fs.exists(inpath)) {
                FileInputFormat.setInputPaths(job, inpath);
            } else {
                throw new RuntimeException("输入路径不存储在.inpath:" + inpath.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
