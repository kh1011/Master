package com.yxclient.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.yxclient.app.R;
import com.yxclient.app.adapter.GoodsKindAdapter;
import com.yxclient.app.app.DemoApplication;
import com.yxclient.app.config.YXConfig;
import com.yxclient.app.http.RetrofitHttp;
import com.yxclient.app.model.bean.DeliverAddrModel;
import com.yxclient.app.model.bean.OrderModel;
import com.yxclient.app.model.bean.OriginAndSite;
import com.yxclient.app.model.bean.PointModel;
import com.yxclient.app.model.bean.newOrderModel;
import com.yxclient.app.poisearch.util.CityModel;
import com.yxclient.app.poisearch.util.CityUtil;
import com.yxclient.app.utils.StringUtil;
import com.yxclient.app.view.MyGridView;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;
import zuo.biao.library.base.BaseActivity;

/**
 * Created by mac on 2017/8/22.
 * 用户货运抢单
 */

public class YXTransOrderSnatchActivity extends BaseActivity {

    private newOrderModel orderModel;
    // 发货联系人、发货联系人电话
    @BindView(R.id.goods_f_name)
    EditText ed_name;
    @BindView(R.id.goods_f_mobile)
    EditText ed_mobile;
    // 收货联系人、收货联系人电话
    @BindView(R.id.goods_s_name)
    EditText ed_sname;
    @BindView(R.id.goods_s_mobile)
    EditText ed_smobile;
    // 货物类型
    @BindView(R.id.trans_goods_type)
    TextView tv_goods_type;
    @BindView(R.id.trans_get_kinds)
    RelativeLayout  tv_goods_re_kinds;
    // 货物大概尺寸
    @BindView(R.id.trans_goods_size)
    TextView tv_goods_size;
    @BindView(R.id.trans_goods_re_size)
    RelativeLayout tv_goods_re_size;
    // 货物重量
    @BindView(R.id.trans_goods_weight)
    TextView tv_goods_weight;
    @BindView(R.id.weight)
    TextView tv_weight;
    @BindView(R.id.trans_goods_re_weight)
    RelativeLayout tv_goods_re_weight;
    // 货运备注信息
    @BindView(R.id.trans_goods_remark)
    EditText ed_goods_remark;
    // 货物类型集合
    private List<String> kindList;
    //货物相关属性
    private double goodsWeight = 0.0;//货物重量
    //是否易碎
    @BindView(R.id.trans_re_ysp)
    RelativeLayout trans_re_ysp;
    @BindView(R.id.recharge_group)
    RadioGroup re_group;
    RadioButton rb;
    //是否易潮
    @BindView(R.id.trans_re_ycp)
    RelativeLayout trans_re_ycp;
    @BindView(R.id.recharge_group2)
    RadioGroup re_group2;
    RadioButton rb2;
    //详细的发货地址
    @BindView(R.id.delivery_addr_origin)
    TextView tv_origin;
    //详细的收货地址
    @BindView(R.id.delivery_addr_site)
    TextView tv_site;

    private CityModel mCurrCity;
    // 发货信息
    private DeliverAddrModel deliverAddrModel;
    //预估费用
    @BindView(R.id.goods_s_money)
    EditText ed_money;

    PointModel origin;
    PointModel site;

    private static final int CHOOSE_ADDR = 1001;    //  出地点

    // 请求码
    private static final int CHOOSE_ADDR_START = 2001;    //  发货点
    private static final int CHOOSE_ADDR_END = 2002;    //  收货点
    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_activity_trans_snatch);
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
     * @param context
     * @param data
     * @return
     */
    public static Intent createIntent(Context context, newOrderModel data) {
        return new Intent(context, YXTransOrderSnatchActivity.class).
                putExtra(RESULT_DATA, data);
    }

    @Override
    public void initView() {
        mCurrCity = CityUtil.getDefCityModel(context);
        deliverAddrModel = new DeliverAddrModel();
    }

    @Override
    public void initData() {
        orderModel = (newOrderModel) getIntent().getSerializableExtra(RESULT_DATA);
        if (orderModel != null) {
           /*Glide.with(context).load(orderModel.getDriver().getHeadImage()).into(imageView);
            tv_driver.setText(orderModel.getDriver().getName());
            tv_carName.setText(orderModel.getCar().getBrand());
            tv_type.setText(orderModel.getInfoType());
            tv_seats.setText(String.valueOf(orderModel.getSeats()));
            tv_datetime.setText(orderModel.getDatetime());
            tv_origin.setText(orderModel.getOrigin().getAddress());
            tv_site.setText(orderModel.getSite().getAddress());*/
        }
        getKinds(DemoApplication.getInstance().getMyToken());

        if (orderModel.getInfoType().equals("快递小件")){
            trans_re_ysp.setVisibility(View.VISIBLE);
            trans_re_ycp.setVisibility(View.VISIBLE);
            tv_goods_re_size.setVisibility(View.GONE);
            //tv_goods_re_weight.setVisibility(View.GONE);
            tv_goods_re_kinds.setVisibility(View.GONE);
            tv_weight.setText("设置货物重量(选填)");
        }else {
            trans_re_ysp.setVisibility(View.GONE);
            trans_re_ycp.setVisibility(View.GONE);
            tv_goods_re_size.setVisibility(View.VISIBLE);
            //tv_goods_re_weight.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initEvent() {

    }


    @OnClick({R.id.trans_get_kinds, R.id.trans_goods_re_size, R.id.trans_goods_re_weight, R.id.trans_btn,R.id.delivery_get_origin,R.id.delivery_get_site})
    void myClick(View view) {
        switch (view.getId()) {
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
            case R.id.trans_btn:
                rb = (RadioButton)YXTransOrderSnatchActivity.this.findViewById(re_group.getCheckedRadioButtonId());
                rb2 = (RadioButton)YXTransOrderSnatchActivity.this.findViewById(re_group2.getCheckedRadioButtonId());
                String goodsType = tv_goods_type.getText().toString();
                String goodsSize = tv_goods_size.getText().toString();
                String weight = tv_goods_weight.getText().toString();
                String remark = ed_goods_remark.getText().toString();
                String name = ed_name.getText().toString();
                String mobile = ed_mobile.getText().toString();
                String consignorName = ed_sname.getText().toString();
                String consignorMobile = ed_smobile.getText().toString();
                String amount=ed_money.getText().toString();
                if (!orderModel.getInfoType().equals("快递小件")){
                       if (StringUtil.isNullOrEmpty(goodsType)) {
                        DialogUIUtils.showToast("请选择货物类型!");
                    } else if (StringUtil.isNullOrEmpty(weight)) {
                        DialogUIUtils.showToast("请设置货物重量!");
                    }
                }
                if (StringUtil.isNullOrEmpty(name)) {
                    DialogUIUtils.showToast("请输入发货联系人!");
                } else if (StringUtil.isNullOrEmpty(mobile)) {
                    DialogUIUtils.showToast("请输入发货联系人电话!");
                } else if (!StringUtil.isTelPhoneNumber(mobile)) {
                    DialogUIUtils.showToast("请输入正确的电话号码!");
                } else if (StringUtil.isNullOrEmpty(consignorName)) {
                    DialogUIUtils.showToast("请输入收货联系人!");
                } else if (StringUtil.isNullOrEmpty(consignorMobile)) {
                    DialogUIUtils.showToast("请输入收货联系人电话!");
                } else if (!StringUtil.isTelPhoneNumber(consignorMobile)) {
                    DialogUIUtils.showToast("请输入正确的电话号码!");
                } else if (StringUtil.isNullOrEmpty(amount)) {
                    DialogUIUtils.showToast("请输入预估费用!");
                }else {
                    JSONObject object = new JSONObject();
                    object.put("origin", origin);
                    object.put("site", site);
                    object.put("name", name);
                    object.put("mobile", mobile);
                    object.put("consignorName", consignorName);
                    object.put("consignorMobile", consignorMobile);
                    object.put("weight", goodsWeight);
                    object.put("bulk", goodsSize);
                    object.put("kind", goodsType);
                    object.put("note", remark);
                    object.put("fragile",ysStatus(rb.getText().toString()));
                    object.put("wet",ysStatus(rb2.getText().toString()));
                    object.put("amount",amount+"00");
                    doOrder(DemoApplication.getInstance().getMyToken(), orderModel.getNo(), object);
                }
                break;
            case R.id.delivery_get_origin:
                Intent intent = new Intent(context, YXChooseAddrActivity.class);
                intent.putExtra("city", "昆明市");
                startActivityForResult(intent, CHOOSE_ADDR_START);
                break;
            case R.id.delivery_get_site:
                Intent intent2 = new Intent(context, YXChooseAddrActivity.class);
                intent2.putExtra("city", "昆明市");
                startActivityForResult(intent2, CHOOSE_ADDR_END);
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
                case CHOOSE_ADDR:
                    //tv_Origin.setText(originAndSite.getAddress());
                    //deliverAddrModel.setAddress(originAndSite.getAddress());
                    //deliverAddrModel.setCity(originAndSite.getCity());
                    //deliverAddrModel.setCounty(originAndSite.getCounty());
                    //deliverAddrModel.setName(originAndSite.getName());
                    //deliverAddrModel.setProvince(originAndSite.getProvince());
                    //deliverAddrModel.setCoordinate(originAndSite.getCoordinate());
                    break;
                case CHOOSE_ADDR_START:
                    originAndSite = (OriginAndSite) bundle.getSerializable("model");
                    tv_origin.setText(originAndSite.getProvince()+originAndSite.getCity()+originAndSite.getCounty()+originAndSite.getAddress());
                    origin=new PointModel();
                    origin.setAddress(originAndSite.getAddress());
                    origin.setProvince(originAndSite.getProvince());
                    origin.setCity(originAndSite.getCity());
                    origin.setCounty(originAndSite.getCounty());
                    origin.setCoordinate(originAndSite.getCoordinate());
                    break;
                case CHOOSE_ADDR_END:
                    originAndSite = (OriginAndSite) bundle.getSerializable("model");
                    tv_site.setText(originAndSite.getProvince()+originAndSite.getCity()+originAndSite.getCounty()+originAndSite.getAddress());
                    site=new PointModel();
                    site.setAddress(originAndSite.getAddress());
                    site.setProvince(originAndSite.getProvince());
                    site.setCity(originAndSite.getCity());
                    site.setCounty(originAndSite.getCounty());
                    site.setCoordinate(originAndSite.getCoordinate());
                    break;
                default:
                    break;
            }
        }
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
        final MyGridView gridView = (MyGridView) rootView.findViewById(R.id.gv_reply);
        final GoodsKindAdapter goodsKindAdapter = new GoodsKindAdapter(context, kinds);
        gridView.setAdapter(goodsKindAdapter);
        rootView.findViewById(R.id.txt_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUIUtils.dismiss(buildBean);
            }
        });
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
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                }
                DialogUIUtils.dismiss(buildBean);
            }
        });
        rootView.findViewById(R.id.txt_down).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                }
                String weight = ed_Weight.getText().toString();
                if (StringUtil.isNullOrEmpty(weight)) {
                    DialogUIUtils.dismiss(buildBean);
                    goodsWeight = Double.parseDouble("0");
                    tv_goods_weight.setText("");

                } else {
                    DialogUIUtils.dismiss(buildBean);
                    goodsWeight = Double.parseDouble(weight);
                    tv_goods_weight.setText(weight + " kg");
                }
            }
        });
    }

    /**
     * 用户用车
     *
     * @param token
     * @param orderId
     */
    private void doOrder(String token, final String orderId, JSONObject jsonObject) {
        final Dialog dialog = DialogUIUtils.showMdLoading(context, "数据加载中...", true, true, true, true).show();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).snatchOrder(token, orderId, StringUtil.getBody(jsonObject))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Response<ResponseBody> response) {
                        dialog.dismiss();
                        try {
                            //String resultErr = response.errorBody().string();
                            switch (response.code()) {
                                case 200:
                                    if (response.body() == null) {
                                        DialogUIUtils.showToast(response.errorBody().string());
                                    } else {
                                        String result = response.body().string();
                                        System.out.println("抢单返回数据" + result);
                                        JSONObject jsonObject = JSON.parseObject(result);
                                        if (jsonObject.getString("code").equals("success")) {
                                            // 抢单成功
                                            toActivity(YXSnatchSuccessActivity.createIntent(context, JSON.parseObject(jsonObject.getString("data"), newOrderModel.class)));
                                            finish();
                                        } else {
                                            DialogUIUtils.showToast(jsonObject.getString("message"));
                                        }
                                    }
                                    break;
                                default:
                                    DialogUIUtils.showToast(JSON.parseObject(response.errorBody().string()).getString("message"));
                                    break;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        dialog.dismiss();
                        DialogUIUtils.showToast(t.getMessage());
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

}
