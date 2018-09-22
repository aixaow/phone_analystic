package com.phone.analystic.mr;

import com.phone.analystic.modle.StatsBaseDimension;
import com.phone.analystic.modle.value.StatsOutpuValue;
import com.phone.analystic.mr.service.IDimension;
import org.apache.hadoop.conf.Configuration;

import java.sql.PreparedStatement;

/**
 * @author axiao
 * @date Create 11:15 2018/9/22 0022
 * @description: 操作结果表的接口
 */
public interface IOutputWritter {
    /**
     * 为每一个kpi的最终结果赋值的接口
     * @param conf  配置对象 -要写一些配置文件
     * @param key  公共维度类的父类
     * @param value  封装map或者是reduce阶段的输出value的类型的顶级父类
     * @param ps  执行sql的参数话查询对下个
     * @param iDimension  根据维度获取对应的id的接口
     *
     *  要把map，reduce阶段的结果写到mysql，所以参数里面要写key、value类型对象
     *
     */
    void ouput(Configuration conf, StatsBaseDimension key,
               StatsOutpuValue value, PreparedStatement ps, IDimension iDimension);
}
