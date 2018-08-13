package com.wstar.house.entity;

import java.util.List;

/**
 * 房子列表项目
 */
public class HouseItemEntity {
    private String houseName;

    private String detailLink;

    private HouseAddressEntity houseAddress;

    private List<HouseLargePicture> houseLargePictures;

    private HouseSmallPicture houseSmallPicture;

    private String areaRange;

    private List<String> bedrooms;

    private List<String> houseRooms;

    private String price;

    private String city;

    public String getHouseName() {
        return houseName;
    }

    public void setHouseName(String houseName) {
        this.houseName = houseName;
    }

    public HouseAddressEntity getHouseAddress() {
        return houseAddress;
    }

    public void setHouseAddress(HouseAddressEntity houseAddress) {
        this.houseAddress = houseAddress;
    }

    public List<HouseLargePicture> getHouseLargePictures() {
        return houseLargePictures;
    }

    public void setHouseLargePictures(List<HouseLargePicture> houseLargePictures) {
        this.houseLargePictures = houseLargePictures;
    }

    public HouseSmallPicture getHouseSmallPicture() {
        return houseSmallPicture;
    }

    public void setHouseSmallPicture(HouseSmallPicture houseSmallPicture) {
        this.houseSmallPicture = houseSmallPicture;
    }

    public String getAreaRange() {
        return areaRange;
    }

    public void setAreaRange(String areaRange) {
        this.areaRange = areaRange;
    }

    public List<String> getHouseRooms() {
        return houseRooms;
    }

    public void setHouseRooms(List<String> houseRooms) {
        this.houseRooms = houseRooms;
    }

    public String getDetailLink() {
        return detailLink;
    }

    public void setDetailLink(String detailLink) {
        this.detailLink = detailLink;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<String> getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(List<String> bedrooms) {
        this.bedrooms = bedrooms;
    }
}
