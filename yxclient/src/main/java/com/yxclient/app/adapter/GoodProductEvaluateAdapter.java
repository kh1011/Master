package com.yxclient.app.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.yxclient.app.model.bean.GoodEvaluateModel;
import com.yxclient.app.viewholder.GoodProductEvaluateViewHolder;


/**
 * 功能：商品信息的评论适配器
 * Created by yun.zhang on 2017/9/29 0029.
 * email:zy19930321@163.com
 */
public class GoodProductEvaluateAdapter extends RecyclerArrayAdapter<GoodEvaluateModel> {
    Context context;

    public GoodProductEvaluateAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new GoodProductEvaluateViewHolder(parent, context);
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