package com.yxdriver.app.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by mac on 2017/9/16.
 * xx
 */

public class IndexViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> list;
    private Context context;
    private String[] titles={"推荐","发单"};
    public IndexViewPagerAdapter(FragmentManager fm, List<Fragment> list, Context context) {
        super(fm);
        this.list = list;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        if(list!=null)
            return list.size();
        return 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
