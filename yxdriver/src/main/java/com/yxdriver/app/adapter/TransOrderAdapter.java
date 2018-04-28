package com.yxdriver.app.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONObject;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.yxdriver.app.viewholder.TrandsViewHolder;

/**
 * Created by mac on 2017/4/17.
 * order adapter
 */

public class TransOrderAdapter extends RecyclerArrayAdapter<JSONObject> {
    Context context;

    public TransOrderAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new TrandsViewHolder(parent, context);
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
