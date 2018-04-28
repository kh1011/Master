package com.yxclient.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import zuo.biao.library.base.BaseActivity;

import com.amap.api.location.AMapLocation;
import com.amap.api.services.core.PoiItem;
import com.yxclient.app.poisearch.searchmodule.ISearchModule.IDelegate.IParentDelegate;
import com.yxclient.app.poisearch.searchmodule.SearchModuleDelegate;
import com.yxclient.app.poisearch.util.CityModel;
import com.google.gson.Gson;
import com.yxclient.app.R;

/**
 * Created by mac on 2017/8/9.
 * dada
 */

public class SetFavAddressActivity extends BaseActivity {
    @Override
    public Activity getActivity() {
        return this;
    }

    private SearchModuleDelegate mSearchModuelDeletage;

    private int mFavType = 0;
    private AMapLocation mCurrLoc;

    public static final String CURR_CITY_KEY = "curr_city_key";
    public static final String CURR_LOC_KEY = "curr_loc_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setfavaddress);

        RelativeLayout contentView = (RelativeLayout) findViewById(R.id.content_view);
        mSearchModuelDeletage = new SearchModuleDelegate();
        mSearchModuelDeletage.bindParentDelegate(mSearchModuleParentDelegate);
        contentView.addView(mSearchModuelDeletage.getWidget(this));

        mSearchModuelDeletage.setFavAddressVisible(false);

        mFavType = getIntent().getIntExtra(ChoosePoiActivity.FAVTYPE_KEY, -1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            String currCityStr = getIntent().getStringExtra(CURR_CITY_KEY);
            Gson gson = new Gson();
            CityModel cityModel = gson.fromJson(currCityStr, CityModel.class);
            mSearchModuelDeletage.setCity(cityModel);

            mCurrLoc = getIntent().getParcelableExtra(CURR_LOC_KEY);
            mSearchModuelDeletage.setCurrLoc(mCurrLoc);
        } catch (Exception e) {
            ;
        }


    }

    private IParentDelegate mSearchModuleParentDelegate = new IParentDelegate() {
        @Override
        public void onChangeCityName() {
            Intent intent = new Intent();
            intent.setClass(SetFavAddressActivity.this, ChooseCityActivity.class);
            intent.putExtra(ChooseCityActivity.CURR_CITY_KEY, mSearchModuelDeletage.getCurrCity().getCity());
            SetFavAddressActivity.this.startActivityForResult(intent, 1);
        }

        @Override
        public void onCancel() {
            SetFavAddressActivity.this.finish();
        }

        @Override
        public void onSetFavHomePoi() {
        }

        @Override
        public void onSetFavCompPoi() {
        }

        @Override
        public void onChooseFavHomePoi(PoiItem poiItem) {
        }

        @Override
        public void onChooseFavCompPoi(PoiItem poiItem) {
        }

        @Override
        public void onSelPoiItem(PoiItem poiItem) {
            String poiitemStr = new Gson().toJson(poiItem);
            Intent resIntent = new Intent();
            resIntent.putExtra(ChoosePoiActivity.FAVTYPE_KEY, mFavType);
            resIntent.putExtra(ChoosePoiActivity.POIITEM_OBJECT, poiitemStr);
            SetFavAddressActivity.this.setResult(RESULT_OK, resIntent);
            SetFavAddressActivity.this.finish();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // 表示结果来自城市选择actiivty
        if (requestCode == 1 && requestCode == RESULT_OK) {
            String currCityStr = data.getStringExtra(ChooseCityActivity.CURR_CITY_KEY);
            Gson gson = new Gson();
            CityModel cityModel = gson.fromJson(currCityStr, CityModel.class);
            mSearchModuelDeletage.setCity(cityModel);
        }
        super.onActivityResult(requestCode, resultCode, data);
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
