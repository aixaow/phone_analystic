package com.phone.analystic.modle.base;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author axiao
 * @date Create 22:44 2018/9/19 0019
 * @description: 支付方式维度信息表
 */
public class PaymentTypeDimension extends BaseDimension {
   private int id;
    /**
     * 支付方式名称
     */
   private String paymentType;



    @Override
    public void write(DataOutput out) throws IOException {
        out.write(id);
        out.writeUTF(paymentType);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.id = in.readInt();
        this.paymentType = in.readUTF();
    }

    @Override
    public int compareTo(BaseDimension o) {
        return 0;
    }

    @Override
    public String toString() {
        return id + "\u0001"+paymentType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
}
