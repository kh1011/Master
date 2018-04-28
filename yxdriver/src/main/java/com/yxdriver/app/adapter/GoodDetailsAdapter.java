package com.yxdriver.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yxdriver.app.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.yxdriver.app.R.id.tv;

/**
 * 功能：商品详细信息的适配器
 * Created by yun.zhang on 2017/9/29 0029.
 * email:zy19930321@163.com
 */
public class GoodDetailsAdapter extends BaseAdapter {
    List<String> images;
    Context context;
    String desc;

    public GoodDetailsAdapter(Context context, List<String> images) {
        this.images = images;
        this.context = context;
    }

    @Override
    public int getCount() {
        return images == null ? 0 : images.size();
    }

    @Override
    public Object getItem(int position) {
        return images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_good_detail_image, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(R.id.tag_one,viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag(R.id.tag_one);
        }
        Glide.with(context).load(images.get(position)).into(viewHolder.ivImage);
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.iv_image)
        ImageView ivImage;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
