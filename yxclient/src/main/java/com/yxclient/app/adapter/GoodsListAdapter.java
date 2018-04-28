package com.yxclient.app.adapter;


import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.yxclient.app.model.bean.GoodPruductModel;
import com.yxclient.app.viewholder.GoodsListViewHoder;

/**
 * Created by mac on 2017/9/6.
 * 商品列表适配器
 */

public class GoodsListAdapter extends RecyclerArrayAdapter<GoodPruductModel> {
    Context context;

    public GoodsListAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new GoodsListViewHoder(parent, context);
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