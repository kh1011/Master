package com.yxdriver.app.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * 功能：
 * Created by yun.zhang on 2017/9/25 0025.
 * email:zy19930321@163.com
 */
public class GoodMainViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> list;
    private Context context;
    private String[] titles={"商品","详情","评价"};
    public GoodMainViewPagerAdapter(FragmentManager fm, List<Fragment> list, Context context) {
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