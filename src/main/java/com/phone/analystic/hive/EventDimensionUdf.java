package com.phone.analystic.hive;

import com.phone.analystic.modle.base.EventDimension;
import com.phone.analystic.mr.service.IDimension;
import com.phone.analystic.mr.service.impl.IDimensionImpl;
import com.phone.common.GlobalConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hive.ql.exec.UDF;

/**
 * @author axiao
 * @date Create 8:56 2018/9/28 0028
 * @description:
 */
public class EventDimensionUdf extends UDF {

    IDimension iDimension = new IDimensionImpl();

    /**
     * 方法重载
     * @param category 事件种类
     * @param action 事件名称
     * @return  事件维度的id
     */
    public int evaluate(String category,String action){
        if(StringUtils.isEmpty(category)){
            category = action = GlobalConstants.DEFAULT_VALUE;
        }
        if(StringUtils.isEmpty(action)){
            action = GlobalConstants.DEFAULT_VALUE;
        }
        int id = -1;

        try {
            EventDimension ed = new EventDimension(category,action);
            id = iDimension.getDiemnsionIdByObject(ed);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }


}
