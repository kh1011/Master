package com.yxclient.app.utils;

import com.yxclient.app.config.YXConfig;

/**
 * Created by mac on 2017/10/9.
 * 订单工具类
 */

public class OrderUtils {
    /**
     * 订单状态处理
     *
     * @param val 状态值
     * @return
     */
    public static final String getStatusStr(int val) {
        String status = "";
        switch (val) {
            case YXConfig.T_ORDER_STATUS_CONFIRM:
                status = YXConfig.S_ORDER_STATUS_CONFIRM;
                break;
            case YXConfig.T_ORDER_STATUS_CREATE:
                status = YXConfig.S_ORDER_STATUS_CREATE;
                break;
            case YXConfig.T_ORDER_STATUS_ACCEPT:
                status = YXConfig.S_ORDER_STATUS_ACCEPT;
                break;
            case YXConfig.T_ORDER_STATUS_GO:
                status = YXConfig.S_ORDER_STATUS_GO;
                break;
            case YXConfig.T_ORDER_STATUS_WAIT_PAY:
                status = YXConfig.S_ORDER_STATUS_WAIT_PAY;
                break;
            case YXConfig.T_ORDER_STATUS_INFO_SUCCESS:
                status = YXConfig.S_ORDER_STATUS_INFO_SUCCESS;
                break;
            case YXConfig.T_ORDER_STATUS_SUCCESS:
                status = YXConfig.S_ORDER_STATUS_SUCCESS;
                break;
            case YXConfig.T_ORDER_STATUS_CLOSE:
                status = YXConfig.S_ORDER_STATUS_CLOSE;
                break;
            default:
                break;
        }
        return status;
    }

    /**
     * 购物订单状态值处理
     *
     * @param val
     * @return
     */
    public static final String getGoodsStatusStr(int val) {
        String status = "";
        switch (val) {
            case YXConfig.G_ORDER_STATUS_CREATE:
                status = YXConfig.S_G_ORDER_STATUS_CREATE;
                break;
            case YXConfig.G_ORDER_STATUS_PAY:
                status = YXConfig.S_G_ORDER_STATUS_PAY;
                break;
            case YXConfig.G_ORDER_STATUS_DISPATCH:
                status = YXConfig.S_G_ORDER_STATUS_DISPATCH;
                break;
            case YXConfig.G_ORDER_STATUS_SIGN:
                status = YXConfig.S_G_ORDER_STATUS_SIGN;
                break;
            case YXConfig.G_ORDER_STATUS_SUCCESS:
                status = YXConfig.S_G_ORDER_STATUS_SUCCESS;
                break;
            case YXConfig.G_ORDER_STATUS_CLOSE:
                status = YXConfig.S_G_ORDER_STATUS_CLOSE;
                break;
            default:
                break;
        }
        return status;
    }
}
