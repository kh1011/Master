package com.yxdriver.app.model.bean;

import java.io.Serializable;

/**
 * 提交订单的bean
 *
 * Created by zhangyun on 2017/9/30 0030
 * EMail -> yun.zhang@chinacreator.com
 */
public class SubmitOrderModel implements Serializable {
    String product;//所购买的产品编码
    int number;//购买数量
    double price;
    String consignee;//收货地址信息编码
    String note;//留言

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }


    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "SubmitOrderModel{" +
                "product='" + product + '\'' +
                ", number='" + number + '\'' +
                ", consignee='" + consignee + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
