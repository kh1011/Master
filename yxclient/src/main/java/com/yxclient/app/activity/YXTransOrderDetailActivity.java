package com.yxclient.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dou361.dialogui.DialogUIUtils;
import com.yxclient.app.R;
import com.yxclient.app.app.DemoApplication;
import com.yxclient.app.model.bean.OrderModel;
import com.yxclient.app.model.bean.PointModel;
import com.yxclient.app.model.bean.newOrderModel;
import com.yxclient.app.utils.PhoneUtils;
import com.yxclient.app.utils.StringUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import zuo.biao.library.base.BaseActivity;

import static com.yxclient.app.utils.StringUtil.changeObject;

/**
 * Created by mac on 2017/8/22.
 * 货运类订单详情（推荐订单）
 */

public class YXTransOrderDetailActivity extends BaseActivity {

    private newOrderModel orderModel;
    @BindView(R.id.profile_image)
    CircleImageView imageView;
    @BindView(R.id.index_item_dirver)
    TextView tv_driver;
    @BindView(R.id.index_item_carname)
    TextView tv_carName;
    @BindView(R.id.index_item_type)
    TextView tv_type;
    @BindView(R.id.index_item_seats)
    TextView tv_seats;
    @BindView(R.id.index_item_datetime)
    TextView tv_datetime;
    @BindView(R.id.index_item_origin)
    TextView tv_origin;
    @BindView(R.id.index_item_site)
    TextView tv_site;
    @BindView(R.id.index_item_txt)
    TextView tv_txt;
    // 途径地点
    @BindView(R.id.index_item_path)
    TextView tv_path;
    @BindView(R.id.order_detail_path)
    LinearLayout li_path;
    // 备注信息
    @BindView(R.id.order_detail_renark)
    TextView tv_remark;

    //车辆载重
    @BindView(R.id.index_item_load)
    TextView tv_load;
    @BindView(R.id.order_detail_load)
    LinearLayout re_load;


    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_ride_order_detail);
        ButterKnife.bind(this);
        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }
    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


    public static Intent createIntent(Context context, newOrderModel data) {
        return new Intent(context, YXTransOrderDetailActivity.class).
                putExtra(RESULT_DATA, data);
    }

    @Override
    public void initView() {
        tv_txt.setVisibility(View.GONE);
        tv_seats.setVisibility(View.GONE);
        li_path.setVisibility(View.GONE);
    }

    @Override
    public void initData() {
        orderModel = (newOrderModel) getIntent().getSerializableExtra(RESULT_DATA);
        if (orderModel.getInfoType().equals("快递小件")){
            re_load.setVisibility(View.GONE);
        }
        if (orderModel != null) {
            Glide.with(context).load(orderModel.getDriver().getHeadImage()).into(imageView);
            tv_driver.setText(orderModel.getDriver().getAlias());
            tv_carName.setText(orderModel.getCar().getBrand());
            tv_type.setText(orderModel.getInfoType());
            tv_seats.setText(String.valueOf(orderModel.getCapacity()));
            tv_datetime.setText(orderModel.getDatetime());
            tv_origin.setText(orderModel.getOrigin().getProvince()+orderModel.getOrigin().getCity()+orderModel.getOrigin().getCounty()+orderModel.getOrigin().getAddress());
            tv_site.setText(orderModel.getSite().getProvince()+orderModel.getSite().getCity()+orderModel.getSite().getCounty()+orderModel.getSite().getAddress());
            tv_remark.setText(orderModel.getNote().equals("请设置备注信息")?"":orderModel.getNote().equals("remark")?" ":orderModel.getNote());
            tv_load.setText(orderModel.getCapacity()+"kg");

        }
    }

    @Override
    public void initEvent() {

    }

    @OnClick({R.id.order_detail_btn, R.id.icon_phone, R.id.order_detail_map})
    void myClick(View view) {
        switch (view.getId()) {
            case R.id.order_detail_btn:
                // 用车
                if (!validateUser()) {
                    toActivity(YXTransOrderSnatchActivity.createIntent(context, orderModel));
                } else {
                    gotoLogin();
                }
                break;
            case R.id.icon_phone:
                // 拨打司机电话联系
                PhoneUtils.call(YXTransOrderDetailActivity.this, orderModel.getDriver().getMobile());
                break;
            case R.id.order_detail_map:
                // 查看地图
                toActivity(YXOrderMapActivity.createIntent(context, orderModel));
                break;
            default:
                break;
        }
    }

    private boolean validateUser() {
        return StringUtil.isNullOrEmpty(DemoApplication.getInstance().getMyToken());
    }

    private void gotoLogin() {
        toActivity(YXLoginActivity.createIntent(context));
    }
}
