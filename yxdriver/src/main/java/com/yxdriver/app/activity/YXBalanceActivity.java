package com.yxdriver.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dou361.dialogui.DialogUIUtils;
import com.yxdriver.app.R;
import com.yxdriver.app.app.DemoApplication;
import com.yxdriver.app.http.RetrofitHttp;
import com.yxdriver.app.model.bean.UserModel;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;
import zuo.biao.library.base.BaseActivity;

/**
 * Created by mac on 2017/9/18.
 * 我的余额
 */

public class YXBalanceActivity extends BaseActivity {
    @BindView(R.id.blance_money)
    TextView tv_Blance;
    Float money;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 判断如果登录过，则进入主页
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_activity_blance);
        ButterKnife.bind(this);
        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }
    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    public static Intent createIntent(Context context) {
        return new Intent(context, YXBalanceActivity.class);
    }
    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        getUserInfo(DemoApplication.getInstance().getMyToken());
    }

    @Override
    public void initEvent() {

    }

    @OnClick({R.id.blance_get,R.id.blance_set})
    void myClick(View view) {
        switch (view.getId()) {
            case R.id.blance_get:
                toActivity(YXRechargeActivity.createIntent(context));
                break;
            case R.id.blance_set:
                toActivity(YXCashActivity.createIntent(context,money));
                break;
            default:
                break;
        }
    }

    private void getUserInfo(String token) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).getUserInfo(token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                if (response == null) {
                    DialogUIUtils.showToast(response.message());
                } else {
                    switch (response.code()) {
                        case 200:
                            try {
                                String result = response.body().string();
                                System.out.println("获取用户信息:" + result);
                                JSONObject jsonObject = JSON.parseObject(result);
                                if ("success".equals(jsonObject.getString("code"))) {
                                    drawaView(JSON.parseObject(jsonObject.getString("data"), UserModel.class));
                                } else {
                                    DialogUIUtils.showToast(jsonObject.getString("message"));
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        default:
                            DialogUIUtils.showToast(response.message());
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                DialogUIUtils.showToast(t.getMessage());
            }
        });
    }

    private void drawaView(UserModel model) {
        tv_Blance.setText(String.valueOf(model.getBalance()/100));
        money=model.getBalance()/100;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo(DemoApplication.getInstance().getMyToken());
    }
}
