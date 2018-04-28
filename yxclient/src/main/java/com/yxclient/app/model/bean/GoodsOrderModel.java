package com.yxclient.app.model.bean;

import java.io.Serializable;

/**
 * Created by mac on 2017/10/12.
 * 购物订单模型
 */

public class GoodsOrderModel implements Serializable {
    private static final long serialVersionUID = -4581336671590489096L;
    private String no;                // 订单编号
    private String orderType;         // 订单类型
    private int amount;               // 订单金额
    private int number;               // 购买数量
    private GoodPruductModel product; // 对应的商品
    private String createTime;        // 下单日期
    private UserModel createUser;     // 创建者
    private int status;               // 商品状态
    private String name;              // 收货人
    private String mobile;            // 收货人电话
    private String address;           // 收货人地址

    public GoodsOrderModel() {
    }

    public GoodsOrderModel(String no, String orderType, int amount, int number,
                           GoodPruductModel product, String createTime,
                           UserModel createUser, int status, String name,
                           String mobile, String address) {
        this.no = no;
        this.orderType = orderType;
        this.amount = amount;
        this.number = number;
        this.product = product;
        this.createTime = createTime;
        this.createUser = createUser;
        this.status = status;
        this.name = name;
        this.mobile = mobile;
        this.address = address;
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public GoodPruductModel getProduct() {
        return product;
    }

    public void setProduct(GoodPruductModel product) {
        this.product = product;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public UserModel getCreateUser() {
        return createUser;
    }

    public void setCreateUser(UserModel createUser) {
        this.createUser = createUser;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
