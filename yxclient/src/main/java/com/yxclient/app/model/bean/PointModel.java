package com.yxclient.app.model.bean;

import java.io.Serializable;

/**
 * Created by mac on 2017/9/27.
 * 地点model
 */

public class PointModel implements Serializable {
    private static final long serialVersionUID = 2488228939963474972L;
    private String province;
    private String city;
    private String county;
    private String name;
    private String address;
    private CoordinateModel coordinate;

    public PointModel() {
    }

    public PointModel(String province, String city, String county, String name, String address) {
        this.province = province;
        this.city = city;
        this.county = county;
        this.name = name;
        this.address = address;
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

    public CoordinateModel getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(CoordinateModel coordinate) {
        this.coordinate = coordinate;
    }
}
