package com.yxclient.app.viewholder;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.yxclient.app.R;
import com.yxclient.app.model.bean.OrderModel;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mac on 2017/4/17.
 * order
 */

public class IndexViewHolder extends BaseViewHolder<OrderModel> {
    private OrderModel data;
    private CircleImageView imageView; // 司机头像
    private TextView driverName;       // 司机姓名
    private TextView carName;          // 车辆名称
    private TextView type;             // 出行类型
    private TextView seats;            // 剩余座位数
    private TextView datetime;         // 出发时间
    private TextView origin;           // 出发地点
    private TextView site;             // 目的地
    private TextView yz;

    public IndexViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_index);
        imageView = $(R.id.profile_image);
        driverName = $(R.id.index_item_dirver);
        carName = $(R.id.index_item_carname);
        type = $(R.id.index_item_type);
        seats = $(R.id.index_item_seats);
        origin = $(R.id.index_item_origin);
        site = $(R.id.index_item_site);
        datetime = $(R.id.index_item_datetime);
        yz=$(R.id.yz);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void setData(OrderModel model) {
        super.setData(model);
        this.data = model;
        //
        Glide.with(getContext()).load(data.getDriver().getHeadImage()).into(imageView);
        driverName.setText(data.getDriver().getAlias());
        carName.setText(data.getCar().getBrand());
        type.setText(data.getInfoType());

        datetime.setText(data.getDatetime());
        origin.setText(data.getOrigin().getProvince()+data.getOrigin().getCity()+data.getOrigin().getCounty()+data.getOrigin().getAddress());
        site.setText(data.getSite().getProvince()+data.getSite().getCity()+data.getSite().getCounty()+data.getSite().getAddress());
        if (model.getInfoType().equals("快递小件")){
            yz.setVisibility(View.GONE);
            seats.setVisibility(View.GONE);
        }else if (model.getOrderType().equals("express")){
            yz.setText("余重");
            seats.setText(model.getCapacity()+"kg");
        }else {
            yz.setText("余座");
            seats.setText(String.valueOf(model.getSeats()));
        }
    }
}
