package com.yxdriver.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.dou361.dialogui.DialogUIUtils;
import com.yxdriver.app.R;
import com.yxdriver.app.app.DemoApplication;
import com.yxdriver.app.config.YXConfig;
import com.yxdriver.app.http.RetrofitHttp;
import com.yxdriver.app.model.bean.OrderModel;
import com.yxdriver.app.model.bean.newOrderModel;
import com.yxdriver.app.utils.OrderUtils;
import com.yxdriver.app.utils.PhoneUtils;

import java.io.IOException;
import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;
import zuo.biao.library.base.BaseActivity;

import static com.yxdriver.app.utils.StringUtil.changeObject;

/**
 * 订单详情
 */
public class
YXOrderDetailActivity extends BaseActivity {
    private newOrderModel orderModel;
    // 取消订单
    @BindView(R.id.order_detail_right)
    Button btn_Cancel;
    // 抢单按钮
    @BindView(R.id.order_detail_btn)
    Button btn_order;
    // 客户姓名
    @BindView(R.id.order_success_name)
    TextView tv_name;
    // 客户电话
    @BindView(R.id.order_success_mobile)
    TextView tv_mobile;
    // 客户头像
    @BindView(R.id.profile_image)
    CircleImageView imageView;
    // 订单类型
    @BindView(R.id.order_success_type)
    TextView tv_Type;
    // 订单状态
    @BindView(R.id.order_success_status)
    TextView tv_Status;
    // 出行日期
    @BindView(R.id.order_success_datetime)
    TextView tv_datetime;
    // 起点
    @BindView(R.id.order_success_origin)
    TextView tv_origin;
    // 目的地
    @BindView(R.id.order_success_site)
    TextView tv_site;
    // 备注信息
    @BindView(R.id.order_success_remark)
    TextView tv_remark;
    // 出行类显示
    @BindView(R.id.re_journey)
    LinearLayout re_journey;
    // 货运类显示
    @BindView(R.id.re_express)
    LinearLayout re_express;
    // 乘车人数
    @BindView(R.id.order_success_number)
    TextView tv_number;
    // 货物重量
    @BindView(R.id.order_success_weight)
    TextView tv_weight;
    //货物尺寸
    @BindView(R.id.order_success_buld)
    TextView tv_bulk;
    @BindView(R.id.re_buld)
    LinearLayout li_bulk;
    // 大概价格
    @BindView(R.id.order_success_money)
    TextView tv_money;
    // 距离
    @BindView(R.id.order_success_discount)
    TextView tv_discount;
    @BindView(R.id.discount)
    LinearLayout re_discount;
    // 订单号
    @BindView(R.id.order_success_orderNo)
    TextView tv_orderNo;
    //易碎品
    @BindView(R.id.re_fragile)
    LinearLayout re_fragile;
    @BindView(R.id.order_fragile)
    TextView tv_fragile;
    //易湿品
    @BindView(R.id.re_tidal)
    LinearLayout re_tidal;
    @BindView(R.id.order_tidal)
    TextView tv_tidal;
    //收货人
    @BindView(R.id.order_success_consignor)
    LinearLayout li_consignor;
    @BindView(R.id.order_success_consignorname)
    TextView tv_consignorname;
    @BindView(R.id.order_success_consignormobile)
    TextView tv_consignormobile;
    @BindView(R.id.order_success_consignorphone)
    ImageView iv_consignorphone;
    //发货人
    @BindView(R.id.order_success_consignee)
    LinearLayout li_consignee;
    @BindView(R.id.order_success_consigneename)
    TextView tv_consigneename;
    @BindView(R.id.order_success_consigneemobile)
    TextView tv_consigneemobile;
    @BindView(R.id.order_success_consigneephone)
    ImageView iv_consigneephone;
    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_order_snatch_success);
        ButterKnife.bind(this);
        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }
    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


    public static Intent createIntent(Context context, newOrderModel data) {
        return new Intent(context, YXOrderDetailActivity.class).
                putExtra(RESULT_DATA, data);
    }

    @Override
    public void initView() {
        //
    }

    @Override
    public void initData() {
        // 获取订单对象
        orderModel = (newOrderModel) getIntent().getSerializableExtra(RESULT_DATA);
        // 判断是否可以抢单
        drawaView(orderModel);
    }

    @Override
    public void initEvent() {

    }

    @OnClick({R.id.order_detail_btn, R.id.order_success_map, R.id.order_success_call,R.id.order_success_consignorphone,R.id.order_success_consigneephone})
    void myClick(View view) {
        switch (view.getId()) {
            case R.id.order_detail_btn:
                getCarInfo(orderModel);
                break;
            case R.id.order_success_map:
                toActivity(YXOrderMapActivity.createIntent(context, orderModel));
                break;
            case R.id.order_success_call:
                // 拨打联系电话
                PhoneUtils.call(context, orderModel.getUser().getMobile());
                break;
            case R.id.order_success_consignorphone:
                PhoneUtils.call(context, orderModel.getConsignorName());
                break;
            case R.id.order_success_consigneephone:
               PhoneUtils.call(context, orderModel.getMobile());
                break;
            default:
                break;
        }
    }

    /**
     * 司机抢单
     *
     * @param token 用户token
     * @param no    订单编号
     */
    private void snatchOrder(String token, String no) {
        final Dialog dialog = DialogUIUtils.showMdLoading(context, "数据加载中", true, true, true, true).show();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).snatchOrder(token, no)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Response<ResponseBody> response) {
                        DialogUIUtils.dismiss(dialog);
                        switch (response.code()) {
                            case 200:
                                try {
                                    if (response.body() == null) {
                                        try {
                                            DialogUIUtils.showToast(JSON.parseObject(response.errorBody().string()).getString("message"));
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        String result = response.body().string();
                                        System.out.println("抢单返回数据" + result);
                                        JSONObject object = JSON.parseObject(result);
                                        if (object.getString("code").equals("success")) {
                                            DialogUIUtils.showToast(object.getString("message"));
                                            OrderModel orderModel2 = JSON.parseObject(object.getString("data"), OrderModel.class);
                                            drawaView(changeObject(orderModel2));
                                        } else {
                                            DialogUIUtils.showToast(object.getString("message"));
                                        }
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            default:
                                try {
                                    DialogUIUtils.showToast(JSON.parseObject(response.errorBody().string()).getString("message"));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        DialogUIUtils.dismiss(dialog);
                        DialogUIUtils.showToast(t.getMessage());
                    }
                });
    }

    /**
     * 绘制UI
     *
     * @param model 订单对象
     */
    private void drawaView(newOrderModel model) {
        if (model.getStatus() == YXConfig.T_ORDER_STATUS_CREATE) {
            btn_order.setVisibility(View.VISIBLE);
        } else {
            btn_order.setVisibility(View.GONE);
        }
        if (model.getOrderType().equals("express")) {
            //re_express.setVisibility(View.VISIBLE);
            re_journey.setVisibility(View.GONE);
            tv_money.setText(String.format("¥ %s", model.getAmount()/100));
            if (model.getInfoType().equals("快递小件")){
                //re_express.setVisibility(View.GONE);
                tv_fragile.setText(getStatus(model.getFragile()));
                tv_tidal.setText(getStatus(model.getWet()));
            }else {
                tv_weight.setText(String.format("%s千克", String.valueOf(model.getWeight())));
                re_fragile.setVisibility(View.GONE);
                re_tidal.setVisibility(View.GONE);
            }
            tv_consignorname.setText("发货人："+model.getConsignorName());
            tv_consignormobile.setText("发货人电话："+model.getConsignorMobile());
            tv_consigneename.setText("收货人："+model.getName());
            tv_consigneemobile.setText("收货人电话："+model.getMobile());
            if (model.getBulk()==null||model.getBulk().equals("")){
                li_bulk.setVisibility(View.GONE);
            }else {
                li_bulk.setVisibility(View.VISIBLE);
                tv_bulk.setText(model.getBulk());
            }
        } else {
            //re_express.setVisibility(View.GONE);
            re_journey.setVisibility(View.VISIBLE);
            tv_money.setText(String.format("¥ %s", model.getAmount() / 100.0));
            re_fragile.setVisibility(View.GONE);
            re_tidal.setVisibility(View.GONE);
            li_consignor.setVisibility(View.GONE);
            li_consignee.setVisibility(View.GONE);
            tv_weight.setText(String.format("%s千克", String.valueOf(model.getWeight())));
        }
        if (String.valueOf(model.getWeight())==null||model.getWeight()==0){
            re_express.setVisibility(View.GONE);
        }else {
            tv_weight.setText(String.format("%s千克", String.valueOf(model.getWeight())));
        }

        tv_number.setText(String.valueOf(model.getNumber()));
        BigDecimal bd = new BigDecimal(model.getMileage());
        BigDecimal setScale = bd.setScale(1, bd.ROUND_DOWN);
        tv_discount.setText(String.valueOf(setScale)+"公里");
        tv_orderNo.setText(model.getExtraInfoUUID());
        Glide.with(context).load(model.getUser().getHeadImage()).into(imageView);
        tv_name.setText(model.getUser().getName());
        tv_mobile.setText(model.getUser().getMobile());
        tv_Type.setText(model.getInfoType());
        tv_Status.setText(OrderUtils.getStatusStr(model.getStatus()));
        tv_datetime.setText(model.getDatetime());
        tv_origin.setText(model.getOrigin().getProvince()+model.getOrigin().getCity()+model.getOrigin().getCounty()+model.getOrigin().getAddress());
        tv_site.setText(model.getSite().getProvince()+model.getSite().getCity()+model.getSite().getCounty()+model.getSite().getAddress());
        String note;
        if (model.getNote()==null){
            note="";
        }else if (model.getNote().equals("remark")||model.getNote().equals("无")){
            note="";
        }else {
            note=model.getNote();
        }
        tv_remark.setText(String.format("备注信息：%s", note));
    }

    private String getStatus(int code){
        String status = null;
        switch (code){
            case 1:
                status="是";
            break;
            case 0:
                status="否";
            break;
        }
        return status;
    }

    /**
     * 获取车辆信息
     *
     */
    private void getCarInfo(final newOrderModel data_) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).getCarInfo(DemoApplication.getInstance().getMyToken())
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
                                                if (data_.getInfoType().equals("顺风车")||data_.getInfoType().equals("快递小件")||data_.getInfoType().equals("专车")){
                                                    snatchOrder(DemoApplication.getInstance().getMyToken(), data_.getExtraInfoUUID());
                                                }else {
                                                    DialogUIUtils.showToast("司机类型不符");
                                                }
                                            }else if (data.getString("type").equals("微型货车")||data.getString("type").equals("轻型货车")||
                                                    data.getString("type").equals("中型货车")||data.getString("type").equals("大型货车")){
                                                if (data_.getInfoType().equals("小型货运")||data_.getInfoType().equals("快递小件")||data_.getInfoType().equals("大型货运")){
                                                    snatchOrder(DemoApplication.getInstance().getMyToken(), data_.getExtraInfoUUID());
                                                }else {
                                                    DialogUIUtils.showToast("司机类型不符");
                                                }

                                            }else {
                                                DialogUIUtils.showToast("错误");
                                            }
                                            //snatchOrder(DemoApplication.getInstance().getMyToken(), data_.getNo());
                                        } else {
                                            DialogUIUtils.showToast(jsonObject.getString("message"));
                                        }
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            default:
                                String err= null;
                                try {
                                    err = response.errorBody().string();
                                    DialogUIUtils.showToast(JSON.parseObject(err).getString("message"));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                break;
                        }
                    }
                    @Override
                    public void onFailure(Throwable t) {
                        DialogUIUtils.showToast(t.getMessage());
                    }
                });
    }
}