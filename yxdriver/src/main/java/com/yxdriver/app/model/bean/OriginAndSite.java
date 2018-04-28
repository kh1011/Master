package com.yxdriver.app.model.bean;

import java.io.Serializable;

/**
 * Created by mac on 2017/8/17.
 * 出发地点、目的地
 */

public class OriginAndSite implements Serializable {
    private static final long serialVersionUID = -4492697680375171515L;
    private String province;
    private String city;
    private String county;
    private String name;
    private String address;
    private CoordinateModel coordinateModel;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public CoordinateModel getCoordinate() {
        return coordinateModel;
    }

    public void setCoordinate(CoordinateModel coordinateModel) {
        this.coordinateModel = coordinateModel;
    }
}
