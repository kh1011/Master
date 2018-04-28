package com.yxclient.app.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yxclient.app.R;
import com.yxclient.app.activity.YXQueryGoodActivity;
import com.yxclient.app.http.RetrofitHttp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;
import zuo.biao.library.base.BaseFragment;

/**
 * Created by jpeng on 16-11-14.
 * 商城
 */
public class ShoppingPager extends BaseFragment {
    List<JSONObject> list = new ArrayList<>();
    private TextView toolsTextViews[];
    private View views[];
    private LayoutInflater inflater;
    @BindView(R.id.tools_scrlllview)
    ScrollView scrollView;
    @BindView(R.id.tools)
    LinearLayout toolsLayout;
    private int scrllViewWidth = 0, scrollViewMiddle = 0;
    @BindView(R.id.goods_pager)
    ViewPager shop_pager;
    private int currentItem = 0;
    private ShopAdapter shopAdapter;
    private int page = 1;


    /**
     * 创建一个Fragment实例
     *
     * @param userId
     * @return
     */
    public static ShoppingPager createInstance(long userId) {
        return createInstance(userId, null);
    }

    /**
     * 创建一个Fragment实例
     *
     * @param userId
     * @param userName
     * @return
     */
    public static ShoppingPager createInstance(long userId, String userName) {
        ShoppingPager fragment = new ShoppingPager();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.yx_shop_fragment);
        ButterKnife.bind(this, view);
        argument = getArguments();
        if (argument != null) {
            //
        }
        setHasOptionsMenu(true);
        initView();
        initData();
        initEvent();
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //加载数据
            //getShopData(DemoApplication.getInstance().getMyToken());
        } else {
            // 不加载数据
            System.out.println("不加载网络数据");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        shopAdapter = new ShopAdapter(getChildFragmentManager());
        inflater = LayoutInflater.from(getActivity());
        getShopData();
    }

    @Override
    public void initView() {
        //getShopData(DemoApplication.getInstance().getMyToken());
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }

    /**
     * 动态生成显示items中的textview
     */
    private void showToolsView() {
        System.out.println("开始编辑数据");
        //shopAdapter = new ShopAdapter(getChildFragmentManager());
        toolsTextViews = new TextView[list.size()];
        inflater = LayoutInflater.from(getActivity());
        views = new View[list.size()];
        for (int i = 0; i < list.size(); i++) {
            View view = inflater.inflate(R.layout.item_b_top_nav_layout, null);
            view.setId(i);
            view.setOnClickListener(toolsItemListener);
            TextView textView = (TextView) view.findViewById(R.id.typetext);
            textView.setText(list.get(i).getString("name"));
            toolsLayout.addView(view);
            toolsTextViews[i] = textView;
            views[i] = view;
        }
        changeTextColor(0);
        initPager();
    }

    @OnClick({R.id.index_query})
    void myClick(View view) {
        switch (view.getId()) {
            case R.id.index_query:
                toActivity(YXQueryGoodActivity.createIntent(context, ""));
                break;
            default:
                break;
        }
    }

    private View.OnClickListener toolsItemListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            shop_pager.setCurrentItem(v.getId());
        }
    };


    /**
     * initPager<br/>
     * 初始化ViewPager控件相关内容
     */
    private void initPager() {
        shop_pager.setAdapter(shopAdapter);
        shop_pager.setOnPageChangeListener(onPageChangeListener);
    }

    /**
     * OnPageChangeListener<br/>
     * 监听ViewPager选项卡变化事的事件
     */

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int arg0) {
            if (shop_pager.getCurrentItem() != arg0) shop_pager.setCurrentItem(arg0);
            if (currentItem != arg0) {
                changeTextColor(arg0);
                changeTextLocation(arg0);
            }
            currentItem = arg0;
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };


    /**
     * ViewPager 加载选项卡
     *
     * @author Administrator
     */
    private class ShopAdapter extends FragmentPagerAdapter {
        public ShopAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            Fragment fragment = new ShopListFragment();
            Bundle bundle = new Bundle();
            //String id = list.get(arg0).getString("gc_id");
            bundle.putString("TYPE_ID", list.get(arg0).getString("uuid"));
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }


    /**
     * 改变textView的颜色
     *
     * @param id
     */
    private void changeTextColor(int id) {
        for (int i = 0; i < toolsTextViews.length; i++) {
            if (i != id) {
                toolsTextViews[i].setBackgroundResource(android.R.color.transparent);
                toolsTextViews[i].setTextColor(0xff5f5f5f);
            }
        }
        toolsTextViews[id].setBackgroundResource(android.R.color.white);
        toolsTextViews[id].setTextColor(0xff0d943a);
    }


    /**
     * 改变栏目位置
     *
     * @param clickPosition
     */
    private void changeTextLocation(int clickPosition) {

        int x = (views[clickPosition].getTop() - getScrollViewMiddle() + (getViewheight(views[clickPosition]) / 2));
        scrollView.smoothScrollTo(0, x);
    }

    /**
     * 返回scrollview的中间位置
     *
     * @return
     */
    private int getScrollViewMiddle() {
        if (scrollViewMiddle == 0)
            scrollViewMiddle = getScrollViewheight() / 2;
        return scrollViewMiddle;
    }

    /**
     * 返回ScrollView的宽度
     *
     * @return
     */
    private int getScrollViewheight() {
        if (scrllViewWidth == 0)
            scrllViewWidth = scrollView.getBottom() - scrollView.getTop();
        return scrllViewWidth;
    }

    /**
     * 返回view的宽度
     *
     * @param view
     * @return
     */
    private int getViewheight(View view) {
        return view.getBottom() - view.getTop();
    }

    /**
     * 获取商品分类
     */
    private void getShopData() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).getShopClassic()
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Response<ResponseBody> response) {
                        try {
                            if (response.body() == null) {
                                Toast.makeText(context, "接口请求错误!", Toast.LENGTH_SHORT).show();
                            } else {
                                String result = response.body().string();
                                System.out.println(result);
                                JSONObject jsonObject = JSON.parseObject(result);
                                if (jsonObject.getString("code").equals("success")) {
                                    JSONArray list_temp = JSON.parseArray(jsonObject.getString("data"));
                                    if (page == 1) {
                                        list.clear();
                                        for (int i = 0; i < list_temp.size(); i++) {
                                            JSONObject json = list_temp
                                                    .getJSONObject(i);
                                            list.add(json);
                                        }
                                    } else {
                                        // 分页
                                        for (int i = 0; i < list_temp.size(); i++) {
                                            JSONObject json = list_temp
                                                    .getJSONObject(i);
                                            list.add(json);
                                            page++;
                                        }

                                    }
                                    //shopAdapter.notifyDataSetChanged();
                                    if (list.size() > 0) {
                                        showToolsView();
                                    } else {
                                        Toast.makeText(context, "获取数据失败", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(context, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(context, "网络不给力", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
