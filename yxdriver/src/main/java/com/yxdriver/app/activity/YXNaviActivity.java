package com.yxdriver.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.yxdriver.app.R;
import com.yxdriver.app.model.bean.OrderModel;
import com.yxdriver.app.model.bean.PointModel;
import com.yxdriver.app.utils.AmapTTSController;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import zuo.biao.library.base.BaseActivity;

/**
 * Created by mac on 2017/11/14.
 * ////////////////////////////////////////////////////////////////////
 * //                            _ooOoo_                             //
 * //                           o8888888o                            //
 * //                           88" . "88                            //
 * //                           (| ^_^ |)                            //
 * //                           O\  =  /O                            //
 * //                        ____/`---'\____                         //
 * //                      .'  \\|     |//  `.                       //
 * //                     /  \\|||  :  |||//  \                      //
 * //                    /  _||||| -:- |||||-  \                     //
 * //                    |   | \\\  -  /// |   |                     //
 * //                    | \_|  ''\---/''  |   |                     //
 * //                    \  .-\__  `-`  ___/-. /                     //
 * //                  ___`. .'  /--.--\  `. . ___                   //
 * //                ."" '<  `.___\_<|>_/___.'  >'"".                //
 * //              | | :  `- \`.;`\ _ /`;.`/ - ` : | |               //
 * //              \  \ `-.   \_ __\ /__ _/   .-` /  /               //
 * //        ========`-.____`-.___\_____/___.-`____.-'========       //
 * //                             `=---='                            //
 * //        ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^      //
 * //         佛祖保佑       永无BUG        永不修改                    //
 * ////////////////////////////////////////////////////////////////////
 */
// 线路导航
public class YXNaviActivity extends BaseActivity implements INaviInfoCallback {
    private AmapTTSController amapTTSController;
    private OrderModel orderModel;

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_order_navi);
        ButterKnife.bind(this);
        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }
    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


    /**
     * 启动这个Activity的Intent
     *
     * @param context dfsfds
     * @param data
     * @return
     */
    public static Intent createIntent(Context context, OrderModel data) {
        return new Intent(context, YXNaviActivity.class).
                putExtra(RESULT_DATA, data);
    }

    @Override
    public void initView() {
        amapTTSController = AmapTTSController.getInstance(getApplicationContext());
        amapTTSController.init();
    }

    @Override
    public void initData() {
        // 设置导航数据
        orderModel = (OrderModel) getIntent().getSerializableExtra(RESULT_DATA);
        // 起点
        LatLng origin = new LatLng(orderModel.getOrigin().getCoordinate().getLat(), orderModel.getOrigin().getCoordinate().getLng());
        // 终点
        LatLng site = new LatLng(orderModel.getSite().getCoordinate().getLat(), orderModel.getSite().getCoordinate().getLng());
        // 途径点
        List<LatLng> paths = new ArrayList<>();
        // 判断是否有途径点
        List<PointModel> pointModelList = orderModel.getPathPoint();
        if (pointModelList != null) {//pointModelList.size() > 0
            for (int i = 0; i < pointModelList.size(); i++) {
                paths.add(new LatLng(pointModelList.get(i).getCoordinate().getLat(), pointModelList.get(i).getCoordinate().getLng()));
            }
        }
        // 如果有途径地点，则进入途径地点导航模式，否则进入起点终点导航模式
        if (pointModelList != null) {//|| pointModelList.size() > 0
            List<Poi> poiList = new ArrayList();
            for (int i = 0; i < pointModelList.size(); i++) {
                poiList.add(new Poi(pointModelList.get(i).getAddress(), new LatLng(pointModelList.get(i).getCoordinate().getLat(), pointModelList.get(i).getCoordinate().getLng()), ""));
            }
            AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), new AmapNaviParams(new Poi(orderModel.getOrigin().getAddress(), origin, ""), poiList, new Poi(orderModel.getSite().getAddress(), site, ""), AmapNaviType.DRIVER), YXNaviActivity.this);
        } else {
            AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), new AmapNaviParams(new Poi(orderModel.getOrigin().getAddress(), origin, ""), null, new Poi(orderModel.getSite().getAddress(), site, ""), AmapNaviType.DRIVER), YXNaviActivity.this);
        }
    }

    @Override
    public void initEvent() {

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        amapTTSController.destroy();
    }
}
