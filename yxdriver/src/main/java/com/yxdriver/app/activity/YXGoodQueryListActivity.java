package com.yxdriver.app.activity;

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
import com.jude.easyrecyclerview.decoration.SpaceDecoration;
import com.yxdriver.app.R;
import com.yxdriver.app.adapter.SearchGoodsListAdapter;
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
public class YXGoodQueryListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnLoadMoreListener {
    private String queryStr;

    @BindView(R.id.order_all_data)
    EasyRecyclerView recyclerView;
    SearchGoodsListAdapter adapter;
    List<GoodPruductModel> list = new ArrayList<>();
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
        setContentView(R.layout.yx_good_query_list);
        ButterKnife.bind(this);
        initView();
        initData();
        initEvent();
    }


    public static Intent createIntent(Context context, String data) {
        return new Intent(context, YXGoodQueryListActivity.class).
                putExtra(RESULT_DATA, data);
    }

    @Override
    public void initView() {
        queryStr=getIntent().getStringExtra(RESULT_DATA);
        list = new ArrayList<>();
        adapter = new SearchGoodsListAdapter(context);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);

        // gridview样式
        recyclerView.setLayoutManager(layoutManager);
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
                toActivity(YXGoodMainActivity.createIntent(context, adapter.getItem(position),0));
            }
        });
        onRefresh();
    }

    @Override
    public void initData() {
        getSearchGoodsData(DemoApplication.getInstance().getMyToken(),queryStr, page, pageSize);
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
                getSearchGoodsData(DemoApplication.getInstance().getMyToken(),queryStr, page, pageSize);
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
                getSearchGoodsData(DemoApplication.getInstance().getMyToken(),queryStr, page, pageSize);
            }
        }, 500);
    }

    /**
     * 获取商品列表
     *
     * @param queryStr         关键字
     * @param curentPage 当前页码
     * @param size       每页总数
     */
    private void getSearchGoodsData(String token, String queryStr, int curentPage, int size) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).getSearchGoodsList(token,queryStr, curentPage, size)
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
                                           /* if (page == 1) {
                                                listProducts.clear();
                                            }
                                            loadDataSuccess(list_temp);
                                            page++;*/
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

    @Override
    public void onResume() {
        super.onResume();
        //onRefresh();
    }
}
