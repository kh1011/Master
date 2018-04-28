package com.yxclient.app.model.bean;

import java.io.Serializable;

/**
 * Created by mac on 2017/9/29.
 * 货运模型
 */

public class TransportModel implements Serializable {
    private static final long serialVersionUID = -8763424392626905032L;
    // 发货地址
    private OriginAndSite origin;
    // 收货地址
    private OriginAndSite site;
    private String date;
    private String time;
    // 运货类型
    private String type;
    private String note;
    // 运输价格
    private int amount;
    private String consignee;
    // 货物重量
    private double weight;
    // 货物类型
    private String kind;
}
