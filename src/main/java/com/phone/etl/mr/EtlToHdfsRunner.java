package com.phone.etl.mr;

import com.phone.Util.TimeUtil;
import com.phone.etl.ip.LogUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;


import java.io.IOException;
import java.util.Map;

import static com.phone.common.GlobalConstants.RUNNING_DATE;

/**
 * @author axiao
 * @date Create 19:20 2018/9/19 0019
 * @description: 驱动类
 *      原数据：/log/09/18
 *      原数据：/log/09/19
 *      清洗后的存储目录: /ods/09/18
 *      清洗后的存储目录: /ods/09/19
 *  yarn jar ./   package.classname -d 2018-09-19
 *
 *  为了简化命令行方式运行作业，Hadoop自带了一些辅助类。
 *  GenericOptionsParser是一个类，用来解释常用的Hadoop命令行选项，
 *  并根据需要，为Configuration对象设置相应的取值。
 *  通常不直接使用GenericOptionsParser，更方便的方式是：
 *  实现Tool接口，通过ToolRunner来运行应用程序，
 *  ToolRunner内部调用GenericOptionsParser。
 */
public class EtlToHdfsRunner implements Tool{
    private static final Logger logger = Logger.getLogger(EtlToHdfsMapper.class);
    private Configuration conf = new Configuration();

    public static void main(String[] args) {
        try {
            ToolRunner.run(new Configuration(),new EtlToHdfsRunner(),args);
        } catch (Exception e) {
            logger.warn("执行etl to hdfs异常",e);
        }

        Map<String, String> stringStringMap = LogUtil.parserLog("114.61.94.253^A1537792027.397^Ahh^A/BCImg.gif?c_time=1495462088397&oid=oid121201&u_mid=Aidon&pl=java_server&en=e_cs&sdk=jdk&ver=1");

        System.out.println(stringStringMap);

    }

    @Override
    public int run(String[] args) throws Exception {

        Configuration conf = getConf();

        //1、获取-d之后的日期并存储到conf中，如果没有-d或者日期不合法则使用昨天为默认值
        this.handleArgs(conf,args);

        //获取job
        Job job= Job.getInstance(conf,"etl to hdfs");

        job.setJarByClass(EtlToHdfsRunner.class);

        //设置map相关
        job.setMapperClass(EtlToHdfsMapper.class);
        job.setMapOutputKeyClass(LogWritable.class);
        job.setMapOutputValueClass(NullWritable.class);

        //没有reduce
        job.setNumReduceTasks(0);

        //设置输入输出
        this.handleInputOutput(job);
        return job.waitForCompletion(true)?1:0;

    }

    /**
     * 设置输入输出的方法
     * 需要设置输入输出的路径--判断路径是否可用
     * @param job
     */
    private void handleInputOutput(Job job) {
        //获取存储到conf中的日期，进行切分的到月和日
        //接受的是args传过来的参数信息
        String[] fields = job.getConfiguration().get(RUNNING_DATE).split("-");
        String month = fields[1];
        String day = fields[2];
        try {
            FileSystem fs = FileSystem.get(job.getConfiguration());
            //设置输入输出路径
            Path inpath = new Path("/log/"+month+"/"+day);
            Path outpath = new Path("/ods/"+month+"/"+day);
            //判断输入路径是否存在，不存在抛出异常
            if (fs.exists(inpath)){
                FileInputFormat.addInputPath(job,inpath);
            }else {
                throw new RuntimeException("输入路径不存储在.inpath:"+inpath.toString());
            }
            //判断输出路径是否存在，存在进行删除
            if (fs.exists(outpath)){
                //true表示即使是文件夹的话也强制删除
                fs.delete(outpath,true);
            }
            //设置输出路径
            FileOutputFormat.setOutputPath(job,outpath);

        } catch (IOException e) {
            logger.warn("设置输入输出路径异常.",e);
        }


    }

    /**
     *获取日期-并将日期存储到conf中
     * @param conf
     * @param args
     */
    private void handleArgs(Configuration conf, String[] args) {
        String date = null;
        //要先判断参数是否为空
        if (args.length > 0){
            //循环args
            for (int i=0;i<args.length;i++){
                //判断参数中是否有-d
                if (args[i].equals("-d")){
                    if (i+1 <= args.length) {
                        date = args[i+1];
                        break;
                    }
                }
            }
        }
        //判断日期是否为空，为空的话调用日期工具中的方法获取昨天的日期
        if (StringUtils.isEmpty(date)){
            date = TimeUtil.getYesterday();
        }

        //将date存储到conf中
        conf.set(RUNNING_DATE,date);
    }

    @Override
    public void setConf(Configuration configuration) {
        configuration = this.conf;
    }

    @Override
    public Configuration getConf() {
        return this.conf;
    }
}
