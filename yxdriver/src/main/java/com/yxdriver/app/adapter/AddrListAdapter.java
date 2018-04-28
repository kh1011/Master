package com.yxdriver.app.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.yxdriver.app.model.bean.OldCarModel;
import com.yxdriver.app.model.bean.OriginAndSite;
import com.yxdriver.app.viewholder.AddrViewHolder;
import com.yxdriver.app.viewholder.CarListViewHoder;

/**
 * Created by mac on 2017/4/17.
 * order adapter
 */

public class AddrListAdapter extends RecyclerArrayAdapter<OriginAndSite> {
    Context context;

    public AddrListAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new AddrViewHolder(parent);
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
