package com.yxdriver.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.Poi;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.trace.LBSTraceClient;
import com.amap.api.trace.TraceListener;
import com.amap.api.trace.TraceLocation;
import com.amap.api.trace.TraceOverlay;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIItemListener;
import com.yxdriver.app.R;
import com.yxdriver.app.adapter.OrderCustomerAdapter;
import com.yxdriver.app.app.DemoApplication;
import com.yxdriver.app.config.YXConfig;
import com.yxdriver.app.database.DbAdapter;
import com.yxdriver.app.event.RideOrderItemClickListener;
import com.yxdriver.app.http.RetrofitHttp;
import com.yxdriver.app.model.bean.CoordinateModel;
import com.yxdriver.app.model.bean.CustomerModel;
import com.yxdriver.app.model.bean.ListDataSave;
import com.yxdriver.app.model.bean.OrderModel;
import com.yxdriver.app.record.PathRecord;
import com.yxdriver.app.recorduitl.Util;
import com.yxdriver.app.utils.AmapTTSController;
import com.yxdriver.app.utils.OrderUtils;
import com.yxdriver.app.utils.PhoneUtils;
import com.yxdriver.app.utils.StringUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;
import zuo.biao.library.base.BaseActivity;

import static com.dou361.dialogui.DialogUIUtils.showToast;
import static com.yxdriver.app.config.YXConfig.T_ORDER_STATUS_CLOSE;
import static com.yxdriver.app.utils.StringUtil.changeObject;

/**
 * Created by mac on 2017/10/8.
 * 行程详情
 */

public class YXTripDetailActivity extends BaseActivity implements LocationSource,
        AMapLocationListener, TraceListener, INaviInfoCallback {
    // 客户列表
    @BindView(R.id.rv_list)
    RecyclerView listView;
    private List<CustomerModel> cusList = new ArrayList<>();
    // 订单对象
    private OrderModel orderModel;
    // 出行类别
    @BindView(R.id.item_trip_type)
    TextView tv_Type;
    // 状态
    @BindView(R.id.item_trip_status)
    TextView tv_Status;
    // 出发日期
    @BindView(R.id.item_trip_datetime)
    TextView tv_datetime;
    // 出发地点
    @BindView(R.id.item_trip_origin)
    TextView tv_origin;
    // 目的地
    @BindView(R.id.item_trip_site)
    TextView tv_site;
    // "客户"
    @BindView(R.id.trip_order_txt)
    TextView tv_Txt;
    // 取消行程按钮
    @BindView(R.id.trip_order_right)
    Button btn_Right;
    // 途径地点
    @BindView(R.id.item_trip_points)
    TextView tv_Points;
    @BindView(R.id.path)
    LinearLayout li_path;
    // 乘车人数
    @BindView(R.id.item_trip_number)
    TextView tv_Number;
    @BindView(R.id.number)
    LinearLayout li_number;
    // 可乘车
    @BindView(R.id.item_trip_number2)
    TextView tv_Number2;
    @BindView(R.id.number2)
    LinearLayout li_number2;
    //车辆载重
    @BindView(R.id.item_trip_capacity)
    TextView tv_capacity;
    @BindView(R.id.capacity)
    LinearLayout li_capacity;
    // 备注信息
    @BindView(R.id.item_trip_remark)
    TextView tv_remark;
    // 取消订单的原因
    String reasonStr = "";
    // 客户适配器
    OrderCustomerAdapter adapter;
    //地图
    @BindView(R.id.item_trip_map)
    RelativeLayout trip_map;
    @BindView(R.id.item_trip_map_origin)
    RelativeLayout trip_map_origin;
    @BindView(R.id.item_trip_map_site)
    RelativeLayout trip_map_site;
    // 里程计算相关
    private MapView mMapView;
    private AMap mAMap;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private PolylineOptions mPolyoptions, tracePolytion;

    private PathRecord record;

    private List<TraceLocation> mTracelocationlist = new ArrayList<TraceLocation>();
    private List<TraceOverlay> mOverlayList = new ArrayList<TraceOverlay>();
    private List<LatLng> recordList = new ArrayList<LatLng>();
    private int tracesize = 10;
    private int mDistance = 0;
    private TraceOverlay mTraceoverlay;
    private Marker mlocMarker;
    private boolean isBegain = false;

    // 起点
    LatLng origin;
    // 终点
    LatLng site;

    //小订单数
    private int orderNum=0;
    private int beginOrderNum=0;
    //小订单里程
    private float orderDistance = 0;
    private float allDistance = 0;

    private String OrderType;
    private String infoType;

    private List<CoordinateModel> carList = new ArrayList<CoordinateModel>();
    private ListDataSave dataSave;

    // 语音播报
    private AmapTTSController amapTTSController;

    LBSTraceClient mTraceClient;
    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_trip_order_detail);
        ButterKnife.bind(this);
        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);// 此方法必须重写
        init();
        initpolyline();
        initView();
        initData();
        initEvent();
    }
    public static Intent createIntent(Context context, OrderModel data) {
        return new Intent(context, YXTripDetailActivity.class).
                putExtra(RESULT_DATA, data);
    }
    @Override
    public void initView() {
        // 轨迹纠偏初始化
        mTraceClient = new LBSTraceClient(getApplicationContext());
        amapTTSController = AmapTTSController.getInstance(getApplicationContext());
        amapTTSController.init();
        li_path.setVisibility(View.GONE);
        orderModel = (OrderModel) getIntent().getSerializableExtra(RESULT_DATA);
        OrderType=orderModel.getOrderType();
        infoType=orderModel.getInfoType();
        System.out.println("订单对象：" + orderModel);
        if (orderModel.getOrderType().equals("express")){
            li_number.setVisibility(View.GONE);
            li_number2.setVisibility(View.GONE);
        }else {
            li_capacity.setVisibility(View.GONE);
        }
        listView.setLayoutManager(new LinearLayoutManager(context));
        listView.addItemDecoration(new DividerItemDecoration(
                context, DividerItemDecoration.VERTICAL));
        adapter = new OrderCustomerAdapter(context, cusList,OrderType,infoType);
        listView.setAdapter(adapter);
        getOrderInfo(DemoApplication.getInstance().getMyToken(), orderModel.getNo());
        // 起点
        origin = new LatLng(orderModel.getOrigin().getCoordinate().getLat(), orderModel.getOrigin().getCoordinate().getLng());
        // 终点
        site = new LatLng(orderModel.getSite().getCoordinate().getLat(), orderModel.getSite().getCoordinate().getLng());

        trip_map.setVisibility(View.GONE);
        trip_map_origin.setVisibility(View.GONE);
        trip_map_site.setVisibility(View.GONE);
    }
    @Override
    public void initData() {
        //
    }
    @Override
    public void initEvent() {
        adapter.setOnItemListener(new RideOrderItemClickListener() {
            @Override
            public void begainOrder(int position) {
                //toActivity(YXTestDistanceActivity.createIntent(context, ""));
                // 开始行程：开始记录行程
                doBegainOrder(DemoApplication.getInstance().getMyToken(), orderModel.getNo(), adapter.getItem(position).getExtraInfoUUID());
            }
            @Override
            public void endOrder(int position) {
                orderNum+=1;
                isend();
                allDistance=orderDistance+allDistance;
                // DialogUIUtils.showToast("总共里程数："+allDistance/1000d+">>>小订单里程数："+orderDistance/1000d);
                orderDistance=0;
                mOverlayList.add(mTraceoverlay);
                mTraceoverlay = new TraceOverlay(mAMap);
                doEndOrder(DemoApplication.getInstance().getMyToken(), orderModel.getNo(), adapter.getItem(position).getExtraInfoUUID(),allDistance/1000d);
                if (orderNum<orderModel.getInfo().size()){
                    begain();
                }else {
                    allDistance=0;
                }
            }
            @Override
            public void callOrder(int position) {
                // 拨打乘车人电话
                if (orderModel.getOrderType().equals("express")){
                    //PhoneUtils.call(context, adapter.getItem(position).getConsignorMobile());
                    String[] words2 = new String[]{"发货人电话："+adapter.getItem(position).getMobile(), "收货人电话："+adapter.getItem(position).getConsignorMobile()};
                    DialogUIUtils.showSingleChoose(context, "选择联系电话", 0, words2, new DialogUIItemListener() {
                        @Override
                        public void onItemClick(CharSequence text, int position) {
                            PhoneUtils.call(context, text.toString().substring(6));
                        }
                    }).show();
                }else {
                    PhoneUtils.call(context, adapter.getItem(position).getMobile());
                }
            }
            //导航到收货地
            @Override
            public void ReceiptPlace(int position){
                // 起点
                LatLng origin = new LatLng(orderModel.getInfo().get(position).getOrigin().getCoordinate().getLat(), orderModel.getInfo().get(position).getOrigin().getCoordinate().getLng());

                AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), new AmapNaviParams(null, null, new Poi(orderModel.getInfo().get(position).getOrigin().getAddress(), origin, ""),AmapNaviType.DRIVER), YXTripDetailActivity.this);
            }

            @Override
            public void confirmOrder(int position) {
                setOrderStatus(DemoApplication.getInstance().getMyToken(),position,1,"");
            }

            @Override
            public void refuseOrder(int position) {
                showrefuselOrderDialog(position);
            }

            //导航到送货地
            @Override
            public void DeliveryPlace(int position){
                // 终点
                LatLng site = new LatLng(orderModel.getInfo().get(position).getSite().getCoordinate().getLat(), orderModel.getInfo().get(position).getSite().getCoordinate().getLng());

                AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), new AmapNaviParams(null, null,new Poi(orderModel.getInfo().get(position).getSite().getAddress(),
                        site, ""),AmapNaviType.DRIVER), YXTripDetailActivity.this);
            }
        });
    }

    @Override
    public void onForwardClick(View v) {
        super.onForwardClick(v);
        // 取消行程
        showCancelOrderDialog();
    }

    @OnClick({R.id.item_trip_map,R.id.item_trip_map_origin,R.id.item_trip_map_site})
    void myClick(View view) {
        switch (view.getId()) {
            case R.id.item_trip_map:
                // 查看地图
                toActivity(YXOrderMapActivity.createIntent(context, changeObject(orderModel)));
                //startActivity(new Intent(YXTripDetailActivity.this, ComponentActivity.class));
                break;
            default:
                break;
        }
    }

    private void drawaView(OrderModel model) {
        tv_Status.setText(OrderUtils.getStatusStr(model.getStatus()));
        tv_datetime.setText(model.getDatetime());
        tv_origin.setText(model.getOrigin().getProvince()+model.getOrigin().getCity()+model.getOrigin().getCounty()+model.getOrigin().getAddress());
        tv_site.setText(model.getSite().getProvince()+model.getSite().getCity()+model.getSite().getCounty()+model.getSite().getAddress());
        tv_Type.setText(String.format("订单类型：%s", model.getInfoType()));
        if (model.getStatus() == YXConfig.T_ORDER_STATUS_ACCEPT || model.getStatus() == YXConfig.T_ORDER_STATUS_CREATE||model.getStatus()==YXConfig.T_ORDER_STATUS_CONFIRM) {
            btn_Right.setVisibility(View.VISIBLE);
        } else {
            btn_Right.setVisibility(View.GONE);
        }
        if (model.getInfoType().equals("快递小件")){
            li_capacity.setVisibility(View.GONE);
        }
        cusList.clear();
        List<CustomerModel> customerModelList = model.getInfo();
        if (customerModelList == null || customerModelList.size() < 1) {
            tv_Txt.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
            li_number.setVisibility(View.GONE);
        } else {
            cusList.addAll(customerModelList);
            OrderType=model.getOrderType();
            adapter.notifyDataSetChanged();
        }
        if (model.getCapacity()==null){
            li_capacity.setVisibility(View.GONE);
        }else {
            tv_capacity.setText(String.valueOf(model.getCapacity())+"kg");
        }
        if (orderModel.getInfo()!=null){
            int num=0;
            for (int i=0;i<model.getInfo().size();i++){
                if (model.getInfo().get(i).getStatus()!=T_ORDER_STATUS_CLOSE){
                    num=num+model.getInfo().get(i).getNumber();
                }
            }
            tv_Number.setText(String.valueOf(num));
        }
        tv_Number2.setText(orderModel.getSeats()+"");
        tv_remark.setText(model.getNote()==null?"":model.getNote().equals("请设置备注信息")?"":model.getNote().equals("remark")?" ":model.getNote());
    }

    private void showCancelOrderDialog() {
        final View rootView = View.inflate(context, R.layout.yx_cancel_order, null);
        final Dialog dialog = DialogUIUtils.showCustomAlert(this, rootView, Gravity.CENTER, true, false).show();
        RadioGroup group = (RadioGroup) rootView.findViewById(R.id.radioGroup);
        final EditText editText = (EditText) rootView.findViewById(R.id.cancel_ed_reason);
        rootView.findViewById(R.id.cancel_order_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reason = editText.getText().toString();
                if (StringUtil.isNullOrEmpty(reason) && StringUtil.isNullOrEmpty(reasonStr)) {
                    showToast("请选择或输入取消行程的原因!");
                } else if (!StringUtil.isNullOrEmpty(reason) && StringUtil.isNullOrEmpty(reasonStr)) {
                    DialogUIUtils.dismiss(dialog);
                    cancelOrder(DemoApplication.getInstance().getMyToken(), orderModel.getNo(), reason, "");
                } else if (!StringUtil.isNullOrEmpty(reasonStr) && StringUtil.isNullOrEmpty(reason)) {
                    reason = reasonStr;
                    DialogUIUtils.dismiss(dialog);
                    cancelOrder(DemoApplication.getInstance().getMyToken(), orderModel.getNo(), reason, "");
                } else if (!StringUtil.isNullOrEmpty(reasonStr) && !StringUtil.isNullOrEmpty(reason)) {
                    DialogUIUtils.dismiss(dialog);
                    cancelOrder(DemoApplication.getInstance().getMyToken(), orderModel.getNo(), reasonStr+",备注："+reason, "");
                }
            }
        });
        rootView.findViewById(R.id.cancel_order_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 取消
                DialogUIUtils.dismiss(dialog);
            }
        });
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //获取变更后的选中项的ID
                int radioButtonId = group.getCheckedRadioButtonId();
                //根据ID获取RadioButton的实例
                RadioButton rb = (RadioButton) rootView.findViewById(radioButtonId);
                reasonStr = rb.getText().toString();
                System.out.println(reasonStr);
            }
        });
    }

    /**
     * 拒绝订单
     * */
    private void showrefuselOrderDialog(final int position) {
        final View rootView = View.inflate(context, R.layout.yx_refuse_order, null);
        final Dialog dialog = DialogUIUtils.showCustomAlert(this, rootView, Gravity.CENTER, true, false).show();
        RadioGroup group = (RadioGroup) rootView.findViewById(R.id.radioGroup);
        final EditText editText = (EditText) rootView.findViewById(R.id.cancel_ed_reason);
        rootView.findViewById(R.id.cancel_order_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reason = editText.getText().toString();
                if (StringUtil.isNullOrEmpty(reason) && StringUtil.isNullOrEmpty(reasonStr)) {
                    showToast("请选择或输入拒绝订单的原因!");
                } else if (!StringUtil.isNullOrEmpty(reason) && StringUtil.isNullOrEmpty(reasonStr)) {
                    DialogUIUtils.dismiss(dialog);
                    setOrderStatus(DemoApplication.getInstance().getMyToken(),position,0,reason);
                } else if (!StringUtil.isNullOrEmpty(reasonStr) && StringUtil.isNullOrEmpty(reason)) {
                    reason = reasonStr;
                    DialogUIUtils.dismiss(dialog);
                    setOrderStatus(DemoApplication.getInstance().getMyToken(),position,0,reason);
                } else if (!StringUtil.isNullOrEmpty(reasonStr) && !StringUtil.isNullOrEmpty(reason)) {
                    DialogUIUtils.dismiss(dialog);
                    setOrderStatus(DemoApplication.getInstance().getMyToken(),position,0,reasonStr+",备注："+reason);
                }
            }
        });
        rootView.findViewById(R.id.cancel_order_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 取消
                DialogUIUtils.dismiss(dialog);
            }
        });
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //获取变更后的选中项的ID
                int radioButtonId = group.getCheckedRadioButtonId();
                //根据ID获取RadioButton的实例
                RadioButton rb = (RadioButton) rootView.findViewById(radioButtonId);
                reasonStr = rb.getText().toString();
                System.out.println(reasonStr);
            }
        });
    }
    /**
     * 取消行程
     *
     * @param token   用户token
     * @param orderId 订单编号
     * @param reason  取消订单的原因
     */
    private void cancelOrder(String token, String orderId, String reason, String extraInfoUUID) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        JSONObject object = new JSONObject();
        if (!StringUtil.isNullOrEmpty(extraInfoUUID)) {
            object.put("extraInfoUUID", extraInfoUUID);
        }
        object.put("orderNo", orderId);
        object.put("reason", reason);
        RetrofitHttp.getRetrofit(builder.build()).cancelOrder(token, StringUtil.getBody(object))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Response<ResponseBody> response) {
                        try {
                            switch (response.code()) {
                                case 200:
                                    if (response.body() == null) {
                                        showToast(response.message());
                                    } else {
                                        String result = response.body().string();
                                        System.out.println("取消行程数据" + result);
                                        JSONObject jsonObject = JSON.parseObject(result);
                                        if (jsonObject.getString("code").equals("success")) {
                                            // 取消行程成功
                                            showToast(jsonObject.getString("message"));
                                            onBackPressed();
                                            finish();
                                        } else {
                                            showToast(jsonObject.getString("message"));
                                        }
                                    }
                                    break;
                                default:
                                    showToast(JSON.parseObject(response.errorBody().string()).getString("message"));
                                    break;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        showToast(t.getMessage());
                    }
                });
    }

    /**
     * 开始行程
     *
     * @param token         用户token
     * @param orderId       订单编号
     * @param extraInfoUUID 行程ID
     */
    private void doBegainOrder(String token, final String orderId, String extraInfoUUID) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        JSONObject object = new JSONObject();
        object.put("orderNo", orderId);
        object.put("extraInfoUUID", extraInfoUUID);
        RetrofitHttp.getRetrofit(builder.build()).begainOrder(token, StringUtil.getBody(object))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Response<ResponseBody> response) {
                        try {
                            switch (response.code()) {
                                case 200:
                                    if (response.body() == null) {
                                        showToast(response.message());
                                    } else {
                                        String result = response.body().string();
                                        System.out.println("开始行程数据" + result);
                                        JSONObject jsonObject = JSON.parseObject(result);
                                        if (jsonObject.getString("code").equals("success")) {
                                            // 开始行程成功
                                            //DialogUIUtils.showToast(jsonObject.getString("message"));
                                            orderModel = JSON.parseObject(jsonObject.getString("data"), OrderModel.class);
                                            drawaView(orderModel);
                                            //开始计算里程

                                            if (beginOrderNum==0){
                                                begain();
                                                beginOrderNum+=1;
                                            }

                                        } else {
                                            showToast(jsonObject.getString("message"));
                                        }
                                    }
                                    break;
                                default:
                                    showToast(response.message());
                                    break;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        showToast(t.getMessage());
                    }
                });
    }

    private void begain() {
        DemoApplication.getInstance().setisBegain("1");
        mAMap.clear(true);
        if (recordList != null) {
            recordList.clear();
        }
        if (DemoApplication.getInstance().getcarList()!=null){
            DemoApplication.getInstance().setcarList("");
        }

    }

    private void isend() {
        // 计算总的里程
        List<AMapLocation> recordList_=new ArrayList<AMapLocation>();
        recordList_= Util.parseLatLngAMapLocation(recordList);
        List<TraceLocation> mGraspTraceLocationList = Util.parseTraceLocationList(recordList_);
        // 调用轨迹纠偏，将mGraspTraceLocationList进行轨迹纠偏处理
        //orderDistance = getDistance(Util.parseLatLngList(recordList_));
        orderDistance = getTotalDistance();
        mTraceClient.queryProcessedTrace(2, mGraspTraceLocationList, LBSTraceClient.TYPE_AMAP, this);
    }

    /**
     * 结束行程
     *
     * @param token         用户token
     * @param orderId       订单编号
     * @param extraInfoUUID 小订单号
     * @param mileage       公里数
     */
    private void doEndOrder(String token, final String orderId, String extraInfoUUID, double mileage) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        JSONObject object = new JSONObject();
        object.put("orderNo", orderId);
        object.put("extraInfoUUID", extraInfoUUID);
        object.put("mileage", mileage);
        RetrofitHttp.getRetrofit(builder.build()).endOrder(token, StringUtil.getBody(object))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Response<ResponseBody> response) {
                        try {
                            switch (response.code()) {
                                case 200:
                                    if (response.body() == null) {
                                        showToast(response.message());
                                    } else {
                                        String result = response.body().string();
                                        System.out.println("结束行程数据" + result);
                                        JSONObject jsonObject = JSON.parseObject(result);
                                        if (jsonObject.getString("code").equals("success")) {
                                            // 结束行程成功
                                            //DialogUIUtils.showToast(jsonObject.getString("message"));

                                            getOrderInfo(DemoApplication.getInstance().getMyToken(), orderModel.getNo());
                                        } else {
                                            showToast(jsonObject.getString("message"));
                                        }
                                    }
                                    break;
                                default:
                                    String err=response.errorBody().string();
                                    showToast(JSON.parseObject(err).getString("message"));
                                    break;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        showToast(t.getMessage());
                    }
                });
    }

    private void getOrderInfo(String token, String orderNo) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).tripOrderInfo(token, orderNo)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Response<ResponseBody> response) {
                        try {
                            switch (response.code()) {
                                case 200:
                                    if (response.body() == null) {
                                        showToast(response.message());
                                    } else {
                                        String result = response.body().string();
                                        System.out.println("订单详情数据：" + result);
                                        JSONObject jsonObject = JSON.parseObject(result);
                                        if (jsonObject.getString("code").equals("success")) {
                                            // 获取订单数据成功
                                            orderModel = JSON.parseObject(jsonObject.getString("data"), OrderModel.class);
                                            drawaView(orderModel);
                                        } else {
                                            showToast(jsonObject.getString("message"));
                                        }
                                    }
                                    break;
                                default:
                                    showToast(response.message());
                                    break;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        showToast(t.getMessage());
                    }
                });
    }

    /////计算里程开始
    private void init() {
        if (mAMap == null) {
            mAMap = mMapView.getMap();
            setUpMap();
        }
        if (record != null) {
            record = null;
        }
        record = new PathRecord();
        mTraceoverlay = new TraceOverlay(mAMap);
    }


    private float getDistance(List<LatLng> list) {
        float distance = 0;
        if (list == null || list.size() == 0) {
            return distance;
        }
        for (int i = 0; i < list.size() - 1; i++) {
            LatLng firstLatLng = list.get(i);
            LatLng secondLatLng =list.get(i + 1) ;
            double betweenDis = AMapUtils.calculateLineDistance(firstLatLng,
                    secondLatLng);
            distance = (float) (distance + betweenDis);
        }
        return distance;
    }

    private String getPathLineString(List<AMapLocation> list) {
        if (list == null || list.size() == 0) {
            return "";
        }
        StringBuffer pathline = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            AMapLocation location = list.get(i);
            String locString = amapLocationToString(location);
            pathline.append(locString).append(";");
        }
        String pathLineString = pathline.toString();
        pathLineString = pathLineString.substring(0,
                pathLineString.length() - 1);
        return pathLineString;
    }

    private String amapLocationToString(AMapLocation location) {
        StringBuffer locString = new StringBuffer();
        locString.append(location.getLatitude()).append(",");
        locString.append(location.getLongitude()).append(",");
        locString.append(location.getProvider()).append(",");
        locString.append(location.getTime()).append(",");
        locString.append(location.getSpeed()).append(",");
        locString.append(location.getBearing());
        return locString.toString();
    }

    private void initpolyline() {
        mPolyoptions = new PolylineOptions();
        mPolyoptions.width(10f);
        mPolyoptions.color(Color.GRAY);
        tracePolytion = new PolylineOptions();
        tracePolytion.width(40);
        tracePolytion.setCustomTexture(BitmapDescriptorFactory.fromResource(R.drawable.grasp_trace_line));
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        mAMap.setLocationSource(this);// 设置定位监听
        mAMap.moveCamera(CameraUpdateFactory.zoomTo(18));
        mAMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        mAMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        mAMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        amapTTSController.destroy();
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
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
     * 定位结果回调
     *
     * @param amapLocation 位置信息类
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                LatLng mylocation = new LatLng(amapLocation.getLatitude(),
                        amapLocation.getLongitude());
                mAMap.moveCamera(CameraUpdateFactory.changeLatLng(mylocation));
                // 如果已经开始行程，则开始记录
                Log.e("onLocationChanged: ","定位成功" );
                if (DemoApplication.getInstance().getisBegain().equals("1")) {
                    CoordinateModel point_=new CoordinateModel();
                    point_.setLat(amapLocation.getLatitude());
                    point_.setLng(amapLocation.getLongitude());
                    LatLng point=new LatLng(amapLocation.getLatitude(),amapLocation.getLongitude());
                    carList.add(point_);
                    DemoApplication.getInstance().setcarList("["+listToString(carList)+"]");
                    if (recordList==null){
                        JSONArray list_temp = JSON.parseArray(DemoApplication.getInstance().getcarList());
                        for (int i = 0; i < list_temp.size(); i++) {
                            JSONObject json = list_temp.getJSONObject(i);
                            recordList.add(new LatLng(Double.valueOf(json.getString("lat")),Double.valueOf(json.getString("lng"))));

                        }
                    }
                    recordList.add(point);

                    record.addpoint(amapLocation);
                    mPolyoptions.add(mylocation);
                    mTracelocationlist.add(Util.parseTraceLocation(amapLocation));
                    if (mTracelocationlist.size() > tracesize - 1) {
                        trace();
                    }
                }
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": "
                        + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }
    //将list转换为带有 ， 的字符串
    public static String listToString(List<CoordinateModel> list) {
        StringBuilder sb = new StringBuilder();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (i < list.size() - 1) {
                    sb.append(list.get(i) + ",");
                } else {
                    sb.append(list.get(i));
                }
            }
        }
        return sb.toString();
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

            mLocationOption.setInterval(2000);

            // 设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mLocationClient.startLocation();

        }
    }


    public void record(View view) {
        Intent intent = new Intent(context, RecordActivity.class);
        startActivity(intent);
    }

    private void trace() {
        List<TraceLocation> locationList = new ArrayList<>(mTracelocationlist);
        LBSTraceClient mTraceClient = new LBSTraceClient(getApplicationContext());
        mTraceClient.queryProcessedTrace(1, locationList, LBSTraceClient.TYPE_AMAP, this);
        TraceLocation lastlocation = mTracelocationlist.get(mTracelocationlist.size() - 1);
        mTracelocationlist.clear();
        mTracelocationlist.add(lastlocation);
    }

    /**
     * 轨迹纠偏失败回调。
     * @param i
     * @param s
     */
    @Override
    public void onRequestFailed(int i, String s) {
        mOverlayList.add(mTraceoverlay);
        mTraceoverlay = new TraceOverlay(mAMap);
    }

    @Override
    public void onTraceProcessing(int i, int i1, List<LatLng> list) {

    }

    /**
     * 轨迹纠偏成功回调。
     * @param lineID 纠偏的线路ID
     * @param linepoints 纠偏结果
     * @param distance 总距离
     * @param waitingtime 等待时间
     */
    @Override
    public void onFinished(int lineID, List<LatLng> linepoints, int distance, int waitingtime) {
        if (lineID == 1) {
            if (linepoints != null && linepoints.size()>0) {
                mTraceoverlay.add(linepoints);
                mDistance += distance;
                mTraceoverlay.setDistance(mTraceoverlay.getDistance()+distance);
            }
        } else if (lineID == 2) {
            DialogUIUtils.showToast("总距离"+distance);
            if (linepoints != null && linepoints.size()>0) {
                mAMap.addPolyline(new PolylineOptions()
                        .color(Color.RED)
                        .width(40).addAll(linepoints));
            }
        }

    }

    /**
     * 最后获取总距离
     * @return
     */
    private int getTotalDistance() {
        int distance = 0;
        for (TraceOverlay to : mOverlayList) {
            distance = distance + to.getDistance();
        }
        mOverlayList.clear();
        return distance;
    }

    /**
     * 司机确认/拒绝订单
     *
     */
    private void setOrderStatus(String token, int postion, int type, final String reason) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        JSONObject object = new JSONObject();
        object.put("orderNo", orderModel.getNo());
        object.put("extraInfoUUID",orderModel.getInfo().get(postion).getExtraInfoUUID() );
        object.put("status",type);
        object.put("reason",reason);
        RetrofitHttp.getRetrofit(builder.build()).selectOrderStatus(token, StringUtil.getBody(object))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Response<ResponseBody> response) {
                        try {
                            switch (response.code()) {
                                case 200:
                                    if (response.body() == null) {
                                        String err = response.errorBody().string();
                                        showToast(JSON.parseObject(err).getString("message"));
                                    } else {
                                        String result = response.body().string();
                                        JSONObject jsonObject = JSON.parseObject(result);
                                        if (jsonObject.getString("code").equals("success")) {
                                            // 获取订单数据成功
                                            if (reason.equals("")){
                                                showToast("确认订单");
                                            }else {
                                                showToast("拒绝订单");
                                            }

                                            getOrderInfo(DemoApplication.getInstance().getMyToken(), orderModel.getNo());
                                            adapter.notifyDataSetChanged();
                                        } else {
                                            showToast(jsonObject.getString("message"));
                                        }
                                    }
                                    break;
                                default:
                                    String err = response.errorBody().string();
                                    showToast(JSON.parseObject(err).getString("message"));
                                    break;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        showToast(t.getMessage());
                    }
                });
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
