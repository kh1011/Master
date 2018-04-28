package com.yxdriver.app.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.yxdriver.app.R;
import com.yxdriver.app.model.bean.PlateColorBean;

import java.util.List;

/**
 * Created by mac on 2017/9/6.
 * 商品列表适配器
 */

public class PlateColorListAdapter extends BaseAdapter {
    private PlateColorBean model;

    private List<PlateColorBean> list;

    private Context context;

    private Hoder hoder;


    public PlateColorListAdapter(Context context, List<PlateColorBean> list) {
        this.context = context;
        this.list = list;
    }


    @Override
    public int getCount() {
        if (list != null && list.size() > 0) {
            return list.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.plate_item, null);
            hoder = new Hoder(convertView);
            convertView.setTag(hoder);
        } else {
            hoder = (Hoder) convertView.getTag();
        }
        if (list != null && list.size() > 0) {
            model = list.get(position);
            // hoder.button.setText(model.getName());
            hoder.button.setBackgroundResource(model.getRes());
        }
        return convertView;
    }

    private static class Hoder {
        private Button button;

        public Hoder(View view) {
            button = (Button) view.findViewById(R.id.plate_item_btn);
        }
    }
}
