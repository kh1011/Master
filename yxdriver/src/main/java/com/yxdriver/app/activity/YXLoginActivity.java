package com.yxdriver.app.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dou361.dialogui.DialogUIUtils;
import com.yxdriver.app.R;
import com.yxdriver.app.app.DemoApplication;
import com.yxdriver.app.http.RetrofitHttp;
import com.yxdriver.app.utils.StringUtil;

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
 * Created by mac on 2017/9/11.
 * 登录
 */

public class YXLoginActivity extends BaseActivity {
    @BindView(R.id.login_ed_phone)
    EditText ed_Phone;
    @BindView(R.id.login_ed_pwd)
    EditText ed_Pwd;
    @BindView(R.id.checkbox_login)
    CheckBox checkBox;
    private boolean isRemenber = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 判断如果登录过，则进入主页
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_activity_login);
        ButterKnife.bind(this);
        if (shouldAskPermissions()) {
            askPermissions();
        }
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
        String name = DemoApplication.getInstance().getName();
        String pwd = DemoApplication.getInstance().getPwd();
        checkBox.setChecked(true);
        if (!StringUtil.isNullOrEmpty(name) && !StringUtil.isNullOrEmpty(pwd)) {
            checkBox.isChecked();
            ed_Phone.setText(name);
            ed_Pwd.setText(pwd);
        } else {
            ed_Phone.setText("");
            ed_Pwd.setText("");
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isRemenber = true;
                } else {
                    isRemenber = false;
                }
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }

    @OnClick({R.id.login_regist, R.id.login_btn, R.id.login_foreget_pwd})
    void myClick(View view) {
        switch (view.getId()) {
            case R.id.login_regist:
                toActivity(YXRegisterActivity.createIntent(context));
                break;
            case R.id.login_btn:
                // 司机登录
                String mobile = ed_Phone.getText().toString().trim();
                String pwd = ed_Pwd.getText().toString().trim();
                if (StringUtil.isNullOrEmpty(mobile)) {
                    DialogUIUtils.showToast("请输入用户名!");
                } else if (StringUtil.isNullOrEmpty(pwd)) {
                    DialogUIUtils.showToast("请输入密码!");
                } else if (!StringUtil.isTelPhoneNumber(mobile)) {
                    DialogUIUtils.showToast("请输入正确手机号!");
                } else {
                    if (isRemenber) {
                        DemoApplication.getInstance().setName(mobile);
                        DemoApplication.getInstance().setPwd(pwd);
                    }
                    // 执行登录
                    doLogin(mobile, MD5Util.MD5(pwd).toLowerCase());
                }
                break;
            case R.id.login_foreget_pwd:
                toActivity(YXForegetPwdActivity.createIntent(context, ""));
                break;
            default:
                break;
        }
    }

    //动态申请权限
    protected boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(23)
    protected void askPermissions() {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE",
                "android.permission.ACCESS_COARSE_LOCATION",
                "android.permission.CAMERA"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }

    /**
     * 司机登录
     *
     * @param mobile   手机号
     * @param password 密码
     */
    private void doLogin(String mobile, String password) {
        final Dialog dialog = DialogUIUtils.showMdLoading(context, "数据加载中", true, true, true, true).show();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).login(mobile, password, "1")
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Response<ResponseBody> response) {
                        DialogUIUtils.dismiss(dialog);
                        switch (response.code()) {
                            case 200:
                                try {
                                    if (response.body() == null) {
                                        DialogUIUtils.showToast(response.message());
                                    } else {
                                        String result = response.body().string();
                                        System.out.println("登录返回结果：" + result);
                                        JSONObject jsonObject = JSON.parseObject(result);
                                        if (jsonObject.getString("code").equals("success")) {
                                            DialogUIUtils.showToast(jsonObject.getString("message"));
                                            JSONObject object = JSON.parseObject(jsonObject.getString("data"));
                                            JSONObject userObject = JSON.parseObject(object.getString("userInfo"));
                                            DemoApplication.getInstance().setMyToken(object.getString("token"));
                                            DemoApplication.getInstance().setVerify(userObject.getString("verify"));
                                            DemoApplication.getInstance().setUserId(userObject.getString("uuid"));
                                            toActivity(YXMainActivity.createIntent(context));
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
                                    String result = response.errorBody().string();
                                    DialogUIUtils.showToast(JSON.parseObject(result).getString("message"));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        DialogUIUtils.dismiss(dialog);
                        DialogUIUtils.showToast(t.getMessage());
                    }
                });
    }
}
