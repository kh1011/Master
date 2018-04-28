package com.yxclient.app.model.bean;

import java.io.Serializable;

/**
 * Created by mac on 2017/8/17.
 * 货运下单模型
 */

public class TransOrderModel implements Serializable {
    private static final long serialVersionUID = 2041005002250301588L;
    private int amount;             // 货运价格
    private String date;            // 发货日期
    private String time;            // 发货时间
    private String type;            // 货运类型
    private double weight;          // 货运重量
    private String kind;            // 货物类型
    private String note;            // 备注信息
    private DeliverAddrModel origin;// 发货地址
    private DeliverAddrModel site;  // 收货地址
    private String consignorName;   // 发货联系人
    private String consignorMobile; // 发货联系人电话
    private String name;            // 收货联系人
    private String mobile;          // 收货人电话
    private String bulk;            // 货物体积
    private Double mileage;         //大约里程
    private int fragile;          //易碎判断isWet
    private int wet;              //易湿判断

    public TransOrderModel() {
    }

    public TransOrderModel(int amount, String date, String time, String type, double weight
            , String kind, String note, DeliverAddrModel origin, DeliverAddrModel site,int fragile
            , String consignorName, String consignorMobile, String name, String mobile, String bulk,Double mileage
            , int wet) {
        this.amount = amount;
        this.date = date;
        this.time = time;
        this.type = type;
        this.weight = weight;
        this.kind = kind;
        this.note = note;
        this.origin = origin;
        this.site = site;
        this.consignorName = consignorName;
        this.consignorMobile = consignorMobile;
        this.name = name;
        this.mobile = mobile;
        this.bulk = bulk;
        this.mileage=mileage;
        this.fragile=fragile;
        this.wet=wet;

    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
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

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public DeliverAddrModel getOrigin() {
        return origin;
    }

    public void setOrigin(DeliverAddrModel origin) {
        this.origin = origin;
    }

    public DeliverAddrModel getSite() {
        return site;
    }

    public void setSite(DeliverAddrModel site) {
        this.site = site;
    }

    public String getConsignorName() {
        return consignorName;
    }

    public void setConsignorName(String consignorName) {
        this.consignorName = consignorName;
    }

    public String getConsignorMobile() {
        return consignorMobile;
    }

    public void setConsignorMobile(String consignorMobile) {
        this.consignorMobile = consignorMobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBulk() {
        return bulk;
    }

    public void setBulk(String bulk) {
        this.bulk = bulk;
    }

    public Double getMileage() {
        return mileage;
    }

    public void setMileage(Double mileage) {
        this.mileage = mileage;
    }

    public int getFragile() {
        return fragile;
    }

    public void setFragile(int fragile) {
        this.fragile = fragile;
    }

    public int getWet() {
        return wet;
    }

    public void setWet(int wet) {
        this.wet = wet;
    }
}
