package com.phone.analystic.modle.base;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author axiao
 * @date Create 22:44 2018/9/19 0019
 * @description: 平台维度信息表
 */
public class PlatformDimension extends BaseDimension {
   private int id;
    /**
     * 平台名称
     */
   private String platformName;



    @Override
    public void write(DataOutput out) throws IOException {
        out.write(id);
        out.writeUTF(platformName);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.id = in.readInt();
        this.platformName = in.readUTF();
    }

    @Override
    public int compareTo(BaseDimension o) {
        return 0;
    }

    @Override
    public String toString() {
        return id + "\u0001"+platformName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKpiName() {
        return platformName;
    }

    public void setKpiName(String kpiName) {
        this.platformName = kpiName;
    }
}
