package com.yxclient.app.poisearch.searchmodule;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.yxclient.app.R;
import com.yxclient.app.poisearch.searchmodule.ICityChooseModule.IDelegate;
import com.yxclient.app.poisearch.util.CityModel;
import com.yxclient.app.view.SideBar;

import java.util.ArrayList;

/**
 * Created by liangchao_suxun on 2017/4/28.
 * 选择城市列表
 */

public class CityChooseWidget extends RelativeLayout implements ICityChooseModule.IWidget, CityListWidget.IParentWidget, CityInputWidget.IParentWidget {

    private CityInputWidget mCityInputWidget;
    private CurrCityWidget mCurrCityWidget;
    private CityListWidget mCityListWidget;

    private SideBar sideBar;// 拼音控件

    public CityChooseWidget(Context context) {
        super(context);
        init();
    }

    public CityChooseWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CityChooseWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.widget_city_choose, this);

        mCurrCityWidget = (CurrCityWidget) findViewById(R.id.curr_city_widget);
        mCityListWidget = (CityListWidget) findViewById(R.id.city_list);
        mCityInputWidget = (CityInputWidget) findViewById(R.id.city_input_widget);
        sideBar = (SideBar) findViewById(R.id.sidrbar);
        mCityListWidget.setParentWidget(this);
        mCityInputWidget.setParentWidget(this);
        // 拼音触摸事件
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = mCityListWidget.cityListAdapter.getPositionForSection(s.charAt(0));
                //int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mCityListWidget.setSelection(position);
                    //sortListView.setSelection(position);
                }

            }
        });
    }

    private IDelegate mDelegate;

    @Override
    public void bindDelegate(IDelegate delegate) {
        mDelegate = delegate;
    }

    @Override
    public void setCurrCity(String cityName) {
        mCurrCityWidget.setCurrCity(cityName);
    }

    @Override
    public void loadCityList(ArrayList<CityModel> data) {
        mCityListWidget.loadCityList(data);
    }

    @Override
    public void onSelCity(CityModel cityModel) {
        if (mDelegate != null) {
            mDelegate.onChooseCity(cityModel);
        }
    }

    @Override
    public void onCityInput(String cityInput) {
        if (mDelegate != null) {
            mDelegate.onCityInput(cityInput);
        }
    }

    @Override
    public void onCancel() {
        if (mDelegate != null) {
            mDelegate.onCancel();
        }
    }
}
