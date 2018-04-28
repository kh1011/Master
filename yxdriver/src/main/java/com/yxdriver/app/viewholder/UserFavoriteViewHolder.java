package com.yxdriver.app.viewholder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dou361.dialogui.DialogUIUtils;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.yxdriver.app.R;
import com.yxdriver.app.model.bean.GoodPruductModel;

import butterknife.BindView;

/**
 * 宝贝收藏viewholder
 * <p>
 * Created by zhangyun on 2017/10/9 0009
 * EMail -> yun.zhang@chinacreator.com
 */
public class UserFavoriteViewHolder extends BaseViewHolder<GoodPruductModel> implements AdapterView.OnItemClickListener {
    Context context;
    ImageView ivTitleImage;
    TextView tvTitle;
    TextView tvPrice;

    public UserFavoriteViewHolder(ViewGroup parent, Context context) {
        super(parent, R.layout.item_user_favorite);
        this.context = context;
        ivTitleImage = $(R.id.iv_title_image);
        tvTitle = $(R.id.tv_title);
        tvPrice = $(R.id.tv_price);
    }

    @Override
    public void setData(GoodPruductModel data) {
        super.setData(data);
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.default_shop)
                .error(R.drawable.default_shop)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(context).load(data.getTitleImage()).apply(options).into(ivTitleImage);
        tvTitle.setText("【" + data.getTag() + "】 " + data.getTitle());
        tvPrice.setText(String.format("￥%s", data.getPrice() / 100.0));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DialogUIUtils.showToast(view.getTag().toString());
    }
}
