package com.yxdriver.app.model.bean;

import java.util.List;

/**
 * Created by mac on 2017/8/17.
 * 出行下单模型
 */

public class RideOrder {
    // 出发地点
    private OriginAndSite origin;
    // 目的地
    private OriginAndSite site;
    // 出发日期
    private String date;
    // 时间
    private String time;
    // 出行类型
    private String type;
    // 备注信息
    private String note;
    // 用车方式：立即用车、预约用车
    private int once;
    // 途经地点
    private List<OriginAndSite> pathPoint;
    //车辆载重
    private Double capacity;
    //可乘人数
    private int seats;
    // 大概里程
    private Double mileage;

    public int getOnce() {
        return once;
    }

    public void setOnce(int once) {
        this.once = once;
    }

    public OriginAndSite getOrigin() {
        return origin;
    }

    public void setOrigin(OriginAndSite origin) {
        this.origin = origin;
    }

    public OriginAndSite getSite() {
        return site;
    }

    public void setSite(OriginAndSite site) {
        this.site = site;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<OriginAndSite> getPathPoint() {
        return pathPoint;
    }

    public void setPathPoint(List<OriginAndSite> pathPoint) {
        this.pathPoint = pathPoint;
    }

    public Double getCapacity() {
        return capacity;
    }

    public void setCapacity(Double capacity) {
        this.capacity = capacity;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public Double getMileage() {
        return mileage;
    }

    public void setMileage(Double mileage) {
        this.mileage = mileage;
    }
}
