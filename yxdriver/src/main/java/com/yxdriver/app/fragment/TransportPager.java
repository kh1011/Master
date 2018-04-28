package com.yxdriver.app.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.yxdriver.app.R;
import com.yxdriver.app.adapter.OrderViewPagerAdapter;
import com.yxdriver.app.view.SlidingTabLayout;
import java.util.ArrayList;
import java.util.List;
import butterknife.ButterKnife;
import zuo.biao.library.base.BaseFragment;

/**
 * Created by jpeng on 16-11-14.
 * 订单管理
 */
public class TransportPager extends BaseFragment {
    private ViewPager viewPager;
    private SlidingTabLayout tabLayout;


    public static TransportPager createInstance(long userId) {
        return createInstance(userId, null);
    }

    public static TransportPager createInstance(long userId, String userName) {
        TransportPager fragment = new TransportPager();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //TODO demo_fragment改为你所需要的layout文件
        setContentView(R.layout.yx_trans_fragment);
        ButterKnife.bind(this, view);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        //toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        tabLayout = (SlidingTabLayout) view.findViewById(R.id.tabLayout);
        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        initViewDate();
        //功能归类分区方法，必须调用>>>>>>>>>>
        return view;//返回值必须为view
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //加载数据
            System.out.println("====");
        }
    }

    @Override
    public void initView() {
        //
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {


    }

    /**
     * 初始化子页面显示
     */
    private void initViewDate() {
        List<Fragment> list = new ArrayList<>();
        list.add(new RideOrderListFragment());
        list.add(new GoodsOrderListFragment());
        list.add(new ShoppingOrderListFragment());
        OrderViewPagerAdapter adapter = new OrderViewPagerAdapter(getChildFragmentManager(), list, view.getContext());
        viewPager.setAdapter(adapter);
        tabLayout.setCustomTabView(R.layout.custom_tab, 0);
        tabLayout.setDistributeEvenly(true);
        tabLayout.setViewPager(viewPager);
        tabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return ContextCompat.getColor(getActivity(), R.color.main_blue);
            }
        });
    }

}
