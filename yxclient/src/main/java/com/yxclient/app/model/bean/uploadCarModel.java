package com.yxclient.app.model.bean;

import java.io.Serializable;

/**
 * Created by mac on 2017/10/3.
 * 车辆
 */

public class uploadCarModel implements Serializable {
    private String carnumber ;  //车牌号
    private String numberColor; //车牌颜色
    private String brand ;      //车辆品牌
    private String color ;      //车辆颜色
    private String carType;     //车辆类型
    private String carSeats ;   //核载人数
    private String capacity;    //载重
    private String length;      //车长

    public String getCarnumber() {
        return carnumber;
    }

    public void setCarnumber(String carnumber) {
        this.carnumber = carnumber;
    }

    public String getNumberColor() {
        return numberColor;
    }

    public void setNumberColor(String numberColor) {
        this.numberColor = numberColor;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getCarSeats() {
        return carSeats;
    }

    public void setCarSeats(String carSeats) {
        this.carSeats = carSeats;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }
}
