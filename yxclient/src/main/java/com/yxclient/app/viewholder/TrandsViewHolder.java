package com.yxclient.app.viewholder;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.yxclient.app.R;
import com.yxclient.app.model.bean.newOrderModel;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mac on 2017/4/17.
 * order
 */

public class TrandsViewHolder extends BaseViewHolder<newOrderModel> {
    private newOrderModel data;
    private TextView drivername;
    private TextView type;
    private TextView datetime;
    private TextView oringin;
    private TextView site;
    private TextView cartype;
    private TextView weight;
    private CircleImageView imageView;

    public TrandsViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_trands);
        imageView = $(R.id.profile_image);
        drivername = $(R.id.item_trands_name);
        type = $(R.id.item_trands_type);
        datetime = $(R.id.item_trands_datetime);
        oringin = $(R.id.item_trands_origin);
        site = $(R.id.item_trands_site);
        cartype = $(R.id.item_trands_car);
        weight=$(R.id.item_trands_weight);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void setData(newOrderModel model) {
        super.setData(model);
        this.data = model;
        Glide.with(getContext()).load(data.getDriver().getHeadImage()).into(imageView);
        drivername.setText(data.getDriver().getAlias());
        type.setText(data.getInfoType());
        datetime.setText(data.getDatetime());
        oringin.setText(data.getOrigin().getProvince()+data.getOrigin().getCity()+data.getOrigin().getCounty()+data.getOrigin().getAddress());
        site.setText(data.getSite().getProvince()+data.getSite().getCity()+data.getSite().getCounty()+data.getSite().getAddress());
        cartype.setText(data.getCar().getType());
        if (!model.getInfoType().equals("快递小件")){
            weight.setVisibility(View.VISIBLE);
            weight.setText("余重:"+model.getCapacity()+"kg");
        }
    }
}
