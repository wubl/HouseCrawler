package com.wstar.house.entity;

public class SimpleHouseEntity {
    /** 楼盘名称 **/
    private String houseName;

    /** 价格 **/
    private String price;

    /** 区域 **/
    private String region;

    /** 详情链接 **/
    private String detailLink;

    /** 楼盘类型 **/
    private HousingType houseType;

    public String getHouseName() {
        return houseName;
    }

    public void setHouseName(String houseName) {
        this.houseName = houseName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDetailLink() {
        return detailLink;
    }

    public void setDetailLink(String detailLink) {
        this.detailLink = detailLink;
    }

    public HousingType getHouseType() {
        return houseType;
    }

    public void setHouseType(HousingType houseType) {
        this.houseType = houseType;
    }
}
