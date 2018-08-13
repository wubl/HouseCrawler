package com.wstar.house.entity;

/**
 * 物业公司
 */
public class PropertyCompanyEntity {
    /** 物业类型 **/
    private String propertyType;

    /** 数量 **/
    private int amount;

    /** 建筑面积 **/
    private String coveredArea;

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCoveredArea() {
        return coveredArea;
    }

    public void setCoveredArea(String coveredArea) {
        this.coveredArea = coveredArea;
    }
}
