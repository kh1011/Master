package com.yxdriver.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.yxdriver.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import zuo.biao.library.base.BaseActivity;

/**
 * 功能：
 * Created by yun.zhang on 2017/9/24 0024.
 * email:zy19930321@163.com
 */
public class YXAboutcompanyActivity extends BaseActivity {
    @BindView(R.id.company_text)
    TextView textView;


    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_company);
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
        return new Intent(context, YXAboutcompanyActivity.class).
                putExtra(RESULT_DATA, data);
    }

    @Override
    public void initView() {
        textView.setText("\u3000\u3000" + getResources().getString(R.string.company));
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }
}