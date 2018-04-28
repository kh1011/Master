package com.yxclient.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
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
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;
import zuo.biao.library.base.BaseActivity;

import static com.yxclient.app.config.YXConfig.T_ORDER_STATUS_INFO_SUCCESS;

/**
 * Created by mac on 2017/10/8.
 * 行程详情
 */

public class YXTripDetailActivity extends BaseActivity {
    private newOrderModel orderModel;

    @BindView(R.id.item_trip_type)
    TextView tv_Type;
    @BindView(R.id.item_trip_status)
    TextView tv_Status;
    @BindView(R.id.item_trip_datetime)
    TextView tv_datetime;
    @BindView(R.id.item_trip_origin)
    TextView tv_origin;
    @BindView(R.id.item_trip_site)
    TextView tv_site;
    // 司机信息
    @BindView(R.id.trip_order_txt)
    TextView tv_infoTxt;
    @BindView(R.id.trip_order_reDriver)
    RelativeLayout re_info;
    // 支付按钮
    @BindView(R.id.order_detail_pay_btn)
    Button btn_Pay;
    // 取消行程
    @BindView(R.id.trip_order_right)
    Button btn_Cancle;
    // 司机头像
    @BindView(R.id.profile_userimage)
    CircleImageView imageView;
    // 司机姓名
    @BindView(R.id.trip_order_detail_driver)
    TextView tv_Driver;
    // 车牌号
    @BindView(R.id.trip_order_detail_carNo)
    TextView tv_carNo;
    // 乘车人数
    @BindView(R.id.item_trip_number)
    TextView tv_number;
    // 订单编号
    @BindView(R.id.order_detail_no)
    TextView tv_no;
    // 途经点
    /*@BindView(R.id.item_trip_path)
    TextView tv_path;
    @BindView(R.id.path)
    LinearLayout li_path;*/
    // 备注信息
    @BindView(R.id.item_trip_remark)
    TextView tv_remark;
    // 取消订单的原因
    String reasonStr = "";
    // 其他评价
    String evaluationStr = "";

    //路程
    @BindView(R.id.lc)
    LinearLayout li_lc;
    @BindView(R.id.item_trip_lc)
    TextView tv_lc;

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_trip_order_detail);
        ButterKnife.bind(this);
        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }

    public static Intent createIntent(Context context, newOrderModel data) {
        return new Intent(context, YXTripDetailActivity.class).
                putExtra(RESULT_DATA, data);

    }

    @Override
    public void initView() {
       // li_path.setVisibility(View.GONE);
        orderModel = (newOrderModel) getIntent().getSerializableExtra(RESULT_DATA);
        switch (orderModel.getStatus()) {
            case YXConfig.T_ORDER_STATUS_CREATE:
                // 订单创建
                btn_Cancle.setVisibility(View.VISIBLE);
                btn_Pay.setVisibility(View.GONE);
                li_lc.setVisibility(View.GONE);
                break;
            case YXConfig.T_ORDER_STATUS_ACCEPT:
                // 已被司机抢单、未开始行程
                btn_Cancle.setVisibility(View.VISIBLE);
                btn_Pay.setVisibility(View.GONE);
                li_lc.setVisibility(View.GONE);
                break;
            case YXConfig.T_ORDER_STATUS_CONFIRM:
                // 待司机确认订单
                btn_Cancle.setVisibility(View.VISIBLE);
                btn_Pay.setVisibility(View.GONE);
                li_lc.setVisibility(View.GONE);
                break;
            case YXConfig.T_ORDER_STATUS_GO:
                // 订单进行中、可支付
                btn_Cancle.setVisibility(View.GONE);
                if (orderModel.getOrderType().equals("express")){
                    btn_Pay.setVisibility(View.VISIBLE);
                }else {
                    btn_Pay.setVisibility(View.GONE);
                }
                li_lc.setVisibility(View.GONE);
                break;
            case YXConfig.T_ORDER_STATUS_WAIT_PAY:
                // 订单完成、待支付
                btn_Cancle.setVisibility(View.GONE);
                btn_Pay.setVisibility(View.VISIBLE);
                li_lc.setVisibility(View.VISIBLE);
                break;
            case T_ORDER_STATUS_INFO_SUCCESS:
                // 订单已支付、可以进行评价
                btn_Cancle.setVisibility(View.GONE);
                btn_Pay.setVisibility(View.VISIBLE);
                btn_Pay.setText("评价");
                li_lc.setVisibility(View.VISIBLE);
                break;
            case YXConfig.T_ORDER_STATUS_SUCCESS:
                // 行程结束、订单关闭
                btn_Cancle.setVisibility(View.GONE);
                btn_Pay.setVisibility(View.GONE);
                li_lc.setVisibility(View.VISIBLE);
                break;
            case YXConfig.T_ORDER_STATUS_CLOSE:
                // 订单关闭
                btn_Cancle.setVisibility(View.GONE);
                btn_Pay.setVisibility(View.GONE);
                li_lc.setVisibility(View.GONE);
                break;
            default:
                break;
        }
        getOrderInfo(DemoApplication.getInstance().getMyToken(),orderModel.getExtraInfoUUID());

        /*selectDate(orderModel.getInfo().get(0).getPayTime());
        if (selectDate(orderModel.getInfo().get(0).getPayTime())){
            evaluationOrder(DemoApplication.getInstance().getMyToken(), orderModel.getNo(), "非常满意");
        }*/
    }

    @Override
    public void initData() {
        //
    }

    @Override
    public void initEvent() {

    }

    private void drawaView(newOrderModel model) {
        if (model.getDriver() == null) {
            tv_infoTxt.setVisibility(View.GONE);
            re_info.setVisibility(View.GONE);
        } else {
            tv_infoTxt.setVisibility(View.VISIBLE);
            re_info.setVisibility(View.VISIBLE);
            Glide.with(context).load(model.getDriver().getHeadImage()).into(imageView);
            tv_Driver.setText(model.getDriver().getName());
            tv_carNo.setText(model.getCar().getPlateNumber());
        }
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
            //tv_path.setText(path);
        } else {
           // tv_path.setText("无途径地点");
        }*/
        if (model.getMileage()+""!=null){
            BigDecimal bd = new BigDecimal(model.getMileage());
            BigDecimal setScale = bd.setScale(1, bd.ROUND_DOWN);
            tv_lc.setText(String.valueOf(setScale)+"km");
        }
        tv_Status.setText(OrderUtils.getStatusStr(model.getStatus()));
        tv_datetime.setText(model.getDatetime());
        tv_origin.setText(model.getOrigin().getProvince()+model.getOrigin().getCity()+model.getOrigin().getCounty()+model.getOrigin().getAddress());
        tv_site.setText(model.getSite().getProvince()+model.getSite().getCity()+model.getSite().getCounty()+model.getSite().getAddress());
        tv_number.setText(String.valueOf(model.getNumber()));
        String note;
        if (orderModel.getNote()==null){
            note=" ";
        }else if (orderModel.getNote().equals("请设置备注信息")|orderModel.getNote().equals("remark")){
            note=" ";
        } else {
            note=orderModel.getNote();
        }
        tv_remark.setText(note);
        tv_no.setText(model.getExtraInfoUUID());
        tv_Type.setText("订单类型："+model.getInfoType());
    }

    @OnClick({R.id.order_detail_pay_btn, R.id.trip_order_detail_map,R.id.order_phone_btn})
    void myClick(View view) {
        switch (view.getId()) {
            case R.id.order_detail_pay_btn:

                if (orderModel.getStatus()==T_ORDER_STATUS_INFO_SUCCESS){
                    // 评论
                    //DialogUIUtils.showToast("评论成功");
                    showEvaluationOrderDialog();
                }else {
                    // 支付车费
                    toActivity(YXTripPayActivity.createIntent(context, orderModel.getExtraInfoUUID()));
                }

                break;
            case R.id.trip_order_detail_map:
                // 查看订单地图
                //startActivity(new Intent(YXTripDetailActivity.this, ComponentActivity.class));
               toActivity(YXOrderMapActivity.createIntent(context, orderModel));
                break;
            case R.id.order_phone_btn:
                //联系司机
               PhoneUtils.call(YXTripDetailActivity.this, orderModel.getDriver().getMobile());
                break;
            default:
                break;
        }
    }


    @Override
    public void onForwardClick(View v) {
        super.onForwardClick(v);
        // 取消行程
        showCancelOrderDialog();
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
                    cancelOrder(DemoApplication.getInstance().getMyToken(), reason, orderModel.getExtraInfoUUID());
                } else if (!StringUtil.isNullOrEmpty(reasonStr) && StringUtil.isNullOrEmpty(reason)) {
                    reason = reasonStr;
                    DialogUIUtils.dismiss(dialog);
                    cancelOrder(DemoApplication.getInstance().getMyToken(), reason, orderModel.getExtraInfoUUID());
                } else if (!StringUtil.isNullOrEmpty(reasonStr) && !StringUtil.isNullOrEmpty(reason)) {
                    DialogUIUtils.dismiss(dialog);
                    cancelOrder(DemoApplication.getInstance().getMyToken(), reasonStr+",备注："+reason, orderModel.getExtraInfoUUID());
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
                    DialogUIUtils.showToast("请选择或输入取消行程的原因!");
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

    /**
     * 取消行程
     *
     * @param token   用户token
     * @param reason  取消原因
     */
    private void cancelOrder(String token,String reason, String extraInfoUUID) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        JSONObject object = new JSONObject();
        if (!StringUtil.isNullOrEmpty(extraInfoUUID)) {
            object.put("extraInfoUUID", extraInfoUUID);
        }
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
                                    DialogUIUtils.showToast(JSON.parseObject(response.errorBody().string()).getString("message"));
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

    @Override
    public void onResume() {
        super.onResume();
        getOrderInfo(DemoApplication.getInstance().getMyToken(), orderModel.getExtraInfoUUID());
    }

    private boolean selectDate(String beginTime) {
        //SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //String date=sdf.format(new java.util.Date());
        if (beginTime==null){
            return false;
        }
        Date nowdate = new Date();
        Date setdate = null;
        try {
            setdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(beginTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long between = (nowdate.getTime() - setdate.getTime());
        boolean result = between > 1000 * 60 * 60*24*5;
        Log.d("selectDate: ",String.valueOf(result));
        return result;
    }
}
