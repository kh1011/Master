package com.yxclient.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.RelativeLayout;

import zuo.biao.library.base.BaseActivity;

import com.yxclient.app.poisearch.searchmodule.CityChooseDelegate;
import com.yxclient.app.poisearch.searchmodule.CityChooseWidget;
import com.yxclient.app.poisearch.searchmodule.ICityChooseModule;
import com.yxclient.app.poisearch.searchmodule.ICityChooseModule.IParentDelegate;
import com.yxclient.app.poisearch.util.CityModel;
import com.google.gson.Gson;
import com.yxclient.app.R;

/**
 * Created by mac on 2017/8/9.
 * 选择城市
 */

public class ChooseCityActivity extends AppCompatActivity {
    /*@Override
    public Activity getActivity() {
        return this;
    }*/

    private CityChooseWidget mCityChooseWidget;
    private CityChooseDelegate mCityChooseDelegate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_city_choose);

        RelativeLayout contentView = (RelativeLayout) findViewById(R.id.content_view);

        mCityChooseDelegate = new CityChooseDelegate();
        mCityChooseDelegate.bindParentDelegate(mCityChooseParentDelegate);
        contentView.addView(mCityChooseDelegate.getWidget(this));
    }

    public static final String CURR_CITY_KEY = "curr_city_key";

    @Override
    protected void onResume() {
        super.onResume();
        String currCity = getIntent().getStringExtra(CURR_CITY_KEY);
        if (TextUtils.isEmpty(currCity)) {
            currCity = "北京市";
        }
        mCityChooseDelegate.setCurrCity(currCity);
    }

    private ICityChooseModule.IParentDelegate mCityChooseParentDelegate = new IParentDelegate() {
        @Override
        public void onChooseCity(CityModel city) {
            Intent intent = new Intent();
            intent.putExtra(CURR_CITY_KEY, city);
            ChooseCityActivity.this.setResult(RESULT_OK, intent);
            ChooseCityActivity.this.finish();
            ChooseCityActivity.this.overridePendingTransition(0, R.anim.slide_out_down);
        }

        @Override
        public void onCancel() {
            ChooseCityActivity.this.finish();
            ChooseCityActivity.this.overridePendingTransition(0, R.anim.slide_out_down);
        }
    };

   /* @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }*/
}
