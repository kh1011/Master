package com.yxdriver.app.viewholder;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.yxdriver.app.R;

import de.hdodenhof.circleimageview.CircleImageView;
import zuo.biao.library.util.JSON;

/**
 * Created by mac on 2017/4/17.
 * order
 */

public class TrandsViewHolder extends BaseViewHolder<JSONObject> {
    private Context context;
    private JSONObject data;
    private TextView drivername;
    private TextView type;
    private TextView datetime;
    private TextView oringin;
    private TextView site;
    private TextView cartype;
    private CircleImageView imageView;

    public TrandsViewHolder(ViewGroup parent, Context context) {
        super(parent, R.layout.item_trands);
        this.context = context;
        imageView=$(R.id.profile_image);
        drivername=$(R.id.item_trands_name);
        type=$(R.id.item_trands_type);
        datetime=$(R.id.item_trands_datetime);
        oringin=$(R.id.item_trands_origin);
        site=$(R.id.item_trands_site);
        cartype=$(R.id.item_trands_car);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void setData(JSONObject model) {
        super.setData(model);
        this.data=model;
        JSONObject driverObject= JSON.parseObject(data.getString("driver"));
        JSONObject originObject= JSON.parseObject(data.getString("origin"));
        JSONObject siteObject= JSON.parseObject(data.getString("site"));
        JSONObject carObject= JSON.parseObject(data.getString("car"));
        drivername.setText(driverObject.getString("name"));
        type.setText(data.getString("infoType"));
        datetime.setText(data.getString("datetime"));
        oringin.setText(originObject.getString("address"));
        site.setText(siteObject.getString("address"));
        cartype.setText(carObject.getString("brand"));
    }
}
