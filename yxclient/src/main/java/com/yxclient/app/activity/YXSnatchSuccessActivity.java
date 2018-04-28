package com.yxclient.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.yxclient.app.R;
import com.yxclient.app.config.YXConfig;
import com.yxclient.app.model.bean.newOrderModel;

import butterknife.ButterKnife;
import butterknife.OnClick;
import zuo.biao.library.base.BaseActivity;

/**
 * Created by mac on 2017/10/11.
 * 用户用车成功
 */

public class YXSnatchSuccessActivity extends BaseActivity {
    private newOrderModel newOrderModel;
    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_snatch_success);
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
    public static Intent createIntent(Context context,newOrderModel data) {
        return new Intent(context, YXSnatchSuccessActivity.class).
                putExtra(RESULT_DATA, data);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        //newOrderModel = (newOrderModel) getIntent().getSerializableExtra(RESULT_DATA);
        newOrderModel= (newOrderModel) getIntent().getSerializableExtra(RESULT_DATA);
    }

    @Override
    public void initEvent() {

    }

    @OnClick({R.id.snatch_btn})
    void myClick(View view) {
        switch (view.getId()) {
            case R.id.snatch_btn:
                // 查看订单详情，根据订单类型，跳转到相应的页面
                switch (newOrderModel.getOrderType()) {
                    case YXConfig.JOURNEY:
                        // 出行订单详情
                        toActivity(YXTripDetailActivity.createIntent(context, newOrderModel));
                        finish();
                        break;
                    case YXConfig.EXPRESS:
                        // 货运订单详情
                        toActivity(YXTransOrderDetailActivity.createIntent(context, newOrderModel));
                        finish();
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }
}
