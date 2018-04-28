package com.yxclient.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.yxclient.app.R;
import com.yxclient.app.adapter.OrderViewPagerAdapter;
import com.yxclient.app.fragment.YXShopOrderFragment;
import com.yxclient.app.fragment.YXTripOrderFragment;
import com.yxclient.app.view.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import zuo.biao.library.base.BaseActivity;

/**
 * Created by mac on 2017/10/7.
 * 订单管理
 */

public class YXMyorderManagerActivity extends BaseActivity {
    private static final String TAG = "YXMyorderManagerActivity";
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabLayout)
    SlidingTabLayout tabLayout;

    private List<Fragment> list;

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_order_manager);
        ButterKnife.bind(this);
        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        initViewDate();
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
        return new Intent(context, YXMyorderManagerActivity.class).
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

    /**
     * 初始化子页面显示
     */
    private void initViewDate() {
        list = new ArrayList<>();
        list.add(new YXTripOrderFragment());
        list.add(new YXShopOrderFragment());
        OrderViewPagerAdapter adapter = new OrderViewPagerAdapter(context.getSupportFragmentManager(), list, context);
        viewPager.setAdapter(adapter);
        tabLayout.setCustomTabView(R.layout.custom_tab, 0);
        tabLayout.setDistributeEvenly(true);
        tabLayout.setViewPager(viewPager);
        tabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                //return Color.WHITE;
                return ContextCompat.getColor(getActivity(), R.color.main_blue);
            }
        });
    }
}
