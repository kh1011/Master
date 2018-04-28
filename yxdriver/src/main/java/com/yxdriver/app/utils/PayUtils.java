package com.yxdriver.app.utils;

import android.app.Activity;
import android.support.v4.app.Fragment;

import zuo.biao.library.pays.sdk.PrePayOrderInfo;

/**
 * Created by mac on 2017/10/25.
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

public class PayUtils {
    // private static String testAliOrderInfo = "partner=\"2088121771207747\"&seller_id=\"cloudtone@163.com\"&out_trade_no=\"201611011806231255910\"&subject=\"流量充值\"&body=\"人民币与云币兑换比例为1:100\"&total_fee=\"0.01\"&notify_url=\"http://112.95.168.216:41002/skysimApp/aliPayNotifyV2\"&service=\"mobile.securitypay.pay\"&payment_type=\"1\"&_input_charset=\"utf-8\"&it_b_pay=\"7d\"&return_url=\"m.alipay.com\"&sign=\"M3K6Yea6e6%2B6pBgn%2BfEmeduuKtUtjPJURRqf%2BZ9LN%2FuqGN2ZVgQho1fNgGHE0Egn8hcPaTkGfyYjW2oKHcKsWihN5ZOMndwNlXZkMkZpGxXz7X5E2gA3TX7JHw1E8au23MCLGL8OCGxKJJ6EMtHwuqd1ciGWHcfrPWmACtfhGgg%3D\"&sign_type=\"RSA\"";

    /**
     * 测试用的服务器返回的微信支付订单信息Json类型数据
     * 这里需要将该Josn数据转换成PrePayOrderInfo对象
     */
    // private static String testWxServerRespData = "{\"appid\":\"wxb4bbf0651d312ab6\",\"partnerid\":\"1372735502\",\"prepayid\":\"wx20161101180444057d3200540481820870\",\"noncestr\":\"6xzactdvq9ct817m7dnoq8u1w3snnr3x\",\"timestamp\":\"1477994684\",\"package\":\"Sign=WXPay\",\"paySign\":\"A8915C273F63CBACA54C360E283EEDC0\",\"orderNo\":\"201611011804441132840\"}";

    /**
     * 微信支付
     *
     * @param curActivity
     * @param curRequestCode
     * @param testWxServerRespData
     */
    public static void doWxPay(Activity curActivity, int curRequestCode, String testWxServerRespData) {
        CustomOrderInfo wxOrderInfo = new CustomOrderInfo();
        wxOrderInfo.parseDataFromJsonStr(testWxServerRespData);
        //PayEntryActivity.startPayActivity(curActivity, wxOrderInfo, curRequestCode, WXPayEntryActivity.class);
    }

    /**
     * fragment里的微信支付
     *
     * @param fragment
     * @param curRequestCode
     * @param testWxServerRespData
     */
    public static void doWxPayInFragment(Fragment fragment, int curRequestCode, String testWxServerRespData) {
        CustomOrderInfo wxOrderInfo = new CustomOrderInfo();
        wxOrderInfo.parseDataFromJsonStr(testWxServerRespData);
        //PayEntryActivity.startPayActivity(fragment, wxOrderInfo, curRequestCode, WXPayEntryActivity.class);
    }

    /**
     * 支付宝支付
     *
     * @param curActivity
     * @param curRequestCode
     * @param testAliOrderInfo
     */
    public static void doZfbPay(Activity curActivity, int curRequestCode, String testAliOrderInfo) {
        PrePayOrderInfo alipayOrderInfo = new PrePayOrderInfo();
        alipayOrderInfo.setAlipayInfo(testAliOrderInfo);
        //PayEntryActivity.startPayActivity(curActivity, alipayOrderInfo, curRequestCode, WXPayEntryActivity.class);
    }

    /**
     * fragment里的支付宝支付
     *
     * @param fragment
     * @param curRequestCode
     * @param testAliOrderInfo
     */
    public static void doZfbPayInFragment(Fragment fragment, int curRequestCode, String testAliOrderInfo) {
        PrePayOrderInfo alipayOrderInfo = new PrePayOrderInfo();
        alipayOrderInfo.setAlipayInfo(testAliOrderInfo);
        //PayEntryActivity.startPayActivity(fragment, alipayOrderInfo, curRequestCode, WXPayEntryActivity.class);
    }
}
