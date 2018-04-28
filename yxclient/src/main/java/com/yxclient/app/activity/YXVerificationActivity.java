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
import com.dou361.dialogui.listener.DialogUIListener;
import com.yxclient.app.R;
import com.yxclient.app.app.DemoApplication;
import com.yxclient.app.http.RetrofitHttp;
import com.yxclient.app.model.bean.verifyDriverModel;
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
 * Created by mac on 2017/8/21.
 * 验证司机
 */

public class YXVerificationActivity extends BaseActivity {
    @BindView(R.id.verification_content)
    EditText editText;

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_verification);
        ButterKnife.bind(this);
        initView();
        initData();
        initEvent();
    }

    public static Intent createIntent(Context context, String data) {
        return new Intent(context, YXVerificationActivity.class).
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

    @OnClick({R.id.verification_btn})
    void myClick(View view) {
        switch (view.getId()) {
            case R.id.verification_btn:
                String content = editText.getText().toString();
                if (StringUtil.isNullOrEmpty(content)) {
                    DialogUIUtils.showToast("请输入司机手机号或车牌号");
                } else {
                    doVerification(DemoApplication.getInstance().getMyToken(), content);
                }
                break;
        }
    }

    private void doVerification(String token, String number) {
        final Dialog dialog = DialogUIUtils.showMdLoading(context, "数据加载中", true, true, true, true).show();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).verifyDriver(token, number).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                DialogUIUtils.dismiss(dialog);
                if (response.body() == null) {
                    try {
                        DialogUIUtils.showToast(JSON.parseObject(response.errorBody().string()).getString("message"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    switch (response.code()) {
                        case 200:
                            try {
                                String result = response.body().string();
                                System.out.println("司机验证信息:" + result);
                                JSONObject jsonObject = JSON.parseObject(result);
                                JSONObject data_=JSON.parseObject(jsonObject.getString("data"));
                                if (data_.getString("car")==null){
                                    DialogUIUtils.showToast("无车辆信息");
                                }else if ("success".equals(jsonObject.getString("code"))) {
                                    toActivity(YXDriverInfoActivity.createIntent(context, JSON.parseObject(jsonObject.getString("data"), verifyDriverModel.class)));
                                } else {
                                    DialogUIUtils.showAlert(context, "验证结果", jsonObject.getString("message"), "", "", "确定", "", true, true, true, new DialogUIListener() {
                                        @Override
                                        public void onPositive() {

                                        }

                                        @Override
                                        public void onNegative() {

                                        }

                                    }).show();
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
            }

            @Override
            public void onFailure(Throwable t) {
                DialogUIUtils.dismiss(dialog);
                DialogUIUtils.showToast(t.getMessage());
            }
        });
    }
}
