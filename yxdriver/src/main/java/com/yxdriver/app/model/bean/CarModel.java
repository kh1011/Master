package com.yxdriver.app.model.bean;

import java.io.Serializable;

/**
 * Created by mac on 2017/10/3.
 * 车辆
 */

public class CarModel implements Serializable {
    private static final long serialVersionUID = 1116291790179172590L;
    private String uuid;
    private String plateNumber;// 车牌号
    private String plateNumberColor;//车牌颜色
    private String brand;//车辆品牌
    private String type;// 车辆类型

    public CarModel() {
    }

    public CarModel(String plateNumber, String plateNumberColor, String brand, String type) {
        this.plateNumber = plateNumber;
        this.plateNumberColor = plateNumberColor;
        this.brand = brand;
        this.type = type;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getPlateNumberColor() {
        return plateNumberColor;
    }

    public void setPlateNumberColor(String plateNumberColor) {
        this.plateNumberColor = plateNumberColor;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
