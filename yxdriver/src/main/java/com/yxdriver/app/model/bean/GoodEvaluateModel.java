package com.yxdriver.app.model.bean;

import java.io.Serializable;

/**
 * 功能：商品评价信息
 * Created by yun.zhang on 2017/9/29 0029.
 * email:zy19930321@163.com
 */
public class GoodEvaluateModel implements Serializable {
    String uuid;
    String orderNo;
    String goodsUUID;
    String content;
    String createTime;
    UserInfo createUser;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getGoodsUUID() {
        return goodsUUID;
    }

    public void setGoodsUUID(String goodsUUID) {
        this.goodsUUID = goodsUUID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public UserInfo getCreateUser() {
        return createUser;
    }

    public void setCreateUser(UserInfo createUser) {
        this.createUser = createUser;
    }
}
