package com.phone.analystic.hive;

import com.phone.analystic.modle.base.PaymentTypeDimension;
import com.phone.analystic.mr.service.IDimension;
import com.phone.analystic.mr.service.impl.IDimensionImpl;
import com.phone.common.GlobalConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hive.ql.exec.UDF;

/**
 * @author axiao
 * @date Create 15:45 2018/10/9 0009
 * @description: 获取支付方式维度信息表id
 */
public class PaymentTypeDimensionUdf extends UDF {

    public IDimension iDimension = null;

    public PaymentTypeDimensionUdf() {
        iDimension = new IDimensionImpl();
    }


    public int evaluate(String name) {
        name = name == null || StringUtils.isEmpty(name.trim()) ? GlobalConstants.DEFAULT_VALUE : name.trim();
        PaymentTypeDimension pd = new PaymentTypeDimension(name);
        try {
            return iDimension.getDiemnsionIdByObject(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException("获取支付方式维度的Id异常");
    }

    public static void main(String[] args) {
        System.out.println(new PaymentTypeDimensionUdf().evaluate("alipay"));
    }
}


