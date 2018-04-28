package com.yxclient.app.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.dou361.dialogui.DialogUIUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yxclient.app.R;
import com.yxclient.app.app.DemoApplication;
import com.yxclient.app.config.YXConfig;
import com.yxclient.app.http.RetrofitHttp;
import com.yxclient.app.model.bean.GoodsOrderModel;
import com.yxclient.app.utils.PayResult;
import com.yxclient.app.utils.WxUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.pays.sdk.CommonPayConfig;

/**
 * 功能：购物订单详情
 * Created by yun.zhang on 2017/9/24 0024.
 * email:zy19930321@163.com
 */
public class YXShoppingOrderInfoActivity extends BaseActivity {
    // 收货人姓名
    @BindView(R.id.order_info_username)
    TextView tv_username;
    // 收货人电话
    @BindView(R.id.order_info_mobile)
    TextView tv_mobile;
    // 收货人详细地址
    @BindView(R.id.order_info_addr)
    TextView tv_addr;
    // 商品名称
    @BindView(R.id.order_info_goodsname)
    TextView tv_goodsname;
    // 购买数量
    @BindView(R.id.order_info_goodsnum)
    TextView tv_num;
    // 商品单价
    @BindView(R.id.order_info_goodsprice)
    TextView tv_price;
    // 商品图片
    @BindView(R.id.order_info_goodsimg)
    ImageView imageView;
    // 订单号
    @BindView(R.id.order_info_orderno)
    TextView tv_orderno;
    // 下单时间
    @BindView(R.id.order_info_datetime)
    TextView tv_datetime;
    // 支付方式
    @BindView(R.id.order_info_paytype)
    TextView tv_paytype;
    @BindView(R.id.rder_info_type)
    LinearLayout ll_paytype;
    // 支付按钮
    @BindView(R.id.rder_info_paybtn)
    Button button;

    // 购物订单对象
    GoodsOrderModel orderModel;
    // 支付方式
    private String payType;
    // 订单号
    private String orderNo;
    private static final int TEST_REQUEST_PAY_CODE = 100;
    private static final int SDK_PAY_FLAG = 1;
    private IWXAPI api;

    @Override
    public Activity getActivity() {
        return this;
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(YXShoppingOrderInfoActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(YXShoppingOrderInfoActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_shopping_order_info);
        ButterKnife.bind(this);
        initView();
        initData();
        initEvent();
    }

    public static Intent createIntent(Context context, GoodsOrderModel data) {
        return new Intent(context, YXShoppingOrderInfoActivity.class).
                putExtra(RESULT_DATA, data);
    }

    @Override
    public void initView() {
        // 默认微信支付
        payType = YXConfig.pay_WXpay;
        orderModel = (GoodsOrderModel) getIntent().getSerializableExtra(RESULT_DATA);
        getData(DemoApplication.getInstance().getMyToken(), orderModel.getNo());
    }

    private void drawaView(GoodsOrderModel model) {
        if (model != null) {
            if (model.getStatus() == 10) {
                // 需要支付：隐藏支付方式、显示支付按钮
                ll_paytype.setVisibility(View.GONE);
                button.setVisibility(View.VISIBLE);
            } else {
                // 不需要支付：显示支付方式、隐藏支付按钮
                ll_paytype.setVisibility(View.VISIBLE);
                button.setVisibility(View.GONE);
            }
            tv_username.setText(model.getName());
            tv_mobile.setText(model.getMobile());
            tv_addr.setText(model.getAddress());
            tv_goodsname.setText(model.getProduct().getTitle());
            tv_num.setText(String.format("x%s", String.valueOf(model.getNumber())));
            tv_price.setText(String.format("¥ %s", model.getAmount() / 100.0));
            Glide.with(context).load(model.getProduct().getTitleImage()).into(imageView);
            tv_orderno.setText(model.getNo());
            tv_datetime.setText(model.getCreateTime());
            tv_paytype.setText("在线支付");
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPayDialog();
            }
        });
    }

    private void getData(String token, String orderId) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).getShoppingOrder(token, orderId)
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
                                        System.out.println("购物订单详情数据" + result);
                                        JSONObject jsonObject = JSON.parseObject(result);
                                        if (jsonObject.getString("code").equals("success")) {
                                            orderModel = JSON.parseObject(jsonObject.getString("data"), GoodsOrderModel.class);
                                            drawaView(orderModel);
                                        } else {
                                            DialogUIUtils.showToast(jsonObject.getString("message"));
                                        }
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            default:
                                try {
                                    String result = response.errorBody().string();
                                    DialogUIUtils.showToast(result);
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

    private void showPayDialog() {
        View rootView = View.inflate(context, R.layout.customer_dialog_pay, null);
        DialogUIUtils.showCustomBottomAlert(context, rootView, true, true).show();
        RadioGroup group = (RadioGroup) rootView.findViewById(R.id.dialog_pay_rg);
        Button button = (Button) rootView.findViewById(R.id.dialog_pay_btn);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb01:
                        // 微信支付
                        payType = YXConfig.pay_WXpay;
                        break;
                    case R.id.rb02:
                        // 支付宝支付
                        payType = YXConfig.pay_Alipay;
                        break;
                    case R.id.rb03:
                        // 余额支付
                        payType = YXConfig.pay_BLANCE;
                        break;
                    default:
                        break;
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 支付订单
                if (!WxUtils.getInstal(context)&&payType==YXConfig.pay_WXpay){
                    DialogUIUtils.showToast("您还未安装微信客户端");
                }else{
                    submitOrder(DemoApplication.getInstance().getMyToken(), orderModel.getNo(), payType);
                }

            }
        });
    }

    /**
     * 订单支付
     *
     * @param token   用户token
     * @param channel 支付方式
     */
    private void submitOrder(String token, String orderId, String channel) {
        Map<String, String> params = new HashMap<>();
        params.put("orderNo", orderId);
        params.put("channel", channel);
        final Dialog dialog = DialogUIUtils.showMdLoading(context, "订单提交中", true, true, true, true).show();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).orderPay(token, params).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                DialogUIUtils.dismiss(dialog);
                switch (response.code()) {
                    case 200:
                        if (response.body() == null) {
                            DialogUIUtils.showToast(response.message());
                        } else {
                            try {
                                String result = response.body().string();
                                System.out.println("商品下单数据：" + result);
                                JSONObject jsonObject = JSON.parseObject(result);
                                JSONObject dataObject = JSON.parseObject(jsonObject.getString("data"));
                                final String data=dataObject.getString("data");
                                if ("success".equals(jsonObject.getString("code"))) {
                                    orderNo = dataObject.getString("orderNo");
                                    //doSubmitSuccess(jsonObject.getJSONObject("data"));
                                    switch (payType) {
                                        case YXConfig.pay_Alipay:
                                            // 调用支付宝支付
                                            //PayUtils.doZfbPay(context, TEST_REQUEST_PAY_CODE, dataObject.getString("data"));
                                            Runnable payRunnable = new Runnable() {
                                                @Override
                                                public void run() {
                                                    PayTask alipay = new PayTask(YXShoppingOrderInfoActivity.this);
                                                    Map<String, String> result = alipay.payV2(data, true);
                                                    Log.i("msp", result.toString());

                                                    Message msg = new Message();
                                                    msg.what = SDK_PAY_FLAG;
                                                    msg.obj = result;
                                                    mHandler.sendMessage(msg);
                                                }
                                            };

                                            Thread payThread = new Thread(payRunnable);
                                            payThread.start();
                                            break;
                                        case YXConfig.pay_WXpay:
                                            // 调用微信支付
                                            //PayUtils.doWxPay(context, TEST_REQUEST_PAY_CODE, dataObject.getString("data"));
                                            // 微信支付
                                            //首先在调用之前，需要先在代码中进行微信API注册
                                            api= WXAPIFactory.createWXAPI(context, null);
                                            // 将该app注册到微信
                                            api.registerApp(YXConfig.APP_ID);
                                            JSONObject json = JSON.parseObject(data);
                                            PayReq req = new PayReq();
                                            req.appId			= json.getString("appid");
                                            req.partnerId		= json.getString("partnerid");
                                            req.prepayId		= json.getString("prepayid");
                                            req.nonceStr		= json.getString("noncestr");
                                            req.timeStamp		= json.getString("timestamp");
                                            req.packageValue	= json.getString("package");
                                            req.sign			= json.getString("sign");
                                            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                                            api.sendReq(req);
                                            break;
                                        case YXConfig.pay_BLANCE:
                                            // 用户余额支付
                                            break;
                                    }
                                } else {
                                    DialogUIUtils.showToast(response.message());
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    default:
                        try {
                            String result = response.errorBody().string();
                            JSONObject jsonObject = JSON.parseObject(result);
                            DialogUIUtils.showToast(jsonObject.getString("message"));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case TEST_REQUEST_PAY_CODE:
                    String toastHint = "支付模式:%s,响应码:%s,结果描述:%s";
                    String payModeDesc = "未知";
                    String payRespCode = "unKnow";
                    int payMode = data.getIntExtra(CommonPayConfig.INTENT_KEY_CUR_PAY_MODE, CommonPayConfig.PAY_MODE_WX);
                    payModeDesc = payMode == CommonPayConfig.PAY_MODE_ALIPAY ? "[支付宝]" : "[微信]";
                    payRespCode = data.getStringExtra(CommonPayConfig.INTENT_KEY_REAL_PAY_RESULT_STATUS_CODE);
                    String resultDesc = "支付失败";
                    switch (resultCode) {
                        case CommonPayConfig.REQ_PAY_RESULT_CODE_OK:
                            resultDesc = "支付成功";
                            // 跳转到购买成功页面
                            DialogUIUtils.showToast(resultDesc);
                            toActivity(YXSubmitOrderSuccessActivity.createIntent(context, orderNo));
                            finish();
                            break;
                        case CommonPayConfig.REQ_PAY_RESULT_CODE_CANCEL:
                            resultDesc = "支付被取消了";
                            DialogUIUtils.showToast(resultDesc);
                            break;
                        case CommonPayConfig.REQ_PAY_RESULT_CODE_NO_WX:
                            resultDesc = "支付失败,未安装微信APP";
                            DialogUIUtils.showToast(resultDesc);
                            break;
                        case CommonPayConfig.REQ_PAY_RESULT_CODE_ERROR:
                            resultDesc = "支付失败";
                            DialogUIUtils.showToast(resultDesc);
                            break;
                    }
                    break;
                default:
                    break;
            }
        }
    }
}