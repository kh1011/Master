package com.yxclient.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.yxclient.app.R;
import com.yxclient.app.app.DemoApplication;
import com.yxclient.app.utils.CodeUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zuo.biao.library.base.BaseActivity;

/**
 * Created by mac on 2017/8/21.
 * 关于我们
 */

public class YXAboutusActivity extends BaseActivity {
    @BindView(R.id.about_code_img)
    ImageView imageView;

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_about);
        ButterKnife.bind(this);
        initView();
        initData();
        initEvent();
    }


    public static Intent createIntent(Context context, String data) {
        return new Intent(context, YXAboutusActivity.class).
                putExtra(RESULT_DATA, data);
    }

    @Override
    public void initView() {
        imageView.setImageBitmap(CodeUtil.createQRImage(
                DemoApplication.getInstance().getApkUrl()
        ));
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }

    @OnClick({R.id.about_company, R.id.about_app})
    void myClick(View view) {
        switch (view.getId()) {
            case R.id.about_company:
                // 关于公司简介
                toActivity(YXAboutcompanyActivity.createIntent(context, ""));
                break;
            case R.id.about_app:
                // app介绍
                toActivity(YXAboutappActivity.createIntent(context, ""));
                break;
            default:
                break;
        }
    }
}
