package com.phone.analystic.mr.nu;

import com.phone.analystic.modle.StatsUserDimension;
import com.phone.analystic.modle.value.map.TimeOutputValue;
import com.phone.analystic.modle.value.reduce.OutputWritable;
import com.phone.analystic.mr.OutputToMySqlFormat;
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

/**
 * @author axiao
 * @date Create 11:14 2018/9/22 0022
 * @description:
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

        return job.waitForCompletion(true) ? 1 : 0;
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
