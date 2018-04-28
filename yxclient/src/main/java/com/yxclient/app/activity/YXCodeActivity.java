package com.yxclient.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dou361.dialogui.DialogUIUtils;
import com.yxclient.app.R;
import com.yxclient.app.app.DemoApplication;
import com.yxclient.app.config.YXConfig;
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
 * Created by mac on 2017/7/1.
 * 验证码验证
 */

public class YXCodeActivity extends BaseActivity {
    @BindView(R.id.code_code)
    EditText ed_Code;
    private String phone = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_activity_code);
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
        return new Intent(context, YXCodeActivity.class).
                putExtra(RESULT_DATA, data);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void initView() {
        phone = getIntent().getStringExtra(RESULT_DATA);

    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        //
    }

    @OnClick({R.id.code_btn})
    void myClick(View view) {
        switch (view.getId()) {
            case R.id.code_btn:
                String code = ed_Code.getText().toString();
                if (StringUtil.isNullOrEmpty(code)) {
                    Toast.makeText(context, "请输入验证码", Toast.LENGTH_SHORT).show();
                } else {
                    doLogin(phone, "", code, YXConfig.type);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 登录
     *
     * @param mobile   手机号
     * @param password 密码
     * @param code     验证码
     * @param type     用户类型
     */
    private void doLogin(String mobile, String password, String code, String type) {
        /*final Dialog dialog = DialogUIUtils.showMdLoading(context, "数据加载中...", true, true, true, true).show();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).login(mobile, password, code, type)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Response<ResponseBody> response) {
                        dialog.dismiss();
                        switch (response.code()) {
                            case 200:
                                try {
                                    if (response.body() == null) {
                                        DialogUIUtils.showToast("接口请求错误!");
                                    } else {
                                        String result = response.body().string();
                                        System.out.println("登录：" + result);
                                        JSONObject jsonObject = JSON.parseObject(result);
                                        if (jsonObject.getString("code").equals("success")) {
                                            // 登录成功 ，进入主界面
                                            //DemoApplication.getInstance().setMyToken(jsonObject.getString("data"));
                                            JSONObject object = JSON.parseObject(jsonObject.getString("data"));
                                            DemoApplication.getInstance().setMyToken(object.getString("token"));
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
                                DialogUIUtils.showToast(response.message());
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        dialog.dismiss();
                        DialogUIUtils.showToast(t.getMessage());
                    }
                });*/
    }
}
