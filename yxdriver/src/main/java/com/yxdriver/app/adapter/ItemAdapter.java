package com.yxdriver.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.yxdriver.app.R;

import java.util.List;


/**
 * Created by Administrator on 2017/11/6.
 */

public class ItemAdapter extends BaseAdapter {
    private List<String> mList;
    private Context mContext;

    final int itemLength = 8;
    private int clickTemp = -1;//标识被选择的item
    private int[] clickedList={0,0,0,0,0,0,0,0};//这个数组用来存放item的点击状态

    private int selectorPosition;
    public ItemAdapter(Context mContext, List<String> text) {
        super();
        this.mContext = mContext;
        this.mList=text;
        //for (int i =0;i<itemLength;i++){
        //    clickedList[i]=0;      //初始化item点击状态的数组
        //}
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.task_item, parent, false);
        }
        TextView tv = QueryGridAdapter.BaseViewHolder.get(convertView, R.id.item);
        tv.setText(mList.get(position));

        //如果当前的position等于传过来点击的position,就去改变他的状态
        if (selectorPosition == position) {
            tv.setBackgroundResource(R.drawable.choose);
            tv.setTextColor(mContext.getResources().getColor(R.color.textColor_1));
        } else {
            //其他的恢复原来的状态
            tv.setBackgroundResource(R.drawable.frame);
            tv.setTextColor(mContext.getResources().getColor(R.color.textColor_2));
        }


        return convertView;
    }

    public void changeState(int pos) {
        selectorPosition = pos;
        notifyDataSetChanged();

    }



}

