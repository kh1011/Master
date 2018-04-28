package com.yxclient.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dou361.dialogui.DialogUIUtils;
import com.yxclient.app.R;
import com.yxclient.app.app.DemoApplication;
import com.yxclient.app.config.YXConfig;
import com.yxclient.app.http.RetrofitHttp;
import com.yxclient.app.model.bean.newOrderModel;
import com.yxclient.app.utils.OrderUtils;
import com.yxclient.app.utils.PhoneUtils;
import com.yxclient.app.utils.StringUtil;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;
import zuo.biao.library.base.BaseActivity;

import static com.yxclient.app.config.YXConfig.T_ORDER_STATUS_CLOSE;
import static com.yxclient.app.config.YXConfig.T_ORDER_STATUS_CONFIRM;
import static com.yxclient.app.config.YXConfig.T_ORDER_STATUS_CREATE;
import static com.yxclient.app.config.YXConfig.T_ORDER_STATUS_INFO_SUCCESS;

/**
 * Created by mac on 2017/11/13.
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
// 个人货运订单详情
public class YXGoodsOrderDetailActivity extends BaseActivity {
    newOrderModel orderModel;
    String InfoUUID;
    // 司机头像
    @BindView(R.id.profile_image)
    CircleImageView imageView;
    // 货运司机
    @BindView(R.id.index_item_dirver)
    TextView tv_driver;
    // 车辆名称
    @BindView(R.id.index_item_carname)
    TextView tv_brand;
    // 货运类型
    @BindView(R.id.index_item_type)
    TextView tv_type;
    // 出发日期
    @BindView(R.id.index_item_datetime)
    TextView tv_datetime;
    // 出发地点
    @BindView(R.id.index_item_origin)
    TextView tv_origin;
    // 目的地
    @BindView(R.id.index_item_site)
    TextView tv_site;
    // 车辆载重
    @BindView(R.id.item_trip_weight)
    TextView tv_weight;
    @BindView(R.id.weight)
    LinearLayout li_weight;
    // 途经点
    @BindView(R.id.item_trip_path)
    TextView tv_path;
    @BindView(R.id.path)
    LinearLayout li_path;
    //  备注信息
    @BindView(R.id.item_trip_remark)
    TextView tv_remark;
    // 订单编号
    @BindView(R.id.order_detail_no)
    TextView tv_no;
    // 订单状态
    @BindView(R.id.order_detail_status)
    TextView tv_status;

    // 取消订单按钮
    @BindView(R.id.goods_order_detail_cancle)
    Button btn_Cancle;
    // 支付按钮
    @BindView(R.id.goods_order_detail_btn)
    Button btn_Pay;
    // 取消订单的原因
    String reasonStr = "";

    //司机信息
    @BindView(R.id.driver_info)
    RelativeLayout driver_info;
    //乘客信息
    ////发货人
    @BindView(R.id.consignor)
    LinearLayout li_consignor;
    @BindView(R.id.item_trip_consignorName)
    TextView tv_consignorName;
    @BindView(R.id.item_trip_consignorMobile)
    TextView tv_consignorMobile;
    @BindView(R.id.item_trip_origin)
    TextView tv_infoOrigin;
    ////收货人
    @BindView(R.id.consignee)
    LinearLayout li_consignee;
    @BindView(R.id.item_trip_consigneeName)
    TextView tv_consigneeName;
    @BindView(R.id.item_trip_consigneeMobile)
    TextView tv_consigneeMobile;
    @BindView(R.id.item_trip_site)
    TextView tv_infoSite;

    // 其他评价
    String evaluationStr = "";
    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_goods_order_detail);
        ButterKnife.bind(this);
        initView();
        initData();
        initEvent();
    }


    public static Intent createIntent(Context context, newOrderModel data,String InfoUUID) {
        return new Intent(context, YXGoodsOrderDetailActivity.class).
                putExtra(RESULT_DATA, data)
                .putExtra(RESULT_CODE,InfoUUID);
    }

    @Override
    public void initView() {
        li_path.setVisibility(View.GONE);
        orderModel = (newOrderModel) getIntent().getSerializableExtra(RESULT_DATA);
        InfoUUID=getIntent().getStringExtra(RESULT_CODE);
        switch (orderModel.getStatus()) {
            case T_ORDER_STATUS_CONFIRM:
                //待确定
                btn_Cancle.setVisibility(View.VISIBLE);
                btn_Pay.setVisibility(View.GONE);
                break;
            case T_ORDER_STATUS_CREATE:
                // 订单创建
                btn_Cancle.setVisibility(View.VISIBLE);
                btn_Pay.setVisibility(View.GONE);
                break;
            case YXConfig.T_ORDER_STATUS_ACCEPT:
                // 已被司机抢单、未开始行程
                btn_Cancle.setVisibility(View.VISIBLE);
                btn_Pay.setVisibility(View.GONE);
                break;
            case YXConfig.T_ORDER_STATUS_GO:
                // 订单进行中、可支付
                btn_Cancle.setVisibility(View.GONE);
                btn_Pay.setVisibility(View.VISIBLE);
                break;
            case YXConfig.T_ORDER_STATUS_WAIT_PAY:
                // 订单完成、待支付
                btn_Cancle.setVisibility(View.GONE);
                btn_Pay.setVisibility(View.VISIBLE);
                break;
            case T_ORDER_STATUS_INFO_SUCCESS:
                // 订单已支付、可以进行评价
                btn_Cancle.setVisibility(View.GONE);
                btn_Pay.setVisibility(View.VISIBLE);
                btn_Pay.setText("评价");
                break;
            case YXConfig.T_ORDER_STATUS_SUCCESS:
                // 行程结束、订单关闭
                btn_Cancle.setVisibility(View.GONE);
                btn_Pay.setVisibility(View.GONE);
                break;
            case T_ORDER_STATUS_CLOSE:
                // 订单关闭
                btn_Cancle.setVisibility(View.GONE);
                btn_Pay.setVisibility(View.GONE);
                break;
            default:
                break;
        }
        tv_type.setText(orderModel.getInfoType());
        getOrderInfo(DemoApplication.getInstance().getMyToken(), orderModel.getExtraInfoUUID());
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.icon_phone, R.id.goods_order_detail_btn, R.id.order_detail_map})
    void myClick(View view) {
        switch (view.getId()) {
            case R.id.icon_phone:
                PhoneUtils.call(YXGoodsOrderDetailActivity.this, orderModel.getDriver().getMobile());
                break;
            case R.id.goods_order_detail_btn:
                // 支付费用
                if (orderModel.getStatus()==T_ORDER_STATUS_INFO_SUCCESS){
                    // 评论
                    //DialogUIUtils.showToast("评论成功");
                    showEvaluationOrderDialog();
                }else {
                    // 支付车费
                    toActivity(YXTripPayActivity.createIntent(context, orderModel.getExtraInfoUUID()));
                }
                break;
            case R.id.order_detail_map:
                toActivity(YXOrderMapActivity.createIntent(context, orderModel));
                break;
            default:
                break;
        }
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void onForwardClick(View v) {
        super.onForwardClick(v);
        // 取消行程
        showCancelOrderDialog();
    }

    private void drawaView(newOrderModel model) {
        switch (model.getStatus()) {
            case T_ORDER_STATUS_CONFIRM:
                //待确定
                btn_Cancle.setVisibility(View.VISIBLE);
                btn_Pay.setVisibility(View.GONE);
                break;
            case T_ORDER_STATUS_CREATE:
                // 订单创建
                btn_Cancle.setVisibility(View.VISIBLE);
                btn_Pay.setVisibility(View.GONE);
                break;
            case YXConfig.T_ORDER_STATUS_ACCEPT:
                // 已被司机抢单、未开始行程
                btn_Cancle.setVisibility(View.VISIBLE);
                btn_Pay.setVisibility(View.GONE);
                break;
            case YXConfig.T_ORDER_STATUS_GO:
                // 订单进行中、可支付
                btn_Cancle.setVisibility(View.GONE);
                btn_Pay.setVisibility(View.VISIBLE);
                break;
            case YXConfig.T_ORDER_STATUS_WAIT_PAY:
                // 订单完成、待支付
                btn_Cancle.setVisibility(View.GONE);
                btn_Pay.setVisibility(View.VISIBLE);
                break;
            case T_ORDER_STATUS_INFO_SUCCESS:
                // 订单已支付、可以进行评价
                btn_Cancle.setVisibility(View.GONE);
                btn_Pay.setVisibility(View.VISIBLE);
                btn_Pay.setText("评价");
                break;
            case YXConfig.T_ORDER_STATUS_SUCCESS:
                // 行程结束、订单关闭
                btn_Cancle.setVisibility(View.GONE);
                btn_Pay.setVisibility(View.GONE);
                break;
            case T_ORDER_STATUS_CLOSE:
                // 订单关闭
                btn_Cancle.setVisibility(View.GONE);
                btn_Pay.setVisibility(View.GONE);
                break;
            default:
                break;
        }
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.default_head)
                .error(R.drawable.default_head)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        if(model.getStatus()!=T_ORDER_STATUS_CREATE&&model.getStatus()!=T_ORDER_STATUS_CLOSE&&model.getStatus()!=T_ORDER_STATUS_CONFIRM){
            Glide.with(context).load(model.getDriver().getHeadImage()).apply(options).into(imageView);
            tv_driver.setText(model.getDriver().getAlias());
            tv_brand.setText(model.getCar().getType());
        }else if (model.getStatus()==T_ORDER_STATUS_CREATE||model.getStatus()==T_ORDER_STATUS_CLOSE||model.getStatus()==T_ORDER_STATUS_CONFIRM){
            driver_info.setVisibility(View.GONE);
        }
        tv_origin.setText(model.getOrigin().getProvince()+model.getOrigin().getCity()+model.getOrigin().getCounty()+model.getOrigin().getAddress());
        tv_site.setText(model.getSite().getProvince()+model.getSite().getCity()+model.getSite().getCounty()+model.getSite().getAddress());
        /*List<PointModel> pointModels = model.getPathPoint();
        if (pointModels != null) {
            String path = "";
            for (int i = 0; i < pointModels.size(); i++) {
                if (i == 0) {
                    path = pointModels.get(i).getAddress();
                } else {
                    path += "、" + pointModels.get(i).getAddress();
                }
            }
            tv_path.setText(path);
        } else {
            tv_path.setText("无途径地点");
        }*/
        tv_datetime.setText(model.getDatetime());
        tv_remark.setText(model.getNote().equals("请设置备注信息")?"":model.getNote().equals("remark")?" ":model.getNote());
        if (model.getInfoType().equals("快递小件")&&model.getWeight()==0){
            li_weight.setVisibility(View.GONE);
        }
        tv_weight.setText(String.valueOf(model.getWeight())+"kg");
        tv_status.setText(OrderUtils.getStatusStr(model.getStatus()));
        tv_no.setText(model.getExtraInfoUUID());
        tv_type.setText(model.getInfoType());
        tv_consignorName.setText(model.getConsignorName());
        tv_consignorMobile.setText(model.getConsignorMobile());
        tv_infoOrigin.setText(model.getOrigin().getProvince()+model.getOrigin().getCity()+model.getOrigin().getCounty()+model.getOrigin().getAddress());

        tv_consigneeName.setText(model.getName());
        tv_consigneeMobile.setText(model.getMobile());
        tv_infoSite.setText(model.getSite().getProvince()+model.getSite().getCity()+model.getSite().getCounty()+model.getSite().getAddress());
    }

    /**
     * 获取订单详情
     *
     * @param token   用户token
     * @param orderNo 订单号
     */
    private void getOrderInfo(String token, String orderNo) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).getTripOrder(token, orderNo)
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
                                        System.out.println("订单详情数据：" + result);
                                        JSONObject jsonObject = JSON.parseObject(result);
                                        if (jsonObject.getString("code").equals("success")) {
                                            // 获取订单数据成功
                                            orderModel = JSON.parseObject(jsonObject.getString("data"), newOrderModel.class);
                                            drawaView(orderModel);
                                        } else {
                                            DialogUIUtils.showToast(jsonObject.getString("message"));
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

    private void showEvaluationOrderDialog() {
        final View rootView = View.inflate(context, R.layout.yx_evaluation_order, null);
        final Dialog dialog = DialogUIUtils.showCustomAlert(this, rootView, Gravity.CENTER, true, false).show();
        RadioGroup group = (RadioGroup) rootView.findViewById(R.id.evaluation_radioGroup);
        final EditText editText = (EditText) rootView.findViewById(R.id.evaluation_ed_reason);
        rootView.findViewById(R.id.evaluation_order_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String evaluation = editText.getText().toString();
                if (StringUtil.isNullOrEmpty(evaluation) && StringUtil.isNullOrEmpty(evaluationStr)) {
                    DialogUIUtils.showToast("请选择或输入评论!");
                } else if (!StringUtil.isNullOrEmpty(evaluation) && StringUtil.isNullOrEmpty(evaluationStr)) {
                    DialogUIUtils.dismiss(dialog);
                    evaluationOrder(DemoApplication.getInstance().getMyToken(), orderModel.getExtraInfoUUID(), evaluation);
                } else if (!StringUtil.isNullOrEmpty(evaluationStr) && StringUtil.isNullOrEmpty(evaluation)) {
                    evaluation = evaluationStr;
                    DialogUIUtils.dismiss(dialog);
                    evaluationOrder(DemoApplication.getInstance().getMyToken(), orderModel.getExtraInfoUUID(), evaluation);
                } else if (!StringUtil.isNullOrEmpty(evaluationStr) && !StringUtil.isNullOrEmpty(evaluation)) {
                    evaluation = evaluationStr;
                    DialogUIUtils.dismiss(dialog);
                    evaluationOrder(DemoApplication.getInstance().getMyToken(), orderModel.getExtraInfoUUID(), evaluationStr+",备注："+evaluation);
                }
            }
        });
        rootView.findViewById(R.id.evaluation_order_txt).setOnClickListener(new View.OnClickListener() {
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
                evaluationStr = rb.getText().toString();
                System.out.println(evaluationStr);
            }
        });
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
                    DialogUIUtils.showToast("请选择或输入取消行程的原因!");
                } else if (!StringUtil.isNullOrEmpty(reason) && StringUtil.isNullOrEmpty(reasonStr)) {
                    DialogUIUtils.dismiss(dialog);
                    cancelOrder(DemoApplication.getInstance().getMyToken(), orderModel.getExtraInfoUUID(), reason, InfoUUID);
                } else if (!StringUtil.isNullOrEmpty(reasonStr) && StringUtil.isNullOrEmpty(reason)) {
                    reason = reasonStr;
                    DialogUIUtils.dismiss(dialog);
                    cancelOrder(DemoApplication.getInstance().getMyToken(), orderModel.getExtraInfoUUID(), reason, InfoUUID);
                } else if (!StringUtil.isNullOrEmpty(reasonStr) && !StringUtil.isNullOrEmpty(reason)) {
                    DialogUIUtils.dismiss(dialog);
                    cancelOrder(DemoApplication.getInstance().getMyToken(), orderModel.getExtraInfoUUID(), reasonStr+",备注："+reason, InfoUUID);
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
     * @param orderId 订单ID
     * @param reason  取消原因
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
                                        DialogUIUtils.showToast(response.message());
                                    } else {
                                        String result = response.body().string();
                                        System.out.println("取消行程数据" + result);
                                        JSONObject jsonObject = JSON.parseObject(result);
                                        if (jsonObject.getString("code").equals("success")) {
                                            // 取消行程成功
                                            DialogUIUtils.showToast(jsonObject.getString("message"));
                                            onBackPressed();
                                            finish();
                                        } else {
                                            DialogUIUtils.showToast(jsonObject.getString("message"));
                                        }
                                    }
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
                        DialogUIUtils.showToast(t.getMessage());
                    }
                });
    }

    /**
     * 订单评论
     *
     * @param token   用户token
     * @param orderNo 订单ID
     * @param content  评论内容
     */
    private void evaluationOrder(String token, String orderNo, String content) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        JSONObject object = new JSONObject();
        object.put("orderNo", orderNo);
        object.put("content", content);
        RetrofitHttp.getRetrofit(builder.build()).evaluationOrder(token, StringUtil.getBody(object))
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
                                        if (jsonObject.getString("code").equals("success")) {
                                            // 取消行程成功
                                            DialogUIUtils.showToast(jsonObject.getString("message"));
                                            onBackPressed();
                                            finish();
                                        } else {
                                            DialogUIUtils.showToast(jsonObject.getString("message"));
                                        }
                                    }
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
                        DialogUIUtils.showToast(t.getMessage());
                    }
                });
    }

}
