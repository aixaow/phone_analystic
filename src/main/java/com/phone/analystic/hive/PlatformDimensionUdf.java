package com.phone.analystic.hive;

import com.phone.analystic.modle.base.PlatformDimension;
import com.phone.analystic.mr.service.IDimension;
import com.phone.analystic.mr.service.impl.IDimensionImpl;
import com.phone.common.GlobalConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hive.ql.exec.UDF;

/**
 * @author axiao
 * @date Create 8:58 2018/9/28 0028
 * @description:
 */
public class PlatformDimensionUdf extends UDF {

    IDimension iDimension = new IDimensionImpl();

    /**
     * @return  事件维度的id
     */
    public int evaluate(String platform){

        if(StringUtils.isEmpty(platform)){
            platform = GlobalConstants.DEFAULT_VALUE;
        }
        int id = -1;

        try {
            PlatformDimension pl = new PlatformDimension(platform);
            id = iDimension.getDiemnsionIdByObject(pl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public static void main(String[] args) {
        System.out.println(new PlatformDimensionUdf().evaluate("website"));
    }

}
