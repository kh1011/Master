package com.yxdriver.app.activity;

import android.app.Activity;

import zuo.biao.library.base.BaseActivity;

/**
 * Created by mac on 2017/10/10.
 * 订单支付
 * 1.	现金支付：司机收取了乘客的现金，则需要点击现金支付进行处理，
 * 点击现金支付后需要提示司机再次确认操作。现金支付完成后，
 * 直接从司机账户中扣除平台服务费。如账户余额不足，记为负数(欠费)，
 * 欠费金额大于200或欠费时间超过1天后不允许该司机再进行接单。
 */

public class YXPayActivity extends BaseActivity {

    private static final String TAG="YXPayActivity";

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }
}
