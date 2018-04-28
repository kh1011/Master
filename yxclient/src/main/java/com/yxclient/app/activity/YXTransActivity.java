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
import android.view.Gravity;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.dou361.dialogui.listener.DialogUIDateTimeSaveListener;
import com.dou361.dialogui.widget.DateSelectorWheelView;
import com.yxclient.app.R;
import com.yxclient.app.adapter.GoodsKindAdapter;
import com.yxclient.app.app.DemoApplication;
import com.yxclient.app.config.YXConfig;
import com.yxclient.app.http.RetrofitHttp;
import com.yxclient.app.model.bean.DeliverAddrModel;
import com.yxclient.app.model.bean.OrderModel;
import com.yxclient.app.model.bean.TransOrderModel;
import com.yxclient.app.model.bean.UserInfo;
import com.yxclient.app.overlay.DrivingRouteOverlay;
import com.yxclient.app.poisearch.util.CityModel;
import com.yxclient.app.poisearch.util.CityUtil;
import com.yxclient.app.utils.AMapUtil;
import com.yxclient.app.utils.DateTimePickOrderUtils;
import com.yxclient.app.utils.DateUtil;
import com.yxclient.app.utils.StringUtil;
import com.yxclient.app.view.MyGridView;

import net.lemonsoft.lemonhello.LemonHello;
import net.lemonsoft.lemonhello.LemonHelloAction;
import net.lemonsoft.lemonhello.LemonHelloInfo;
import net.lemonsoft.lemonhello.LemonHelloView;
import net.lemonsoft.lemonhello.interfaces.LemonHelloActionDelegate;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;
import zuo.biao.library.base.BaseActivity;

/**
 * Created by mac on 2017/8/15.
 * 货运下单
 */

public class YXTransActivity extends BaseActivity implements AMap.OnMapLoadedListener, AMap.OnMapClickListener, LocationSource, AMapLocationListener, RouteSearch.OnRouteSearchListener {
    private AMap aMap;
    private MapView mapView;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private OnLocationChangedListener mListener;

    private Marker locMarker;
    private Circle ac;
    private Circle c;
    private long start;
    private final Interpolator interpolator = new CycleInterpolator(1);
    private final Interpolator interpolator1 = new LinearInterpolator();
    private TimerTask mTimerTask;
    private Timer mTimer = new Timer();

    @BindView(R.id.mytitle)
    TextView tv_Title;

    @BindView(R.id.ride_origin)
    TextView tv_Origin;
    @BindView(R.id.ride_site)
    TextView tv_Site;
    // 货物类型
    @BindView(R.id.trans_goods_type)
    TextView tv_goods_type;
    // 货物大概尺寸
    @BindView(R.id.trans_goods_size)
    TextView tv_goods_size;
    @BindView(R.id.trans_goods_re_size)
    RelativeLayout tv_get_size;
    // 货物重量
    @BindView(R.id.trans_goods_weight)
    TextView tv_goods_weight;
    @BindView(R.id.trans_goods_re_weight)
    RelativeLayout tv_get_weight;
    @BindView(R.id.weight)
    TextView tv_weight;
    // 发货时间
    @BindView(R.id.trans_goods_datetime)
    TextView tv_goods_DateTime;
    // 货运费用
    @BindView(R.id.trans_goods_money)
    EditText ed_goods_money;
    // 货运备注信息
    @BindView(R.id.trans_goods_remark)
    EditText ed_goods_remark;
    //货物类型
    @BindView(R.id.trans_get_kinds)
    RelativeLayout tv_get_kinds;

    @BindView(R.id.trans_re_linker1)
    RelativeLayout re_linker1;
    @BindView(R.id.linkername1)
    TextView tv_linkername1;
    @BindView(R.id.linkermobile1)
    TextView tv_linkermobile1;

    @BindView(R.id.trans_re_linker2)
    RelativeLayout re_linker2;
    @BindView(R.id.linkername2)
    TextView tv_linkername2;
    @BindView(R.id.linkermobile2)
    TextView tv_linkermobile2;
    // 是否易碎品
    @BindView(R.id.trans_re_ysp)
    RelativeLayout re_YSP;
    @BindView(R.id.recharge_group)
    RadioGroup re_group;
    RadioButton rb;
    // 是否易湿品
    @BindView(R.id.trans_re_ycp)
    RelativeLayout re_YCP;
    @BindView(R.id.recharge_group2)
    RadioGroup re_group2;
    RadioButton rb2;

    private CityModel mCurrCity;
    private String type = "";
    private String typeStr = "";
    // 货物类型集合
    private List<String> kindList;
    // 发货信息
    DeliverAddrModel deliverAddrModel1;
    // 收货信息
    DeliverAddrModel deliverAddrModel2;
    // 货运订单
    TransOrderModel transOrderModel;

    //货物相关属性
    private double goodsWeight = 0.0;//货物重量

    private RouteSearch mRouteSearch;
    private DriveRouteResult mDriveRouteResult;

    // 大约里程
    private double discount = 0.0;
    private final int ROUTE_TYPE_DRIVE = 2;
    LatLonPoint latLonPoint;
    LatLonPoint latLonPoint1;

    @Override
    public Activity getActivity() {
        return this;
    }
    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    public static Intent createIntent(Context context) {
        return new Intent(context, YXTransActivity.class);
    }

    public static Intent createIntent(Context context, String data) {
        return new Intent(context, YXTransActivity.class).
                putExtra(RESULT_DATA, data);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_activity_trans);
        ButterKnife.bind(this);
        initView();
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init();
        initData();
        initEvent();
    }

    @Override
    public void initView() {
        transOrderModel = new TransOrderModel();
        type = getIntent().getStringExtra(RESULT_DATA);
        switch (type) {
            case "1":
                tv_Title.setText("小型货运");
                typeStr = "小型货运";
                re_YSP.setVisibility(View.GONE);
                re_YCP.setVisibility(View.GONE);
                break;
            case "2":
                tv_Title.setText("大型货运");
                typeStr = "大型货运";
                re_YSP.setVisibility(View.GONE);
                re_YCP.setVisibility(View.GONE);
                break;
            case "3":
                tv_Title.setText("快递小件");
                typeStr = "快递小件";
                re_YSP.setVisibility(View.VISIBLE);
                tv_get_size.setVisibility(View.GONE);
                re_YCP.setVisibility(View.VISIBLE);
                //tv_get_weight.setVisibility(View.GONE);
                tv_weight.setText("设置货物重量(选填)");
                tv_get_kinds.setVisibility(View.GONE);
                break;
            default:
                break;
        }
        transOrderModel.setType(typeStr);
        String testDate = DateUtil.getCurDateStr("yyyy-MM-dd HH:mm");
        String date[] = testDate.split(" ");
        System.out.println(date[0]);
        System.out.println(date[1]);
        mCurrCity = CityUtil.getDefCityModel(context);
        System.out.println(mCurrCity.getCity());
        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(this);
    }

    @Override
    public void initData() {
        getKinds(DemoApplication.getInstance().getMyToken());
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
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        startlocation();
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
                //tv_Origin.setText(aMapLocation.getAddress());
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
    ////////路径计算
    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    // 驾车路径计算
    @Override
    public void onDriveRouteSearched(DriveRouteResult walkRouteResult, int i) {
        if (i == AMapException.CODE_AMAP_SUCCESS) {
            if (walkRouteResult != null && walkRouteResult.getPaths() != null) {
                if (walkRouteResult.getPaths().size() > 0) {
                    mDriveRouteResult = walkRouteResult;
                    final DrivePath drivePath = mDriveRouteResult.getPaths()
                            .get(0);
                    DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
                            context, aMap, drivePath,
                            mDriveRouteResult.getStartPos(),
                            mDriveRouteResult.getTargetPos(), null);
                    drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                    drivingRouteOverlay.setIsColorfulline(true);//是否用颜色展示交通拥堵情况，默认true
                    drivingRouteOverlay.removeFromMap();
                    drivingRouteOverlay.addToMap();
                    drivingRouteOverlay.zoomToSpan();
                    //mBottomLayout.setVisibility(View.VISIBLE);
                    int dis = (int) drivePath.getDistance();
                    int dur = (int) drivePath.getDuration();
                    //String des = AMapUtil.getFriendlyTime(dur) + "(" + AMapUtil.getFriendlyLength(dis) + ")";
                    //String des = AMapUtil.getFriendlyLength1(dis);
                    discount = Double.parseDouble(AMapUtil.getFriendlyLength1(dis));
                    //tv_Discount.setText(String.valueOf(discount));

                    // 计算价格
                    //doCalculation(discount);
                } else if (walkRouteResult != null && walkRouteResult.getPaths() == null) {
                    DialogUIUtils.showToast(R.string.no_result);
                }

            } else {
                DialogUIUtils.showToast(R.string.no_result);
            }
        }
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }
    ////////路径计算


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

    @OnClick({R.id.ride_get_origin, R.id.ride_get_site, R.id.trans_get_kinds, R.id.trans_goods_re_size, R.id.trans_goods_re_weight, R.id.trans_btn, R.id.trans_datetime})
    void myClick(View view) {
        switch (view.getId()) {
            case R.id.ride_get_origin:
                // 设置发货信息
                Intent intent = new Intent();
                intent.setClass(context, YXSetDeliveryAddrActivity.class);
                intent.putExtra("type", "0");
                startActivityForResult(intent, REQUEST_CHOOSE_START_POI);
                YXTransActivity.this.overridePendingTransition(R.anim.slide_in_up, 0);
                break;
            case R.id.ride_get_site:
                // 设置收货信息
                Intent intent2 = new Intent(YXTransActivity.this, YXSetDeliveryAddrActivity.class);
                intent2.putExtra("type", "1");
                startActivityForResult(intent2, REQUEST_CHOOSE_DEST_POI);
                YXTransActivity.this.overridePendingTransition(R.anim.slide_in_up, 0);
                break;
            // 选择货物类型
            case R.id.trans_get_kinds:
                if (kindList != null && kindList.size() > 0) {
                    showKindDialog(kindList);
                } else {
                    DialogUIUtils.showToast("暂无货物类型供选择");
                    // 默认为"其他"
                }
                break;
            // 设置货物尺寸
            case R.id.trans_goods_re_size:
                showSizeDialog();
                break;
            case R.id.trans_goods_re_weight:
                showWeightDialog();
                break;
            case R.id.trans_datetime:
                // 选择发货时间
                DateTimePickOrderUtils dateTimePicKDialog = new DateTimePickOrderUtils(
                        YXTransActivity.this, null, tv_goods_DateTime,3);
                dateTimePicKDialog.dateTimePicKDialog();
                //showDateTimeDialog();
                break;
            case R.id.trans_btn:
                String goodsType = tv_goods_type.getText().toString();
                String goodsSize = tv_goods_size.getText().toString();
                String weight = tv_goods_weight.getText().toString();
                String money = ed_goods_money.getText().toString();
                String remark = ed_goods_remark.getText().toString();
                String datetime = tv_goods_DateTime.getText().toString();
                rb = (RadioButton)YXTransActivity.this.findViewById(re_group.getCheckedRadioButtonId());
                rb2 = (RadioButton)YXTransActivity.this.findViewById(re_group2.getCheckedRadioButtonId());
                // 发布订单
                if (!transOrderModel.getType().equals("快递小件")){
                    if (StringUtil.isNullOrEmpty(goodsType)){
                        DialogUIUtils.showToast("请选择货物类型");
                    }else if (StringUtil.isNullOrEmpty(weight)) {
                        DialogUIUtils.showToast("请设置货物重量");
                    }
                }
                if (transOrderModel.getOrigin() == null) {
                    DialogUIUtils.showToast("请设置发货信息");
                } else if (transOrderModel.getSite() == null) {
                    DialogUIUtils.showToast("请设置收货信息");
                }else if (StringUtil.isNullOrEmpty(money)) {
                    DialogUIUtils.showToast("请输入运费");
                } else if (StringUtil.isNullOrEmpty(datetime)) {
                    DialogUIUtils.showToast("请选择发货日期");
                }else {
                    if (StringUtil.isNullOrEmpty(remark)){
                        remark = "无";
                    }
                    if (transOrderModel.getType().equals("快递小件")){
                        transOrderModel.setFragile(ysStatus(rb.getText().toString()));
                        transOrderModel.setWet(ysStatus(rb2.getText().toString()));
                    }else {
                        transOrderModel.setKind(goodsType);
                        transOrderModel.setBulk(goodsSize);
                    }
                    transOrderModel.setWeight(goodsWeight);
                    transOrderModel.setNote(remark);
                    String[] dates = datetime.split(" ");
                    transOrderModel.setDate(dates[0]);
                    transOrderModel.setTime(dates[1]);
                    transOrderModel.setAmount(Integer.parseInt(money)*100);
                    transOrderModel.setMileage(discount);
                    System.out.println("===============货运下单参数================");
                    System.out.println(JSON.toJSONString(transOrderModel));
                    System.out.println("=========================================");
                    doOrder(DemoApplication.getInstance().getMyToken(), transOrderModel);
                }
                break;
            default:
                break;
        }
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
                //mTripHostDelegate.setCurrCity(cityModel);
            }
        }
        if (REQUEST_CHOOSE_DEST_POI == requestCode) {
            if (resultCode == RESULT_OK) {
                deliverAddrModel2 = (DeliverAddrModel) data.getSerializableExtra("deliver");
                UserInfo deliver = (UserInfo) data.getSerializableExtra("user");
                transOrderModel.setSite(deliverAddrModel2);
                transOrderModel.setName(deliver.getName());
                transOrderModel.setMobile(deliver.getMobile());

                re_linker2.setVisibility(View.VISIBLE);
                tv_linkername2.setText(deliver.getName());
                tv_linkermobile2.setText(deliver.getMobile());

                tv_Site.setText(deliverAddrModel2.getAddress());
                double lat1 = transOrderModel.getSite().getCoordinate().getLat();
                double lng1 = transOrderModel.getSite().getCoordinate().getLng();
                latLonPoint1 = new LatLonPoint(lat1, lng1);
                searchRouteResult(ROUTE_TYPE_DRIVE, RouteSearch.DrivingDefault, latLonPoint, latLonPoint1);
            }
        }
        if (REQUEST_CHOOSE_START_POI == requestCode) {
            if (resultCode == RESULT_OK) {
                deliverAddrModel1 = (DeliverAddrModel) data.getSerializableExtra("deliver");
                transOrderModel.setOrigin(deliverAddrModel1);
                UserInfo recept = (UserInfo) data.getSerializableExtra("user");
                transOrderModel.setSite(deliverAddrModel2);
                transOrderModel.setConsignorName(recept.getName());
                transOrderModel.setConsignorMobile(recept.getMobile());
                re_linker1.setVisibility(View.VISIBLE);
                tv_linkername1.setText(recept.getName());
                tv_linkermobile1.setText(recept.getMobile());

                tv_Origin.setText(deliverAddrModel1.getAddress());

                double lat = transOrderModel.getOrigin().getCoordinate().getLat();
                double lng = transOrderModel.getOrigin().getCoordinate().getLng();
                latLonPoint = new LatLonPoint(lat, lng);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
     * 开始搜索路径规划方案
     */
    public void searchRouteResult(int routeType, int mode, LatLonPoint mStartPoint, LatLonPoint mEndPoint) {
        if (mStartPoint == null) {
            DialogUIUtils.showToast("定位中，稍后再试...");
            return;
        }
        if (mEndPoint == null) {
            DialogUIUtils.showToast("终点未设置");
        }
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                mStartPoint, mEndPoint);
        if (routeType == ROUTE_TYPE_DRIVE) {// 驾车路径规划
            RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, mode, null,
                    null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
            mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
        }
    }
    private int getNumber(String str) {
        int val = 0;
        switch (str) {
            case "1人":
                val = 1;
                break;
            case "2人":
                val = 2;
                break;
            case "3人":
                val = 3;
                break;
            case "4人":
                val = 4;
                break;
            default:
                val = 0;
                break;
        }
        return val;
    }

    private void showTokenDialog() {
        LemonHello.getInformationHello("您的token无效，请登录", "")
                .addAction(new LemonHelloAction("取消", new LemonHelloActionDelegate() {
                    @Override
                    public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                        helloView.hide();
                        // 退出程序
                    }
                }))
                .addAction(new LemonHelloAction("确定", Color.RED, new LemonHelloActionDelegate() {
                    @Override
                    public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                        helloView.hide();
                        // 跳转到登录页面
                        Intent logoutIntent = new Intent(context, YXLoginActivity.class);
                        logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(logoutIntent);
                    }
                }))
                .show(YXTransActivity.this);
    }

    /**
     * 获取货物类型列表
     *
     * @param token
     */
    private void getKinds(String token) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).transKindlist(token)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Response<ResponseBody> response) {
                        try {
                            switch (response.code()) {
                                case 200:
                                    if (response.body() == null) {
                                        DialogUIUtils.showToast(response.message());
                                    } else {
                                        String result = response.body().string();
                                        JSONObject jsonObject = JSON.parseObject(result);
                                        System.out.println("货物类型列表:" + result);
                                        switch (jsonObject.getString("code")) {
                                            case YXConfig.SUCCESS:
                                                kindList = JSON.parseArray(jsonObject.getString("data"), String.class);
                                                break;
                                            case YXConfig.ERROR:
                                                DialogUIUtils.showToast(jsonObject.getString("message"));
                                                break;
                                        }
                                    }
                                    break;
                                case 401:
                                    // token无效
                                    showTokenDialog();
                                    break;
                                default:
                                    DialogUIUtils.showToast(response.message());
                                    break;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        DialogUIUtils.showToast(t.getMessage());
                    }
                });
    }


    /**
     * 获取类型弹出框
     *
     * @param kinds
     */
    private void showKindDialog(final List<String> kinds) {
        //自定义
        View rootView = View.inflate(context, R.layout.custom_kind_dialog_layout, null);
        final BuildBean buildBean = DialogUIUtils.showCustomBottomAlert(context, rootView);
        buildBean.show();
        //final Dialog dialog = DialogUIUtils.showCustomAlert(context, rootView, Gravity.BOTTOM, true, false).show();
        //final EditText ed_Content = (EditText) rootView.findViewById(R.id.remark_dialog_content);
        final MyGridView gridView = (MyGridView) rootView.findViewById(R.id.gv_reply);
        final GoodsKindAdapter goodsKindAdapter = new GoodsKindAdapter(context, kinds);
        gridView.setAdapter(goodsKindAdapter);
        rootView.findViewById(R.id.txt_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUIUtils.dismiss(buildBean);
            }
        });
       /* rootView.findViewById(R.id.remark_dialog_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUIUtils.dismiss(dialog);
            }
        });*/
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goodsKindAdapter.changeState(position);
                tv_goods_type.setText(kinds.get(position));
                DialogUIUtils.dismiss(buildBean);
            }
        });
    }

    /**
     * 货物尺寸设置弹出框
     */
    private void showSizeDialog() {
        //自定义
        View rootView = View.inflate(context, R.layout.custom_size_dialog_layout, null);
        final BuildBean buildBean = DialogUIUtils.showCustomBottomAlert(context, rootView);
        buildBean.show();
        final EditText ed_Length = (EditText) rootView.findViewById(R.id.goods_size_length);
        final EditText ed_Width = (EditText) rootView.findViewById(R.id.goods_size_width);
        final EditText ed_Height = (EditText) rootView.findViewById(R.id.goods_size_height);
        rootView.findViewById(R.id.txt_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUIUtils.dismiss(buildBean);
            }
        });
        rootView.findViewById(R.id.txt_down).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String length = ed_Length.getText().toString();
                String width = ed_Width.getText().toString();
                String height = ed_Height.getText().toString();
                if (StringUtil.isNullOrEmpty(length)) {
                    DialogUIUtils.showToast("请设置货物长度");
                } else if (StringUtil.isNullOrEmpty(width)) {
                    DialogUIUtils.showToast("请设置货物宽度");
                } else if (StringUtil.isNullOrEmpty(height)) {
                    DialogUIUtils.showToast("请设置货物高度");
                } else {
                    DialogUIUtils.dismiss(buildBean);
                    tv_goods_size.setText(length + "-" + width + "-" + height);
                }
            }
        });
    }

    /**
     * 易碎状态
     */
    private int ysStatus(String status){
        switch (status){
            case "是":
                return 1;
            case "否":
                return 0;
            default:
                return 1;
        }
    }

    /**
     * 货物重量设置弹出框
     */
    private void showWeightDialog() {
        //自定义
        View rootView = View.inflate(context, R.layout.custom_weight_dialog_layout, null);
        final BuildBean buildBean = DialogUIUtils.showCustomBottomAlert(context, rootView);
        buildBean.show();
        final EditText ed_Weight = (EditText) rootView.findViewById(R.id.goods_weight_weight);
        rootView.findViewById(R.id.txt_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUIUtils.dismiss(buildBean);
            }
        });
        rootView.findViewById(R.id.txt_down).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String weight = ed_Weight.getText().toString();
                if (StringUtil.isNullOrEmpty(weight)) {
                    DialogUIUtils.showToast("请设置货物重量");
                } else {
                    DialogUIUtils.dismiss(buildBean);
                    goodsWeight = Double.parseDouble(weight);
                    tv_goods_weight.setText(String.format("%s kg", weight));
                }
            }
        });
    }

    /**
     * 出发时间
     */
    private void showDateTimeDialog() {
        DialogUIUtils.showDatePick(context, Gravity.CENTER, "选择日期", System.currentTimeMillis() + 60000, DateSelectorWheelView.TYPE_YYYYMMDDHHMM, 0, new DialogUIDateTimeSaveListener() {
            @Override
            public void onSaveSelectedDate(int tag, String selectedDate) {
                tv_goods_DateTime.setText(selectedDate);
            }
        }).show();
    }

    /**
     * 货运下单
     *
     * @param token      用户token
     * @param orderModel 下单参数模型
     */
    private void doOrder(String token, TransOrderModel orderModel) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).transOrder(token, StringUtil.getBody(JSON.parseObject(JSON.toJSONString(orderModel))))

                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Response<ResponseBody> response) {
                        try {
                            switch (response.code()) {
                                case 200:
                                    if (response.body() == null) {
                                        DialogUIUtils.showToast(response.message());
                                    } else {
                                        String result = response.body().string();
                                        System.out.println("货运下单返回数据:" + result);
                                        JSONObject jsonObject = JSON.parseObject(result);
                                        switch (jsonObject.getString("code")) {
                                            case YXConfig.SUCCESS:
                                                // 跳转到下单成功显示页面
                                                toActivity(YXRideSuccessActivity.createIntent(context, JSON.parseObject(jsonObject.getString("data"), OrderModel.class), String.valueOf(discount),"0"));
                                                finish();
                                                break;
                                            case YXConfig.ERROR:
                                                // 提示
                                                DialogUIUtils.showToast(jsonObject.getString("message"));
                                                break;
                                            default:
                                                break;
                                        }
                                    }
                                    break;
                                case 401:
                                    // token无效
                                    showTokenDialog();
                                    break;
                                default:
                                    break;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        DialogUIUtils.showToast(t.getMessage());
                    }
                });
    }

}
