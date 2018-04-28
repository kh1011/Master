package com.yxclient.app.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yxclient.app.R;

import java.util.List;

/**
 * 查询
 */
public class QueryGridAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> list;
    private int[] img;
    private int selectorPosition;
    public QueryGridAdapter(Context mContext, List<String> list,int[] img) {
        super();
        this.mContext = mContext;
        this.list = list;
        this.img=img;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.query_item, parent, false);
        }
        TextView tv = BaseViewHolder.get(convertView, R.id.query_item_title);
        ImageView iv=BaseViewHolder.get(convertView,R.id.query_item_img);
        tv.setText(list.get(position));
        iv.setImageResource(img[position]);
        //如果当前的position等于传过来点击的position,就去改变他的状态
        if (selectorPosition == position) {
            tv.setTextColor(mContext.getResources().getColor(R.color.textColor_2));
        } else {
            //其他的恢复原来的状态
            tv.setTextColor(mContext.getResources().getColor(R.color.textColor_3));
        }
        return convertView;
    }

    static class BaseViewHolder {
        @SuppressWarnings("unchecked")
        public static <T extends View> T get(View view, int id) {
            SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
            if (viewHolder == null) {
                viewHolder = new SparseArray<View>();
                view.setTag(viewHolder);
            }
            View childView = viewHolder.get(id);
            if (childView == null) {
                childView = view.findViewById(id);
                viewHolder.put(id, childView);
            }
            return (T) childView;
        }

    }

    public void changeState(int pos) {
        selectorPosition = pos;
        notifyDataSetChanged();

    }
}