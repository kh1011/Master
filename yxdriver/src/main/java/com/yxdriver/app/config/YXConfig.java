package com.yxdriver.app.config;

import com.yxdriver.app.R;

/**
 * Created by mac on 2017/6/21.
 * 静态资源配置
 */

public class YXConfig {
    public static final String appId = "a93b9f80-95c9-4707-9690-85330a3630bc";

    /**
     * 引导界面本地图片
     */
    public static final int[] pics = {R.drawable.frist, R.drawable.second, R.drawable.third};

    /**
     * 引导界面服务器地址上的图片
     */
    public static final String[] pricUrls = {
            "http://www.anumbrella.net/App/AppShopService/Guide/guide1.jpg",
            "http://www.anumbrella.net/App/AppShopService/Guide/guide2.jpg",
            "http://www.anumbrella.net/App/AppShopService/Guide/guide3.jpg",
            "http://www.anumbrella.net/App/AppShopService/Guide/guide4.jpg"
    };
    // 订单类型
    public static final String EXPRESS = "express"; //货运
    public static final String JOURNEY = "journey"; //出行
    public static final String GOODS = "";            //购物
    // 订单状态
    public static final int T_ORDER_STATUS_CONFIRM = 101;// 待确定
    public static final int T_ORDER_STATUS_CREATE = 10;// 订单创建
    public static final int T_ORDER_STATUS_ACCEPT = 11;// 已经接单
    public static final int T_ORDER_STATUS_GO = 12;    // 进行中
    public static final int T_ORDER_STATUS_WAIT_PAY = 13;// 待支付
    public static final int T_ORDER_STATUS_INFO_SUCCESS = 14;// 已支付
    public static final int T_ORDER_STATUS_SUCCESS = 19;//   行程结束
    public static final int T_ORDER_STATUS_CLOSE = -11;//    订单关闭

    public static final String S_ORDER_STATUS_CONFIRM = "待确定";// 待确定
    public static final String S_ORDER_STATUS_CREATE = "未接单";// 订单创建
    public static final String S_ORDER_STATUS_ACCEPT = "已接单";// 已经接单
    public static final String S_ORDER_STATUS_GO = "进行中";    // 进行中
    public static final String S_ORDER_STATUS_WAIT_PAY = "待支付";// 待支付
    public static final String S_ORDER_STATUS_INFO_SUCCESS = "已支付";// 已支付
    public static final String S_ORDER_STATUS_SUCCESS = "已结束";//   行程结束
    public static final String S_ORDER_STATUS_CLOSE = "订单关闭";//    订单关闭
    // 商品订单状态
    public static final int G_ORDER_STATUS_CREATE = 10;// 订单下单、发布
    public static final int G_ORDER_STATUS_PAY = 12; // 订单已支付
    public static final int G_ORDER_STATUS_DISPATCH = 14; // 订单配送、运行中、运送中
    public static final int G_ORDER_STATUS_SIGN = 15; // 订单签收、到达
    public static final int G_ORDER_STATUS_SUCCESS = 19;// 订单完成
    public static final int G_ORDER_STATUS_CLOSE = -11; // 订单关闭

    public static final String S_G_ORDER_STATUS_CREATE = "未支付";// 订单下单、发布
    public static final String S_G_ORDER_STATUS_PAY = "已支付"; // 订单已支付
    public static final String S_G_ORDER_STATUS_DISPATCH = "配送中"; // 订单配送、运行中、运送中
    public static final String S_G_ORDER_STATUS_SIGN = "已签收"; // 订单签收、到达
    public static final String S_G_ORDER_STATUS_SUCCESS = "已完成";// 订单完成
    public static final String S_G_ORDER_STATUS_CLOSE = ""; // 订单关闭

    // 支付方式
    public static final String pay_WXpay = "WX_APP";    // 微信支付
    public static final String pay_Alipay = "AP_APP";   // 支付宝支付
    public static final String pay_BLANCE = "BALANCE";  // 余额支付

    //微信支付
    public static final String APP_ID = "wxc98afe2020170720";

}
