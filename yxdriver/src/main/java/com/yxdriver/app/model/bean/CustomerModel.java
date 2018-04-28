package com.yxdriver.app.model.bean;

import java.io.Serializable;

/**
 * Created by mac on 2017/9/27.
 * 乘车客户、发布订单对应的客户信息
 */

public class CustomerModel implements Serializable {
    private static final long serialVersionUID = 4352453959542195624L;
    // 小订单ID
    private String extraInfoUUID;
    // 小订单状态
    private int status;
    // 备注
    private String note;
    // 姓名
    private String name;
    // 电话号码
    private String mobile;
    // 乘车人数
    private int number;
    // 发布者
    private UserModel user;
    // 途径点
    private PointModel point;
    // 价格
    private int amount;
    // 目的地
    private PointModel site;
    // 联系人
    private String consignorName;
    // 联系人电话号码
    private String consignorMobile;
    // 目的地
    private PointModel origin;
    // 货物重量
    private int weight;
    // 货物体积
    private String bulk;
    // 货物类别
    private String kind;
    //是否易碎
    private int fragile;
    //是否易湿
    private int wet;

    private Double mileage;         //大约里程

    public CustomerModel() {
    }

    public CustomerModel(int status, String note, String name, String mobile, int number) {
        this.status = status;
        this.note = note;
        this.name = name;
        this.mobile = mobile;
        this.number = number;
    }

    public String getExtraInfoUUID() {
        return extraInfoUUID;
    }

    public void setExtraInfoUUID(String extraInfoUUID) {
        this.extraInfoUUID = extraInfoUUID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public PointModel getPoint() {
        return point;
    }

    public void setPoint(PointModel point) {
        this.point = point;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public PointModel getSite() {
        return site;
    }

    public void setSite(PointModel site) {
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

    public PointModel getOrigin() {
        return origin;
    }

    public void setOrigin(PointModel origin) {
        this.origin = origin;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getBulk() {
        return bulk;
    }

    public void setBulk(String bulk) {
        this.bulk = bulk;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
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

    public Double getMileage() {
        return mileage;
    }

    public void setMileage(Double mileage) {
        this.mileage = mileage;
    }
}
