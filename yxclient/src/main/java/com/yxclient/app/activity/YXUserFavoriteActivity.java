package com.yxclient.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dou361.dialogui.DialogUIUtils;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.yxclient.app.R;
import com.yxclient.app.adapter.UserFavoriteAdapter;
import com.yxclient.app.app.DemoApplication;
import com.yxclient.app.http.RetrofitHttp;
import com.yxclient.app.model.bean.GoodPruductModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;
import zuo.biao.library.base.BaseActivity;

/**
 * 宝贝收藏
 * <p>
 * Created by zhangyun on 2017/10/9 0009
 * EMail -> yun.zhang@chinacreator.com
 */
public class YXUserFavoriteActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnLoadMoreListener {
    @BindView(R.id.order_all_data)
    EasyRecyclerView recyclerView;

    UserFavoriteAdapter adapter;
    private GridLayoutManager girdLayoutManager;
    List<GoodPruductModel> list = new ArrayList<>();
    private int page = 1;
    private int pageSize = 5;
    private Handler handler = new Handler();

    /**
     * 获取Activity
     *
     * @must 在非抽象Activity中 return this;
     */
    @Override
    public Activity getActivity() {
        return this;
    }


    public static Intent createIntent(Context context) {
        return new Intent(context, YXUserFavoriteActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_user_favorite);
        ButterKnife.bind(this);
        initView();
        initData();
        initEvent();
    }

    @Override
    public void initView() {
        adapter = new UserFavoriteAdapter(context);
        girdLayoutManager = new GridLayoutManager(getActivity(), 2);
        girdLayoutManager.setSpanSizeLookup(adapter.obtainTipSpanSizeLookUp());
        recyclerView.setErrorView(R.layout.view_net_error);
        recyclerView.setLayoutManager(girdLayoutManager);
        recyclerView.setAdapterWithProgress(adapter);
        recyclerView.setRefreshListener(this);
        recyclerView.setEmptyView(R.layout.view_empty);
        adapter.setMore(R.layout.view_more, this);
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                toActivity(YXGoodMainActivity.createIntent(context, adapter.getItem(position),1));
            }
        });
        onRefresh();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }

    @Override
    public void onRefresh() {
        // 刷新数据
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                page = 1;
                adapter.clear();
                getOrderData(DemoApplication.getInstance().getMyToken(), page, pageSize);
            }
        }, 500);
    }

    @Override
    public void onLoadMore() {
        // 加载更多
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //刷新
                page++;
                getOrderData(DemoApplication.getInstance().getMyToken(), page, pageSize);
            }
        }, 500);
    }

    private void getOrderData(String token, int mypage, int size) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).getFavoriteList(token, mypage, size)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Response<ResponseBody> response) {
                        switch (response.code()) {
                            case 200:
                                try {
                                    if (response.body() == null) {
                                        DialogUIUtils.showToast(response.message());
                                    } else {
                                        String result = response.body().string();
                                        System.out.println("我的收藏数据" + result);
                                        JSONObject jsonObject = JSON.parseObject(result);
                                        if (jsonObject.getString("code").equals("success")) {
                                            JSONObject data = JSON.parseObject(jsonObject.getString("data"));
                                            JSONArray list_temp = JSON.parseArray(data.getString("list"));
                                            if (page == 1) {
                                                list.clear();
                                                for (int i = 0; i < list_temp.size(); i++) {
                                                    JSONObject json = list_temp
                                                            .getJSONObject(i);
                                                    list.add(JSON.parseObject(json.toJSONString(), GoodPruductModel.class));
                                                    adapter.clear();
                                                    adapter.addAll(list);
                                                }
                                            } else {
                                                // 分页
                                                list.clear();
                                                for (int i = 0; i < list_temp.size(); i++) {
                                                    JSONObject json = list_temp
                                                            .getJSONObject(i);
                                                    list.add(JSON.parseObject(json.toJSONString(), GoodPruductModel.class));
                                                }
                                                adapter.addAll(list);
                                            }
                                            adapter.notifyDataSetChanged();
                                        } else {
                                            DialogUIUtils.showToast(jsonObject.getString("message"));
                                        }
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            default:
                                DialogUIUtils.showToast(response.message());
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        DialogUIUtils.showToast(t.getMessage());
                    }
                });
    }

}