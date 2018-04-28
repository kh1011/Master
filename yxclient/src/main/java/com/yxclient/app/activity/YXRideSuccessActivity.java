package com.yxclient.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yxclient.app.R;
import com.yxclient.app.model.bean.OrderModel;

import java.text.MessageFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import zuo.biao.library.base.BaseActivity;

/**
 * Created by mac on 2017/8/21.
 * 货运下单成功展示
 */

public class YXRideSuccessActivity extends BaseActivity {
    @BindView(R.id.rs_origin)
    TextView tv_Origin;
    @BindView(R.id.rs_site)
    TextView tv_Site;
    @BindView(R.id.rs_datetime)
    TextView tv_Datetime;
    @BindView(R.id.rs_no)
    TextView tv_No;
    @BindView(R.id.rs_discount)
    TextView tv_Discount;
    @BindView(R.id.rs_money)
    TextView tv_Money;
    @BindView(R.id.moneytype)
    TextView tv_Moneytype;
    @BindView(R.id.discount)
    LinearLayout tv_discount;

    OrderModel orderModel;// 订单对象
    String distance;
    String flog;
    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_ride_success);
        ButterKnife.bind(this);
        initView();
        initData();
        initEvent();
    }


    public static Intent createIntent(Context context, OrderModel data, String distance,String flog) {
        return new Intent(context, YXRideSuccessActivity.class).
                putExtra(RESULT_DATA, data).putExtra("distance", distance).putExtra("flog",flog);
    }

    @Override
    public void initView() {
        orderModel = (OrderModel) getIntent().getSerializableExtra(RESULT_DATA);
        distance = getIntent().getStringExtra("distance");
        flog=getIntent().getStringExtra("flog");
        drawView(orderModel);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }

    /**
     * 绘制UI
     *
     * @param object 订单
     */
    private void drawView(OrderModel object) {
        tv_Origin.setText(object.getOrigin().getAddress());
        tv_Site.setText(object.getSite().getAddress());
        tv_Datetime.setText(object.getDatetime());
        tv_No.setText(object.getNo());
        tv_Discount.setText(String.format("%s公里", distance));
        if (flog.equals("1")){
            tv_Money.setText(MessageFormat.format("¥ {0}", object.getAmount() / 100.0));
            tv_Moneytype.setText("车费");
        }else {
            tv_Money.setText(MessageFormat.format("¥ {0}", object.getAmount() / 100.0));
            tv_Moneytype.setText("运输费");
        }

    }
}
