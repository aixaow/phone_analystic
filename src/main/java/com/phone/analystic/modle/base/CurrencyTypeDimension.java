package com.phone.analystic.modle.base;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author axiao
 * @date Create 23:01 2018/9/19 0019
 * @description: 支付货币类型维度信息表
 */
public class CurrencyTypeDimension extends BaseDimension{
    private int id;
    /**
     * 货币名称
     */
    private String currencyName;


    @Override
    public void write(DataOutput out) throws IOException {
        out.write(id);
        out.writeUTF(currencyName);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.id = in.readInt();
        this.currencyName = in.readUTF();
    }

    @Override
    public int compareTo(BaseDimension o) {
        return 0;
    }

    @Override
    public String toString() {
        return id + "\u0001"+currencyName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }
}
