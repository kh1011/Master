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

package com.yxdriver.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.yxdriver.app.activity.WelcomeActivity;
import com.yxdriver.app.activity.YXLoginActivity;
import com.yxdriver.app.utils.PreferenceUtils;

import butterknife.ButterKnife;
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
        intent.setClass(this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 直接进入app
     */
    private void ComeingApp() {
        Intent intent = new Intent();
        intent.setClass(this, YXLoginActivity.class);
        startActivity(intent);
        finish();
    }
}