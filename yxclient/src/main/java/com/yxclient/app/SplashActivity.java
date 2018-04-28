/*Copyright ©2015 TommyLemon(https://github.com/TommyLemon)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package com.yxclient.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dou361.dialogui.DialogUIUtils;
import com.yxclient.app.activity.YXLoginActivity;
import com.yxclient.app.activity.YXMainActivity;
import com.yxclient.app.activity.YXWelcomeActivity;
import com.yxclient.app.app.DemoApplication;
import com.yxclient.app.http.RetrofitHttp;
import com.yxclient.app.model.bean.UserModel;
import com.yxclient.app.utils.PreferenceUtils;
import com.yxclient.app.utils.StringUtil;

import java.io.IOException;

import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;
import zuo.biao.library.base.BaseActivity;

/**
 * 闪屏activity，保证点击桌面应用图标后无延时响应
 *
 * @author Lemon
 */
public class SplashActivity extends BaseActivity {

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splas);
        ButterKnife.bind(this);
        initView();
        initData();
        initEvent();
    }

    @Override
    public void initView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                DoAction();
            }
        }, 3000);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }

    private void DoAction() {
        boolean isFirst = PreferenceUtils.readBoolean(this, "First", "isFirst", true);
        if (isFirst) {
            PreferenceUtils.write(this, "First", "isFirst", false);
            Welcome();
        } else {
            ComeingApp();
        }
    }

    private void Welcome() {
        Intent intent = new Intent();
        intent.setClass(this, YXWelcomeActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 直接进入app
     */
    private void ComeingApp() {
        /*if (!StringUtil.isNullOrEmpty(DemoApplication.getInstance().getMyToken())) {
            //getUserInfo(DemoApplication.getInstance().getMyToken());
        } else {
            *//*toActivity(YXMainActivity.createIntent(context));
            finish();*//*
            startActivity(new Intent(context, YXMainActivity.class));
            finish();
        }*/
        startActivity(new Intent(SplashActivity.this, YXMainActivity.class));
        finish();
    }

    private void getUserInfo(String token) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).getUserInfo(token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                System.out.println("xxx");
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
                                    startActivity(new Intent(context, YXMainActivity.class));
                                    //toActivity(YXMainActivity.createIntent(context));
                                    finish();
                                } else {
                                    DialogUIUtils.showToast(jsonObject.getString("message"));
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 401:
                            // token过期
                            DialogUIUtils.showToast("token过期");
                            toActivity(YXLoginActivity.createIntent(context));
                            break;
                        default:
                            DialogUIUtils.showToast("数据获取失败");
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

    @Override
    protected void onResume() {
        super.onResume();
        /*if (!StringUtil.isNullOrEmpty(DemoApplication.getInstance().getMyToken())) {
            startActivity(new Intent(context, YXMainActivity.class));
            finish();
        }*/
    }
}