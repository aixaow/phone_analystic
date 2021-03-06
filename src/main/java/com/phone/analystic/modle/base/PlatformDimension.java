package com.phone.analystic.modle.base;

import com.phone.common.GlobalConstants;
import org.apache.commons.lang.StringUtils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

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

    public PlatformDimension(){}

    public PlatformDimension(String platformName) {
        this.platformName = platformName;
    }

    public PlatformDimension(int id,String patformName) {
        this.platformName = platformName;
        this.id = id;
    }

    public static PlatformDimension getInstance(String patformName){
        String pl = StringUtils.isEmpty(patformName) ? GlobalConstants.DEFAULT_VALUE : patformName;
        return new PlatformDimension(pl);
    }




    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(id);
        out.writeUTF(platformName);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.id = in.readInt();
        this.platformName = in.readUTF();
    }

    @Override
    public int compareTo(BaseDimension o) {
        if(this == o){
            return 0;
        }

        PlatformDimension other = (PlatformDimension) o;
        int tmp = this.id - other.id;
        if(tmp != 0){
            return tmp;
        }
        return this.platformName.compareTo(other.platformName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PlatformDimension that = (PlatformDimension) o;
        return id == that.id &&
                Objects.equals(platformName, that.platformName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, platformName);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }
}
