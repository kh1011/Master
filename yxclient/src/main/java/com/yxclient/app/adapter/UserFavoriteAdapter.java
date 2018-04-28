package com.yxclient.app.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.yxclient.app.model.bean.GoodPruductModel;
import com.yxclient.app.viewholder.UserFavoriteViewHolder;

/**
 * 宝贝收藏适配器
 *
 * Created by zhangyun on 2017/10/9 0009
 * EMail -> yun.zhang@chinacreator.com
 */
public class UserFavoriteAdapter extends RecyclerArrayAdapter<GoodPruductModel> {
    Context context;

    public UserFavoriteAdapter(Context context) {
        super(context);
        this.context=context;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserFavoriteViewHolder( parent,context);
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

