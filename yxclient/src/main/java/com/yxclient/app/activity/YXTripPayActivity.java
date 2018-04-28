package com.yxclient.app.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.dou361.dialogui.DialogUIUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yxclient.app.R;
import com.yxclient.app.app.DemoApplication;
import com.yxclient.app.config.YXConfig;
import com.yxclient.app.http.RetrofitHttp;
import com.yxclient.app.model.bean.MsgPayModel;
import com.yxclient.app.model.bean.TripPayModel;
import com.yxclient.app.model.bean.newOrderModel;
import com.yxclient.app.utils.PayResult;
import com.yxclient.app.utils.StringUtil;
import com.yxclient.app.utils.WxUtils;

import java.io.IOException;
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
 * Created by mac on 2017/11/9.
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
// 出行类订单支付
public class YXTripPayActivity extends BaseActivity {
    @BindView(R.id.radioGroup)
    RadioGroup group;
    // 应付金额
    @BindView(R.id.trip_pay_amount)
    TextView tv_Amount;
    // 出行类型
    @BindView(R.id.trip_pay_type)
    TextView tv_Type;
    // 出发地点
    @BindView(R.id.trip_pay_origin)
    TextView tv_Origin;
    // 目的地
    @BindView(R.id.trip_pay_site)
    TextView tv_Site;

    private String payType;
    private newOrderModel orderModel;
    private static final int TEST_REQUEST_PAY_CODE = 100;
    public static boolean isForeground = false;

    private static final int SDK_PAY_FLAG = 1;
    private String InfoUUID;
    //微信支付
    private IWXAPI api;
    @Override
    public Activity getActivity() {
        return this;
    }

    public static Intent createIntent(Context context,String InfoUUID) {
        return new Intent(context, YXTripPayActivity.class).putExtra(RESULT_CODE,InfoUUID);
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
                        Toast.makeText(YXTripPayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(YXTripPayActivity.this, payResult.getMemo(), Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.yx_activity_trip_pay);
        ButterKnife.bind(this);
        api= WXAPIFactory.createWXAPI(YXTripPayActivity.this,YXConfig.APP_ID,true);
        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }

    @Override
    public void initView() {
       // orderModel = (OrderModel) getIntent().getSerializableExtra(RESULT_DATA);
        // 默认支付方式为微信支付
        payType = YXConfig.pay_WXpay;

        InfoUUID=getIntent().getStringExtra(RESULT_CODE);
    }

    @Override
    public void initData() {
        getOrderInfo(DemoApplication.getInstance().getMyToken());
    }

    /**
     * 获取订单详情
     *
     * @param token   用户Token
     */
    private void getOrderInfo(String token) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).getTripOrder(token,InfoUUID)
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
                                        System.out.println("支付数据：" + result);
                                        JSONObject jsonObject = JSON.parseObject(result);
                                        if (jsonObject.getString("code").equals("success")) {
                                            orderModel = JSON.parseObject(jsonObject.getString("data"), newOrderModel.class);
                                            drawView(orderModel);
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

    private void drawView(newOrderModel model) {
        tv_Amount.setText(String.valueOf(model.getAmount() / 100.00));
        tv_Type.setText(model.getInfoType());
        tv_Origin.setText(model.getOrigin().getAddress());
        tv_Site.setText(model.getSite().getAddress());
    }

    @Override
    public void initEvent() {
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //获取变更后的选中项的ID
                int radioButtonId = group.getCheckedRadioButtonId();
                switch (radioButtonId) {
                    case R.id.rb1:
                        // 微信支付
                        payType = YXConfig.pay_WXpay;
                        break;
                    case R.id.rb2:
                        // 支付宝支付
                        payType = YXConfig.pay_Alipay;
                        break;
                    case R.id.rb3:
                        // 现金支付
                        payType = YXConfig.pay_MONEY;
                        break;
                    case R.id.rb4:
                        // 余额支付
                        payType = YXConfig.pay_BLANCE;
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @OnClick({R.id.pay_btn})
    void myCilck(View view) {
        switch (view.getId()) {
            case R.id.pay_btn:
                // 支付订单;;;;
                int amount ;
                amount=orderModel.getAmount();
                /*if (orderModel.getOrderType().equals("express")){

                }else {
                    amount=orderModel.getAmount() / 100;
                }*/
                if (!WxUtils.getInstal(context)&&payType==YXConfig.pay_WXpay){
                DialogUIUtils.showToast("您还未安装微信客户端");
                }else{
                    doPay(DemoApplication.getInstance().getMyToken(), new TripPayModel("",orderModel.getExtraInfoUUID(),payType, amount));
                }
                break;
        }
    }

    private void doPay(String token, TripPayModel model) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).tripPay(token, StringUtil.getBody(zuo.biao.library.util.JSON.parseObject(model)))
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
                                        System.out.println("支付数据：" + result);
                                        final JSONObject jsonObject = JSON.parseObject(result);
                                        JSONObject data_ = JSON.parseObject(jsonObject.getString("data"));
                                        final String data=data_.getString("data");

                                        if (jsonObject.getString("code").equals("success")) {
                                            // 支付成功
                                            switch (payType) {
                                                case YXConfig.pay_WXpay:
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
                                                case YXConfig.pay_Alipay:
                                                    // 支付宝支付
                                                    //PayUtils.doZfbPay(context, TEST_REQUEST_PAY_CODE, jsonObject.getString("data"));
                                                    Runnable payRunnable = new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            PayTask alipay = new PayTask(YXTripPayActivity.this);
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
                                                case YXConfig.pay_BLANCE:
                                                    finish();
                                                default:
                                                    break;
                                            }
                                        } else {
                                            DialogUIUtils.showToast(jsonObject.getString("message"));
                                        }
                                    }
                                    break;
                                default:
                                    try {
                                        String result = response.errorBody().string();
                                        DialogUIUtils.showToast(JSON.parseObject(result).getString("message") );
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case TEST_REQUEST_PAY_CODE:
                    String toastHint = "支付模式:%s,响应码:%s,结果描述:%s";
                    String payModeDesc = "未知";
                    String payRespCode = "unKnow";
                    if (data != null) {
                        int payMode = data.getIntExtra(CommonPayConfig.INTENT_KEY_CUR_PAY_MODE, CommonPayConfig.PAY_MODE_WX);
                        payModeDesc = payMode == CommonPayConfig.PAY_MODE_ALIPAY ? "[支付宝]" : "[微信]";
                        payRespCode = data.getStringExtra(CommonPayConfig.INTENT_KEY_REAL_PAY_RESULT_STATUS_CODE);
                    }
                    String resultDesc = "支付失败";
                    switch (resultCode) {
                        case CommonPayConfig.REQ_PAY_RESULT_CODE_OK:
                            resultDesc = "支付成功";
                            DialogUIUtils.showToast(resultDesc);
                            /*toActivity(YXRechargeDownActivity.createIntent(context));
                            finish();*/
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
            }
        }
    }

    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.yxclient.app.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                    if (!StringUtil.isNullOrEmpty(extras)) {
                        showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                    }
                    setUI(extras);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setUI(String extras) {
        JSONObject object = JSON.parseObject(extras);
        MsgPayModel payModel = JSON.parseObject(object.getString("msgInfo"), MsgPayModel.class);
        tv_Amount.setText(String.valueOf(payModel.getAmount() / 100.00));
    }

    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
    }

    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }

}
