package com.phone.common;

/**
 * @author axiao
 * @date Create 17:48 2018/9/20 0020
 * @description: 统计指标的枚举
 */
public enum  KpiType {

    NEW_USER("new_user"),
    BROWSER_NEW_USER("browser_new_user")
    ;

    public String kpiName;

    KpiType(String kpiName) {
        this.kpiName = kpiName;
    }

    /**
     * 根据kpi的name获取对应的指标
     * @param name
     * @return
     */
    public static KpiType valueOfKpiName(String name){
        for (KpiType kpi : values()){
            if(kpi.kpiName.equals(name)){
                return kpi;
            }
        }
        return null;
    }


}
