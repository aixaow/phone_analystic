package com.phone.analystic.modle.base;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author axiao
 * @date Create 22:44 2018/9/19 0019
 * @description: 地域信息维度表
 */
public class LocationDimension extends BaseDimension {
   private int id;
    /**
     * 国家名称
     */
   private String country;
    /**
     * 省份名称
     */
   private String province;
    /**
     * 城市名称
     */
    private String city;


    @Override
    public void write(DataOutput out) throws IOException {
        out.write(id);
        out.writeUTF(country);
        out.writeUTF(province);
        out.writeUTF(city);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.id = in.readInt();
        this.country = in.readUTF();
        this.province = in.readUTF();
        this.city = in.readUTF();
    }

    @Override
    public int compareTo(BaseDimension o) {
        return 0;
    }

    @Override
    public String toString() {
        return id + "\u0001"+country+""+province+""+city;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
