package com.yxclient.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dou361.dialogui.DialogUIUtils;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.yxclient.app.R;
import com.yxclient.app.adapter.IndexOrderAdapter;
import com.yxclient.app.app.DemoApplication;
import com.yxclient.app.http.RetrofitHttp;
import com.yxclient.app.model.bean.OrderModel;

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
 * Created by mac on 2017/11/12.
 * ////////////////////////////////////////////////////////////////////
 * //                            _ooOoo_                             //
 * //                           o8888888o                            //
 * //                           88" . "88                            //
 * //                           (| ^_^ |)                            //
 * //                           O\  =  /O                            //
 * //                        ____/`---'\____                         //
 * //                      .'  \\|     |//  `.                       //
 * //                     /  \\|||  :  |||//  \                      //
 * //                    /  _||||| -:- |||||-  \                     //
 * //                    |   | \\\  -  /// |   |                     //
 * //                    | \_|  ''\---/''  |   |                     //
 * //                    \  .-\__  `-`  ___/-. /                     //
 * //                  ___`. .'  /--.--\  `. . ___                   //
 * //                ."" '<  `.___\_<|>_/___.'  >'"".                //
 * //              | | :  `- \`.;`\ _ /`;.`/ - ` : | |               //
 * //              \  \ `-.   \_ __\ /__ _/   .-` /  /               //
 * //        ========`-.____`-.___\_____/___.-`____.-'========       //
 * //                             `=---='                            //
 * //        ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^      //
 * //         佛祖保佑       永无BUG        永不修改                    //
 * ////////////////////////////////////////////////////////////////////
 */
// 出行类查询结果列表
public class YXTripQueryListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnLoadMoreListener {
    private String origin_;
    private String site_;
    private String infotype_;
    private String time_;
    @BindView(R.id.order_all_data)
    EasyRecyclerView recyclerView;
    IndexOrderAdapter adapter;
    List<OrderModel> list = new ArrayList<>();
    private int page = 1;
    private int pageSize = 5;
    private Handler handler = new Handler();

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_trip_query_list);
        ButterKnife.bind(this);
        initView();
        initData();
        initEvent();
    }


    public static Intent createIntent(Context context, String origin, String site, String infotype, String time) {
        return new Intent(context, YXTripQueryListActivity.class).
                putExtra(RESULT_ORIGIN, origin).putExtra(RESULT_SITE, site).putExtra(RESULT_INFOTYPE, infotype)
                .putExtra(RESULT_TIME, time);
    }

    @Override
    public void initView() {
        origin_ = getIntent().getStringExtra(RESULT_ORIGIN);
        site_ = getIntent().getStringExtra(RESULT_SITE);
        infotype_ = getIntent().getStringExtra(RESULT_INFOTYPE);
        time_ = getIntent().getStringExtra(RESULT_TIME);
        adapter = new IndexOrderAdapter(context);
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
                toActivity(YXRideOrderDetailActivity.createIntent(context, adapter.getItem(position)));
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
        getOrderData(DemoApplication.getInstance().getMyToken(), origin_,site_,infotype_,time_, page, pageSize);
    }

    @Override
    public void initEvent() {

    }

    // 刷新数据
    @Override
    public void onRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                page = 1;
                adapter.clear();
                getOrderData(DemoApplication.getInstance().getMyToken(),  origin_,site_,infotype_,time_, page, pageSize);
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
                getOrderData(DemoApplication.getInstance().getMyToken(),  origin_,site_,infotype_,time_, page, pageSize);
            }
        }, 500);
    }

    /**
     * 获取商品分类
     *
     * @param token
     */
    private void getOrderData(String token, String origin, String site, String infotype, String time,int mypage, int size) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).getTripSearchList(token, origin,site,infotype,time,mypage,size)
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
                                        System.out.println("出行查询列表数据" + result);
                                        JSONObject jsonObject = JSON.parseObject(result);
                                        if (jsonObject.getString("code").equals("success")) {
                                            JSONObject data = JSON.parseObject(jsonObject.getString("data"));
                                            JSONArray list_temp = JSON.parseArray(data.getString("list"));
                                            if (page == 1) {
                                                list.clear();
                                                for (int i = 0; i < list_temp.size(); i++) {
                                                    JSONObject json = list_temp
                                                            .getJSONObject(i);
                                                    list.add(JSON.parseObject(json.toJSONString(), OrderModel.class));
                                                    adapter.clear();
                                                    adapter.addAll(list);
                                                }
                                            } else {
                                                // 分页
                                                list.clear();
                                                for (int i = 0; i < list_temp.size(); i++) {
                                                    JSONObject json = list_temp
                                                            .getJSONObject(i);
                                                    list.add(JSON.parseObject(json.toJSONString(), OrderModel.class));
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

    @Override
    public void onResume() {
        super.onResume();
        //onRefresh();
    }
}
