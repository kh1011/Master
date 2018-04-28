package com.yxdriver.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
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
import com.dou361.dialogui.DialogUIUtils;
import com.yxdriver.app.R;
import com.yxdriver.app.app.DemoApplication;
import com.yxdriver.app.http.RetrofitHttp;
import com.yxdriver.app.model.bean.CoordinateModel;
import com.yxdriver.app.model.bean.OrderModel;
import com.yxdriver.app.model.bean.OriginAndSite;
import com.yxdriver.app.model.bean.RideOrder;
import com.yxdriver.app.poisearch.util.CityModel;
import com.yxdriver.app.poisearch.util.CityUtil;
import com.yxdriver.app.utils.DateTimePickUtils;
import com.yxdriver.app.utils.StringUtil;

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
 * Created by mac on 2017/9/20.
 * 司机发布货运
 */

public class YXReleaseGoodsOrderActivity extends BaseActivity implements AMap.OnMapLoadedListener, AMap.OnMapClickListener, LocationSource, AMapLocationListener {
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
    private CityModel mCurrCity;
    // 顶部切换按钮
    @BindView(R.id.do_order_sfc)
    Button btn_sfc;
    @BindView(R.id.do_order_zc)
    Button btn_zc;
    @BindView(R.id.do_order_xj)
    Button btn_xj;
    // 出发日期
    @BindView(R.id.do_order_datetime)
    TextView tv_DateTime;
    @BindView(R.id.do_order_origin)
    TextView tv_Origin;
    @BindView(R.id.do_order_site)
    TextView tv_Site;
    @BindView(R.id.do_order_points)
    TextView tv_Points;
    @BindView(R.id.do_order_remark)
    TextView tv_Remark;
    // 车辆载重
    @BindView(R.id.do_order_weight)
    TextView tv_Weight;
    @BindView(R.id.do_order_re_weight)
    RelativeLayout re_weright;
    // 货运类别
    private String orderType;
    // 货运订单
    private RideOrder rideOrder;
    // 途经地点
    private List<OriginAndSite> pointList;
    @BindView(R.id.do_order_re_rows)
    RelativeLayout re_rows;
    // 请求码
    private static final int CHOOSE_ADDR_START = 1001;    //  出发地点
    private static final int CHOOSE_ADDR_DESTINTS = 1002; //  目的地
    private static final int CHOOSE_ADDR_POINTS = 1003;   //  途径地点
    // 选择所在城市
    private static final int REQUEST_CHOOSE_CITY = 1;


    public static Intent createIntent(Context context, String data) {
        return new Intent(context, YXReleaseGoodsOrderActivity.class).
                putExtra(RESULT_DATA, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 判断如果登录过，则进入主页
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_activity_goods_doorder);
        ButterKnife.bind(this);
        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void initView() {
        // 默认为小型货运
        orderType = "小型货运";
        rideOrder = new RideOrder();
        rideOrder.setType(orderType);
        mCurrCity = CityUtil.getDefCityModel(context);
        System.out.println("当前城市：" + mCurrCity);
        pointList = new ArrayList<>();
        re_rows.setVisibility(View.GONE);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }

    @SuppressWarnings("all")
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        // dadadadafsfs
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
        return aMap.addMarker(new MarkerOptions().position(point).icon(des)
                .anchor(0.5f, 0.5f));
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

    @OnClick({R.id.do_order_re_weight, R.id.do_order_sfc, R.id.do_order_zc, R.id.do_order_xj, R.id.do_order_re_datetime, R.id.do_order_re_orign, R.id.do_order_re_site, R.id.do_order_re_rows, R.id.do_order_re_remark, R.id.do_order_btn})
    void myClick(View view) {
        switch (view.getId()) {
            case R.id.do_order_sfc:
                orderType = "小型货运";
                re_weright.setVisibility(View.VISIBLE);
                rideOrder.setType(orderType);
                btn_sfc.setTextColor(ContextCompat.getColor(context, R.color.main_blue));
                btn_zc.setTextColor(Color.WHITE);
                btn_xj.setTextColor(Color.WHITE);
                btn_sfc.setBackgroundResource(R.drawable.left_bold);
                btn_zc.setBackgroundResource(R.drawable.right_transparent);
                btn_xj.setBackgroundResource(R.drawable.right_transparent);
                break;
            case R.id.do_order_zc:
                orderType = "大型货运";
                re_weright.setVisibility(View.VISIBLE);
                rideOrder.setType(orderType);
                btn_zc.setTextColor(ContextCompat.getColor(context, R.color.main_blue));
                btn_sfc.setTextColor(Color.WHITE);
                btn_xj.setTextColor(Color.WHITE);
                btn_zc.setBackgroundResource(R.drawable.left_bold);
                btn_sfc.setBackgroundResource(R.drawable.right_transparent);
                btn_xj.setBackgroundResource(R.drawable.right_transparent);
                break;
            case R.id.do_order_xj:
                orderType = "快递小件";
                re_weright.setVisibility(View.GONE);
                rideOrder.setType(orderType);
                btn_xj.setTextColor(ContextCompat.getColor(context, R.color.main_blue));
                btn_zc.setTextColor(Color.WHITE);
                btn_sfc.setTextColor(Color.WHITE);
                btn_xj.setBackgroundResource(R.drawable.left_bold);
                btn_sfc.setBackgroundResource(R.drawable.right_transparent);
                btn_zc.setBackgroundResource(R.drawable.right_transparent);
                break;
            case R.id.do_order_re_datetime:
                // 选择出行日期、时间
                DateTimePickUtils dateTimePicKDialog = new DateTimePickUtils(
                        context, null, tv_DateTime);
                dateTimePicKDialog.dateTimePicKDialog();
                break;
            case R.id.do_order_re_orign:
                // 获取出发地点
                Intent intent = new Intent(context, YXChooseAddrActivity.class);
                intent.putExtra("city", mCurrCity.getCity());
                startActivityForResult(intent, CHOOSE_ADDR_START);
                break;
            case R.id.do_order_re_site:
                // 获取目的地
                Intent intent2 = new Intent(context, YXChooseAddrActivity.class);
                intent2.putExtra("city", mCurrCity.getCity());
                startActivityForResult(intent2, CHOOSE_ADDR_DESTINTS);
                break;
            case R.id.do_order_re_rows:
                // 获取途经点
                Intent intent3 = new Intent(context, YXChooseAddrActivity.class);
                intent3.putExtra("city", mCurrCity.getCity());
                startActivityForResult(intent3, CHOOSE_ADDR_POINTS);
                break;
            case R.id.do_order_re_remark:
                showRemarkDialog();
                break;
            case R.id.do_order_re_weight:
                // 设置车辆载重
                showWeightDialog();
                break;
            case R.id.do_order_btn:
                // 发布出行
                String dateTime = tv_DateTime.getText().toString();
                String origin = tv_Origin.getText().toString();
                String site = tv_Site.getText().toString();
                String remark = tv_Remark.getText().toString();
                String weight = tv_Weight.getText().toString();
                if (!orderType.equals("快递小件")){
                    if (StringUtil.isNullOrEmpty(weight)||weight.equals("请输入车辆载重")) {
                        DialogUIUtils.showToast("请输入车辆载重");
                    }else {
                        rideOrder.setCapacity(Double.valueOf(weight.substring(0,weight.length()-2)));
                    }
                }
                if (dateTime.equals("请选择出发日期")) {
                    DialogUIUtils.showToast("请选择出发日期");
                } else if (origin.equals("请选择出发地")) {
                    DialogUIUtils.showToast("请选择出发地");
                }  else if (site.equals("请选择目的地")) {
                    DialogUIUtils.showToast("请选择目的地");
                } else {
                    //调用发布接口
                    //调用发布接口
                    rideOrder.setNote(remark);
                    // 日期处理
                    String[] dates = dateTime.split(" ");
                    rideOrder.setDate(dates[0]);
                    rideOrder.setTime(dates[1]);
                    rideOrder.setNote(remark);
                    System.out.println(JSON.toJSONString(rideOrder));
                    getCarInfo(DemoApplication.getInstance().getMyToken());
                }
                break;
            default:
                break;
        }
    }

    private void showWeightDialog() {
        View rootView = View.inflate(context, R.layout.driver_weight_dialog, null);
        final Dialog dialog = DialogUIUtils.showCustomAlert(this, rootView, Gravity.CENTER, true, false).show();
        final EditText ed_Content = (EditText) rootView.findViewById(R.id.drivernum_dialog_content);
        rootView.findViewById(R.id.remark_dialog_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = ed_Content.getText().toString();
                if (StringUtil.isNullOrEmpty(content)) {
                    DialogUIUtils.showToast("请输入车辆载重!");
                } else {
                    DialogUIUtils.dismiss(dialog);
                    tv_Weight.setText(String.format("%skg", content));
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

    /**
     * 获取车辆信息
     * @param token 用户token
     */
    private void getCarInfo(final String token) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).getCarInfo(token)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Response<ResponseBody> response) {
                        switch (response.code()) {
                            case 200:
                                try {
                                    if (response.body() == null) {
                                        DialogUIUtils.showToast(response.message());
                                    } else {
                                        String result = response.body().string();
                                        System.out.println("发布订单列表数据" + result);
                                        JSONObject jsonObject = JSON.parseObject(result);
                                        if (jsonObject.getString("code").equals("success")) {
                                            JSONObject data = JSON.parseObject(jsonObject.getString("data"));
                                            if (data.getString("type").equals("面包车")||data.getString("type").equals("轿车")||
                                                    data.getString("type").equals("SUV")||data.getString("type").equals("MPV")) {
                                                if (orderType.equals("快递小件")){
                                                    doOrder(token, rideOrder);
                                                }else{
                                                    DialogUIUtils.showToast("出行司机不能发布货运订单(快递小件除外)");
                                                }

                                            }else if (data.getString("type").equals("微型货车")||data.getString("type").equals("轻型货车")||
                                                    data.getString("type").equals("中型货车")||data.getString("type").equals("大型货车")){
                                                doOrder(token, rideOrder);
                                            }else {
                                                DialogUIUtils.showToast("发布错误");
                                            }

                                        } else {
                                            DialogUIUtils.showToast(jsonObject.getString("message"));
                                        }
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            default:
                                DialogUIUtils.showToast(response.message());
                                break;
                        }
                    }
                    @Override
                    public void onFailure(Throwable t) {
                        DialogUIUtils.showToast(t.getMessage());
                    }
                });
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
                    break;
                case CHOOSE_ADDR_POINTS:
                    originAndSite = (OriginAndSite) bundle.getSerializable("model");
                    assert originAndSite != null;
                    pointList.add(originAndSite);
                    rideOrder.setPathPoint(pointList);
                    String val = tv_Points.getText().toString();
                    if (val.equals("请设置途径地点")) {
                        tv_Points.setText(originAndSite.getName());
                    } else {
                        tv_Points.setText(val + "、" + originAndSite.getName());
                    }
                    break;
                default:
                    break;
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
        assert cityModel != null;
        if (!mCurrCity.getAdcode().equals(cityModel.getAdcode())) {
            mCurrCity = cityModel;
        }
        System.out.println("xxxxx" + mCurrCity.getCity());
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

    /**
     * 货运下单
     *
     * @param token     用户token
     * @param rideOrder 下单参数模型
     */
    private void doOrder(String token, RideOrder rideOrder) {
        final Dialog dialog = DialogUIUtils.showMdLoading(context, "数据加载中", true, true, true, true).show();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).goodsOrder(token, StringUtil.getBody(JSON.parseObject(JSON.toJSONString(rideOrder))))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Response<ResponseBody> response) {
                        DialogUIUtils.dismiss(dialog);
                        try {
                            switch (response.code()) {
                                case 200:
                                    if (response.body() == null) {
                                        DialogUIUtils.showToast("接口请求错误!");
                                    } else {
                                        String result = response.body().string();
                                        System.out.println("货运下单返回结果：" + result);
                                        JSONObject jsonObject = JSON.parseObject(result);
                                        switch (jsonObject.getString("code")) {
                                            case "success":
                                                // 到下单成功页面
                                                DialogUIUtils.showToast(jsonObject.getString("message"));
                                                toActivity(YXOrderSuccessActivity.createIntent(context, JSON.parseObject(jsonObject.getString("data"), OrderModel.class)));
                                                finish();
                                                break;
                                            default:
                                                DialogUIUtils.showToast(jsonObject.getString("message"));
                                                break;
                                        }
                                    }
                                    break;
                                case 401:
                                    // token无效
                                    DialogUIUtils.showToast("登录过期");
                                    break;
                                default:
                                    String err=response.errorBody().string();
                                    DialogUIUtils.showToast(JSON.parseObject(err).getString("message"));
                                    break;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        DialogUIUtils.dismiss(dialog);
                        DialogUIUtils.showToast("网络不给力");
                    }
                });
    }
}
