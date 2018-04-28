package com.yxclient.app.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.dou361.dialogui.DialogUIUtils;
import com.yxclient.app.R;
import com.yxclient.app.model.bean.newOrderModel;
import com.yxclient.app.overlay.DrivingRouteOverlay;
import com.yxclient.app.utils.AMapUtil;
import com.yxclient.app.utils.AmapTTSController;

import butterknife.BindView;
import butterknife.ButterKnife;
import zuo.biao.library.base.BaseActivity;

/**
 * 订单地图
 * 标注出发地点、目的地、途径地点、实现导航
 */
public class YXOrderMapActivity extends BaseActivity implements AMap.OnMapClickListener,
        AMap.OnMarkerClickListener, AMap.OnInfoWindowClickListener, AMap.InfoWindowAdapter, RouteSearch.OnRouteSearchListener,INaviInfoCallback {
    @BindView(R.id.route_map)
    MapView mapView;
    @BindView(R.id.map_origin)
    TextView tv_Origin;
    @BindView(R.id.map_site)
    TextView tv_Site;

    private newOrderModel model;// 订单

    private AMap aMap;
    private RouteSearch mRouteSearch;
    private DriveRouteResult mDriveRouteResult;
    // private LatLonPoint mStartPoint = new LatLonPoint(39.942295, 116.335891);//起点，39.942295,116.335891
    // private LatLonPoint mEndPoint = new LatLonPoint(39.995576, 116.481288);//终点，39.995576,116.481288

    private LatLonPoint mStartPoint;
    private LatLonPoint mEndPoint;
    private final int ROUTE_TYPE_DRIVE = 2;
    private RelativeLayout mBottomLayout, mHeadLayout;
    private TextView mRotueTimeDes, mRouteDetailDes;
    private ProgressDialog progDialog = null;// 搜索时进度条
    // 语音播报
    private AmapTTSController amapTTSController;

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_order_map);
        ButterKnife.bind(this);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }
    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    public static Intent createIntent(Context context, newOrderModel data) {
        return new Intent(context, YXOrderMapActivity.class).
                putExtra(RESULT_DATA, data);
    }

    @Override
    public void initView() {
        amapTTSController = AmapTTSController.getInstance(getApplicationContext());
        amapTTSController.init();
        model = (newOrderModel) getIntent().getSerializableExtra(RESULT_DATA);
        tv_Origin.setText(model.getOrigin().getProvince()+model.getOrigin().getCity()+model.getOrigin().getCounty()+model.getOrigin().getAddress());
        tv_Site.setText(model.getSite().getProvince()+model.getSite().getCity()+model.getSite().getCounty()+model.getSite().getAddress());
        mStartPoint = new LatLonPoint(model.getOrigin().getCoordinate().getLat(), model.getOrigin().getCoordinate().getLng());
        mEndPoint = new LatLonPoint(model.getSite().getCoordinate().getLat(), model.getSite().getCoordinate().getLng());
        init();
        setfromandtoMarker();
        searchRouteResult(ROUTE_TYPE_DRIVE, RouteSearch.DrivingDefault);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }

    @Override
    public void onForwardClick(View v) {
        super.onForwardClick(v);

        //toActivity(new Intent(YXOrderMapActivity.this,ComponentActivity.class));
        // 起点
        LatLng origin = new LatLng(model.getOrigin().getCoordinate().getLat(), model.getOrigin().getCoordinate().getLng());
        // 终点
        LatLng site = new LatLng(model.getSite().getCoordinate().getLat(), model.getSite().getCoordinate().getLng());
        AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), new AmapNaviParams(new Poi(model.getOrigin().getAddress(), origin, ""), null,
                                    new Poi(model.getSite().getAddress(), site, ""), AmapNaviType.DRIVER), YXOrderMapActivity.this);
    }

    private void setfromandtoMarker() {
        aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(mStartPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.start)));
        aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(mEndPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.end)));
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        registerListener();
        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(this);
        mBottomLayout = (RelativeLayout) findViewById(R.id.bottom_layout);
        mHeadLayout = (RelativeLayout) findViewById(R.id.routemap_header);
        mRotueTimeDes = (TextView) findViewById(R.id.firstline);
        mRouteDetailDes = (TextView) findViewById(R.id.secondline);
        mHeadLayout.setVisibility(View.GONE);
    }

    /**
     * 注册监听
     */
    private void registerListener() {
        aMap.setOnMapClickListener(YXOrderMapActivity.this);
        aMap.setOnMarkerClickListener(YXOrderMapActivity.this);
        aMap.setOnInfoWindowClickListener(YXOrderMapActivity.this);
        aMap.setInfoWindowAdapter(YXOrderMapActivity.this);

    }

    @Override
    public View getInfoContents(Marker arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public View getInfoWindow(Marker arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onInfoWindowClick(Marker arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onMarkerClick(Marker arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onMapClick(LatLng arg0) {
        // TODO Auto-generated method stub

    }

    /**
     * 开始搜索路径规划方案
     */
    public void searchRouteResult(int routeType, int mode) {
        if (mStartPoint == null) {
            DialogUIUtils.showToast("定位中，稍后再试...");
            return;
        }
        if (mEndPoint == null) {
            DialogUIUtils.showToast("终点未设置");
        }
        showProgressDialog();
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                mStartPoint, mEndPoint);
        if (routeType == ROUTE_TYPE_DRIVE) {// 驾车路径规划
            RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, mode, null,
                    null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
            mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
        }
    }

    @Override
    public void onBusRouteSearched(BusRouteResult result, int errorCode) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
        dissmissProgressDialog();
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mDriveRouteResult = result;
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
                    mBottomLayout.setVisibility(View.VISIBLE);
                    int dis = (int) drivePath.getDistance();
                    int dur = (int) drivePath.getDuration();
                    String des = AMapUtil.getFriendlyTime(dur) + "(" + AMapUtil.getFriendlyLength(dis) + ")";
                    mRotueTimeDes.setText(des);
                    mRouteDetailDes.setVisibility(View.VISIBLE);
                    int taxiCost = (int) mDriveRouteResult.getTaxiCost();
                    mRouteDetailDes.setText("打车约" + taxiCost + "元");
                    mBottomLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            /*Intent intent = new Intent(context,
                                    DriveRouteDetailActivity.class);
                            intent.putExtra("drive_path", drivePath);
                            intent.putExtra("drive_result",
                                    mDriveRouteResult);
                            startActivity(intent);*/
                        }
                    });

                } else if (result != null && result.getPaths() == null) {
                    DialogUIUtils.showToast(R.string.no_result);
                }

            } else {
                DialogUIUtils.showToast(R.string.no_result);
            }
        } else {
            DialogUIUtils.showToast(errorCode);
        }


    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {

    }


    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("正在搜索");
        progDialog.show();
    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
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
        amapTTSController.destroy();
    }

    @Override
    public void onRideRouteSearched(RideRouteResult arg0, int arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onArriveDestination(boolean b) {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {

    }

    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onGetNavigationText(String s) {
        amapTTSController.onGetNavigationText(s);
    }

    @Override
    public void onStopSpeaking() {
        amapTTSController.stopSpeaking();
    }

    @Override
    public void onReCalculateRoute(int i) {

    }

    @Override
    public void onExitPage(int i) {

    }
}