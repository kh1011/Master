package com.yxclient.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
 * 用户注册
 */

public class YXRegisterActivity extends BaseActivity {
    @BindView(R.id.register_phone)
    EditText ed_Phone;
    @BindView(R.id.register_code)
    EditText ed_Code;
    @BindView(R.id.register_pwd)
    EditText ed_Pwd;

    @BindView(R.id.register_sendcode)
    TextView tv_getCode;

    @BindView(R.id.userAgreementCB)
    CheckBox ck_userAgreement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 判断如果登录过，则进入主页
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_activity_register);
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
        return new Intent(context, YXRegisterActivity.class);
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

    @OnClick({R.id.register_btn, R.id.register_sendcode,R.id.copyright})
    void myClick(View view) {
        switch (view.getId()) {
            case R.id.register_btn:
                String mobile = ed_Phone.getText().toString();
                String code = ed_Code.getText().toString().trim();
                String password = ed_Pwd.getText().toString();
                if (StringUtil.isNullOrEmpty(mobile)) {
                    DialogUIUtils.showToast("请输入手机号码");
                } else if (!StringUtil.isTelPhoneNumber(mobile)) {
                    DialogUIUtils.showToast("请输入正确的手机号码");
                } else if (StringUtil.isNullOrEmpty(code)) {
                    DialogUIUtils.showToast("请输入验证码");
                } else if (StringUtil.isNullOrEmpty(password)) {
                    DialogUIUtils.showToast("请输入密码");
                } else if (password.length()<6){
                    DialogUIUtils.showToast("请输入正确的密码");
                }else if (!ck_userAgreement.isChecked()){
                DialogUIUtils.showToast("请查看并同意用户协议");
                }else {
                    doRegister(mobile, MD5Util.MD5(password), code, "2");
                }
                break;
            case R.id.register_sendcode:
                // 发送验证码
                // 设置不可点击
                String number = ed_Phone.getText().toString().trim();
                if (!ck_userAgreement.isChecked()){
                    DialogUIUtils.showToast("请查看并同意用户协议");
                }else if (StringUtil.isNullOrEmpty(number)) {
                    DialogUIUtils.showToast("请输入电话号码!");
                } else if (!StringUtil.isTelPhoneNumber(number)) {
                    DialogUIUtils.showToast("请输入正确的电话号码!");
                } else {
                    tv_getCode.setEnabled(false);
                    timer.start();
                    // 获取验证码
                    getCode(ed_Phone.getText().toString().trim());
                }
                break;
            case R.id.copyright:
                toActivity(YXCopyrightActivity.createIntent(context,""));
                break;
            default:
                break;
        }
    }

    /**
     * 获取验证码
     *
     * @param phone
     */
    private void getCode(final String phone) {
        final Dialog dialog = DialogUIUtils.showMdLoading(context, "数据加载中...", true, true, true, true).show();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).getCode(phone,2)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Response<ResponseBody> response) {
                        dialog.dismiss();
                        switch (response.code()) {
                            case 200:
                                try {
                                    if (response.body() == null) {
                                        DialogUIUtils.showToast("接口请求错误");
                                    } else {
                                        String result = response.body().string();
                                        System.out.println("获取验证码：" + result);
                                        JSONObject jsonObject = JSON.parseObject(result);
                                        if (jsonObject.getString("code").equals("success")) {
                                            System.out.println("获取验证码成功");
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

    /**
     * 倒计时
     */
    CountDownTimer timer = new CountDownTimer(60000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            tv_getCode.setText("剩余" + millisUntilFinished / 1000 + "秒");
        }

        @Override
        public void onFinish() {
            tv_getCode.setEnabled(true);
            tv_getCode.setText("发送验证码");
        }
    };

    /**
     * 用户注册
     *
     * @param mobile   手机号码
     * @param password 电话号码
     * @param code     验证码
     * @param type     用户类型
     */
    private void doRegister(String mobile, String password, String code, String type) {
        final Dialog dialog = DialogUIUtils.showMdLoading(context, "数据加载中...", true, true, true, true).show();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).userRegister(mobile, password, code, type)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Response<ResponseBody> response) {
                        dialog.dismiss();
                        switch (response.code()) {
                            case 200:
                                try {
                                    if (response.body() == null) {
                                        DialogUIUtils.showToast(response.message());
                                    } else {
                                        String result = response.body().string();
                                        System.out.println("获取验证码：" + result);
                                        JSONObject jsonObject = JSON.parseObject(result);
                                        if (jsonObject.getString("code").equals("success")) {
                                            // 注册成功 保存token
                                            DemoApplication.getInstance().setMyToken(jsonObject.getString("data"));
                                            JSONObject userObject = JSON.parseObject(jsonObject.getString("userInfo"));
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
                                DialogUIUtils.showToast(response.message());
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
