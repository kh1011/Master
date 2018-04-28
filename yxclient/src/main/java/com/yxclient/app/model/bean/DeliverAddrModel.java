package com.yxclient.app.model.bean;

import java.io.Serializable;

/**
 * Created by mac on 2017/10/4.
 * 发货、收货信息
 */

public class DeliverAddrModel implements Serializable {
    private static final long serialVersionUID = -3915279976760006850L;
    private String province;
    private String city;
    private String county;
    private String name;
    // 定位地址
    private String address;
    // 详细地址
    private String street;
    // 经纬度信息
    private CoordinateModel coordinate;

    public DeliverAddrModel() {
    }

    public DeliverAddrModel(String province, String city, String county, String name
            , String address, String street, CoordinateModel coordinate) {
        this.province = province;
        this.city = city;
        this.county = county;
        this.name = name;
        this.address = address;
        this.street = street;
        this.coordinate = coordinate;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public CoordinateModel getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(CoordinateModel coordinate) {
        this.coordinate = coordinate;
    }

}
