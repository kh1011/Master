package com.yxclient.app.poisearch.searchmodule;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.yxclient.app.poisearch.util.CityModel;

import java.util.ArrayList;

public class CityListViewAdapter extends BaseAdapter implements SectionIndexer {
    private ArrayList<CityModel> list;
    private Context mContext;

    public void setData(ArrayList<CityModel> data) {
        this.list = data;
    }

    ArrayList<CityModel> getData() {
        return list;
    }

    public CityListViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void updateListView(ArrayList<CityModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup arg2) {
       /* ViewHolder viewHolder = null;
        final CityModel mContent = list.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_city, null);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
            viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        int section = getSectionForPosition(position);
        if (position == getPositionForSection(section)) {
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText(mContent.getSortLetters());
        } else {
            viewHolder.tvLetter.setVisibility(View.GONE);
        }

        viewHolder.tvTitle.setText(this.list.get(position).getCity());

        return view;*/
        CityModel cityData = list.get(position);

        if (convertView == null) {
            if (cityData.isGroupModel()) {
                convertView = new CityListItemView.GroupView(mContext);
            } else {
                convertView = new CityListItemView.ChildView(mContext);
            }
        }
        if (cityData.isGroupModel()) {
            ((CityListItemView.GroupView) convertView).setGroup(cityData.getCity().toUpperCase());
            return convertView;
        }

        ((CityListItemView.ChildView) convertView).setCity(cityData.getCity());
        return convertView;

    }


    final static class ViewHolder {
        TextView tvLetter;
        TextView tvTitle;
    }

    public int getSectionForPosition(int position) {
        return list.get(position).getSortLetters().charAt(0);
    }

    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    private String getAlpha(String str) {
        String sortStr = str.trim().substring(0, 1).toUpperCase();
        if (sortStr.matches("[A-Z]")) {
            return sortStr;
        } else {
            return "#";
        }
    }

    @Override
    public Object[] getSections() {
        return null;
    }
}