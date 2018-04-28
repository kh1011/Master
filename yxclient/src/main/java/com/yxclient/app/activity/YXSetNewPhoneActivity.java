package com.yxclient.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

/**
 * 设置新手机号
 */
public class YXSetNewPhoneActivity extends BaseActivity {
    @BindView(R.id.regist_get_code)
    RelativeLayout re_get;
    // 获取验证码
    @BindView(R.id.regist_timer)
    TextView tv_Timer;
    // 手机号输入框
    @BindView(R.id.regist_ed_phone)
    EditText ed_Phone;
    // 验证码输入框
    @BindView(R.id.regist_ed_code)
    EditText ed_Code;

    private String codeStr;

    //new倒计时对象,总共的时间,每隔多少秒更新一次时间
    final MyCountDownTimer myCountDownTimer = new MyCountDownTimer(60000, 1000);

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_chang_phone);
        ButterKnife.bind(this);
        initView();
        initData();
        initEvent();
    }

    public static Intent createIntent(Context context, String data) {
        return new Intent(context, YXSetNewPhoneActivity.class).
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

    @OnClick({R.id.regist_btn, R.id.regist_get_code})
    void myClick(View view) {
        switch (view.getId()) {
            case R.id.regist_btn:
                String code = ed_Code.getText().toString();
                String mobile1 = ed_Phone.getText().toString();
                if (StringUtil.isNullOrEmpty(code)) {
                    DialogUIUtils.showToast("请输入验证码");
                } else if (StringUtil.isNullOrEmpty(mobile1)) {
                    DialogUIUtils.showToast("请先获取验证码");
                } else {
                    // 找回密码
                    doChanager(DemoApplication.getInstance().getMyToken(), mobile1, code);
                }
                break;
            case R.id.regist_get_code:
                // 获取验证码
                String mobile = ed_Phone.getText().toString();
                if (StringUtil.isNullOrEmpty(mobile)) {
                    DialogUIUtils.showToast("请输入手机号");
                } else if (!StringUtil.isTelPhoneNumber(mobile)) {
                    DialogUIUtils.showToast("请输入正确的手机号");
                } else {
                    myCountDownTimer.start();
                    getCode(mobile);
                }
                break;
            default:
                break;
        }
    }

    //复写倒计时
    private class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //计时过程
        @Override
        public void onTick(long l) {
            //防止计时过程中重复点击
            re_get.setClickable(false);
            tv_Timer.setText(l / 1000 + "s");

        }

        //计时完毕的方法
        @Override
        public void onFinish() {
            //重新给Button设置文字
            tv_Timer.setText("重新获取验证码");
            //设置可点击
            re_get.setClickable(true);
        }
    }

    private void getCode(String phone) {
        final Dialog dialog = DialogUIUtils.showMdLoading(context, "数据加载中", false, true, true, true).show();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).getCode(phone,2)
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
                                        System.out.println(result);
                                        JSONObject jsonObject = JSON.parseObject(result);
                                        if (jsonObject.getString("code").equals("success")) {
                                            codeStr = jsonObject.getString("data");
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
                        DialogUIUtils.dismiss(dialog);
                        DialogUIUtils.showToast(t.getMessage());
                    }
                });
    }

    private void doChanager(String token, String mobile, String code) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).changePhone(token, mobile, code).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                if (response == null) {
                    DialogUIUtils.showToast("请求错误!");
                } else {
                    switch (response.code()) {
                        case 200:
                            try {
                                String result = response.body().string();
                                JSONObject jsonObject = JSON.parseObject(result);
                                if ("success".equals(jsonObject.getString("code"))) {
                                    // 修改手机号码成功，返回
                                    DialogUIUtils.showToast(jsonObject.getString("message"));
                                    onBackPressed();
                                    finish();
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
}