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
import com.github.clans.fab.FloatingActionButton;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.yxclient.app.R;
import com.yxclient.app.activity.YXTripDetailActivity;
import com.yxclient.app.adapter.TripOrderAdapter;
import com.yxclient.app.app.DemoApplication;
import com.yxclient.app.config.YXConfig;
import com.yxclient.app.http.RetrofitHttp;
import com.yxclient.app.model.bean.newOrderModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;
import zuo.biao.library.base.BaseFragment;

/**
 * Created by jpeng on 16-11-14.
 * 出行订单
 */
public class RideOrderListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnLoadMoreListener {

    @BindView(R.id.order_all_data)
    EasyRecyclerView recyclerView;
    @BindView(R.id.top)
    FloatingActionButton top;
    TripOrderAdapter adapter;
    List<newOrderModel> list = new ArrayList<>();
    private int page = 1;
    private int pageSize = 5;
    private Handler handler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //TODO demo_fragment改为你所需要的layout文件
        setContentView(R.layout.yx_index_list_fragment);
        ButterKnife.bind(this, view);
        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
        return view;//返回值必须为view
    }


    @Override
    public void initView() {
        adapter = new TripOrderAdapter(context);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setErrorView(R.layout.view_net_error);
        recyclerView.setEmptyView(R.layout.view_order_empty);
        recyclerView.setAdapterWithProgress(adapter);
        adapter.setMore(R.layout.view_more, this);
        adapter.setNoMore(R.layout.view_nomore);
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // 到详情页面
                toActivity(YXTripDetailActivity.createIntent(context, adapter.getItem(position)));
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
        // 回到顶部
        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.scrollToPosition(0);
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

    @Override
    public void onRefresh() {
        page = 1;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.clear();
                getOrderData(DemoApplication.getInstance().getMyToken(), "", YXConfig.JOURNEY, page, pageSize);
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
                getOrderData(DemoApplication.getInstance().getMyToken(), "", YXConfig.JOURNEY, page, pageSize);
            }
        }, 500);
    }

    /**
     * 获取出行订单
     *
     * @param token
     */
    private void getOrderData(String token, String infoType, String orderType, int mypage, int size) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).getMyTripOrderList(token, infoType, orderType, mypage, size)
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
                                        System.out.println("我的出行列表数据" + result);
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

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }
}
