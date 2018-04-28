package com.yxdriver.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIListener;
import com.yxdriver.app.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import zuo.biao.library.base.BaseActivity;

/**
 * 功能：客服中心
 * Created by yun.zhang on 2017/9/24 0024.
 * email:zy19930321@163.com
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
    @OnClick({R.id.call})
    void click(View v){
        switch (v.getId()){
            case R.id.call:
                showDialog();
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
