package com.phone.analystic.mr.nu;

import com.phone.analystic.modle.StatsUserDimension;
import com.phone.analystic.modle.value.map.TimeOutputValue;
import com.phone.analystic.modle.value.reduce.OutputWritable;
import com.phone.analystic.mr.OutputToMySqlFormat;
import com.phone.etl.mr.EtlToHdfsMapper;
import com.phone.etl.mr.EtlToHdfsRunner;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;

/**
 * @author axiao
 * @date Create 11:14 2018/9/22 0022
 * @description:
 */
public class NewUserRunner implements Tool {

    private static final Logger logger = Logger.getLogger(EtlToHdfsMapper.class);
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
        return null;
    }

    @Override
    public int run(String[] strings) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        job.setMapperClass(NewUserMapper.class);
        job.setMapOutputKeyClass(StatsUserDimension.class);
        job.setMapOutputValueClass(TimeOutputValue.class);



        //设置redcuce的输出格式类

        job.setReducerClass(NewUserReducer.class);
        job.setOutputFormatClass(OutputToMySqlFormat.class);
        job.setOutputKeyClass(StatsUserDimension.class);
        job.setOutputValueClass(OutputWritable.class);

        FileInputFormat.addInputPath(job,new Path("/ods/09/18/part-m-00000"));

        job.setNumReduceTasks(2);


        return job.waitForCompletion(true)?1:0;
    }
}
