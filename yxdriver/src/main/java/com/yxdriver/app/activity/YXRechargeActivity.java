package com.yxdriver.app.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.dou361.dialogui.DialogUIUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yxdriver.app.R;
import com.yxdriver.app.app.DemoApplication;
import com.yxdriver.app.config.YXConfig;
import com.yxdriver.app.http.RetrofitHttp;
import com.yxdriver.app.utils.PayResult;
import com.yxdriver.app.utils.PayUtils;
import com.yxdriver.app.utils.StringUtil;
import com.yxdriver.app.utils.WxUtils;

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
 * Created by mac on 2017/9/18.
 * 账户充值
 */

public class YXRechargeActivity extends BaseActivity {
    @BindView(R.id.recharge_group)
    RadioGroup group;
    @BindView(R.id.recharge_money)
    EditText ed_Money;
    private String payType;
    IWXAPI api;
    public static final String RECEIVER_ACTION = "location_in_background";
    private static final int TEST_REQUEST_PAY_CODE = 100;
    private static final int SDK_PAY_FLAG = 1;

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
                        Toast.makeText(YXRechargeActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(YXRechargeActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
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
        // 判断如果登录过，则进入主页
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_activity_recharge);
        ButterKnife.bind(this);
        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }
    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    public static Intent createIntent(Context context) {
        return new Intent(context, YXRechargeActivity.class);
    }
    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void initView() {
        payType = YXConfig.pay_WXpay;
        //支付通知广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RECEIVER_ACTION);
        registerReceiver(payReceiver, intentFilter);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.btn_0:
                        // 微信支付
                        payType = YXConfig.pay_WXpay;
                        break;
                    case R.id.btn_1:
                        // 支付宝支付
                        payType = YXConfig.pay_Alipay;
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @OnClick({R.id.recharge_btn})
    void myClick(View view) {
        switch (view.getId()) {
            case R.id.recharge_btn:
                String money = ed_Money.getText().toString();
                if (StringUtil.isNullOrEmpty(money)) {
                    DialogUIUtils.showToast("请输入金额");
                } else {
                    // 充值
                    if (!WxUtils.getInstal(context)&&payType==YXConfig.pay_WXpay){
                        DialogUIUtils.showToast("您还未安装微信客户端");
                    }else{
                        doRecharge(DemoApplication.getInstance().getMyToken(), money, payType);
                    }
                }
                break;
            default:
                break;
        }
    }
    /**
     * 充值
     *
     * @param token   用户ID
     * @param money   金额
     * @param channel 支付方式
     */
    private void doRecharge(String token, String money, String channel) {
        JSONObject object = new JSONObject();
        object.put("amount", money+"00");
        object.put("channel", channel);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).recharge(token, StringUtil.getBody(object)).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                if (response == null) {
                    DialogUIUtils.showToast(response.message());
                } else {
                    switch (response.code()) {
                        case 200:
                            try {
                                String result = response.body().string();
                                System.out.println("获取用户信息:" + result);
                                JSONObject jsonObject = JSON.parseObject(result);
                                final String data = jsonObject.getString("data");
                                if ("success".equals(jsonObject.getString("code"))) {
                                    // 调用支付SDK
                                    switch (payType) {
                                        case YXConfig.pay_WXpay:
                                            // 微信支付
                                            JSONObject json = JSON.parseObject(data);

                                            api = WXAPIFactory.createWXAPI(YXRechargeActivity.this,null);
                                            api.registerApp(YXConfig.APP_ID);
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
                                                    PayTask alipay = new PayTask(YXRechargeActivity.this);
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
                                        default:
                                            break;
                                    }
                                } else {
                                    DialogUIUtils.showToast(jsonObject.getString("message"));
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        default:
                            try {
                                String result = response.errorBody().string();
                                JSONObject errObject = JSON.parseObject(result);
                                DialogUIUtils.showToast(errObject.getString("message"));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                    }
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
                            toActivity(YXRechargeDownActivity.createIntent(context));
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
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (payReceiver != null)
            unregisterReceiver(payReceiver);
    }

    private BroadcastReceiver payReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(RECEIVER_ACTION)) {
                if (intent.getIntExtra("resultCode",1)==0){
                    finish();
                }

            }
        }
    };
}
