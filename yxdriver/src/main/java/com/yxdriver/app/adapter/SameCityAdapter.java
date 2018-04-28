package com.yxdriver.app.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.yxdriver.app.model.bean.OldCarModel;
import com.yxdriver.app.model.bean.SameCityModel;
import com.yxdriver.app.viewholder.SameCityViewHolder;

/**
 * 功能：
 * Created by yun.zhang on 2017/9/26 0026.
 * email:zy19930321@163.com
 */
public class SameCityAdapter extends RecyclerArrayAdapter<OldCarModel> {
    Context context;

    public SameCityAdapter(Context context) {
        super(context);
        this.context=context;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new SameCityViewHolder(parent);
    }

    public TipSpanSizeLookUp obtainTipSpanSizeLookUp() {
        return new TipSpanSizeLookUp();
    }

    public class TipSpanSizeLookUp extends RecyclerArrayAdapter.GridSpanSizeLookup {

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
