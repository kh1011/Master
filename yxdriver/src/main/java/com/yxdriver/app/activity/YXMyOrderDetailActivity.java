package com.yxdriver.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dou361.dialogui.DialogUIUtils;
import com.yxdriver.app.R;
import com.yxdriver.app.app.DemoApplication;
import com.yxdriver.app.config.YXConfig;
import com.yxdriver.app.http.RetrofitHttp;
import com.yxdriver.app.model.bean.OrderModel;
import com.yxdriver.app.utils.OrderUtils;
import com.yxdriver.app.utils.StringUtil;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;
import zuo.biao.library.base.BaseActivity;

import static com.yxdriver.app.utils.StringUtil.changeObject;

/**
 * Created by mac on 2017/10/17.
 * 个人订单详情
 */

public class YXMyOrderDetailActivity extends BaseActivity {
    private OrderModel orderModel;
    // 取消订单
    @BindView(R.id.order_detail_right)
    Button btn_Cancel;
    //
    @BindView(R.id.order_detail_type)
    TextView tv_Type;
    //
    @BindView(R.id.order_detail_no)
    TextView tv_No;
    //
    @BindView(R.id.order_detail_status)
    TextView tv_Status;
    //
    @BindView(R.id.order_detail_datetime)
    TextView tv_Datetime;
    // 出发地
    @BindView(R.id.order_detail_origin)
    TextView tv_Origin;
    // 目的地
    @BindView(R.id.order_detail_site)
    TextView tv_Site;
    // 途经点
    @BindView(R.id.order_detail_points)
    TextView tv_Points;
    // 订单备注
    @BindView(R.id.order_detail_note)
    TextView tv_Note;
    // 取消订单的原因
    String reasonStr = "";
    //车辆载重
    @BindView(R.id.order_capacity)
    TextView tv_capacity;


    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_myorder_detail);
        ButterKnife.bind(this);
        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }
    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    public static Intent createIntent(Context context, OrderModel data) {
        return new Intent(context, YXMyOrderDetailActivity.class).
                putExtra(RESULT_DATA, data);
    }

    @Override
    public void initView() {
        //
        tv_Points.setVisibility(View.GONE);
    }

    @Override
    public void initData() {
        // 获取传过来的订单对象
        orderModel = (OrderModel) getIntent().getSerializableExtra(RESULT_DATA);
        if (orderModel.getStatus() == YXConfig.T_ORDER_STATUS_CREATE) {
            btn_Cancel.setVisibility(View.VISIBLE);
        }
        tv_Type.setText(orderModel.getInfoType());
        tv_No.setText("订单编号：" + orderModel.getNo());
        tv_Status.setText(OrderUtils.getStatusStr(orderModel.getStatus()));
        tv_Datetime.setText(orderModel.getDatetime());
        tv_Origin.setText(orderModel.getOrigin().getProvince()+orderModel.getOrigin().getCity()+orderModel.getOrigin().getCounty()+orderModel.getOrigin().getAddress());
        tv_Site.setText(orderModel.getSite().getProvince()+orderModel.getSite().getCity()+orderModel.getSite().getCounty()+orderModel.getSite().getAddress());
        tv_Note.setText("备注信息：" + orderModel.getNote());
        if (orderModel.getOrderType().equals("express")){
           // tv_capacity.setText("车辆载重："+orderModel.getCapacity()+"kg");
        }else {
            tv_capacity.setVisibility(View.GONE);
        }

    }

    @Override
    public void initEvent() {

    }

    @OnClick({R.id.order_detail_map})
    void myClick(View view) {
        switch (view.getId()) {
            case R.id.order_detail_map:
                toActivity(YXOrderMapActivity.createIntent(context, changeObject(orderModel)));
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

    /**
     * 取消订单提示框
     */
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
                    cancelOrder(DemoApplication.getInstance().getMyToken(), orderModel.getNo(), reason, "");
                } else if (!StringUtil.isNullOrEmpty(reasonStr) && StringUtil.isNullOrEmpty(reason)) {
                    reason = reasonStr;
                    DialogUIUtils.dismiss(dialog);
                    cancelOrder(DemoApplication.getInstance().getMyToken(), orderModel.getNo(), reason, "");
                }
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
     * @param token
     * @param orderId
     * @param reason
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
}
