package com.yxdriver.app.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mac on 2017/9/27.
 * 订单(出行、货运)
 */

public class OrderModel implements Serializable {
    private static final long serialVersionUID = -6019199232845964791L;
    private String no;           //订单号
    private String orderType;    //订单类型（出行、货运）
    private String createTime;   //创建时间
    private String infoType;     //出行类型（顺风车、专车）
    private String date;         //出行日期
    private String time;         // 出行时间
    private String datetime;     // 出行日期+时间
    private int status;          // 订单状态
    private String note;         // 备注信息
    private String closeTime;    // 订单关闭时间
    private int seats;           //  总座位数
    private List<CustomerModel> info;//客户信息
    private PointModel origin;   // 出发地点
    private PointModel site;     // 目的地点
    private UserModel driver;    //驾驶员信息
    private UserModel createUser;// 订单创建者
    private CarModel car;        //车辆信息
    private List<PointModel> pathPoint;  //途径地点
    private int amount;      // 订单金额
    private Double mileage;         //大约里程
    private Double capacity;          //车辆载重

    public OrderModel() {
    }

    public OrderModel(String no, String orderType, String createTime, String infoType, String date, String time, String datetime,
                      int status, String note, String closeTime, int seats, List<CustomerModel> info, PointModel origin, PointModel site, UserModel driver, CarModel car) {
        this.no = no;
        this.orderType = orderType;
        this.createTime = createTime;
        this.infoType = infoType;
        this.date = date;
        this.time = time;
        this.datetime = datetime;
        this.status = status;
        this.note = note;
        this.closeTime = closeTime;
        this.seats = seats;
        this.info = info;
        this.origin = origin;
        this.site = site;
        this.driver = driver;
        this.car = car;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getInfoType() {
        return infoType;
    }

    public void setInfoType(String infoType) {
        this.infoType = infoType;
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

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
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

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public List<CustomerModel> getInfo() {
        return info;
    }

    public void setInfo(List<CustomerModel> info) {
        this.info = info;
    }

    public PointModel getOrigin() {
        return origin;
    }

    public void setOrigin(PointModel origin) {
        this.origin = origin;
    }

    public PointModel getSite() {
        return site;
    }

    public void setSite(PointModel site) {
        this.site = site;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public UserModel getDriver() {
        return driver;
    }

    public void setDriver(UserModel driver) {
        this.driver = driver;
    }

    public CarModel getCar() {
        return car;
    }

    public void setCar(CarModel car) {
        this.car = car;
    }

    public List<PointModel> getPathPoint() {
        return pathPoint;
    }

    public void setPathPoint(List<PointModel> pathPoint) {
        this.pathPoint = pathPoint;
    }

    public UserModel getCreateUser() {
        return createUser;
    }

    public void setCreateUser(UserModel createUser) {
        this.createUser = createUser;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Double getMileage() {
        return mileage;
    }

    public void setMileage(Double mileage) {
        this.mileage = mileage;
    }

    public Double getCapacity() {
        return capacity;
    }

    public void setCapacity(Double capacity) {
        this.capacity = capacity;
    }
}
