package com.yxdriver.app.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dou361.dialogui.DialogUIUtils;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.SpaceDecoration;
import com.yxdriver.app.R;
import com.yxdriver.app.activity.YXGoodMainActivity;
import com.yxdriver.app.adapter.GoodsListAdapter;
import com.yxdriver.app.app.DemoApplication;
import com.yxdriver.app.http.RetrofitHttp;
import com.yxdriver.app.model.bean.GoodPruductModel;
import com.yxdriver.app.utils.DensityUtils;

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
 * Created by mac on 2017/8/25.
 * 商品列表
 */

public class ShopListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        RecyclerArrayAdapter.OnLoadMoreListener {
    public static final String TYPE_ID = "TYPE_ID";

    @BindView(R.id.order_all_data)
    EasyRecyclerView recyclerView;

    private GridLayoutManager girdLayoutManager;
    private String typeId = "";
    List<GoodPruductModel> list;
    private int page = 1;
    private int pageSize = 6;
    private Handler handler = new Handler();
    GoodsListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //TODO demo_fragment改为你所需要的layout文件
        setContentView(R.layout.yx_shop_list);
        ButterKnife.bind(this, view);
        argument = getArguments();
        if (argument != null) {
            typeId = argument.getString(TYPE_ID, typeId);
        }
        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
        return view;//返回值必须为view
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
    public void initView() {
        list = new ArrayList<>();
        adapter = new GoodsListAdapter(context);
        girdLayoutManager = new GridLayoutManager(getActivity(), 2);
        girdLayoutManager.setSpanSizeLookup(adapter.obtainTipSpanSizeLookUp());
        // gridview样式
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        // 添加边框
        SpaceDecoration itemDecoration = new SpaceDecoration((int) DensityUtils.dp2px(context, 8));
        itemDecoration.setPaddingEdgeSide(true);
        itemDecoration.setPaddingStart(true);
        itemDecoration.setPaddingHeaderFooter(false);
        recyclerView.addItemDecoration(itemDecoration);

        recyclerView.setErrorView(R.layout.view_net_error);
        recyclerView.setAdapterWithProgress(adapter);
        recyclerView.setRefreshListener(this);
        adapter.setMore(R.layout.view_more, this);
        adapter.setNoMore(R.layout.view_nomore);
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // 到详情页面
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

    /**
     * 获取商品列表
     *
     * @param token
     * @param id
     * @param curentPage
     * @param size
     */
    private void getGoodsData(String token, String id, int curentPage, int size) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).getGoodsList(token, id, curentPage, size)
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
                                        System.out.println(result);
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
                        //Toast.makeText(context, "网络不给力", Toast.LENGTH_SHORT).show();
                    }
                });
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
                getGoodsData(DemoApplication.getInstance().getMyToken(), typeId, page, pageSize);
            }
        }, 500);
    }

    @Override
    public void onLoadMore() {
        page++;
        getGoodsData(DemoApplication.getInstance().getMyToken(), typeId, page, pageSize);
    }
}
