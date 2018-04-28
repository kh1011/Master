package com.yxdriver.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yxdriver.app.R;
import com.yxdriver.app.model.bean.OrderModel;
import com.yxdriver.app.utils.CodeUtil;
import com.yxdriver.app.utils.LocUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zuo.biao.library.base.BaseActivity;

/**
 * Created by mac on 2017/11/10.
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

public class TripOrderPayActivity extends BaseActivity {
    // 微信支付码
    @BindView(R.id.pay_image_wechat)
    ImageView imgWechat;
    // 支付宝支付码
    @BindView(R.id.pay_image_pay)
    ImageView imgPay;
    // 金额
    @BindView(R.id.trip_pay_amount)
    TextView tv_Amount;
    // 出行类型
    @BindView(R.id.trip_pay_type)
    TextView tv_Type;
    // 起点
    @BindView(R.id.trip_pay_origin)
    TextView tv_Origin;
    // 目的地
    @BindView(R.id.trip_pay_site)
    TextView tv_Site;
    // 里程数
    @BindView(R.id.trip_pay_distance)
    TextView tv_distance;

    // 订单对象
    private OrderModel orderModel;

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_activity_trip_pay);
        ButterKnife.bind(this);
        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }
    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


    public static Intent createIntent(Context context, OrderModel data, String dis) {
        return new Intent(context, TripOrderPayActivity.class).
                putExtra(RESULT_DATA, data).putExtra("dis", dis);
    }

    @Override
    public void initView() {
        orderModel = (OrderModel) getIntent().getSerializableExtra(RESULT_DATA);
        tv_distance.setText(getIntent().getStringExtra("dis"));
        drawView(orderModel);
    }

    private void drawView(OrderModel model) {
        imgWechat.setImageBitmap(CodeUtil.createQRImage("123"));
        imgPay.setImageBitmap(CodeUtil.createQRImage("123"));
        tv_Amount.setText(String.valueOf(Double.parseDouble(String.valueOf(model.getAmount() / 100))));
        tv_Type.setText(model.getInfoType());
        tv_Origin.setText(model.getOrigin().getAddress());
        tv_Site.setText(model.getSite().getAddress());
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }

    @OnClick({R.id.trip_pay_recode})
    void myClick(View view) {
        switch (view.getId()) {
            case R.id.trip_pay_recode:
                Intent intent = new Intent(context, RecordActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
