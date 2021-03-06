package com.phone.common;

/**
 * @author axiao
 * @date Create 16:23 2018/9/20 0020
 * @description: 日期的枚举
 */
public enum DateEnum {

    YEAR("year"),
    SEASON("season"),
    MONTH("month"),
    WEEK("week"),
    DAY("day"),
    HOUR("hour")
            ;

    public String dateType;

    DateEnum(String dateType) {
        this.dateType = dateType;
    }

    /**
     * 根据type返回对应的枚举
     * @param type
     * @return
     */
    public static DateEnum valueOfDateType(String type){
        for (DateEnum date : values()){
            if(date.dateType.equals(type)){
                return date;
            }
        }
        return null;
    }
}
