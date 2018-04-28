package com.yxclient.app.model.bean;

import java.io.Serializable;

/**
 * Created by mac on 2017/11/14.
 * ////////////////////////////////////////////////////////////////////
 * //                            _ooOoo_                             //
 * //                           o8888888o                            //
 * //                           88" . "88                            //
 * //                           (| ^_^ |)                            //
 * //                           O\  =  /O                            //
 * //                        ____/`---'\____                         //
 * //                      .'  \\|     |//  `.                       //
 * //                     /  \\|||  :  |||//  \                      //
 * //                    /  _||||| -:- |||||-  \                     //
 * //                    |   | \\\  -  /// |   |                     //
 * //                    | \_|  ''\---/''  |   |                     //
 * //                    \  .-\__  `-`  ___/-. /                     //
 * //                  ___`. .'  /--.--\  `. . ___                   //
 * //                ."" '<  `.___\_<|>_/___.'  >'"".                //
 * //              | | :  `- \`.;`\ _ /`;.`/ - ` : | |               //
 * //              \  \ `-.   \_ __\ /__ _/   .-` /  /               //
 * //        ========`-.____`-.___\_____/___.-`____.-'========       //
 * //                             `=---='                            //
 * //        ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^      //
 * //         佛祖保佑       永无BUG        永不修改                    //
 * ////////////////////////////////////////////////////////////////////
 */
// 出行订单支付参数模型
public class TripPayModel implements Serializable {
    private static final long serialVersionUID = 6112231155199992286L;
    private String orderNo;       // 订单号
    private String extraInfoUUID; // 客户编号
    private String channel;       // 支付方式
    private int amount;       // 应付金额

    public TripPayModel() {
    }

    public TripPayModel(String orderNo, String extraInfoUUID, String channel, int amount) {
        this.orderNo = orderNo;
        this.extraInfoUUID = extraInfoUUID;
        this.channel = channel;
        this.amount = amount;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getExtraInfoUUID() {
        return extraInfoUUID;
    }

    public void setExtraInfoUUID(String extraInfoUUID) {
        this.extraInfoUUID = extraInfoUUID;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
