package com.yxdriver.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

/**
 * Created by mac on 2017/9/11.
 * 司机注册
 */

public class YXRegisterActivity extends BaseActivity {
    @BindView(R.id.regist_get_code)
    RelativeLayout re_get;
    @BindView(R.id.regist_timer)
    TextView tv_Timer;
    @BindView(R.id.regist_ed_phone)
    EditText ed_Phone;
    @BindView(R.id.regist_ed_code)
    EditText ed_Code;
    @BindView(R.id.userAgreementCB)
    CheckBox ck_userAgreement;
    private String codeStr;

    private boolean isRemenber = true;

    //new倒计时对象,总共的时间,每隔多少秒更新一次时间
    final MyCountDownTimer myCountDownTimer = new MyCountDownTimer(60000, 1000);

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
        ck_userAgreement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    isRemenber=false;
                }else {
                    isRemenber=true;
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


    @OnClick({R.id.regist_btn, R.id.regist_get_code,R.id.copyright})
    void myClick(View view) {
        switch (view.getId()) {
            case R.id.regist_btn:

                String code = ed_Code.getText().toString();
                String mobile1 = ed_Phone.getText().toString();
                if(StringUtil.isNullOrEmpty(code)) {
                    DialogUIUtils.showToast("请输入验证码");
                } else if(StringUtil.isNullOrEmpty(mobile1)) {
                    DialogUIUtils.showToast("请先获取验证码");
                } else if (!isRemenber){
                    DialogUIUtils.showToast("请查看并同意用户协议");
                } else {
                    doRegist(mobile1, "", code, "1");
                }
                break;
            case R.id.regist_get_code:
                // 获取验证码
                String mobile = ed_Phone.getText().toString();
                if (!isRemenber){
                    DialogUIUtils.showToast("请查看并同意用户协议");
                }else if (StringUtil.isNullOrEmpty(mobile)) {
                    Toast.makeText(context, "请输入手机号", Toast.LENGTH_SHORT).show();
                } else if (!StringUtil.isTelPhoneNumber(mobile)) {
                    Toast.makeText(context, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                } else {
                    myCountDownTimer.start();
                    getCode(mobile);
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
        final Dialog dialog = DialogUIUtils.showMdLoading(context, "数据加载中", false, true, true, true).show();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).getCode(phone,1)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Response<ResponseBody> response) {
                        DialogUIUtils.dismiss(dialog);
                        try {
                            if (response.body() == null) {
                                Toast.makeText(context, "接口请求错误!", Toast.LENGTH_SHORT).show();
                            } else {
                                String result = response.body().string();
                                System.out.println(result);
                                JSONObject jsonObject = JSON.parseObject(result);
                                if (jsonObject.getString("code").equals("success")) {
                                    //toActivity(YXSetPwdActivity.createIntent(context));
                                    //finish();
                                    codeStr = jsonObject.getString("data");
                                } else {
                                    Toast.makeText(context, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        DialogUIUtils.dismiss(dialog);
                        Toast.makeText(context, "网络不给力", Toast.LENGTH_SHORT).show();
                    }
                });
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

    /**
     * 注册
     *
     * @param mobile   手机号
     * @param password 密码
     * @param code     验证码
     * @param type     用户类型
     */
    private void doRegist(String mobile, String password, String code, String type) {
        final Dialog dialog = DialogUIUtils.showMdLoading(context, "数据加载中", false, true, true, true).show();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).regist(mobile, password, code, type)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Response<ResponseBody> response) {
                        DialogUIUtils.dismiss(dialog);
                        try {
                            if (response.body() == null) {
                                DialogUIUtils.showToast(JSON.parseObject(response.errorBody().string()).getString("message"));
                                //Toast.makeText(context, "接口请求错误!", Toast.LENGTH_SHORT).show();
                            } else {
                                String result = response.body().string();
                                System.out.println("注册返回结果：" + result);
                                JSONObject jsonObject = JSON.parseObject(result);
                                if (jsonObject.getString("code").equals("success")) {
                                    DialogUIUtils.showToast(jsonObject.getString("message"));
                                    DemoApplication.getInstance().setMyToken(jsonObject.getString("data"));
                                    toActivity(YXSetPwdActivity.createIntent(context));
                                    finish();
                                } else {
                                    Toast.makeText(context, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        DialogUIUtils.dismiss(dialog);
                        Toast.makeText(context, "网络不给力", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
