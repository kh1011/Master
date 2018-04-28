package com.yxclient.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.dou361.dialogui.DialogUIUtils;
import com.yxclient.app.R;
import com.yxclient.app.model.bean.CoordinateModel;
import com.yxclient.app.model.bean.DeliverAddrModel;
import com.yxclient.app.model.bean.OriginAndSite;
import com.yxclient.app.model.bean.UserInfo;
import com.yxclient.app.poisearch.util.CityModel;
import com.yxclient.app.poisearch.util.CityUtil;
import com.yxclient.app.utils.StringUtil;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zuo.biao.library.base.BaseActivity;

/**
 * Created by mac on 2017/8/21.
 * 设置发货信息
 */

public class YXSetDeliveryAddrActivity extends BaseActivity implements AMap.OnMapLoadedListener, AMap.OnMapClickListener, LocationSource, AMapLocationListener {
    /**
     * ====================
     * 地图相关
     */
    private AMap aMap;
    private MapView mapView;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private LocationSource.OnLocationChangedListener mListener;

    private Marker locMarker;
    private Circle ac;
    private Circle c;
    private long start;
    private final Interpolator interpolator = new CycleInterpolator(1);
    private final Interpolator interpolator1 = new LinearInterpolator();
    private TimerTask mTimerTask;
    private Timer mTimer = new Timer();
    private CityModel mCurrCity;
    String type;
    @BindView(R.id.textView3)
    TextView title;
    //地图相关end
    @BindView(R.id.delivery_addr_origin)
    TextView tv_Origin;
    // 详细地址
    @BindView(R.id.delivery_addr_address)
    EditText ed_Addr;
    // 联系人
    @BindView(R.id.delivery_addr_linker)
    EditText ed_Linker;
    // 联系电话
    @BindView(R.id.delivery_addr_mobile)
    EditText ed_Mobile;
    // 发货信息
    private DeliverAddrModel deliverAddrModel;
    // 请求码
    private static final int CHOOSE_ADDR_START = 1001;    //  出地点
    // 选择所在城市
    private static final int REQUEST_CHOOSE_CITY = 1;     //  选择城市
    //详细地址
    @BindView(R.id.ride_get_site)
    RelativeLayout re_detail_site;

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_set_deliveryaddr);
        ButterKnife.bind(this);
        initView();
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        init();
        initData();
        initEvent();
    }


    public static Intent createIntent(Context context, String data) {
        return new Intent(context, YXSetDeliveryAddrActivity.class).
                putExtra(RESULT_DATA, data);
    }

    @Override
    public void initView() {
        re_detail_site.setVisibility(View.GONE);
        type = getIntent().getStringExtra("type");
        switch (type) {
            case "0":
                title.setText("发货信息");
                ed_Linker.setHint("请输入发货人姓名");
                ed_Mobile.setHint("请输入发货人联系电话");
                //ed_Addr.setHint("请输入详细发货地址");
                break;
            case "1":
                title.setText("收货信息");
                ed_Linker.setHint("请输入收货人姓名");
                ed_Mobile.setHint("请输入收货人联系电话");
                //ed_Addr.setHint("请输入详细收货地址");
                tv_Origin.setText("请选择收货地址");
                break;
            default:
                break;
        }

        mCurrCity = CityUtil.getDefCityModel(context);
        deliverAddrModel = new DeliverAddrModel();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }

    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        aMap.setOnMapLoadedListener(this);
        aMap.setOnMapClickListener(this);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
    }

    private Marker addMarker(LatLng point) {
        Bitmap bMap = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.navi_map_gps_locked);
        BitmapDescriptor des = BitmapDescriptorFactory.fromBitmap(bMap);
        Marker marker = aMap.addMarker(new MarkerOptions().position(point).icon(des)
                .anchor(0.5f, 0.5f));
        return marker;
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
        try {
            mTimer.cancel();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        deactivate();
    }

    @Override
    public void onMapLoaded() {
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void activate(LocationSource.OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (type.equals("0")){
            startlocation();
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    /**
     * 开始定位。
     */
    private void startlocation() {

        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            // 设置定位监听
            mLocationClient.setLocationListener(this);
            // 设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置为单次定位
            mLocationOption.setOnceLocation(true);
            // 设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            mLocationClient.startLocation();
        } else {
            mLocationClient.startLocation();
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (mTimerTask != null) {
                mTimerTask.cancel();
                mTimerTask = null;
            }
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                LatLng mylocation = new LatLng(aMapLocation.getLatitude(),
                        aMapLocation.getLongitude());
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mylocation, 19));
                addLocationMarker(aMapLocation);
                tv_Origin.setText(aMapLocation.getAddress());
                CoordinateModel coordinate = new CoordinateModel();
                deliverAddrModel.setAddress(aMapLocation.getPoiName());
                deliverAddrModel.setCity(aMapLocation.getCity());
                deliverAddrModel.setCounty(aMapLocation.getDistrict());
                deliverAddrModel.setName(aMapLocation.getPoiName());
                deliverAddrModel.setProvince(aMapLocation.getProvince());
                coordinate.setLng(aMapLocation.getLongitude());
                coordinate.setLat(aMapLocation.getLatitude());
                deliverAddrModel.setCoordinate(coordinate);
                resetCityModelByLocation(aMapLocation);
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": "
                        + aMapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    private void addLocationMarker(AMapLocation aMapLocation) {
        LatLng mylocation = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
        float accuracy = aMapLocation.getAccuracy();
        if (locMarker == null) {
            locMarker = addMarker(mylocation);
            ac = aMap.addCircle(new CircleOptions().center(mylocation)
                    .fillColor(Color.argb(100, 255, 218, 185)).radius(accuracy)
                    .strokeColor(Color.argb(255, 255, 228, 185)).strokeWidth(5));
            c = aMap.addCircle(new CircleOptions().center(mylocation)
                    .fillColor(Color.argb(70, 255, 218, 185))
                    .radius(accuracy).strokeColor(Color.argb(255, 255, 228, 185))
                    .strokeWidth(0));
        } else {
            locMarker.setPosition(mylocation);
            ac.setCenter(mylocation);
            ac.setRadius(accuracy);
            c.setCenter(mylocation);
            c.setRadius(accuracy);
        }
        Scalecircle(c);
    }


    public void Scalecircle(final Circle circle) {
        start = SystemClock.uptimeMillis();
        mTimerTask = new circleTask(circle, 1000);
        mTimer.schedule(mTimerTask, 0, 30);
    }


    private class circleTask extends TimerTask {
        private double r;
        private Circle circle;
        private long duration = 1000;

        public circleTask(Circle circle, long rate) {
            this.circle = circle;
            this.r = circle.getRadius();
            if (rate > 0) {
                this.duration = rate;
            }
        }

        @Override
        public void run() {
            try {
                long elapsed = SystemClock.uptimeMillis() - start;
                float input = (float) elapsed / duration;
//                外圈循环缩放
//                float t = interpolator.getInterpolation((float)(input-0.25));//return (float)(Math.sin(2 * mCycles * Math.PI * input))
//                double r1 = (t + 2) * r;
//                外圈放大后消失
                float t = interpolator1.getInterpolation(input);
                double r1 = (t + 1) * r;
                circle.setRadius(r1);
                if (input > 2) {
                    start = SystemClock.uptimeMillis();
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置城市。如果定位结果返回的城市和当前城市不同，则进行修正
     */
    private void resetCityModelByLocation(AMapLocation location) {
        if (location == null || location.getCityCode() == null) {
            return;
        }
        CityModel cityModel = CityUtil.getCityByCode(context, location.getCityCode());
        if (!mCurrCity.getAdcode().equals(cityModel.getAdcode())) {
            mCurrCity = cityModel;
        }
        System.out.println("xxxxx" + mCurrCity.getCity());
    }

    /**
     * 点击事件
     */
    @OnClick({R.id.delivery_get_origin, R.id.delivery_addr_btn})
    void myClick(View view) {
        switch (view.getId()) {
            case R.id.delivery_get_origin:
                // 选择地点
                Intent intent = new Intent(context, YXChooseAddrActivity.class);
                intent.putExtra("city", mCurrCity.getCity().equals("北京市")?"昆明市":mCurrCity.getCity());
                startActivityForResult(intent, CHOOSE_ADDR_START);
                break;
            case R.id.delivery_addr_btn:
                //String address = ed_Addr.getText().toString();
                String linker = ed_Linker.getText().toString();
                String mobile = ed_Mobile.getText().toString();
                if (tv_Origin.getText().toString().equals("请选择收货地址")){
                    DialogUIUtils.showToast("请选择收货地址");
                }
                else if (StringUtil.isNullOrEmpty(linker)) {
                    DialogUIUtils.showToast("请输入联系人");
                } else if (StringUtil.isNullOrEmpty(mobile)) {
                    DialogUIUtils.showToast("请输入联系人电话");
                } else if (!StringUtil.isTelPhoneNumber(mobile)) {
                    DialogUIUtils.showToast("请输入正确的电话号码");
                } else {
                    UserInfo userInfo = new UserInfo(linker, mobile);
                    String testStr = JSON.toJSONString(deliverAddrModel);
                    System.out.println("发货信息:" + testStr);
                    Intent intent1 = new Intent();
                    intent1.putExtra("deliver", deliverAddrModel);
                    intent1.putExtra("user", userInfo);
                    setResult(RESULT_OK, intent1);
                    YXSetDeliveryAddrActivity.this.finish();
                    YXSetDeliveryAddrActivity.this.overridePendingTransition(0, R.anim.slide_out_down);
                }
                break;
            default:
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Bundle bundle = data.getExtras();
            OriginAndSite originAndSite;
            switch (requestCode) {
                case REQUEST_CHOOSE_CITY:
                    if (resultCode == RESULT_OK) {
                        CityModel cityModel = data.getParcelableExtra(ChooseCityActivity.CURR_CITY_KEY);
                        //mTripHostDelegate.setCurrCity(cityModel);
                        System.out.println(cityModel.getCity());
                    }
                    break;
                case CHOOSE_ADDR_START:
                    originAndSite = (OriginAndSite) bundle.getSerializable("model");
                    assert originAndSite != null;
                    tv_Origin.setText(originAndSite.getProvince()+originAndSite.getCity()+originAndSite.getCounty()+originAndSite.getAddress());
                    deliverAddrModel.setAddress(originAndSite.getAddress());
                    deliverAddrModel.setCity(originAndSite.getCity());
                    deliverAddrModel.setCounty(originAndSite.getCounty());
                    deliverAddrModel.setName(originAndSite.getName());
                    deliverAddrModel.setProvince(originAndSite.getProvince());
                    deliverAddrModel.setCoordinate(originAndSite.getCoordinate());
                    break;
                default:
                    break;
            }
        }
    }
}
