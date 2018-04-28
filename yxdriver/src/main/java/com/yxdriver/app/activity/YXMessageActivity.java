package com.yxdriver.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.yxdriver.app.R;
import com.yxdriver.app.utils.CodeUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zuo.biao.library.base.BaseActivity;

/**
 * 系统消息
 */
public class YXMessageActivity extends BaseActivity {
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
        return new Intent(context, YXMessageActivity.class).
                putExtra(RESULT_DATA, data);
    }

    @Override
    public void initView() {
        imageView.setImageBitmap(CodeUtil.createQRImage("test"));
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
                toActivity(YXAboutcompanyActivity.createIntent(context, ""));
                break;
            case R.id.about_app:
                toActivity(YXAboutappActivity.createIntent(context, ""));
                break;
            default:
                break;
        }
    }
}