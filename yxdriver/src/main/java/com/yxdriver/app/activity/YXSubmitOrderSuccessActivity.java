package com.yxdriver.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dou361.dialogui.DialogUIUtils;
import com.yxdriver.app.R;
import com.yxdriver.app.app.DemoApplication;
import com.yxdriver.app.http.RetrofitHttp;
import com.yxdriver.app.model.bean.GoodsOrderModel;

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
 * 订单提交成功
 * <p>
 * Created by zhangyun on 2017/10/9 0009
 * EMail -> yun.zhang@chinacreator.com
 */
public class YXSubmitOrderSuccessActivity extends BaseActivity {

    @BindView(R.id.success_goods_recepter)
    TextView tv_Recepter;

    @BindView(R.id.success_goods_addr)
    TextView tv_Addr;

    @BindView(R.id.success_goods_money)
    TextView tv_Money;

    private GoodsOrderModel model;

    @Override
    public Activity getActivity() {
        return this;
    }

    public static Intent createIntent(Context context, String data) {
        return new Intent(context, YXSubmitOrderSuccessActivity.class).putExtra(RESULT_DATA, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_submin_order_success);
        ButterKnife.bind(this);
        initView();
        initData();
        initEvent();
    }

    @Override
    public void initView() {
        getOrderInfo(DemoApplication.getInstance().getMyToken(), getIntent().getStringExtra(RESULT_DATA));
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }

    /**
     * 获取订单信息
     *
     * @param token user token
     * @param no    订单号
     */
    private void getOrderInfo(String token, String no) {
        final Dialog dialog = DialogUIUtils.showMdLoading(context, "数据加载中...", true, true, true, true).show();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).queryGoodsOrder(token, no).enqueue(new Callback<ResponseBody>() {
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
                                JSONObject jsonObject = JSON.parseObject(result);
                                if ("success".equals(jsonObject.getString("code"))) {
                                    model = JSON.parseObject(jsonObject.getString("data"), GoodsOrderModel.class);
                                    drawView(model);
                                } else {
                                    DialogUIUtils.showToast(jsonObject.getString("message"));
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
                DialogUIUtils.dismiss(dialog);
                DialogUIUtils.showToast(t.getMessage());
            }
        });
    }

    /**
     * 绘制页面
     *
     * @param orderModel 商品订单对象
     */
    private void drawView(GoodsOrderModel orderModel) {
        tv_Recepter.setText(orderModel.getName());
        tv_Addr.setText(orderModel.getAddress());
        tv_Money.setText(String.format("¥ %s", orderModel.getAmount() / 100.0));
    }

    @OnClick({R.id.success_goods_btn})
    void myClick(View view) {
        switch (view.getId()) {
            case R.id.success_goods_btn:
                // 查看订单
                toActivity(YXShoppingOrderInfoActivity.createIntent(context, model));
                break;
            default:
                break;
        }
    }
}
