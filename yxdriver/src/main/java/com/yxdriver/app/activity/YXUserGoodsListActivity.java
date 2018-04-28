package com.yxdriver.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dou361.dialogui.DialogUIUtils;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.yxdriver.app.R;
import com.yxdriver.app.adapter.UserFavoriteAdapter;
import com.yxdriver.app.app.DemoApplication;
import com.yxdriver.app.http.RetrofitHttp;
import com.yxdriver.app.model.bean.GoodPruductModel;
import com.yxdriver.app.model.bean.UserInfo;
import com.yxdriver.app.utils.ParseUtils;

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
 * 功能：用户的订单列表
 * Created by yun.zhang on 2017/10/11 0011.
 * email:zy19930321@163.com
 */
public class YXUserGoodsListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnLoadMoreListener {
    @BindView(R.id.order_all_data)
    EasyRecyclerView recyclerView;

    UserFavoriteAdapter adapter;
    private GridLayoutManager girdLayoutManager;
    List<GoodPruductModel> goodPruductModels;
    int page = 1;
    String token;

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
        setContentView(R.layout.yx_user_goods_list);
        ButterKnife.bind(this);
        initView();
        initData();
        initEvent();
    }

    @Override
    public void initView() {
        token = DemoApplication.getInstance().getMyToken();
        goodPruductModels = new ArrayList<>();
        adapter = new UserFavoriteAdapter(context);
        girdLayoutManager = new GridLayoutManager(getActivity(), 2);
        girdLayoutManager.setSpanSizeLookup(adapter.obtainTipSpanSizeLookUp());
        recyclerView.setErrorView(R.layout.view_net_error);
        recyclerView.setLayoutManager(girdLayoutManager);
        recyclerView.setAdapterWithProgress(adapter);
        recyclerView.setRefreshListener(this);
        adapter.setMore(R.layout.view_more, this);
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                toActivity(YXGoodMainActivity.createIntent(context, goodPruductModels.get(position),0));
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
        //刷新时，重置页码为1
        page = 1;
        goodPruductModels.clear();
        getDataFromService();
    }

    @Override
    public void onLoadMore() {
        getDataFromService();
    }

    /**
     * 从服务器中获取数据
     */
    private void getDataFromService() {
        /*final Dialog dialog = DialogUIUtils.showMdLoading(context, "数据加载中", true, true, true, true).show();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).getFavoriteList(token, page).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                DialogUIUtils.dismiss(dialog);
                if (response == null || response.body() == null) {
                    DialogUIUtils.showToast("获取请求数据失败");
                } else {
                    try {
                        String result = response.body().string();
                        JSONObject jsonObject = JSON.parseObject(result);
                        if (jsonObject == null || !"success".equals(jsonObject.get("code"))) {
                            DialogUIUtils.showToast("获取数据列表失败，请重试！");
                        } else {
                            JSONArray array = jsonObject.getJSONArray("data");
                            doGetSuccess(array);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });*/
    }

    /**
     * 请求数据列表成功
     *
     * @param array
     */
    private void doGetSuccess(JSONArray array) {
        if (page == 1) {
            goodPruductModels.clear();
            adapter.clear();
        }
        if (array == null || array.size() == 0) {
            DialogUIUtils.showToast("没有列表");
            return;
        }
        for (int i = 0; i < array.size(); i++) {
            JSONObject object = array.getJSONObject(i);
            GoodPruductModel m = ParseUtils.parseToGoodProduct(object);
            goodPruductModels.add(m);
        }
        adapter.addAll(goodPruductModels);
        page++;
    }

    /**
     * 解析获取创建此信息的用户的信息
     *
     * @param object
     * @return
     */
    private UserInfo getCreateUserInfo(JSONObject object) {
        if (object == null) {
            return new UserInfo();
        }
        UserInfo user = new UserInfo();

        user.setUuid(object.getString("uuid"));
        user.setName(object.getString("name"));
        user.setMobile(object.getString("mobile"));
        user.setMobile(object.getString("headImage"));

        return user;
    }


}