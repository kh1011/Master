package com.yxclient.app.poisearch.searchmodule;


import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;

import com.yxclient.app.R;
import com.yxclient.app.poisearch.util.CityModel;
import com.yxclient.app.utils.StringUtil;

import java.util.ArrayList;

/**
 * Created by liangchao_suxun on 2017/4/28.
 * 城市列表
 */

public class CityListWidget extends ListView {

    public CityListWidget(Context context) {
        super(context);
        init();
    }

    public CityListWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CityListWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    // public CityListViewAdapter mCityListAdapter;
    public CityListAdapter cityListAdapter;
    private IParentWidget mIParentWidget;

    public void setParentWidget(IParentWidget mIParentWidget) {
        this.mIParentWidget = mIParentWidget;
    }

    private void init() {
        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.common_bg));
        //setBackgroundColor(getContext().getResources().getColor(R.color.common_bg));
        // mCityListAdapter = new CityListViewAdapter(getContext());
        cityListAdapter = new CityListAdapter(getContext());
        setAdapter(cityListAdapter);

        //setDivider(new ColorDrawable(getResources().getColor(R.color.divider_color)));
        setDivider(new ColorDrawable(ContextCompat.getColor(getContext(), R.color.divider_color)));
        setDividerHeight(1);

        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < 0 || position >= cityListAdapter.getData().size()) {
                    return;
                }

                if (cityListAdapter.getData().get(position).isGroupModel()) {
                    return;
                }

                if (mIParentWidget == null) {
                    return;
                }

                mIParentWidget.onSelCity(cityListAdapter.getData().get(position));
            }
        });
    }

    public void loadCityList(ArrayList<CityModel> data) {
        cityListAdapter.setData(data);
        cityListAdapter.notifyDataSetChanged();

    }

    public static class CityListAdapter extends BaseAdapter implements SectionIndexer {

        ArrayList<CityModel> data;
        private Context context;

        CityListAdapter(Context context) {
            this.context = context;
        }

        public void setData(ArrayList<CityModel> data) {
            this.data = data;
        }

        ArrayList<CityModel> getData() {
            return data;
        }

        @Override
        public int getCount() {
            if (data == null) {
                return 0;
            }
            return data.size();
        }

        public void updateListView(ArrayList<CityModel> data) {
            this.data = data;
            notifyDataSetChanged();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            CityModel cityData = data.get(position);
            return cityData.isGroupModel() ? 0 : 1;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            CityModel cityData = data.get(position);

            if (convertView == null) {
                if (cityData.isGroupModel()) {
                    convertView = new CityListItemView.GroupView(context);
                } else {
                    convertView = new CityListItemView.ChildView(context);
                }
            }

            if (cityData.isGroupModel()) {
                ((CityListItemView.GroupView) convertView).setGroup(cityData.getCity().toUpperCase());
                return convertView;
            }

            ((CityListItemView.ChildView) convertView).setCity(cityData.getCity());
            return convertView;
        }

        @Override
        public Object[] getSections() {
            return null;
        }

        @Override
        public int getPositionForSection(int sectionIndex) {
            for (int i = 0; i < getCount(); i++) {
                String sortStr = data.get(i).getPinyin();
                if (!StringUtil.isNullOrEmpty(sortStr)) {
                    char firstChar = sortStr.toUpperCase().charAt(0);
                    if (firstChar == sectionIndex) {
                        return i;
                    }
                }
            }

            return -1;
        }

        @Override
        public int getSectionForPosition(int position) {
            if (data.get(position) != null) {
                if (data.get(position).getPinyin() != null) {
                    System.out.println("======:" + data.get(position).getPinyin().charAt(0));
                    return data.get(position).getPinyin().charAt(0);
                }
                return 0;
            }
            return 0;
        }
    }

    public static interface IParentWidget {
        public void onSelCity(CityModel cityModel);
    }

}
