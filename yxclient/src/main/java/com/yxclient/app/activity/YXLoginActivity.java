package com.yxclient.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dou361.dialogui.DialogUIUtils;
import com.yxclient.app.R;
import com.yxclient.app.app.DemoApplication;
import com.yxclient.app.http.RetrofitHttp;
import com.yxclient.app.utils.StringUtil;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.util.MD5Util;

/**
 * Created by mac on 2017/7/1.
 * 用户登录
 */

public class YXLoginActivity extends BaseActivity {
    // 手机号
    @BindView(R.id.login_phone)
    EditText ed_Phone;
    // 密码
    @BindView(R.id.login_pwd)
    EditText ed_Pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 判断如果登录过，则进入主页
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_activity_login);
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
     * @return
     */
    public static Intent createIntent(Context context) {
        return new Intent(context, YXLoginActivity.class);
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

    }

    @Override
    public void initEvent() {

    }

    @OnClick({R.id.login_btn, R.id.login_register,R.id.login_findpwd})
    void loginClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                String mobile = ed_Phone.getText().toString().trim();
                String password = ed_Pwd.getText().toString().trim();
                if (StringUtil.isNullOrEmpty(mobile)) {
                    DialogUIUtils.showToast("请输入手机号码");
                } else if (!StringUtil.isTelPhoneNumber(mobile)) {
                    DialogUIUtils.showToast("请输入正确的手机号码");
                } else if (StringUtil.isNullOrEmpty(password)) {
                    DialogUIUtils.showToast("请输入密码");
                } else if (password.length()<6){
                    DialogUIUtils.showToast("请输入正确的密码");
                }else {
                    doLogin(mobile, MD5Util.MD5(password), "2");
                }
                break;
            case R.id.login_register:
                // 去注册
                toActivity(YXRegisterActivity.createIntent(context));
                finish();
                break;
            case R.id.login_findpwd:
                // 找回密码
                toActivity(YXForegetPwdActivity.createIntent(context, ""));
                break;
            default:
                break;
        }
    }

    /**
     * 登录
     *
     * @param phone    电话号码
     * @param password 密码
     * @param type     用户类型  1司机  2用户
     */
    private void doLogin(final String phone, String password, String type) {
        final Dialog dialog = DialogUIUtils.showMdLoading(context, "数据加载中...", true, true, true, true).show();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).userlogin(phone, password, type)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Response<ResponseBody> response) {
                        dialog.dismiss();
                        switch (response.code()) {
                            case 200:
                                try {
                                    if (response.body() == null) {
                                        try {
                                            DialogUIUtils.showToast(JSON.parseObject(response.errorBody().string()).getString("message"));
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        String result = response.body().string();
                                        System.out.println("登录返回的数据：" + result);
                                        JSONObject jsonObject = JSON.parseObject(result);
                                        if (jsonObject.getString("code").equals("success")) {
                                            // 保存用户信息
                                            JSONObject data = JSON.parseObject(jsonObject.getString("data"));
                                            JSONObject userObject = JSON.parseObject(data.getString("userInfo"));
                                            DemoApplication.getInstance().setMyToken(data.getString("token"));
                                            DemoApplication.getInstance().setUserId(userObject.getString("uuid"));
                                            onBackPressed();
                                            finish();
                                        } else {
                                            DialogUIUtils.showToast(jsonObject.getString("message"));
                                        }
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                                default:
                                    try {
                                        DialogUIUtils.showToast(JSON.parseObject(response.errorBody().string()).getString("message"));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        dialog.dismiss();
                        DialogUIUtils.showToast(t.getMessage());
                    }
                });
    }
}
