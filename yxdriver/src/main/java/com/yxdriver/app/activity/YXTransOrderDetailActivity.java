package com.yxdriver.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import com.yxdriver.app.model.bean.CustomerModel;
import com.yxdriver.app.model.bean.OrderModel;
import com.yxdriver.app.utils.OrderUtils;
import com.yxdriver.app.utils.StringUtil;
import com.yxdriver.app.view.NoScrollListView;

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

import static com.yxdriver.app.utils.StringUtil.changeObject;

/**
 * Created by mac on 2017/10/8.
 * 货运行程详情
 */

public class YXTransOrderDetailActivity extends BaseActivity {
    // 客户列表
    private List<CustomerModel> cusList;
    private OrderModel orderModel;

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
    @BindView(R.id.trip_order_txt)
    TextView tv_Txt;
    @BindView(R.id.trip_order_right)
    Button btn_Right;
    // 取消订单的原因
    String reasonStr = "";

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
    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


    /**
     * 启动这个Activity的Intent
     *
     * @param context
     * @param data
     * @return
     */
    public static Intent createIntent(Context context, OrderModel data) {
        return new Intent(context, YXTransOrderDetailActivity.class).
                putExtra(RESULT_DATA, data);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        orderModel = (OrderModel) getIntent().getSerializableExtra(RESULT_DATA);
        tv_Type.setText("订单类型：" + orderModel.getInfoType());
        tv_Status.setText(OrderUtils.getStatusStr(orderModel.getStatus()));
        tv_datetime.setText(orderModel.getDatetime());
        tv_origin.setText(orderModel.getOrigin().getAddress());
        tv_site.setText(orderModel.getSite().getAddress());
        cusList = orderModel.getInfo();
        if (orderModel.getStatus() == -11) {
            btn_Right.setVisibility(View.GONE);
        }
    }

    @Override
    public void initEvent() {

    }

    @OnClick({R.id.item_trip_map})
    void myClick(View view) {
        toActivity(YXOrderMapActivity.createIntent(context, changeObject(orderModel)));
    }

    class MyAdapter extends BaseAdapter {


        //获取当前items项的大小，也可以看成是数据源的大小
        @Override
        public int getCount() {
            return cusList.size();
        }

        //根据item的下标获取到View对象
        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        //获取到items的id
        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = null;

            //获取填充器对象，这个对象可以帮助我们绘制出items项，获取方式有多种：
            //LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LayoutInflater inflater = context.getLayoutInflater();
            /*
            调用打气筒中的 inflate(int resource, ViewGroup root) 方法
            第一个参数是一个布局文件对象，通过这个布局文件，inflater会在这布局文件中绘制items项
            第二个参数是需不需要将第一个参数中的那个布局文件嵌入到另外一个布局文件中。如果要写上布局文件的id，如果不需要直接写null
            */
            view = inflater.inflate(R.layout.item_trip_order, null);
            CustomerModel customerModel = cusList.get(position);//通过回调这个方法传过来的position参数获取到指定数据源中的对象
            //找到布局文件中的控件
            TextView name = (TextView) view.findViewById(R.id.trip_c_item_name);
            Button btn = (Button) view.findViewById(R.id.trip_c_item_btn);
            switch (customerModel.getStatus()) {
                case YXConfig.T_ORDER_STATUS_ACCEPT:
                    btn.setBackgroundResource(R.drawable.green_btn);
                    btn.setText("开始行程");
                    break;
                case YXConfig.T_ORDER_STATUS_GO:
                    btn.setBackgroundResource(R.drawable.grive_btn);
                    btn.setText("待支付");
                    break;
                case YXConfig.T_ORDER_STATUS_INFO_SUCCESS:
                    btn.setBackgroundResource(R.drawable.red_btn);
                    btn.setText("已结束");
                    break;
                case YXConfig.T_ORDER_STATUS_SUCCESS:
                    btn.setBackgroundResource(R.drawable.red_btn);
                    btn.setText("已结束");
                    break;
                case YXConfig.T_ORDER_STATUS_CLOSE:
                    btn.setBackgroundResource(R.drawable.red_btn);
                    btn.setText("已结束");
                    break;
                default:
                    break;
            }
            name.setText(customerModel.getName());
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 开始行程、结束行程(支付)

                }
            });
            return view;
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
                    cancelOrder(DemoApplication.getInstance().getMyToken(), orderModel.getNo(), reason);
                } else if (!StringUtil.isNullOrEmpty(reasonStr) && StringUtil.isNullOrEmpty(reason)) {
                    reason = reasonStr;
                    DialogUIUtils.dismiss(dialog);
                    cancelOrder(DemoApplication.getInstance().getMyToken(), orderModel.getNo(), reason);
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
    private void cancelOrder(String token, String orderId, String reason) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        JSONObject object = new JSONObject();
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

    private void begainOrder(String token, String orderId, String extraInfoUUID) {
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
