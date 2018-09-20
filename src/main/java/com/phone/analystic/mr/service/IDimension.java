package com.phone.analystic.mr.service;

import com.phone.analystic.modle.base.BaseDimension;

import java.io.IOException;
import java.sql.SQLException;

/**
 * @author axiao
 * @date Create 18:02 2018/9/20 0020
 * @description:  根据维度获取对应的id的接口--有约束(这一类型的都要实现接口)
 */
public interface IDimension {

    /**
     *
     * @param dimension  基础维度的对象
     * @return
     * @throws IOException
     * @throws SQLException
     */
    int getDiemnsionIdByObject(BaseDimension dimension) throws IOException,SQLException;
}
