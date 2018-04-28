package com.yxdriver.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dou361.dialogui.DialogUIUtils;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.jude.rollviewpager.Util;
import com.yxdriver.app.R;
import com.yxdriver.app.adapter.IndexOrderAdapter;
import com.yxdriver.app.adapter.SameCityAdapter;
import com.yxdriver.app.app.DemoApplication;
import com.yxdriver.app.http.RetrofitHttp;
import com.yxdriver.app.model.bean.OldCarModel;
import com.yxdriver.app.model.bean.SameCityModel;
import com.yxdriver.app.model.bean.UserInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;
import zuo.biao.library.base.BaseActivity;

/**
 * 同城信息
 * Created by zhangyun on 2017/9/25 0025
 * EMail -> yun.zhang@chinacreator.com
 */
public class YXSameCityActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnLoadMoreListener {
    @BindView(R.id.order_all_data)
    EasyRecyclerView recyclerView;
    private static final int REQUEST_IMAGE = 1001;

    SameCityAdapter adapter;
    List<OldCarModel> list = new ArrayList<>();
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
        return new Intent(context, YXSameCityActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_same_city_activity);
        ButterKnife.bind(this);
        initView();
        initData();
        initEvent();
    }

    /**
     * UI显示方法(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)
     *
     * @must Activity-在子类onCreate方法内初始化View(setContentView)后调用；Fragment-在子类onCreateView方法内初始化View后调用
     */
    @Override
    public void initView() {
        adapter = new SameCityAdapter(context);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setErrorView(R.layout.view_net_error);
        recyclerView.setEmptyView(R.layout.view_order_empty);
        recyclerView.setAdapterWithProgress(adapter);
        // 添加分割线
        DividerDecoration itemDecoration = new DividerDecoration(R.color.diver_bg, Util.dip2px(this, 1.5f), Util.dip2px(this, 0), 0);//颜色 & 高度 & 左边距 & 右边距
        itemDecoration.setDrawLastItem(false);//有时候你不想让最后一个item有分割线,默认true.
        itemDecoration.setDrawHeaderFooter(false);//是否对Header于Footer有效,默认false.
        recyclerView.addItemDecoration(itemDecoration);

        adapter.setNoMore(R.layout.view_nomore);
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // 到详情页面
                toActivity(YXInfoDetailActivity.createIntent(context, adapter.getItem(position)));
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

    /**
     * Data数据方法(存在数据获取或处理代码，但不存在事件监听代码)
     *
     * @must Activity-在子类onCreate方法内初始化View(setContentView)后调用；Fragment-在子类onCreateView方法内初始化View后调用
     */
    @Override
    public void initData() {

    }

    /**
     * Event事件方法(只要存在事件监听代码就是)
     *
     * @must Activity-在子类onCreate方法内初始化View(setContentView)后调用；Fragment-在子类onCreateView方法内初始化View后调用
     */
    @Override
    public void initEvent() {

    }

    @Override
    public void onForwardClick(View v) {
        super.onForwardClick(v);
        // 发布同城信息
        ArrayList<String> originImages = new ArrayList<>();
        toActivity(YXSameCityPushActivity.createIntent(context, originImages));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK) {//文章图片
            PostImagesActivity.startPostActivity(context,
                    data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT));
        }
    }

    /**
     * Called when a swipe gesture triggers a refresh.
     */
    @Override
    public void onRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                page = 1;
                adapter.clear();
                getData(DemoApplication.getInstance().getMyToken(), page, pageSize);
            }
        }, 500);
    }

    @Override
    public void onLoadMore() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //刷新
                page++;
                getData(DemoApplication.getInstance().getMyToken(), page, pageSize);
            }
        }, 500);
    }

    private void getData(String token, final int curentPage, int size) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).getSameCityList(token, curentPage, size).enqueue(new Callback<ResponseBody>() {
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
                                    if (curentPage == 1) {
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

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
    }
}
