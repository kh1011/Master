package com.yxdriver.app.viewholder;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dou361.dialogui.DialogUIUtils;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.yxdriver.app.R;
import com.yxdriver.app.activity.YXOrderDetailActivity;
import com.yxdriver.app.app.DemoApplication;
import com.yxdriver.app.fragment.IndexDoOrderFragment;
import com.yxdriver.app.fragment.IndexListFragment;
import com.yxdriver.app.http.RetrofitHttp;
import com.yxdriver.app.model.bean.CustomerModel;
import com.yxdriver.app.model.bean.OrderModel;
import com.yxdriver.app.model.bean.PointModel;
import com.yxdriver.app.model.bean.UserModel;
import com.yxdriver.app.utils.OrderUtils;
import com.yxdriver.app.utils.StringUtil;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mac on 2017/4/17.
 * 我发布的订单
 */

public class MyOrderViewHolder extends BaseViewHolder<OrderModel> {
    private Context context;
    private OrderModel data;

    private TextView status;
    private TextView dateTime;
    private TextView origin;
    private TextView site;
    private TextView type;
    private static IndexDoOrderFragment object = new IndexDoOrderFragment();


    public MyOrderViewHolder(ViewGroup parent, Context context) {
        super(parent, R.layout.item_myorder);
        this.context = context;
        status = $(R.id.item_myorder_status);
        dateTime = $(R.id.item_myorder_datetime);
        origin = $(R.id.item_myorder_origin);
        site = $(R.id.item_myorder_site);
        type = $(R.id.item_myorder_typename);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void setData(OrderModel model) {
        super.setData(model);
        this.data = model;
        // 司机
        UserModel user = data.getDriver();
        // 出发地点
        PointModel originModel = data.getOrigin();
        // 目的地
        PointModel siteModel = data.getSite();
        if (StringUtil.isNullOrEmpty(user.getName())) {
            user.setName("未知用户");
        }
        dateTime.setText(data.getDatetime());
        origin.setText(originModel.getProvince()+originModel.getCity()+originModel.getCounty()+originModel.getAddress());
        site.setText(siteModel.getProvince()+siteModel.getCity()+siteModel.getCounty()+siteModel.getAddress());
        type.setText(data.getInfoType());
        status.setText(OrderUtils.getStatusStr(data.getStatus()));
    }
}
