package com.yxclient.app.viewholder;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.yxclient.app.R;
import com.yxclient.app.model.bean.OriginAndSite;

/**
 * Created by mac on 2017/4/17.
 * 检索地址列表
 */

public class AddrViewHolder extends BaseViewHolder<OriginAndSite> {
    private OriginAndSite data;

    private TextView name;
    private TextView address;

    public AddrViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_layout);
        name = $(R.id.poi_field_id);
        address = $(R.id.poi_value_id);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void setData(OriginAndSite model) {
        super.setData(model);
        this.data = model;
        name.setText(data.getAddress());
        address.setText(data.getName());
    }
}
