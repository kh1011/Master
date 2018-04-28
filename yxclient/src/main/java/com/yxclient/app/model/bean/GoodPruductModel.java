package com.yxclient.app.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 商品分类下的产品列表
 * <p>
 * Created by zhangyun on 2017/9/29 0029
 * EMail -> yun.zhang@chinacreator.com
 */
public class GoodPruductModel implements Serializable {
    String uuid;
    GoodTypeModel kind;
    String title;
    String titleImage;
    String desc;
    int sales;//总销量
    int monthSales;//月销量
    int stock;//库存量
    String unit;
    float price;//销售价格
    float basePrice;//原始价格
    String createTime;
    int status;//状态 - 1：正常 0： 停售
    List<String> images;
    String tag;
    String note;
    // 商家电话号码
    String businessMobile;
    private List<PromotionModel> promotion;//促销信息
    String color;
    String weight;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public GoodTypeModel getKind() {
        return kind;
    }

    public void setKind(GoodTypeModel kind) {
        this.kind = kind;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleImage() {
        return titleImage;
    }

    public void setTitleImage(String titleImage) {
        this.titleImage = titleImage;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public int getMonthSales() {
        return monthSales;
    }

    public void setMonthSales(int monthSales) {
        this.monthSales = monthSales;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(float basePrice) {
        this.basePrice = basePrice;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getBusinessMobile() {
        return businessMobile;
    }

    public void setBusinessMobile(String businessMobile) {
        this.businessMobile = businessMobile;
    }

    public List<PromotionModel> getPromotion() {
        return promotion;
    }

    public void setPromotion(List<PromotionModel> promotion) {
        this.promotion = promotion;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "GoodPruductModel{" +
                "note='" + note + '\'' +
                ", tag='" + tag + '\'' +
                ", images=" + images +
                ", status=" + status +
                ", createTime='" + createTime + '\'' +
                ", basePrice=" + basePrice +
                ", price=" + price +
                ", unit='" + unit + '\'' +
                ", stock=" + stock +
                ", monthSales=" + monthSales +
                ", sales=" + sales +
                ", desc='" + desc + '\'' +
                ", titleImage='" + titleImage + '\'' +
                ", title='" + title + '\'' +
                ", kind=" + kind +
                ", uuid='" + uuid + '\'' +
                ", color='" + color + '\'' +
                ", weight='" + weight + '\'' +
                '}';
    }
}
