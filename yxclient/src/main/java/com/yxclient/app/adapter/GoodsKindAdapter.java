package com.yxclient.app.adapter;

import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.yxclient.app.R;

@SuppressWarnings("rawtypes")
public class GoodsKindAdapter extends ListAdapter {

    private Activity activity;
    private List<String> demodels;
    private LayoutInflater inflater;
    private int pos;
    private int lastPosition = -1;                                //记录上一次选中的图片位置，-1表示未选中
    private Vector<Boolean> vector = new Vector<Boolean>();       // 定义一个向量作为选中与否容器

    public GoodsKindAdapter(Activity activity, List<String> demodels) {
        super(activity);
        this.activity = activity;
        this.demodels = demodels;
        inflater = LayoutInflater.from(activity);

        for (int i = 0; i < demodels.size(); i++) {
            vector.add(false);
        }
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return demodels.size();
    }

    @Override
    protected ViewHolder createViewHolder(View root) {
        // TODO Auto-generated method stub
        demodelHolder hold = new demodelHolder();
        hold.tv_demodel = (TextView) root.findViewById(R.id.tv_demodel);
        return hold;
    }

    @Override
    protected void fillView(View root, Object item, ViewHolder holder,
                            int position) {
        // TODO Auto-generated method stub
        final demodelHolder hold = (demodelHolder) holder;
        hold.demodel = demodels.get(position);
        if (!"".equals(demodels.get(position))) {
            hold.tv_demodel.setText(demodels.get(position));
        }
        if (vector.elementAt(position)) {
            hold.tv_demodel.setTextColor(ContextCompat.getColor(activity, R.color.kinds_selected));
            hold.tv_demodel.setBackgroundResource(R.drawable.bg_selected);
            // hold.tv_demodel.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.bg_selected));
        } else {
            hold.tv_demodel.setTextColor(ContextCompat.getColor(activity, R.color.kinds_default));
            hold.tv_demodel.setBackgroundResource(R.drawable.bg_default);
            // hold.tv_demodel.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.bg_default));
        }
    }

    @Override
    protected int getItemViewId() {
        // TODO Auto-generated method stub
        return R.layout.item_goods_kind;
    }

    private class demodelHolder extends ViewHolder {
        private TextView tv_demodel;
        private String demodel;
    }

    /**
     * 修改选中时的状态
     *
     * @param position
     */
    public void changeState(int position) {
        if (lastPosition != -1)
            vector.setElementAt(false, lastPosition);                   //取消上一次的选中状态
        vector.setElementAt(!vector.elementAt(position), position);     //直接取反即可
        lastPosition = position;                                        //记录本次选中的位置
        notifyDataSetChanged();                                         //通知适配器进行更新
    }
}
