package com.phone.analystic.modle.value.reduce;

import com.phone.analystic.modle.value.StatsOutpuValue;
import com.phone.common.KpiType;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.WritableUtils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author axiao
 * @date Create 17:46 2018/9/20 0020
 * @description: 用于reduce阶段输出的value的类型
 */
public class OutputWritable extends StatsOutpuValue {
    private KpiType kpi;
    /**
     * reduce端输出的数据一个封装到了kpi中 一个是统计的数  适合用MapWritable
     */
    private MapWritable value = new MapWritable();

    @Override
    public void write(DataOutput out) throws IOException {
        WritableUtils.writeEnum(out,kpi);
        this.value.write(out);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        WritableUtils.readEnum(in,KpiType.class);
        this.value.readFields(in);
    }

    /**
     * 从reduce输出的数据能否顺利到达数据库和表--kpi至关重要
     * @return
     */
    @Override
    public KpiType getKpi() {
        return this.kpi;
    }

    public void setKpi(KpiType kpi) {
        this.kpi = kpi;
    }

    public MapWritable getValue() {
        return value;
    }

    public void setValue(MapWritable value) {
        this.value = value;
    }
}
