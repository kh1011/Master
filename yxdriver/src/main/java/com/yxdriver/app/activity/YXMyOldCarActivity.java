package com.yxdriver.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dou361.dialogui.DialogUIUtils;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.jude.rollviewpager.Util;
import com.yxdriver.app.R;
import com.yxdriver.app.adapter.OloCarListAdapter;
import com.yxdriver.app.app.DemoApplication;
import com.yxdriver.app.http.RetrofitHttp;
import com.yxdriver.app.model.bean.OldCarModel;

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
 * Created by mac on 2017/10/18.
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

/**
 * 个人二手车信息管理
 */
public class YXMyOldCarActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnLoadMoreListener {
    @BindView(R.id.order_all_data)
    EasyRecyclerView recyclerView;
    @BindView(R.id.old_car_righttxt)
    Button btn_Right;
    List<OldCarModel> list = new ArrayList<>();
    private int page = 1;
    private int pageSize = 5;
    private Handler handler = new Handler();
    OloCarListAdapter adapter;

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_old_car);
        ButterKnife.bind(this);
        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }
    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


    /**
     * 启动这个Activity的Intent
     *
     * @param context
     * @param data
     * @return
     */
    public static Intent createIntent(Context context, String data) {
        return new Intent(context, YXMyOldCarActivity.class).
                putExtra(RESULT_DATA, data);
    }

    @Override
    public void initView() {
        btn_Right.setVisibility(View.GONE);
        adapter = new OloCarListAdapter(context);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setErrorView(R.layout.view_net_error);
        recyclerView.setEmptyView(R.layout.view_order_empty);
        recyclerView.setAdapterWithProgress(adapter);
        // 添加分割线
        DividerDecoration itemDecoration = new DividerDecoration(R.color.divider_color, Util.dip2px(this, 1.5f), Util.dip2px(this, 0), 0);//颜色 & 高度 & 左边距 & 右边距
        itemDecoration.setDrawLastItem(false);//有时候你不想让最后一个item有分割线,默认true.
        itemDecoration.setDrawHeaderFooter(false);//是否对Header于Footer有效,默认false.
        recyclerView.addItemDecoration(itemDecoration);

        adapter.setNoMore(R.layout.view_nomore);
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // 到详情页面
                Intent intent = new Intent(context, YXInfoDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("info", adapter.getItem(position));
                bundle.putString("flag", "1");
                intent.putExtras(bundle);
                startActivity(intent);
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
        onRefresh();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }

    @Override
    public void onForwardClick(View v) {
        super.onForwardClick(v);
        ArrayList<String> originImages = new ArrayList<>();
        toActivity(YXPublishCarActivity.createIntent(context, originImages));
    }

    // 刷新数据
    @Override
    public void onRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                page = 1;
                adapter.clear();
                getOrderData(DemoApplication.getInstance().getMyToken(), page, pageSize);
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
                getOrderData(DemoApplication.getInstance().getMyToken(), page, pageSize);
            }
        }, 500);
    }

    /**
     * 获取商品分类
     *
     * @param token
     */
    private void getOrderData(String token, final int mypage, int size) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).getMyOldCarList(token, mypage, size)
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
                                        System.out.println("二手车列表数据" + result);
                                        JSONObject jsonObject = JSON.parseObject(result);
                                        if (jsonObject.getString("code").equals("success")) {
                                            JSONObject data = JSON.parseObject(jsonObject.getString("data"));
                                            JSONArray list_temp = JSON.parseArray(data.getString("list"));
                                            if (mypage == 1) {
                                                list.clear();
                                                for (int i = 0; i < list_temp.size(); i++) {
                                                    JSONObject json = list_temp
                                                            .getJSONObject(i);
                                                    list.add(JSON.parseObject(json.toJSONString(), OldCarModel.class));
                                                    adapter.clear();
                                                    adapter.addAll(list);
                                                }
                                            } else {
                                                // 分页
                                                list.clear();
                                                for (int i = 0; i < list_temp.size(); i++) {
                                                    JSONObject json = list_temp
                                                            .getJSONObject(i);
                                                    list.add(JSON.parseObject(json.toJSONString(), OldCarModel.class));
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
    protected void onResume() {
        super.onResume();
        onRefresh();
    }
}
