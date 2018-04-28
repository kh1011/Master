package com.yxclient.app.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONObject;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.yxclient.app.model.bean.OrderModel;
import com.yxclient.app.viewholder.IndexViewHolder;

/**
 * Created by mac on 2017/4/17.
 * order adapter
 */

public class IndexOrderAdapter extends RecyclerArrayAdapter<OrderModel> {

    public IndexOrderAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new IndexViewHolder(parent);
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
