package com.yxclient.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.maps.AMap;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.bumptech.glide.Glide;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIListener;
import com.yxclient.app.R;
import com.yxclient.app.app.DemoApplication;
import com.yxclient.app.http.RetrofitHttp;
import com.yxclient.app.model.bean.OrderModel;
import com.yxclient.app.model.bean.OriginAndSite;
import com.yxclient.app.model.bean.PointModel;
import com.yxclient.app.model.bean.newOrderModel;
import com.yxclient.app.overlay.DrivingRouteOverlay;
import com.yxclient.app.utils.AMapUtil;
import com.yxclient.app.utils.CostUtils;
import com.yxclient.app.utils.StringUtil;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;
import zuo.biao.library.base.BaseActivity;

import static com.dou361.dialogui.DialogUIUtils.showToast;
import static com.yxclient.app.utils.StringUtil.changeObject;

/**
 * Created by mac on 2017/8/22.
 * 出行类订单详情（推荐订单）
 */

public class YXRideOrderDetailActivity extends BaseActivity implements RouteSearch.OnRouteSearchListener {
    private OrderModel orderModel;

    @BindView(R.id.profile_image)
    CircleImageView imageView;
    @BindView(R.id.index_item_dirver)
    TextView tv_driver;
    @BindView(R.id.index_item_carname)
    TextView tv_carName;
    @BindView(R.id.index_item_type)
    TextView tv_type;
    @BindView(R.id.index_item_txt)
    TextView tv_txt;
    @BindView(R.id.index_item_seats)
    TextView tv_seats;
    @BindView(R.id.index_item_datetime)
    TextView tv_datetime;
    @BindView(R.id.index_item_origin)
    TextView tv_origin;
    @BindView(R.id.index_item_site)
    TextView tv_site;
    // 途径地点
    @BindView(R.id.index_item_path)
    TextView tv_Path;
    @BindView(R.id.order_detail_path)
    LinearLayout llpath;
    // 备注信息
    @BindView(R.id.order_detail_renark)
    TextView tv_Remark;
    //载重
    @BindView(R.id.index_item_load)
    TextView tv_load;
    @BindView(R.id.order_detail_load)
    LinearLayout li_load;

    private AMap aMap;

    TextView tv_discount;
    // 大约里程
    private double discount = 0.0;

    String content_;
    String linker;
    String mobile;
    String remark;
    String origin_;
    PointModel origin;
    TextView tv_origins;

    private Double peoNum;
    TextView tv_money;

    private RouteSearch mRouteSearch;
    private DriveRouteResult mDriveRouteResult;
    private final int ROUTE_TYPE_DRIVE = 2;

    private static final int CHOOSE_ADDR = 1001;    //  出地点
    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_ride_order_detail);
        ButterKnife.bind(this);
        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }
    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


    public static Intent createIntent(Context context, OrderModel data) {
        return new Intent(context, YXRideOrderDetailActivity.class).
                putExtra(RESULT_DATA, data);
    }

    @Override
    public void initView() {
        llpath.setVisibility(View.GONE);
        li_load.setVisibility(View.GONE);
        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(this);
    }

    @Override
    public void initData() {
        orderModel = (OrderModel) getIntent().getSerializableExtra(RESULT_DATA);
        //getOrderInfo(DemoApplication.getInstance().getMyToken(), orderModel.getNo());
        drawaView(orderModel);
    }


    @Override
    public void initEvent() {

    }

    private void drawaView(OrderModel model) {
        if (model != null) {
            Glide.with(context).load(model.getDriver().getHeadImage()).into(imageView);
            tv_driver.setText(model.getDriver().getAlias());
            tv_carName.setText(model.getCar().getBrand());
            tv_type.setText(model.getInfoType());

            tv_datetime.setText(model.getDatetime());
            tv_origin.setText(model.getOrigin().getProvince()+model.getOrigin().getCity()+model.getOrigin().getCounty()+model.getOrigin().getAddress());
            if (model.getInfoType().equals("快递小件")){
                tv_txt.setVisibility(View.GONE);
                tv_seats.setVisibility(View.GONE);
            }else if (model.getOrderType().equals("express")){
                tv_txt.setText("余重");
                tv_seats.setText(model.getCapacity()+"kg");
            }else {
                tv_txt.setText("余座");
                tv_seats.setText(String.valueOf(model.getSeats()));
            }
            tv_site.setText(model.getSite().getProvince()+model.getSite().getCity()+model.getSite().getCounty()+model.getSite().getAddress());
            tv_load.setText(1000+"kg");
            // 添加途径点
            List<PointModel> pointModels = model.getPathPoint();
            if (pointModels != null && pointModels.size() > 0) {
                String pathStr = "";
                for (int i = 0; i < pointModels.size(); i++) {
                    pathStr += pointModels.get(i).getAddress();
                }
                tv_Path.setText(pathStr);
            } else {
                llpath.setVisibility(View.GONE);
            }
            // 备注信息
            tv_Remark.setText(model.getNote().equals("请设置备注信息")?"":model.getNote().equals("remark")?" ":model.getNote());
        }
    }

    private boolean validateUser() {
        return StringUtil.isNullOrEmpty(DemoApplication.getInstance().getMyToken());
    }

    private void gotoLogin() {
        toActivity(YXLoginActivity.createIntent(context));
    }

    @OnClick({R.id.order_detail_btn, R.id.icon_phone, R.id.order_detail_map})
    void myClick(View view) {
        switch (view.getId()) {

            case R.id.order_detail_btn:
                // 用车
                if (!validateUser()) {
                    if (orderModel.getOrderType().equals("express")){
                        //toActivity(YXTransOrderSnatchActivity.createIntent(context, orderModel));
                        DialogUIUtils.showToast("eRRRRRRrrrrrEEE");
                    }else {
                        showSnatchDialog(orderModel.getInfoType());
                    }

                } else {
                    gotoLogin();
                }

                break;
            case R.id.icon_phone:
                // 电话联系
                call(orderModel.getDriver().getMobile());
                break;
            case R.id.order_detail_map:
                // 查看订单地图
                toActivity(YXOrderMapActivity.createIntent(context, changeObject(orderModel)));
                break;
            default:
                break;
        }
    }

    /**
     * 调用拨号界面
     *
     * @param phone 电话号码
     */
    private void call(final String phone) {
        DialogUIUtils.showMdAlert(context, "拨打以下电话", phone, new DialogUIListener() {
            @Override
            public void onPositive() {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

            @Override
            public void onNegative() {
            }

        }).show();

    }

    /**
     * 用户用车
     *
     * @param token
     * @param orderId
     */
    private void doOrder(String token, String orderId, JSONObject jsonObject) {
        final Dialog dialog = DialogUIUtils.showMdLoading(context, "数据加载中...", true, true, true, true).show();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).snatchOrder(token, orderId, StringUtil.getBody(jsonObject))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Response<ResponseBody> response) {
                        dialog.dismiss();
                        try {
                            switch (response.code()) {
                                case 200:
                                    if (response.body() == null) {
                                        showToast(response.message());
                                    } else {
                                        String result = response.body().string();
                                        System.out.println("抢单返回数据" + result);
                                        JSONObject jsonObject = JSON.parseObject(result);
                                        if (jsonObject.getString("code").equals("success")) {
                                            // 抢单成功
                                            toActivity(YXSnatchSuccessActivity.createIntent(context,
                                                    JSON.parseObject(jsonObject.getString("data"), newOrderModel.class)));
                                            finish();
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
                        dialog.dismiss();
                        showToast(t.getMessage());
                    }
                });
    }

    private void showSnatchDialog(final String type) {
        View rootView = View.inflate(context, R.layout.custom_snatch_dialog_layout, null);
        final Dialog dialog = DialogUIUtils.showCustomAlert(this, rootView, Gravity.CENTER, true, false).show();
        final EditText ed_Content = (EditText) rootView.findViewById(R.id.snatch_dialog_content);
        final EditText ed_Linker = (EditText) rootView.findViewById(R.id.snatch_dialog_linker);
        final EditText ed_Mobile = (EditText) rootView.findViewById(R.id.snatch_dialog_linkermobile);
        final EditText ed_Remark = (EditText) rootView.findViewById(R.id.snatch_dialog_remark);
        tv_origins = (TextView) rootView.findViewById(R.id.snatch_dialog_origin);
        final LinearLayout li_money= (LinearLayout) rootView.findViewById(R.id.ride_re_money);
        tv_discount = (TextView) rootView.findViewById(R.id.ride_discount);
        tv_money = (TextView) rootView.findViewById(R.id.ride_money);
        //tv_discount.setText(String.valueOf(orderModel.getMileage()));
        //选择上车点
        rootView.findViewById(R.id.snatch_dialog_origin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, YXChooseAddrActivity.class);
                intent.putExtra("city", "昆明市");
                startActivityForResult(intent, CHOOSE_ADDR);
            }
        });
        rootView.findViewById(R.id.snatch_dialog_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content_ = ed_Content.getText().toString();
                linker = ed_Linker.getText().toString();
                mobile = ed_Mobile.getText().toString();
                remark = ed_Remark.getText().toString();
                origin_ = tv_origins.getText().toString();
                if (StringUtil.isNullOrEmpty(content_)) {
                    showToast("请输入乘车人数!");
                } else if (Integer.parseInt(content_) > orderModel.getSeats()) {
                    showToast("乘车人数不能大于" + orderModel.getSeats() + "人！");
                }else if (StringUtil.isNullOrEmpty(origin_)) {
                    showToast("请输入上车点");
                } else {
                    DialogUIUtils.dismiss(dialog);
                    JSONObject object = new JSONObject();
                    object.put("origin", origin);
                    object.put("site", orderModel.getSite());
                    object.put("number", Integer.parseInt(content_));
                    if (!StringUtil.isNullOrEmpty(linker)) {
                        object.put("name", linker);
                    }
                    if (!StringUtil.isNullOrEmpty(mobile)) {
                        object.put("mobile", mobile);
                    }
                    if (!StringUtil.isNullOrEmpty(remark)) {
                        object.put("note", remark);
                    }
                    object.put("mileage",discount);
                    JSON.toJSONString(object);
                    doOrder(DemoApplication.getInstance().getMyToken(), orderModel.getNo(), object);
                }
            }
        });
        rootView.findViewById(R.id.snatch_dialog_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUIUtils.dismiss(dialog);
            }
        });
        ed_Content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //showToast(String.valueOf(s));
                String ss=String.valueOf(s).equals("")?"0":String.valueOf(s);
                peoNum=Double.valueOf(ss);
                switch (type) {
                    case "顺风车":
                        //money = (int) mul_(num,CostUtils.getSFCMoney(discount),100);
                        tv_money.setText(discount==0?"0":String.valueOf(CostUtils.countMoney(peoNum*CostUtils.getSFCMoney(discount))));
                        break;
                    case "专车":
                        //money = (int) mul_(num,CostUtils.getZCMoney(discount),100);
                        tv_money.setText(discount==0?"0":String.valueOf(CostUtils.getZCMoney(discount)));
                        break;
                }
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null){
            Bundle bundle = data.getExtras();
            OriginAndSite originAndSite;
            switch (requestCode){
                case CHOOSE_ADDR:
                    originAndSite = (OriginAndSite) bundle.getSerializable("model");
                    tv_origins.setText(originAndSite.getProvince()+originAndSite.getCity()+originAndSite.getCounty()+originAndSite.getAddress());
                    origin=new PointModel();
                    origin.setAddress(originAndSite.getAddress());
                    origin.setProvince(originAndSite.getProvince());
                    origin.setCity(originAndSite.getCity());
                    origin.setCounty(originAndSite.getCounty());
                    origin.setCoordinate(originAndSite.getCoordinate());

                    double lat = originAndSite.getCoordinate().getLat();
                    double lng = originAndSite.getCoordinate().getLng();
                    LatLonPoint latLonPoint = new LatLonPoint(lat, lng);

                    double lat1 =orderModel.getSite().getCoordinate().getLat();
                    double lng1 =orderModel.getSite().getCoordinate().getLng();
                    LatLonPoint latLonPoint1 = new LatLonPoint(lat1, lng1);
                    searchRouteResult(ROUTE_TYPE_DRIVE, RouteSearch.DrivingDefault, latLonPoint, latLonPoint1);

            }
        }
    }

    /**
     * 获取订单详情
     *
     * @param token   用户token
     * @param orderNo 订单号
     */
    /*private void getOrderInfo(String token, String orderNo) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).getTripOrder(token, orderNo,"")
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
    }*/

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
                    tv_discount.setText(String.valueOf(discount));


                    switch (orderModel.getInfoType()) {
                        case "顺风车":
                            //money = (int) mul_(num,CostUtils.getSFCMoney(discount),100);
                            tv_money.setText(discount==0?"0":String.valueOf(CostUtils.countMoney(peoNum*CostUtils.getSFCMoney(discount))));
                            break;
                        case "专车":
                            //money = (int) mul_(num,CostUtils.getZCMoney(discount),100);
                            tv_money.setText(discount==0?"0":String.valueOf(CostUtils.getZCMoney(discount)));
                            break;
                    }

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
}
