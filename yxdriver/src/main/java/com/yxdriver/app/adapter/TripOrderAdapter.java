package com.yxdriver.app.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.yxdriver.app.model.bean.OrderModel;
import com.yxdriver.app.viewholder.IndexViewHolder;
import com.yxdriver.app.viewholder.TripOrderViewHolder;

/**
 * Created by mac on 2017/4/17.
 * 订单管理适配器
 */

public class TripOrderAdapter extends RecyclerArrayAdapter<OrderModel> {

    public TripOrderAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new TripOrderViewHolder(parent);
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
