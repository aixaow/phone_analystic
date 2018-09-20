package com.phone.analystic.mr.nu;

import com.phone.analystic.modle.StatsUserDimension;
import com.phone.analystic.modle.value.map.TimeOutputValue;
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
public class NewUserMapper extends Mapper<LongWritable,Text,StatsUserDimension,TimeOutputValue> {
    private static final Logger logger = Logger.getLogger(EtlToHdfsMapper.class);





}
