package com.yxclient.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.PoiItem;
import com.yxclient.app.poisearch.util.CityModel;
import com.yxclient.app.trip.tripmodule.ITripHostModule.IDelegate;
import com.yxclient.app.trip.tripmodule.ITripHostModule.IParentDelegate;
import com.yxclient.app.trip.tripmodule.TripHostModuleDelegate;
import com.jude.fitsystemwindowlayout.FitSystemWindowsLinearLayout;
import com.yxclient.app.R;

import butterknife.ButterKnife;
import zuo.biao.library.base.BaseActivity;

import static com.yxclient.app.poisearch.searchmodule.ISearchModule.IDelegate.DEST_POI_TYPE;
import static com.yxclient.app.poisearch.searchmodule.ISearchModule.IDelegate.START_POI_TYPE;

/**
 * Created by mac on 2017/8/9.
 * 顺风车
 */

public class YXRideActivity extends BaseActivity {
    private TripHostModuleDelegate mTripHostDelegate;

    @Override
    public Activity getActivity() {
        return this;
    }
    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**
     * 启动这个Activity的Intent
     *
     * @param context
     * @return
     */
    public static Intent createIntent(Context context) {
        return new Intent(context, YXRideActivity.class);
    }
    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 判断如果登录过，则进入主页
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_activity_ride);
        RelativeLayout contentView = (RelativeLayout) findViewById(R.id.content_view);
        mTripHostDelegate = new TripHostModuleDelegate();
        mTripHostDelegate.bindParentDelegate(mParentTripDelegate);

        contentView.addView(mTripHostDelegate.getWidget(this));
        mTripHostDelegate.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
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

    private IParentDelegate mParentTripDelegate = new IParentDelegate() {
        @Override
        public void onIconClick() {
            showMsg("on icon click");
        }

        @Override
        public void onMsgClick() {
            showMsg("on msg click");
        }

        @Override
        public void onChooseCity() {
            Intent intent = new Intent(YXRideActivity.this, ChooseCityActivity.class);
            intent.putExtra(ChooseCityActivity.CURR_CITY_KEY, mTripHostDelegate.getCurrCity().getCity());
            startActivityForResult(intent, REQUEST_CHOOSE_CITY);
            YXRideActivity.this.overridePendingTransition(R.anim.slide_in_up, 0);
        }

        @Override
        public void onChooseDestPoi() {
            Intent intent = new Intent(YXRideActivity.this, ChoosePoiActivity.class);
            intent.putExtra(ChoosePoiActivity.POI_TYPE_KEY, DEST_POI_TYPE);
            intent.putExtra(ChoosePoiActivity.CITY_KEY, mTripHostDelegate.getCurrCity());
            startActivityForResult(intent, REQUEST_CHOOSE_DEST_POI);
            YXRideActivity.this.overridePendingTransition(R.anim.slide_in_up, 0);
        }

        @Override
        public void onChooseStartPoi() {
            Intent intent = new Intent(YXRideActivity.this, ChoosePoiActivity.class);
            intent.putExtra(ChoosePoiActivity.POI_TYPE_KEY, START_POI_TYPE);
            intent.putExtra(ChoosePoiActivity.CITY_KEY, mTripHostDelegate.getCurrCity());
            startActivityForResult(intent, REQUEST_CHOOSE_START_POI);
            YXRideActivity.this.overridePendingTransition(R.anim.slide_in_up, 0);
        }

        @Override
        public void onBackToInputMode() {
            YXRideActivity.this.onBackToInputMode();
        }

        @Override
        public void onStartPoiChange(PoiItem poiItem) {
            if (poiItem == null) {
                return;
            }

            mTripHostDelegate.setStartLocation(poiItem.getTitle());
            mStartPoi = poiItem;
        }

        @Override
        public void onStartCall() {
            showMsg("on start call");
        }
    };

    private void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private static final int REQUEST_CHOOSE_CITY = 1;
    private static final int REQUEST_CHOOSE_START_POI = 2;
    private static final int REQUEST_CHOOSE_DEST_POI = 3;
    private static final int MIN_START_DEST_DISTANCE = 1000;
    private PoiItem mStartPoi;
    private PoiItem mDestPoi;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_CHOOSE_CITY == requestCode) {
            if (resultCode == RESULT_OK) {
                CityModel cityModel = data.getParcelableExtra(ChooseCityActivity.CURR_CITY_KEY);
                mTripHostDelegate.setCurrCity(cityModel);
            }
        }

        if (REQUEST_CHOOSE_DEST_POI == requestCode) {
            if (resultCode == RESULT_OK) {
                try {
                    PoiItem poiItem = data.getParcelableExtra(ChoosePoiActivity.POIITEM_OBJECT);

                    float res[] = new float[1];
                    Location.distanceBetween(poiItem.getLatLonPoint().getLatitude(),
                            poiItem.getLatLonPoint().getLongitude(), mStartPoi.getLatLonPoint().getLatitude(),
                            mStartPoi.getLatLonPoint().getLongitude(), res);

                    if (res[0] <= MIN_START_DEST_DISTANCE) {
                        showMsg("距离过近，请重新选择目的地");
                        return;
                    }

                    mDestPoi = poiItem;
                    mTripHostDelegate.setDestLocation(poiItem.getTitle());

                    if (mDestPoi != null && mStartPoi != null) {
                        onShowPoiRes();
                    }

                } catch (Exception e) {
                    showMsg("请选择正确的目的地");
                }

            }
        }

        if (REQUEST_CHOOSE_START_POI == requestCode) {
            if (resultCode == RESULT_OK) {
                try {
                    PoiItem poiItem = data.getParcelableExtra(ChoosePoiActivity.POIITEM_OBJECT);
                    mStartPoi = poiItem;
                    mTripHostDelegate.setStartLocation(poiItem.getTitle());
                    mTripHostDelegate.moveCameraPosTo(
                            new LatLng(poiItem.getLatLonPoint().getLatitude(), poiItem.getLatLonPoint().getLongitude()));
                } catch (Exception e) {
                    showMsg("请选择正确的目的地");
                }

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void onShowPoiRes() {


        LatLng startLL = new LatLng(mStartPoi.getLatLonPoint().getLatitude(),
                mStartPoi.getLatLonPoint().getLongitude());
        LatLng destLL = new LatLng(mDestPoi.getLatLonPoint().getLatitude(),
                mDestPoi.getLatLonPoint().getLongitude());


        mTripHostDelegate.showPoiRes(startLL, destLL);
    }

    @Override
    public void onBackPressed() {
        if (mTripHostDelegate.getMode() == IDelegate.SHOW_RES_MODE) {
            onBackToInputMode();
            return;
        }

        super.onBackPressed();
    }

    /**
     * 在切换为输入模式后，设置模式，触发TripHostDelegate重置
     */
    private void onBackToInputMode() {
        mTripHostDelegate.setMode(IDelegate.INPUT_MODE, mStartPoi);
        mDestPoi = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTripHostDelegate.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mTripHostDelegate.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTripHostDelegate.onDestroy();
    }
}
