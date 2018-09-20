package com.phone.analystic.mr.nu;

import com.phone.etl.mr.EtlToHdfsMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * @author axiao
 * @date Create 22:45 2018/9/19 0019
 * @description:
 */
public class NewUserMapper extends Mapper<LongWritable,Text,Text,Text> {
    private static final Logger logger = Logger.getLogger(EtlToHdfsMapper.class);

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String lines = value.toString();
        if (StringUtils.isNotEmpty(lines)){
            if (lines.contains("e_l")){
                //同一时间，平台，uuid

            }
        }



    }
}
