package com.yxclient.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.yxclient.app.R;

import butterknife.ButterKnife;
import zuo.biao.library.base.BaseActivity;

/**
 * Created by mac on 2017/9/9.
 * 商品详情
 */

public class YXGoodsDetailActivity extends BaseActivity {
    private String goodsId;

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_goods_detail);
        ButterKnife.bind(this);
        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }
    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


    /**
     * 启动这个Activity的Intent
     *
     * @param context
     * @param data
     * @return
     */
    public static Intent createIntent(Context context, String data) {
        return new Intent(context, YXGoodsDetailActivity.class).
                putExtra(RESULT_DATA, data);
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, YXGoodsDetailActivity.class);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public void initView() {
        goodsId = getIntent().getStringExtra(RESULT_DATA);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }
}
