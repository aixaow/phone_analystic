package com.phone.analystic.modle.base;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author axiao
 * @date Create 22:44 2018/9/19 0019
 * @description: 操作系统信息维度表
 */
public class OsDimension extends BaseDimension {
   private int id;
    /**
     * 操作系统名称
     */
   private String osName;
    /**
     * 操作系统版本号
     */
   private String osVersion;


    @Override
    public void write(DataOutput out) throws IOException {
        out.write(id);
        out.writeUTF(osName);
        out.writeUTF(osVersion);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.id = in.readInt();
        this.osName = in.readUTF();
        this.osVersion = in.readUTF();
    }

    @Override
    public int compareTo(BaseDimension o) {
        return 0;
    }

    @Override
    public String toString() {
        return id + "\u0001"+osName+""+osVersion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }
}
