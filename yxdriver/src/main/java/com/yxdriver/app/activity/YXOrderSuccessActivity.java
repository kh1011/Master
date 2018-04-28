package com.yxdriver.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yxdriver.app.R;
import com.yxdriver.app.model.bean.OrderModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import zuo.biao.library.base.BaseActivity;

import static com.yxdriver.app.utils.StringUtil.changeObject;

/**
 * 司机发布订单单、抢单成功
 */
public class YXOrderSuccessActivity extends BaseActivity {
    private OrderModel orderModel;

    @BindView(R.id.profile_image)
    CircleImageView imageView;
    @BindView(R.id.order_success_name)
    TextView tv_Driver;
    @BindView(R.id.order_success_type)
    TextView tv_Type;
    @BindView(R.id.order_success_datetime)
    TextView tv_DateTime;
    @BindView(R.id.order_success_origin)
    TextView tv_Origin;
    @BindView(R.id.order_success_site)
    TextView tv_Site;
    @BindView(R.id.order_success_remark)
    TextView tv_Remark;
    @BindView(R.id.order_success_capacity)
    TextView tv_Capacity;


    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_order_detail);
        ButterKnife.bind(this);
        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }
    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


    public static Intent createIntent(Context context, OrderModel data) {
        return new Intent(context, YXOrderSuccessActivity.class).
                putExtra(RESULT_DATA, data);
    }

    @Override
    public void initView() {
        //
    }

    @Override
    public void initData() {
        orderModel = (OrderModel) getIntent().getSerializableExtra(RESULT_DATA);
        Glide.with(context).load(orderModel.getDriver().getHeadImage()).into(imageView);
        tv_Driver.setText(orderModel.getDriver().getAlias());
        tv_Type.setText(orderModel.getInfoType());
        tv_DateTime.setText(orderModel.getDatetime());
        tv_Origin.setText(orderModel.getOrigin().getProvince()+orderModel.getOrigin().getCity()+orderModel.getOrigin().getCounty()+orderModel.getOrigin().getAddress());
        tv_Site.setText(orderModel.getSite().getProvince()+orderModel.getSite().getCity()+orderModel.getSite().getCounty()+orderModel.getSite().getAddress());
        String note=orderModel.getNote().equals("请设置备注信息")?"":orderModel.getNote();
        tv_Remark.setText("备注信息:"+note);
        if (orderModel.getOrderType().equals("express")){
            tv_Capacity.setText("车辆载重:"+orderModel.getCapacity()+"kg");
        }else {
            tv_Capacity.setVisibility(View.GONE);
        }

    }

    @Override
    public void initEvent() {

    }

    @OnClick({R.id.order_success_map})
    void myClick(View view) {
        switch (view.getId()) {
            case R.id.order_success_map:
                // 查看地图
                toActivity(YXOrderMapActivity.createIntent(context, changeObject(orderModel)));
                break;
            default:
                break;
        }
    }
}