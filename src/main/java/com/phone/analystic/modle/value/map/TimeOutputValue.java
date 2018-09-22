package com.phone.analystic.modle.value.map;

import com.phone.analystic.modle.value.StatsOutpuValue;
import com.phone.common.KpiType;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author axiao
 * @date Create 17:45 2018/9/20 0020
 * @description:  map端的输出的value类型
 */
public class TimeOutputValue extends StatsOutpuValue{

    /**
     * 对id的泛指，可以是uuid，可以是umid，可以是sessionId
     */
    private String id;
    /**
     * 时间戳
     */
    private long time;

    /**
     * 可以直接返回null，是因为map的key中也有kpi对象，直接使用那个就可以
     * @return
     */
    @Override
    public KpiType getKpi() {
        return null;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(this.id);
        out.writeLong(this.time);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.id = in.readUTF();
        this.time = in.readLong();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
