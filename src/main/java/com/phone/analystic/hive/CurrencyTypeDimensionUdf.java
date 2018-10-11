package com.phone.analystic.hive;

import com.phone.analystic.modle.base.CurrencyTypeDimension;
import com.phone.analystic.mr.service.IDimension;
import com.phone.analystic.mr.service.impl.IDimensionImpl;
import com.phone.common.GlobalConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hive.ql.exec.UDF;

/**
 * @author axiao
 * @date Create 15:48 2018/10/9 0009
 * @description: 获取支付货币类型维度信息表id
 */
public class CurrencyTypeDimensionUdf extends UDF{
    public IDimension iDimension = null;

    public CurrencyTypeDimensionUdf() {
        iDimension = new IDimensionImpl();
    }
    /**
     * @param name
     * @return
     */
    public int evaluate(String name) {
        name = name == null || StringUtils.isEmpty(name.trim()) ? GlobalConstants.DEFAULT_VALUE : name.trim();
        CurrencyTypeDimension currencyTypeDimension = new CurrencyTypeDimension(name);
        try {
            return iDimension.getDiemnsionIdByObject(currencyTypeDimension);
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException("获取货币类型维度的Id异常");
    }

    public static void main(String[] args) {
        System.out.println(new CurrencyTypeDimensionUdf().evaluate(null));
    }
}


