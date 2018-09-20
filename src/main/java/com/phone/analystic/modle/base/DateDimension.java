package com.phone.analystic.modle.base;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author axiao
 * @date Create 23:03 2018/9/19 0019
 * @description: 时间维度信息表
 */
public class DateDimension extends BaseDimension{
    private int id;
    private int year;
    private int season;
    private int month;
    private int week;
    private int day;

    //类型不对
    private String calendar;
    private String type;


    @Override
    public void write(DataOutput out) throws IOException {
        out.write(id);

    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.id = in.readInt();

    }

    @Override
    public int compareTo(BaseDimension o) {
        return 0;
    }

    @Override
    public String toString() {
        return id + "\u0001";
    }
}
