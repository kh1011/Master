package com.yxclient.app.activity;

import android.app.Activity;
import android.app.Dialog;
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
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.TieBean;
import com.dou361.dialogui.listener.DialogUIItemListener;
import com.yxclient.app.R;
import com.yxclient.app.app.DemoApplication;
import com.yxclient.app.config.YXConfig;
import com.yxclient.app.http.RetrofitHttp;
import com.yxclient.app.model.bean.CoordinateModel;
import com.yxclient.app.model.bean.OrderModel;
import com.yxclient.app.model.bean.OriginAndSite;
import com.yxclient.app.model.bean.RideOrder;
import com.yxclient.app.overlay.DrivingRouteOverlay;
import com.yxclient.app.poisearch.util.CityModel;
import com.yxclient.app.poisearch.util.CityUtil;
import com.yxclient.app.utils.AMapUtil;
import com.yxclient.app.utils.CostUtils;
import com.yxclient.app.utils.DateTimePickUtils;
import com.yxclient.app.utils.DateUtil;
import com.yxclient.app.utils.StringUtil;

import net.lemonsoft.lemonhello.LemonHello;
import net.lemonsoft.lemonhello.LemonHelloAction;
import net.lemonsoft.lemonhello.LemonHelloInfo;
import net.lemonsoft.lemonhello.LemonHelloView;
import net.lemonsoft.lemonhello.interfaces.LemonHelloActionDelegate;

import java.io.IOException;
import java.util.ArrayList;
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
 * 顺风车
 */

public class YXNewRideActivity extends BaseActivity implements AMap.OnMapLoadedListener, AMap.OnMapClickListener, LocationSource, AMapLocationListener, RouteSearch.OnRouteSearchListener {
    private AMap aMap;
    private MapView mapView;
    private AMapLocationClient mLocationClient;
    private OnLocationChangedListener mListener;

    private Marker locMarker;
    private Circle ac;
    private Circle c;
    private long start;
    private final Interpolator interpolator1 = new LinearInterpolator();
    private TimerTask mTimerTask;
    private Timer mTimer = new Timer();

    @BindView(R.id.mytitle)
    TextView tv_Title;
    @BindView(R.id.ride_type)
    TextView tv_Type;
    @BindView(R.id.ride_ll_number)
    LinearLayout ll_Number;
    @BindView(R.id.ride_ll_line)
    View line;
    @BindView(R.id.ride_time)
    TextView tv_Time;
    @BindView(R.id.ride_num)
    TextView tv_Num;
    @BindView(R.id.ride_origin)
    TextView tv_Origin;
    @BindView(R.id.ride_site)
    TextView tv_Site;
    @BindView(R.id.ride_remark)
    TextView tv_Remark;
    @BindView(R.id.ride_re_money)
    LinearLayout re_Money;
    // 费用预算
    @BindView(R.id.ride_money)
    TextView tv_Money;
    // 里程
    @BindView(R.id.ride_discount)
    TextView tv_Discount;

    private CityModel mCurrCity;
    private String type = "";
    private String typeStr = "";
    // 请求码
    private static final int CHOOSE_ADDR_START = 1001;    //  出发地点
    private static final int CHOOSE_ADDR_DESTINTS = 1002; //  目的地
    // 选择所在城市
    private static final int REQUEST_CHOOSE_CITY = 1;     //  选择城市
    private RouteSearch mRouteSearch;
    private DriveRouteResult mDriveRouteResult;
    private final int ROUTE_TYPE_DRIVE = 2;
    // 大约里程
    private double discount = 0.0;
    // 乘车人数
    private int num;
    // 大约费用
    private double  money;


    @Override
    public Activity getActivity() {
        return this;
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, YXNewRideActivity.class);
    }

    public static Intent createIntent(Context context, String data) {
        return new Intent(context, YXNewRideActivity.class).
                putExtra(RESULT_DATA, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_activity_new_ride);
        ButterKnife.bind(this);
        initView();
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        init();
        initData();
        initEvent();
    }

    @Override
    public void initView() {
        type = getIntent().getStringExtra(RESULT_DATA);
        // 乘车人数默认为1人
        num = 1;
        tv_Num.setText("1人");
        switch (type) {
            case "1":
                tv_Title.setText("顺风车");
                tv_Type.setText("顺风车");
                typeStr = "顺风车";
                break;
            case "2":
                tv_Title.setText("专车");
                typeStr = "专车";
                tv_Type.setText("专车");
                //ll_Number.setVisibility(View.INVISIBLE);
                //line.setVisibility(View.INVISIBLE);
                break;
        }
        String testDate = DateUtil.getCurDateStr("yyyy-MM-dd HH:mm");
        String date[] = testDate.split(" ");
        System.out.println(date[0]);
        System.out.println(date[1]);
        rideOrder = new RideOrder();
        mCurrCity = CityUtil.getDefCityModel(context);
        System.out.println(mCurrCity.getCity());
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

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
        //aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setMyLocationEnabled(true);
        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(this);
    }

    private Marker addMarker(LatLng point) {
        Bitmap bMap = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.navi_map_gps_locked);
        BitmapDescriptor des = BitmapDescriptorFactory.fromBitmap(bMap);
        return aMap.addMarker(new MarkerOptions().position(point).icon(des)
                .anchor(0.5f, 0.5f));
    }

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
            AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
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
            if (aMapLocation.getErrorCode() == 0) {
                LatLng mylocation = new LatLng(aMapLocation.getLatitude(),
                        aMapLocation.getLongitude());
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mylocation, 19));
                addLocationMarker(aMapLocation);
                tv_Origin.setText(aMapLocation.getAddress());
                OriginAndSite originAndSite = new OriginAndSite();
                CoordinateModel coordinate = new CoordinateModel();
                originAndSite.setAddress(aMapLocation.getPoiName());
                originAndSite.setCity(aMapLocation.getCity());
                originAndSite.setCounty(aMapLocation.getDistrict());
                originAndSite.setName(aMapLocation.getPoiName());
                originAndSite.setProvince(aMapLocation.getProvince());
                coordinate.setLng(aMapLocation.getLongitude());
                coordinate.setLat(aMapLocation.getLatitude());
                originAndSite.setCoordinate(coordinate);
                rideOrder.setOrigin(originAndSite);
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
                    int dis = (int) drivePath.getDistance();
                    int dur = (int) drivePath.getDuration();
                    discount = Double.parseDouble(AMapUtil.getFriendlyLength1(dis));
                    tv_Discount.setText(String.valueOf(discount));

                    // 计算价格
                    doCalculation(discount);
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

        circleTask(Circle circle, long rate) {
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

    @OnClick({R.id.ride_num, R.id.ride_time, R.id.ride_appointment, R.id.ride_order, R.id.ride_get_origin, R.id.ride_get_site, R.id.ride_get_remark})
    void myClick(View view) {
        switch (view.getId()) {
            case R.id.ride_num:
                // 选择乘车人数
                /*NumDialogUtils numDialogUtils = new NumDialogUtils(YXNewRideActivity.this, tv_Num);
                numDialogUtils.dateTimePicKDialog();*/
                shoNumDialog();
                break;
            case R.id.ride_time:
                // 弹出日期选择
                DateTimePickUtils dateTimePicKDialog = new DateTimePickUtils(
                        YXNewRideActivity.this, null, tv_Time);
                dateTimePicKDialog.dateTimePicKDialog();
                break;
            case R.id.ride_appointment:
                String timeValue = tv_Time.getText().toString().trim();
                if ("出发时间".equals(timeValue)) {
                    DialogUIUtils.showToast("请选择出发时间!!");
                } else {
                    // 调用预约接口
                    String numStr = tv_Num.getText().toString().trim();
                    if ("乘车人数".equals(numStr) && type.equals("顺风车")) {
                        DialogUIUtils.showToast("请选择乘车人数!!");
                    } else {
                        rideOrder.setNumber(getNumber(numStr));
                        // 备注
                        String remark = tv_Remark.getText().toString();
                        if (remark.equals("请设置备注信息")) {
                            remark = "remark";
                        }
                        rideOrder.setNote(remark);
                        rideOrder.setType(typeStr);
                        String date[] = timeValue.split(" ");
                        rideOrder.setDate(date[0]);
                        rideOrder.setTime(date[1]);
                        rideOrder.setOnce(1);
                        rideOrder.setAmount(money);
                        rideOrder.setMileage(discount);
                        // 下单(预约)
                        doOrder(DemoApplication.getInstance().getMyToken(), rideOrder);
                    }
                }
                break;
            case R.id.ride_order:
                // 立即下单
                if (tv_Discount.getText().toString().equals("0.0")){
                    DialogUIUtils.showToast("操作太频繁");
                }
                String numStr = tv_Num.getText().toString().trim();
                if ("乘车人数".equals(numStr) && type.equals("顺风车")) {
                    DialogUIUtils.showToast("请选择乘车人数");
                }else if (tv_Site.getText().toString().trim().equals("请选择目的地")){
                    DialogUIUtils.showToast("请选择目的地");
                }else {
                    rideOrder.setNumber(getNumber(numStr));
                    // 备注
                    String remark = tv_Remark.getText().toString();
                    if (remark.equals("请设置备注信息")) {
                        remark = "remark";
                    }
                    rideOrder.setNote(remark);
                    rideOrder.setType(typeStr);
                    // 默认为当前日期+20分钟
                    String date[] = DateUtil.getRideTime().split(" ");
                    rideOrder.setDate(date[0]);
                    rideOrder.setTime(date[1]);
                    rideOrder.setOnce(1);
                    rideOrder.setAmount(money);
                    rideOrder.setMileage(discount);
                    // 下单
                    doOrder(DemoApplication.getInstance().getMyToken(), rideOrder);
                }
                break;
            case R.id.ride_get_remark:
                // 设置备注信息
                showRemarkDialog();
                break;
            case R.id.ride_get_origin:
                // 选择出发地点
                Intent intent = new Intent(context, YXChooseAddrActivity.class);
                intent.putExtra("city", mCurrCity.getCity());
                startActivityForResult(intent, CHOOSE_ADDR_START);
                break;
            case R.id.ride_get_site:
                // 获取目的地
                Intent intent2 = new Intent(context, YXChooseAddrActivity.class);
                intent2.putExtra("city", mCurrCity.getCity());
                startActivityForResult(intent2, CHOOSE_ADDR_DESTINTS);
                break;
            default:
                break;
        }
    }

    private void shoNumDialog() {
        List<TieBean> strings = new ArrayList<TieBean>();
        strings.add(new TieBean("1人"));
        strings.add(new TieBean("2人"));
        strings.add(new TieBean("3人"));
        strings.add(new TieBean("4人"));
        strings.add(new TieBean("5人"));
        strings.add(new TieBean("6人"));
        strings.add(new TieBean("7人"));
        DialogUIUtils.showSheet(context, strings, "", Gravity.CENTER, true, true, new DialogUIItemListener() {
            @Override
            public void onItemClick(CharSequence text, int position) {
                //showToast(text);
                num = getNumber(text.toString());
                tv_Num.setText(text);
                if (discount != 0.0) {
                    doCalculation(discount);
                }
            }
        }).show();
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
                    rideOrder.setOrigin(originAndSite);
                    assert originAndSite != null;
                    tv_Origin.setText(originAndSite.getProvince()+originAndSite.getCity()+originAndSite.getCounty()+originAndSite.getAddress());
                    break;
                case CHOOSE_ADDR_DESTINTS:
                    originAndSite = (OriginAndSite) bundle.getSerializable("model");
                    rideOrder.setSite(originAndSite);
                    assert originAndSite != null;
                    tv_Site.setText(originAndSite.getProvince()+originAndSite.getCity()+originAndSite.getCounty()+originAndSite.getAddress());
                    re_Money.setVisibility(View.VISIBLE);
                    double lat = rideOrder.getOrigin().getCoordinate().getLat();
                    double lng = rideOrder.getOrigin().getCoordinate().getLng();
                    LatLonPoint latLonPoint = new LatLonPoint(lat, lng);

                    double lat1 = rideOrder.getSite().getCoordinate().getLat();
                    double lng1 = rideOrder.getSite().getCoordinate().getLng();
                    LatLonPoint latLonPoint1 = new LatLonPoint(lat1, lng1);
                    searchRouteResult(ROUTE_TYPE_DRIVE, RouteSearch.DrivingDefault, latLonPoint, latLonPoint1);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 计算费用
     *
     * @param discount 距离
     */
    private void doCalculation(double discount) {
        switch (type) {
            case "1":
                //money = (int) mul_(num,CostUtils.getSFCMoney(discount),100);
                money =  CostUtils.countMoney(Double.valueOf(num)*CostUtils.getSFCMoney(discount))*100;
                tv_Money.setText(String.valueOf(CostUtils.countMoney(Double.valueOf(num)*CostUtils.getSFCMoney(discount))));
                break;
            case "2":
                //money = (int) mul_(num,CostUtils.getZCMoney(discount),100);
                money =  CostUtils.getZCMoney(discount)*100;
                tv_Money.setText(String.valueOf(CostUtils.getZCMoney(discount)));
                break;
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
        assert cityModel != null;
        if (!mCurrCity.getAdcode().equals(cityModel.getAdcode())) {
            mCurrCity = cityModel;
        }
        System.out.println("xxxxx" + mCurrCity.getCity());
    }

    private int getNumber(String str) {
        int val;
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
            case "5人":
                val = 5;
                break;
            case "6人":
                val = 6;
                break;
            case "7人":
                val = 7;
                break;
            default:
                val = 1;
                break;
        }
        return val;
    }

    private RideOrder rideOrder;

    /**
     * 出行下单
     *
     * @param token     用户token
     * @param rideOrder 下单参数模型
     */
    private void doOrder(String token, RideOrder rideOrder) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).rideOrder(token, StringUtil.getBody(JSON.parseObject(JSON.toJSONString(rideOrder))))
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
                                        System.out.println("出行下单返回数据:" + result);
                                        JSONObject jsonObject = JSON.parseObject(result);
                                        switch (jsonObject.getString("code")) {
                                            case YXConfig.SUCCESS:
                                                // 跳转到下单成功显示页面
                                                OrderModel orderModel = JSON.parseObject(jsonObject.getString("data"), OrderModel.class);
                                                toActivity(YXRideSuccessActivity.createIntent(context, orderModel, String.valueOf(discount),"1"));
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
                .show(YXNewRideActivity.this);
    }

    /**
     * 备注信息
     */
    private void showRemarkDialog() {
        View rootView = View.inflate(context, R.layout.custom_row_dialog_layout, null);
        final Dialog dialog = DialogUIUtils.showCustomAlert(this, rootView, Gravity.CENTER, true, false).show();
        final EditText ed_Content = (EditText) rootView.findViewById(R.id.remark_dialog_content);
        rootView.findViewById(R.id.remark_dialog_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = ed_Content.getText().toString();
                if (StringUtil.isNullOrEmpty(content)) {
                    DialogUIUtils.showToast("请输入备注信息!");
                } else {
                    DialogUIUtils.dismiss(dialog);
                    tv_Remark.setText(content);
                }
            }
        });
        rootView.findViewById(R.id.remark_dialog_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUIUtils.dismiss(dialog);
            }
        });
    }
}
