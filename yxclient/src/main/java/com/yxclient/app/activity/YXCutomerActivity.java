package com.yxclient.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIListener;
import com.yxclient.app.R;
import com.yxclient.app.utils.PhoneUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zuo.biao.library.base.BaseActivity;

/**
 * Created by mac on 2017/8/21.
 * 下单成功展示页
 */

public class YXCutomerActivity extends BaseActivity {

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_customer);
        ButterKnife.bind(this);
        initView();
        initData();
        initEvent();
    }

    public static Intent createIntent(Context context, String data) {
        return new Intent(context, YXCutomerActivity.class).
                putExtra(RESULT_DATA, data);
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

    @OnClick({R.id.customer_btn})
    void myClick(View view) {
        switch (view.getId()) {
            case R.id.customer_btn:
                showDialog();
                break;
            default:
                break;
        }
    }

    private void showDialog() {
        DialogUIUtils.showAlert(context, "提示", "拨打客服电话：18887716668", "", "", "确定", "", true, true, true, new DialogUIListener() {
            @Override
            public void onPositive() {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "18887716668"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

            @Override
            public void onNegative() {
                //showToast("onNegative");
            }

        }).show();
    }
}
