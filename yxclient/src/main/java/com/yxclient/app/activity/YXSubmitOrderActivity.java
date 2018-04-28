package com.yxclient.app.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dou361.dialogui.DialogUIUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yxclient.app.R;
import com.yxclient.app.app.DemoApplication;
import com.yxclient.app.config.YXConfig;
import com.yxclient.app.http.RetrofitHttp;
import com.yxclient.app.model.bean.GoodPruductModel;
import com.yxclient.app.model.bean.ReceiptAddressModel;
import com.yxclient.app.utils.PayResult;
import com.yxclient.app.utils.StringUtil;
import com.yxclient.app.utils.WxUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.pays.sdk.CommonPayConfig;

/**
 * 订单确认界面
 * <p>
 * Created by zhangyun on 2017/9/30 0030
 * EMail -> yun.zhang@chinacreator.com
 */
public class YXSubmitOrderActivity extends BaseActivity {
    public static final int REQUEST_CHOOSE_ADDRESS = 111;

    // 收货人姓名
    @BindView(R.id.tv_receipt_user)
    TextView tvReceiptUser;
    // 收货人联系电话
    @BindView(R.id.tv_receipt_phone)
    TextView tvReceiptPhone;
    // 收货人地址
    @BindView(R.id.tv_receipt_address)
    TextView tvReceiptAddress;
    // 实付款金额
    @BindView(R.id.tv_all_price)
    TextView tvAllPrice;
    // 商品标题
    @BindView(R.id.submit_order_title)
    TextView order_Title;
    // 商品单价
    @BindView(R.id.submit_order_price)
    TextView order_Price;
    // 购买数量
    @BindView(R.id.submit_order_number)
    TextView order_Number;
    // 商品应付金额合计
    @BindView(R.id.submit_order_totale)
    TextView order_Total;
    // 商品图片
    @BindView(R.id.submit_order_img)
    ImageView imageView;
    // 根据加减显示购买商品数量
    @BindView(R.id.btn_result)
    Button btn_Number;
    // 提示添加收货地址
    @BindView(R.id.submit_order_addAddr)
    RelativeLayout re_Add;
    // 默认收货地址显示区域
    @BindView(R.id.ll_receipt_address)
    LinearLayout re_address;
    //颜色，重量
    @BindView(R.id.submit_order_weight)
    TextView tv_weight;
    @BindView(R.id.submit_order_color)
    TextView tv_color;
    @BindView(R.id.submit_order_color_)
    TextView tv_color_;
    // 支付方式弹出框
    PopupWindow pop;
    LinearLayout ll_popup;
    // 支付方式
    String payType;
    String paytypeStr;
    // 总金额
    double allPrice = 0;
    // 购买数量
    private int number = 0;
    //颜色
    private String color;
    //重量
    private String weight;
    // 商品对象
    GoodPruductModel model;
    // 收货地址
    ReceiptAddressModel receiptAddressModel;
    private static final int TEST_REQUEST_PAY_CODE = 100;
    // 订单号
    private String orderNo;

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
                        Toast.makeText(YXSubmitOrderActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(YXSubmitOrderActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.yx_activity_submit_order);
        ButterKnife.bind(this);
        initView();
        initData();
        initEvent();
    }

    @Override
    public void initView() {
        //  支付方式默认为支付宝支付
        payType = YXConfig.pay_Alipay;
        paytypeStr = "支付宝支付";
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            model = (GoodPruductModel) bundle.getSerializable("model");
            number = bundle.getInt("number");
            color = bundle.getString("goodcolor");
            weight = bundle.getString("goodweight");
            drawaView(model);
        }

    }

    private void drawaView(GoodPruductModel goodPruductModel) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.default_shop)
                .error(R.drawable.default_shop)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(context).load(goodPruductModel.getTitleImage()).apply(options).into(imageView);
        order_Title.setText(goodPruductModel.getTitle());
        order_Number.setText(String.format("x%s", String.valueOf(number)));
        order_Price.setText(String.format("¥ %s", String.format("%.2f",goodPruductModel.getPrice() / 100.0)));
        order_Total.setText(String.format("¥ %s", String.format("%.2f",number *goodPruductModel.getPrice() / 100.0)));
        btn_Number.setText(String.valueOf(number));
        tvAllPrice.setText(String.format("¥ %s", String.format("%.2f",number *goodPruductModel.getPrice() / 100.0)));
        if (color.equals("")){
            tv_color.setVisibility(View.GONE);
            tv_color_.setVisibility(View.GONE);
        }else {
            tv_color.setText(color);
        }
        if (weight.equals("")){
            tv_weight.setVisibility(View.GONE);
        }else {
            tv_weight.setText(weight+"kg");
        }

    }

    @Override
    public void initData() {
        if (receiptAddressModel == null) {
            getReceiptDefault(DemoApplication.getInstance().getMyToken());
        }else {
            re_Add.setVisibility(View.GONE);
            re_address.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 获取用户默认收货地址；；；；；如果没有收货地址，则提示用户去添加收货地址
     *
     * @param token 用户token
     */
    private void getReceiptDefault(String token) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).getDefaultReceiptAddress(token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                switch (response.code()) {
                    case 200:
                        if (response.body() == null) {
                            DialogUIUtils.showToast(response.message());
                        } else {
                            try {
                                String result = response.body().string();
                                if (StringUtil.isNullOrEmpty(result)) {
                                    DialogUIUtils.showToast("解析默认数据失败");
                                } else {
                                    JSONObject jsonObject = JSON.parseObject(result);
                                    if ("success".equals(jsonObject.getString("code"))) {
                                        if (StringUtil.isNullOrEmpty(jsonObject.getString("data"))) {
                                            re_Add.setVisibility(View.VISIBLE);
                                            re_address.setVisibility(View.GONE);
                                        } else {
                                            receiptAddressModel = JSON.parseObject(jsonObject.getString("data"), ReceiptAddressModel.class);
                                            tvReceiptUser.setText(String.format("收货人：%s", receiptAddressModel.getName()));
                                            tvReceiptPhone.setText(receiptAddressModel.getMobile());
                                            tvReceiptAddress.setText(receiptAddressModel.getProvince() + "省" + receiptAddressModel.getCity() + "市" +
                                                    receiptAddressModel.getCounty() + receiptAddressModel.getStreet()+
                                                    receiptAddressModel.getAddress().split(receiptAddressModel.getStreet())[1]);
                                        }
                                    } else {
                                        DialogUIUtils.showToast("请求数据失败");
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    default:
                        DialogUIUtils.showToast(response.message());
                        break;
                }
            }

            @Override
            public void onFailure(Throwable t) {
                DialogUIUtils.showToast(t.getMessage());
            }
        });
    }


    @Override
    public void initEvent() {

    }

    @OnClick({R.id.btn_submit_order, R.id.ll_receipt_address, R.id.btn_reduce, R.id.btn_add, R.id.submit_order_addAddr})
    void viewOnclick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit_order:
                //TODO 提交订单
                // 1、判断是否设置收货地址
                if (receiptAddressModel == null) {
                    DialogUIUtils.showToast("请设置收货地址");
                } else {
                    showPopupWindow();
                    ll_popup.startAnimation(AnimationUtils.loadAnimation(
                            context, R.anim.activity_translate_in));
                    pop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                }
                break;
            case R.id.ll_receipt_address:
                // 切换收货地址
                Intent intent1 = new Intent(context, YXReceiptAddressActivity.class);
                startActivityForResult(intent1, REQUEST_CHOOSE_ADDRESS);
                break;
            case R.id.submit_order_addAddr:
                // 选择收货地址
                Intent intent = new Intent(context, YXReceiptAddressActivity.class);
                startActivityForResult(intent, REQUEST_CHOOSE_ADDRESS);
                break;
            case R.id.btn_reduce:
                // 减少购买数量
                if (number > 1) {
                    number--;
                }
                btn_Number.setText(String.valueOf(number));
                order_Number.setText(String.format("x%s", String.valueOf(number)));
                order_Total.setText(String.format("¥ %s", String.format("%.2f",number *model.getPrice() / 100.0)));
                tvAllPrice.setText(String.format("¥ %s", String.format("%.2f",number *model.getPrice() / 100.0)));
                if (!weight.equals("")){
                    tv_weight.setText(Double.valueOf(weight)*number+"kg");
                }

                break;
            case R.id.btn_add:
                // 添加购买数量
                number++;
                btn_Number.setText(String.valueOf(number));
                order_Number.setText(String.format("x%s", String.valueOf(number)));
                order_Total.setText(String.format("¥ %s", String.format("%.2f",number *model.getPrice() / 100.0)));
                tvAllPrice.setText(String.format("¥ %s", String.format("%.2f",number *model.getPrice() / 100.0)));
                if (!weight.equals("")){
                    tv_weight.setText(Double.valueOf(weight)*number+"kg");
                }
                break;
            default:
                break;
        }

    }


    /****
     * 付款提示框
     */
    public void showPopupWindow() {
        pop = new PopupWindow(context);
        View view = getLayoutInflater().inflate(R.layout.item_pop_confirm_order,
                null);
        ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);
        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
        final ImageView btn_cancel = (ImageView) view.findViewById(R.id.iv_close);
        final ImageView iv_back = (ImageView) view.findViewById(R.id.iv_back);
        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
        final LinearLayout ll_pice = (LinearLayout) view.findViewById(R.id.ll_pice);
        final TextView tv_choose_pay_type = (TextView) view.findViewById(R.id.tv_choose_pay_type);
        TextView tv_all_price = (TextView) view.findViewById(R.id.tv_all_price);
        LinearLayout ll_show_choose_pay_type = (LinearLayout) view.findViewById(R.id.ll_show_choose_pay_type);
        final ScrollView ll_choose_pay_type = (ScrollView) view.findViewById(R.id.sv_choose_pay_type);
        LinearLayout ll_choose_zhifubao = (LinearLayout) view.findViewById(R.id.ll_choose_zhifubao);
        final ImageView iv_choose_zhifubao = (ImageView) view.findViewById(R.id.iv_choose_zhifubao);
        LinearLayout ll_choose_wechat = (LinearLayout) view.findViewById(R.id.ll_choose_wechat);
        final ImageView iv_choose_wechat = (ImageView) view.findViewById(R.id.iv_choose_wechat);
        LinearLayout ll_choose_yue = (LinearLayout) view.findViewById(R.id.ll_choose_yue);
        final ImageView iv_choose_yue = (ImageView) view.findViewById(R.id.iv_choose_yue);

        iv_back.setVisibility(View.GONE);
        ll_choose_pay_type.setVisibility(View.GONE);
        tv_choose_pay_type.setText(paytypeStr);
        tv_all_price.setText(String.format("¥ %s", String.format("%.2f",number *model.getPrice() / 100.0)));
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!WxUtils.getInstal(context)&&payType==YXConfig.pay_WXpay){
                    DialogUIUtils.showToast("您还未安装微信客户端");
                }else{
                    submitOrder(DemoApplication.getInstance().getMyToken(), model.getUuid(), number, receiptAddressModel.getUuid(), payType);
                    pop.dismiss();
                    ll_popup.clearAnimation();
                }

            }
        });

        //显示选择支付方式
        ll_show_choose_pay_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_choose_pay_type.setVisibility(View.VISIBLE);
                ll_pice.setVisibility(View.GONE);
                iv_back.setVisibility(View.VISIBLE);
                btn_cancel.setVisibility(View.GONE);
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_choose_pay_type.setVisibility(View.GONE);
                iv_back.setVisibility(View.GONE);
                ll_pice.setVisibility(View.VISIBLE);
                btn_cancel.setVisibility(View.VISIBLE);
                tv_choose_pay_type.setText(payType);
            }
        });
        ll_choose_wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetChooseView(iv_choose_wechat, iv_choose_yue, iv_choose_zhifubao);
                setChooseView(iv_choose_wechat);
                tv_choose_pay_type.setText("微信");
                payType = YXConfig.pay_WXpay;
                paytypeStr = "微信支付";
            }
        });
        ll_choose_zhifubao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetChooseView(iv_choose_wechat, iv_choose_yue, iv_choose_zhifubao);
                setChooseView(iv_choose_zhifubao);
                tv_choose_pay_type.setText("支付宝");
                payType = YXConfig.pay_Alipay;
                paytypeStr = "支付宝支付";
            }
        });
        ll_choose_yue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetChooseView(iv_choose_wechat, iv_choose_yue, iv_choose_zhifubao);
                setChooseView(iv_choose_yue);
                tv_choose_pay_type.setText("余额");
                payType = YXConfig.pay_BLANCE;
                paytypeStr = "余额支付";
            }
        });
    }

    void resetChooseView(ImageView iv1, ImageView iv2, ImageView iv3) {
        iv1.setImageResource(R.mipmap.no_choose);
        iv2.setImageResource(R.mipmap.no_choose);
        iv3.setImageResource(R.mipmap.no_choose);
    }

    void setChooseView(ImageView iv) {
        iv.setImageResource(R.mipmap.ok);
    }

    /**
     * 商品下单
     *
     * @param token     用户token
     * @param productID 商品ID
     * @param number    购买数量
     * @param consignee 收货地址ID
     * @param channel   支付方式
     */
    private void submitOrder(String token, String productID, int number, String consignee, String channel) {
        Map<String, String> params = new HashMap<>();
        params.put("product", productID);
        params.put("number", String.valueOf(number));
        params.put("consignee", consignee);
        params.put("channel", channel);
        if (!StringUtil.isNullOrEmpty(weight)){
            params.put("productWeight", weight.equals("")?"":Double.valueOf(weight)*number+"");
        }
        if (!StringUtil.isNullOrEmpty(color)){
            params.put("productColor", color);
        }

        final Dialog dialog = DialogUIUtils.showMdLoading(context, "订单提交中", true, true, true, true).show();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).submitOrder(token, params).enqueue(new Callback<ResponseBody>() {
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
                                                    PayTask alipay = new PayTask(YXSubmitOrderActivity.this);
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
                                    DialogUIUtils.showToast(jsonObject.getString("message"));
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
                case REQUEST_CHOOSE_ADDRESS:
                    if (resultCode == RESULT_OK) {
                        receiptAddressModel = (ReceiptAddressModel) data.getExtras().get("model");
                        assert receiptAddressModel != null;
                        tvReceiptUser.setText(String.format("收货人：%s", receiptAddressModel.getName()));
                        tvReceiptPhone.setText(receiptAddressModel.getMobile());
                        tvReceiptAddress.setText(receiptAddressModel.getProvince() + "省" + receiptAddressModel.getCity() + "市" +
                                receiptAddressModel.getCounty() + receiptAddressModel.getStreet()+receiptAddressModel.getAddress().split(receiptAddressModel.getStreet())[1]);
                        initData();
                    }
                    break;
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
