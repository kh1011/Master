package com.yxclient.app.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.alibaba.fastjson.JSONObject;
import com.yxclient.app.fragment.ShopListFragment;

import java.util.List;

/**
 * author：Anumbrella
 * Date：16/5/26 下午7:08
 */
public class CategorizeProductAdapter extends FragmentStatePagerAdapter {


    private List<JSONObject> list;

    public CategorizeProductAdapter(FragmentManager fm, List<JSONObject> array) {
        super(fm);
        this.list = array;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new ShopListFragment();
        Bundle bundle = new Bundle();
        // 把选中的index指针传入过去
        bundle.putString("TYPE_ID", list.get(position).getString("uuid"));
        // 设定在fragment当中去
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
