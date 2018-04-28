package com.yxclient.app.viewholder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.yxclient.app.R;
import com.yxclient.app.model.bean.GoodPruductModel;

/**
 * 商城中商品列表的viewholder
 * <p>
 * Created by zhangyun on 2017/9/29 0029
 * EMail -> yun.zhang@chinacreator.com
 */
public class GoodsListViewHoder extends BaseViewHolder<GoodPruductModel> {
    private GoodPruductModel model;
    TextView title;
    ImageView imageView;

    public GoodsListViewHoder(ViewGroup parent, Context context) {
        super(parent, R.layout.goods_item);
        title = $(R.id.item_goods_title);
        imageView = $(R.id.item_goods_img);
    }

    @Override
    public void setData(GoodPruductModel data) {
        super.setData(data);
        //左边数据填充
        model = data;
        title.setText(model.getTitle());
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.default_shop)
                .error(R.drawable.default_shop)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(getContext()).load(model.getTitleImage()).apply(options).into(imageView);
    }
}
