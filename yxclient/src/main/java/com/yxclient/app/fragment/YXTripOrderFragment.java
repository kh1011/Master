package com.yxclient.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yxclient.app.R;
import com.yxclient.app.activity.YXCutomerActivity;
import com.yxclient.app.activity.YXQueryActivity;
import com.yxclient.app.activity.YXSettingActivity;
import com.yxclient.app.activity.YXVerificationActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import zuo.biao.library.base.BaseFragment;

/**
 * Created by jpeng on 16-11-14.
 * 我的行程
 */
public class YXTripOrderFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //TODO demo_fragment改为你所需要的layout文件
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this, view);
        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
        return view;//返回值必须为view
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
}
