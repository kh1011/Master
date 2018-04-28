package com.yxdriver.app.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dou361.dialogui.DialogUIUtils;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.swipe.SwipeRefreshLayout;
import com.yxdriver.app.R;
import com.yxdriver.app.adapter.GoodProductEvaluateAdapter;
import com.yxdriver.app.app.DemoApplication;
import com.yxdriver.app.http.RetrofitHttp;
import com.yxdriver.app.model.bean.GoodEvaluateModel;
import com.yxdriver.app.model.bean.GoodPruductModel;
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
import zuo.biao.library.base.BaseFragment;

/**
 * 功能：评价界面
 * Created by yun.zhang on 2017/9/24 0024.
 * email:zy19930321@163.com
 */
public class GoodEvaluatePager extends BaseFragment implements android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener,
        RecyclerArrayAdapter.OnLoadMoreListener {

    @BindView(R.id.order_all_data)
    EasyRecyclerView recyclerView;

    GoodPruductModel model;

    GoodProductEvaluateAdapter adapter;
    private GridLayoutManager girdLayoutManager;
    List<GoodEvaluateModel> listEvaluates;
    String token;

    /*public GoodEvaluatePager(GoodPruductModel model) {
        this.model = model;
    }*/

    /**
     * 创建一个Fragment实例
     *
     * @return
     */
    public static IndexPager createInstance() {
        IndexPager fragment = new IndexPager();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //TODO demo_fragment改为你所需要的layout文件
        setContentView(R.layout.yx_fragment_shop_evaluate);
        ButterKnife.bind(this, view);
        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        return view;//返回值必须为view
    }

    @Override
    public void initView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            model = (GoodPruductModel) bundle.getSerializable("model");
        }
        token = DemoApplication.getInstance().getMyToken();
        listEvaluates = new ArrayList<>();
        adapter = new GoodProductEvaluateAdapter(context);
        girdLayoutManager = new GridLayoutManager(getActivity(), 2);
        girdLayoutManager.setSpanSizeLookup(adapter.obtainTipSpanSizeLookUp());
        recyclerView.setErrorView(R.layout.view_net_error);
        recyclerView.setLayoutManager(girdLayoutManager);
        recyclerView.setAdapterWithProgress(adapter);
        recyclerView.setRefreshListener(this);
        adapter.setMore(R.layout.view_more, this);
        onRefresh();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadMore() {
        //TODO   加载更多
        loadDataFromService();
    }

    /**
     * 刷新
     */
    @Override
    public void onRefresh() {
        //TODO   刷新数据
        loadDataFromService();
    }

    /**
     * 从服务器中加载数据
     */
    private void loadDataFromService() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).getGoodProductDetails(token, model.getUuid()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                if (response == null || response.body() == null) {
                    DialogUIUtils.showToast("加载商品评论列表失败，请重试。");
                } else {
                    try {
                        String result = response.body().string();
                        JSONObject jsonObject = JSON.parseObject(result);
                        if (jsonObject != null && "success".equals(jsonObject.getString("code"))) {
                            loadSuccess(jsonObject.getJSONObject("data"));
                        } else {
                            DialogUIUtils.showToast("解析商品评论列表失败，请重试。");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                DialogUIUtils.showToast("加载商品评论列表失败，请重试。");
            }
        });
    }

    /**
     * 解析数据成功
     *
     * @param object
     */
    private void loadSuccess(JSONObject object) {
        if (object == null) {
            DialogUIUtils.showToast("解析商品评论列表失败，请重试。");
            return;
        }
        JSONArray array = object.getJSONArray("evaluations");
        if (array == null) {
            return;
        }
        List<GoodEvaluateModel> datas = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            JSONObject obj = array.getJSONObject(i);
            GoodEvaluateModel model = ParseUtils.parseToGoodEvaluate(obj);
            datas.add(model);
        }
        refreshPage(datas);
    }

    private void refreshPage(List<GoodEvaluateModel> datas) {
        listEvaluates.clear();
        adapter.clear();
        listEvaluates.addAll(datas);
        adapter.addAll(datas);
        adapter.pauseMore();
    }
}
