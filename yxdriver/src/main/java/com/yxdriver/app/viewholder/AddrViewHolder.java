package com.yxdriver.app.viewholder;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.yxdriver.app.R;
import com.yxdriver.app.fragment.IndexDoOrderFragment;
import com.yxdriver.app.model.bean.OrderModel;
import com.yxdriver.app.model.bean.OriginAndSite;
import com.yxdriver.app.model.bean.PointModel;
import com.yxdriver.app.model.bean.UserModel;
import com.yxdriver.app.utils.StringUtil;

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
