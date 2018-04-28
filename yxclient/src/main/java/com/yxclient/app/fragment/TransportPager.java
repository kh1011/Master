package com.yxclient.app.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dou361.dialogui.DialogUIUtils;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.yxclient.app.R;
import com.yxclient.app.activity.YXLoginActivity;
import com.yxclient.app.activity.YXTransActivity;
import com.yxclient.app.activity.YXTransOrderDetailActivity;
import com.yxclient.app.adapter.TransOrderAdapter;
import com.yxclient.app.app.DemoApplication;
import com.yxclient.app.config.YXConfig;
import com.yxclient.app.http.RetrofitHttp;
import com.yxclient.app.model.bean.newOrderModel;
import com.yxclient.app.utils.StringUtil;

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
 * 货运模块
 */
public class TransportPager extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnLoadMoreListener {
    private static final String TAG = "TransportPager";
    @BindView(R.id.order_all_data)
    EasyRecyclerView recyclerView;
    TransOrderAdapter adapter;
    private int page = 1;
    private int pageSize = 5;
    private Handler handler = new Handler();
    private List<newOrderModel> list = new ArrayList<>();

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
        initView();
        initData();
        initEvent();
        return view;
    }


    @Override
    public void initView() {
        adapter = new TransOrderAdapter(context);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setErrorView(R.layout.view_net_error);
        //recyclerView.setLayoutManager(girdLayoutManager);
        recyclerView.setAdapterWithProgress(adapter);
        adapter.setMore(R.layout.view_more, this);
        adapter.setNoMore(R.layout.view_nomore);
        //adapter.setMore(R.layout.view_more, getActivity());
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // 到详情页面
                toActivity(YXTransOrderDetailActivity.createIntent(context, adapter.getItem(position)));
            }
        });
        // 长按删除当前项
        adapter.setOnItemLongClickListener(new RecyclerArrayAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(int position) {
                adapter.remove(position);
                return true;
            }
        });
        adapter.setError(R.layout.view_error, new RecyclerArrayAdapter.OnErrorListener() {
            @Override
            public void onErrorShow() {
                adapter.resumeMore();
            }

            @Override
            public void onErrorClick() {
                adapter.resumeMore();
            }
        });
        recyclerView.setRefreshListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            onRefresh();
        }
    }

    // 刷新数据
    @Override
    public void onRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                page = 1;
                adapter.clear();
                getOrderData(YXConfig.EXPRESS, page, pageSize);
            }
        }, 500);
    }

    // 加载下一页
    @Override
    public void onLoadMore() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //刷新
                page++;
                getOrderData(YXConfig.EXPRESS, page, pageSize);
            }
        }, 500);
    }

    /**
     * 获取货运推荐列表
     *
     * @param orderType 订单类型
     * @param mypage    当前页码
     * @param size      每页总条数
     */
    private void getOrderData(String orderType, int mypage, int size) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).ridelist(orderType, mypage, size)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Response<ResponseBody> response) {
                        try {
                            switch (response.code()) {
                                case 200:
                                    if (response.body() == null) {
                                        DialogUIUtils.showToast(response.message());
                                    } else {
                                        String result = response.body().string();
                                        System.out.println("推荐列表数据" + result);
                                        JSONObject jsonObject = JSON.parseObject(result);
                                        if (jsonObject.getString("code").equals("success")) {
                                            JSONObject data = JSON.parseObject(jsonObject.getString("data"));
                                            JSONArray list_temp = JSON.parseArray(data.getString("list"));
                                            if (page == 1) {
                                                list.clear();
                                                for (int i = 0; i < list_temp.size(); i++) {
                                                    JSONObject json = list_temp
                                                            .getJSONObject(i);
                                                    list.add(JSON.parseObject(json.toJSONString(), newOrderModel.class));
                                                    adapter.clear();
                                                    adapter.addAll(list);
                                                }
                                            } else {
                                                // 分页
                                                list.clear();
                                                for (int i = 0; i < list_temp.size(); i++) {
                                                    JSONObject json = list_temp
                                                            .getJSONObject(i);
                                                    list.add(JSON.parseObject(json.toJSONString(), newOrderModel.class));
                                                }
                                                adapter.addAll(list);
                                            }
                                            adapter.notifyDataSetChanged();
                                        } else {
                                            DialogUIUtils.showToast(jsonObject.getString("message"));
                                        }
                                    }
                                    break;
                                default:
                                    DialogUIUtils.showToast(response.message());
                                    break;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        DialogUIUtils.showToast(t.getMessage());
                    }
                });
    }

    @OnClick({R.id.trans_little, R.id.trans_big, R.id.trans_package, R.id.trans_more})
    void myClick(View view) {
        if (!validateUser()) {
            switch (view.getId()) {
                case R.id.trans_little:
                    // 小型货运
                    toActivity(YXTransActivity.createIntent(context, "1"));
                    break;
                case R.id.trans_big:
                    // 大型货运
                    toActivity(YXTransActivity.createIntent(context, "2"));
                    break;
                case R.id.trans_package:
                    // 包裹类型
                    toActivity(YXTransActivity.createIntent(context, "3"));
                    break;
                default:
                    break;
            }
        } else {
            gotoLogin();
        }
    }

    /**
     * 用户验证
     *
     * @return
     */
    private boolean validateUser() {
        return StringUtil.isNullOrEmpty(DemoApplication.getInstance().getMyToken());
    }

    /**
     * 跳转到登录页面
     */
    private void gotoLogin() {
        toActivity(YXLoginActivity.createIntent(context));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
