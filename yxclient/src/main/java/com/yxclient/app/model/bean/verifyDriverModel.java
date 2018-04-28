package com.yxclient.app.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mac on 2017/9/27.
 * 订单(出行、货运)
 */

public class verifyDriverModel implements Serializable {
    private DriverModel driver;    //驾驶员信息
    private CarModel car;       //车辆信息


    public verifyDriverModel() {
    }

    public verifyDriverModel(DriverModel driver,CarModel car) {
        this.driver = driver;
        this.car=car;

    }

    public DriverModel getDriver() {
        return driver;
    }

    public void setDriver(DriverModel driver) {
        this.driver = driver;
    }

    public CarModel getCar() {
        return car;
    }

    public void setCar(CarModel car) {
        this.car = car;
    }
}
