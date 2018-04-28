package com.yxclient.app.model.bean;

/**
 * Created by mac on 2017/8/17.
 * 出行下单模型
 */

public class RideOrder {
    // 起点
    private OriginAndSite origin;
    // 终点
    private OriginAndSite site;
    // 日期
    private String date;
    // 时间
    private String time;
    // 出行类型
    private String type;
    // 乘车人数
    private int number;
    // 备注
    private String note;
    // 用车方式：立即用车、预约用车
    private int once;
    // 大概费用
    private double amount;



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

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Double getMileage() {
        return mileage;
    }

    public void setMileage(Double mileage) {
        this.mileage = mileage;
    }
}
