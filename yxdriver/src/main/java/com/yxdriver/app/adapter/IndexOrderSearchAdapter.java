package com.yxdriver.app.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.yxdriver.app.model.bean.OrderModel;
import com.yxdriver.app.model.bean.newOrderModel;
import com.yxdriver.app.viewholder.IndexSearchViewHolder;
import com.yxdriver.app.viewholder.IndexViewHolder;

/**
 * Created by mac on 2017/4/17.
 * order adapter
 */

public class IndexOrderSearchAdapter extends RecyclerArrayAdapter<newOrderModel> {
    Context context;

    public IndexOrderSearchAdapter(Context context) {
        super(context);
        this.context=context;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new IndexSearchViewHolder(parent,context);
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
