package com.yxdriver.app.adapter;


import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.yxdriver.app.R;
import com.yxdriver.app.event.ItemGoodsOnclickListener;
import com.yxdriver.app.model.bean.GoodPruductModel;
import com.yxdriver.app.viewholder.GoodsListViewHoder;

import java.util.List;

/**
 * Created by mac on 2017/9/6.
 * 商品列表适配器
 */

public class GoodsListAdapter extends RecyclerArrayAdapter<GoodPruductModel> {
    Context context;
    ItemGoodsOnclickListener listener;

    public GoodsListAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new GoodsListViewHoder(parent, context, listener);
    }

    public TipSpanSizeLookUp obtainTipSpanSizeLookUp() {
        return new TipSpanSizeLookUp();
    }

    public class TipSpanSizeLookUp extends GridSpanSizeLookup {

        public TipSpanSizeLookUp() {
            //列数默认为2
            super(2);
        }

        @Override
        public int getSpanSize(int position) {
            return 2;
        }
    }
}