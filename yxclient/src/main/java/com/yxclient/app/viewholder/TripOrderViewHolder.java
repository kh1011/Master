package com.yxclient.app.viewholder;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.yxclient.app.R;
import com.yxclient.app.model.bean.newOrderModel;
import com.yxclient.app.utils.OrderUtils;

/**
 * Created by mac on 2017/4/17.
 * 出行、货运订单
 */

public class TripOrderViewHolder extends BaseViewHolder<newOrderModel> {
    private newOrderModel data;

    private TextView status;
    private TextView dateTime;
    private TextView origin;
    private TextView site;
    private TextView type;


    public TripOrderViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_trip_ordermanager);
        status = $(R.id.item_trip_status);
        dateTime = $(R.id.item_trip_datetime);
        origin = $(R.id.item_trip_origin);
        site = $(R.id.item_trip_site);
        type = $(R.id.item_trip_typename);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void setData(newOrderModel model) {
        super.setData(model);
        this.data = model;
        // 出发地点

            //newOrderModel.OriginBean originModel = data.getOrigin();
            //newOrderModel.SiteBean siteModel = data.getSite();
            dateTime.setText(data.getDatetime());
            origin.setText(data.getOrigin()==null?"": data.getOrigin().getProvince()+data.getOrigin().getCity()+data.getOrigin().getCounty()+data.getOrigin().getAddress());
            site.setText(data.getSite()==null?"": data.getSite().getProvince()+data.getSite().getCity()+data.getSite().getCounty()+data.getSite().getAddress());
            type.setText(data.getInfoType());
            status.setText(OrderUtils.getStatusStr(data.getStatus()));
    }
}
