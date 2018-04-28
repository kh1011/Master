package com.yxdriver.app.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIListener;
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

public class YXSetPwdActivity extends BaseActivity {
    @BindView(R.id.setpwd_pwd)
    EditText ed_Pwd;
    @BindView(R.id.setpwd_repwd)
    EditText ed_rePwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 判断如果登录过，则进入主页
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_activity_setpwd);
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
        return new Intent(context, YXSetPwdActivity.class);
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

    @OnClick({R.id.setpwd_btn})
    void myClick(View view) {
        switch (view.getId()) {
            case R.id.setpwd_btn:
                String pwd = ed_Pwd.getText().toString().trim();
                String repwd = ed_rePwd.getText().toString().trim();
                if (StringUtil.isNullOrEmpty(pwd)) {
                    DialogUIUtils.showToast("请输入密码!");
                } else if (StringUtil.isNullOrEmpty(repwd)) {
                    DialogUIUtils.showToast("请确认密码!");
                } else if (!pwd.equals(repwd)) {
                    DialogUIUtils.showToast("两次输入密码不一致，请重新输入!");
                } else {
                    // 密码需要md5加密
                    setMyPwd(DemoApplication.getInstance().getMyToken(), MD5Util.MD5(pwd));
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        // 弹出提示
        showDialog();
    }

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            System.out.println("按下了back键   onKeyDown()");
            showDialog();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }*/

    private void showDialog() {
        DialogUIUtils.showMdAlert(context, "提示", "不设置密码，您将无法登录", true, true, new DialogUIListener() {
            @Override
            public void onPositive() {
                // 返回
                finish();
            }

            @Override
            public void onNegative() {

            }
        }).show();
    }

    /**
     * 设置密码
     *
     * @param token    tokenStr
     * @param password 密码
     */
    private void setMyPwd(String token, String password) {
        final Dialog dialog = DialogUIUtils.showMdLoading(context, "数据加载中", false, true, true, true).show();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).setPwd(token, password)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Response<ResponseBody> response) {
                        DialogUIUtils.dismiss(dialog);
                        try {
                            if (response.body() == null) {
                                Toast.makeText(context, "接口请求错误!", Toast.LENGTH_SHORT).show();
                            } else {
                                String result = response.body().string();
                                System.out.println("设置密码返回结果：" + result);
                                JSONObject jsonObject = JSON.parseObject(result);
                                if (jsonObject.getString("code").equals("success")) {
                                    DialogUIUtils.showToast(jsonObject.getString("message"));
                                    toActivity(YXSetPersonalActivity.createIntent(context));
                                    finish();
                                } else {
                                    DialogUIUtils.showToast(jsonObject.getString("message"));
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        DialogUIUtils.dismiss(dialog);
                        DialogUIUtils.showToast("网络不给力");
                    }
                });
    }
}
