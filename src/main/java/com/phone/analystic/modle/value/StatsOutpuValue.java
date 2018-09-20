package com.phone.analystic.modle.value;

import com.phone.common.KpiType;
import org.apache.hadoop.io.Writable;

/**
 * @author axiao
 * @date Create 17:47 2018/9/20 0020
 * @description: 封装map或者是reduce阶段的输出value的类型的顶级父类
 */
public abstract class StatsOutpuValue implements Writable {
    //获取kpi的抽象方法
    public abstract KpiType getKpi();
}
