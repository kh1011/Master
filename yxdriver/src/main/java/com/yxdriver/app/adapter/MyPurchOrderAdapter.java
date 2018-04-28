package com.yxdriver.app.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.yxdriver.app.model.bean.OrderModel;
import com.yxdriver.app.viewholder.IndexViewHolder;
import com.yxdriver.app.viewholder.MyOrderViewHolder;

/**
 * Created by mac on 2017/4/17.
 * 我发布的订单适配器
 */

public class MyPurchOrderAdapter extends RecyclerArrayAdapter<OrderModel> {
    Context context;

    public MyPurchOrderAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyOrderViewHolder(parent, context);
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
