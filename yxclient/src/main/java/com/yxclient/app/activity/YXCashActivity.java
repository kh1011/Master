package com.yxclient.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dou361.dialogui.DialogUIUtils;
import com.yxclient.app.R;
import com.yxclient.app.app.DemoApplication;
import com.yxclient.app.http.RetrofitHttp;
import com.yxclient.app.model.bean.BankInfoBean;
import com.yxclient.app.utils.StringUtil;

import net.lemonsoft.lemonhello.LemonHello;
import net.lemonsoft.lemonhello.LemonHelloAction;
import net.lemonsoft.lemonhello.LemonHelloInfo;
import net.lemonsoft.lemonhello.LemonHelloView;
import net.lemonsoft.lemonhello.interfaces.LemonHelloActionDelegate;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;
import zuo.biao.library.base.BaseActivity;

/**
 * Created by mac on 2017/9/18.
 * 提现
 */

public class YXCashActivity extends BaseActivity {

    @BindView(R.id.bankuser_name)
    EditText ed_username;
    @BindView(R.id.bank_name)
    EditText ed_bankname;
    @BindView(R.id.bank_code)
    EditText ed_bankcode;
    @BindView(R.id.subbranch_name)
    EditText ed_subbranchname;
    @BindView(R.id.bank_address)
    EditText ed_bankaddress;
    @BindView(R.id.bank_money)
    EditText ed_bankmoney;

    @BindView(R.id.cash_poundage)
    TextView tv_poundage;
    private BankInfoBean bankinfobean;
    float money;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 判断如果登录过，则进入主页
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_activity_cash);
        ButterKnife.bind(this);
        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }
    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    public static Intent createIntent(Context context,float money) {
        return new Intent(context, YXCashActivity.class).
                putExtra(RESULT_DATA, money);
    }
    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void initView() {
        money=getIntent().getFloatExtra(RESULT_DATA,0);
        ed_bankcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 在输入数据时监听
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                int huoqu = ed_bankcode.getText().toString().length();
                if (huoqu >= 16 ||huoqu==19) {
                    String huoqucc = ed_bankcode.getText().toString();
                    bankinfobean = new BankInfoBean(huoqucc);
                    if (bankinfobean.getBankName().equals("未知")){
                        DialogUIUtils.showToast("未识别该银行，请手动输入开户银行");
                    }else{
                        ed_bankname.setEnabled(false);
                    }
                    ed_bankname.setText(bankinfobean.getBankName().equals("未知")?null:bankinfobean.getBankName());
                } else {
                    ed_bankname.setText(null);
                }
            }
        });

        ed_bankmoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (ed_bankmoney.getText().toString().length()!=0){
                    float mmm= (float) (Float.valueOf(ed_bankmoney.getText().toString())*0.002);
                    tv_poundage.setVisibility(View.VISIBLE);
                    tv_poundage.setText("手续费："+String.format("¥%s", String.format("%.2f",mmm<2?2:mmm))+"元");
                }else {
                    tv_poundage.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }

    @OnClick({R.id.cash_btn})
    void myClick(View view) {
        switch (view.getId()) {
            case R.id.cash_btn:
                final String usrname=ed_username.getText().toString();
                final String bankcode=ed_bankcode.getText().toString();
                final String bankname=ed_bankname.getText().toString();
                final String subbranchname=ed_subbranchname.getText().toString();
                final String bankaddress=ed_bankaddress.getText().toString();
                final String bankmoney=ed_bankmoney.getText().toString();
                if (StringUtil.isNullOrEmpty(usrname)){
                    DialogUIUtils.showToast("姓名不能为空");
                } else if (StringUtil.isNullOrEmpty(bankcode)){
                    DialogUIUtils.showToast("银行号不能为空");
                } else if (StringUtil.isNullOrEmpty(bankname)){
                    DialogUIUtils.showToast("开户银行不能为空");
                } else if (StringUtil.isNullOrEmpty(subbranchname)){
                    DialogUIUtils.showToast("开户支行不能为空");
                } else if (StringUtil.isNullOrEmpty(bankmoney)){
                    DialogUIUtils.showToast("提现金额不能为空");
                }else if (Double.valueOf(bankmoney)>money){
                    DialogUIUtils.showToast("超出可提现金额");
                }else{
                    LemonHello.getSuccessHello("提示", "确定提现:"+bankmoney+"元")
                            .addAction(new LemonHelloAction("确定", new LemonHelloActionDelegate() {
                                @Override
                                public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                                    setCash(DemoApplication.getInstance().getMyToken(),usrname,bankcode,bankname,subbranchname,Integer.valueOf(bankmoney));
                                    helloView.hide();
                                }
                            }))
                            .addAction(new LemonHelloAction("取消", new LemonHelloActionDelegate() {
                                @Override
                                public void onClick(LemonHelloView lemonHelloView, LemonHelloInfo lemonHelloInfo, LemonHelloAction lemonHelloAction) {
                                    //DialogUIUtils.showToast("正在开发...");
                                    lemonHelloView.hide();
                                }
                            }))
                            .show(context);
                }
                // 确认收货

            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 提现
     *
     * @param token   用户token
     */
    private void setCash(String token, String usrname,String bankcode,String bankname,String subbranchname,int bankmoney) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        JSONObject object = new JSONObject();
        object.put("cardName", usrname);
        object.put("cardNo", bankcode);
        object.put("bankType", bankname);
        object.put("bankName", subbranchname);
        //object.put("bankAddress", bankaddress);
        object.put("amount", bankmoney*100);
        RetrofitHttp.getRetrofit(builder.build()).setCash(token, StringUtil.getBody(object))
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
                                            // 提现成功
                                            DialogUIUtils.showToast(jsonObject.getString("message"));
                                            toActivity(YXBalanceCashSuccessActivity.createIntent(context));
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
