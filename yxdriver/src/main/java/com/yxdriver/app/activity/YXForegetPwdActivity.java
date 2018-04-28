package com.yxdriver.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dou361.dialogui.DialogUIUtils;
import com.hys.utils.MD5Utils;
import com.yxdriver.app.R;
import com.yxdriver.app.app.DemoApplication;
import com.yxdriver.app.http.RetrofitHttp;
import com.yxdriver.app.utils.CodeUtil;
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
 * 找回密码
 */
public class YXForegetPwdActivity extends BaseActivity {
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
    // 新密码
    @BindView(R.id.regist_ed_pwd)
    EditText ed_Pwd;
    // 确认密码
    @BindView(R.id.regist_ed_repwd)
    EditText ed_rePwd;
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
        setContentView(R.layout.yx_foreget_pwd);
        ButterKnife.bind(this);
        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }
    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    public static Intent createIntent(Context context, String data) {
        return new Intent(context, YXForegetPwdActivity.class).
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
                String pwd = ed_Pwd.getText().toString();
                String rePwd = ed_rePwd.getText().toString();
                if (StringUtil.isNullOrEmpty(code)) {
                    DialogUIUtils.showToast("请输入验证码");
                } else if (StringUtil.isNullOrEmpty(mobile1)) {
                    DialogUIUtils.showToast("请先获取验证码");
                } else if (StringUtil.isNullOrEmpty(pwd)) {
                    DialogUIUtils.showToast("请输入新密码");
                } else if (StringUtil.isNullOrEmpty(rePwd)) {
                    DialogUIUtils.showToast("请确认密码");
                } else if (!pwd.equals(rePwd)) {
                    DialogUIUtils.showToast("两次密码不一致，请确认");
                } else {
                    // 找回密码
                    findPwd(DemoApplication.getInstance().getMyToken(), mobile1, MD5Util.MD5(pwd).toLowerCase(), code, "1");
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

    /**
     * 获取验证码
     *
     * @param phone
     */
    private void getCode(String phone) {
        final Dialog dialog = DialogUIUtils.showMdLoading(context, "数据加载中", false, true, true, true).show();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).getCode(phone,1)
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


    /**
     * 找回密码/修改密码
     *
     * @param mobile   手机号
     * @param password 密码
     * @param code     验证码
     * @param type     用户类型
     */
    private void findPwd(String token, String mobile, String password, String code, String type) {
        final Dialog dialog = DialogUIUtils.showMdLoading(context, "数据加载中", false, true, true, true).show();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).findPassword(token, mobile, password, code, type)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Response<ResponseBody> response) {
                        DialogUIUtils.dismiss(dialog);
                        switch (response.code()) {
                            case 200:
                                try {
                                    if (response.body() == null) {
                                        Toast.makeText(context, "接口请求错误!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        String result = response.body().string();
                                        System.out.println("注册返回结果：" + result);
                                        JSONObject jsonObject = JSON.parseObject(result);
                                        if (jsonObject.getString("code").equals("success")) {
                                            DialogUIUtils.showToast(jsonObject.getString("message"));
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
                        DialogUIUtils.dismiss(dialog);
                        DialogUIUtils.showToast(t.getMessage());
                    }
                });
    }
}