package com.yxdriver.app.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dou361.dialogui.DialogUIUtils;
import com.yxdriver.app.R;
import com.yxdriver.app.activity.YXGoodMainActivity;
import com.yxdriver.app.activity.YXShoppingOrderInfoActivity;
import com.yxdriver.app.adapter.ShoppingManagerListAdapter;
import com.yxdriver.app.app.DemoApplication;
import com.yxdriver.app.config.YXConfig;
import com.yxdriver.app.event.ShopingOrderItemListener;
import com.yxdriver.app.http.RetrofitHttp;
import com.yxdriver.app.listener.EndLessOnScrollListener;
import com.yxdriver.app.model.bean.GoodsOrderModel;
import com.yxdriver.app.utils.StringUtil;

import net.lemonsoft.lemonhello.LemonHello;
import net.lemonsoft.lemonhello.LemonHelloAction;
import net.lemonsoft.lemonhello.LemonHelloInfo;
import net.lemonsoft.lemonhello.LemonHelloView;
import net.lemonsoft.lemonhello.interfaces.LemonHelloActionDelegate;

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
 * 购物订单
 */
public class ShoppingOrderListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.radioGroup)
    RadioGroup group;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.layout_swipe_refresh)
    SwipeRefreshLayout mRefreshLayout;
    ShoppingManagerListAdapter adapter;
    List<GoodsOrderModel> list = new ArrayList<>();
    private int page = 1;
    private int pageSize = 5;
    // 订单状态
    private String status = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //TODO demo_fragment改为你所需要的layout文件
        setContentView(R.layout.yx_shoping_order_fragment);
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
        adapter = new ShoppingManagerListAdapter(context, list);
        // 转圈的颜色
        mRefreshLayout.setColorSchemeResources(
                R.color.main_blue,
                R.color.yancy_yellow50,
                R.color.green_deep
        );
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(
                context, DividerItemDecoration.VERTICAL));
        mRefreshLayout.setOnRefreshListener(this);
        recyclerView.addOnScrollListener(new EndLessOnScrollListener(layoutManager) {
            @Override
            public void onViewLoadMore(int currentPage) {
                onLoadMore();
            }
        });

    }

    @Override
    public void initData() {
        getOrderData(DemoApplication.getInstance().getMyToken(), status, page, pageSize);
    }

    @Override
    public void initEvent() {
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //获取变更后的选中项的ID
                int radioButtonId = group.getCheckedRadioButtonId();
                switch (radioButtonId) {
                    case R.id.rb1:
                        status = "";
                        // status=String.valueOf(YXConfig.G_ORDER_STATUS_CREATE)
                        break;
                    case R.id.rb2:
                        status = String.valueOf(YXConfig.G_ORDER_STATUS_CREATE);
                        break;
                    case R.id.rb3:
                        status = String.valueOf(YXConfig.G_ORDER_STATUS_PAY);
                        break;
                    case R.id.rb4:
                        status = String.valueOf(YXConfig.G_ORDER_STATUS_DISPATCH);
                        break;
                    case R.id.rb5:
                        status = String.valueOf(YXConfig.G_ORDER_STATUS_SIGN);
                        break;
                    default:
                        break;
                }
                onRefresh();
            }
        });
        adapter.setOnItemListener(new ShopingOrderItemListener() {
            @Override
            public void evaluate(int position) {
                // 评价订单
                showEvaluationOrderDialog(position);
            }

            @Override
            public void buy(int position) {
                // 再次购买
                toActivity(YXGoodMainActivity.createIntent(context, adapter.getItem(position).getProduct(),1));
            }

            @Override
            public void confirm(final int position) {
                // 确认收货
                LemonHello.getSuccessHello("提示", "确定收货")
                        .addAction(new LemonHelloAction("确认", new LemonHelloActionDelegate() {
                            @Override
                            public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                                ConfirmReceipt(DemoApplication.getInstance().getMyToken(),adapter.getItem(position).getNo());
                                helloView.hide();
                            }
                        }))
                        .addAction(new LemonHelloAction("取消", new LemonHelloActionDelegate() {
                            @Override
                            public void onClick(LemonHelloView lemonHelloView, LemonHelloInfo lemonHelloInfo, LemonHelloAction lemonHelloAction) {
                                //DialogUIUtils.showToast("正在开发...");
                                lemonHelloView.hide();
                            }
                        }))
                        .show(context);
            }

            @Override
            public void delete(final int position) {
                // 删除订单
                LemonHello.getWarningHello("提示", "确定删除订单")
                        .addAction(new LemonHelloAction("删除", new LemonHelloActionDelegate() {
                            @Override
                            public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                                deleteOrderData(DemoApplication.getInstance().getMyToken(),adapter.getItem(position).getNo());
                                helloView.hide();
                            }
                        }))
                        .addAction(new LemonHelloAction("取消", new LemonHelloActionDelegate() {
                            @Override
                            public void onClick(LemonHelloView lemonHelloView, LemonHelloInfo lemonHelloInfo, LemonHelloAction lemonHelloAction) {
                                //DialogUIUtils.showToast("正在开发...");
                                lemonHelloView.hide();
                            }
                        }))
                        .show(context);
            }

            @Override
            public void pay(int position) {
                // 支付订单
                toActivity(YXShoppingOrderInfoActivity.createIntent(context, adapter.getItem(position)));
            }

            //跳转到商品详情
            @Override
            public void goMain(int position) {
                toActivity(YXGoodMainActivity.createIntent(context, adapter.getItem(position).getProduct(),1));
            }
        });
    }

    @Override
    public void onRefresh() {
        list.clear();
        page = 1;
        getOrderData(DemoApplication.getInstance().getMyToken(), status, page, pageSize);
        mRefreshLayout.setRefreshing(false);
    }

    // 加载下一页
    public void onLoadMore() {
        page++;
        getOrderData(DemoApplication.getInstance().getMyToken(), status, page, pageSize);
    }

    private void showEvaluationOrderDialog(final int position) {
        final View rootView = View.inflate(context, R.layout.yx_evaluation_good, null);
        final Dialog dialog = DialogUIUtils.showCustomAlert(context, rootView, Gravity.CENTER, true, false).show();
        final EditText edText = (EditText) rootView.findViewById(R.id.evaluation_good);
        rootView.findViewById(R.id.evaluation_order_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String evaluation = edText.getText().toString();
                if (StringUtil.isNullOrEmpty(evaluation)) {
                    DialogUIUtils.showToast("请填写你的意见");
                } else {
                    DialogUIUtils.dismiss(dialog);
                    goodevaluationOrder(DemoApplication.getInstance().getMyToken(), adapter.getItem(position).getNo(),adapter.getItem(position).getProduct().getUuid(), evaluation);
                }
            }
        });
        rootView.findViewById(R.id.evaluation_order_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 取消
                DialogUIUtils.dismiss(dialog);
            }
        });
    }

    /**
     * 订单评论
     *
     * @param token   用户token
     * @param orderNo 订单ID
     * @param content  评论内容
     */
    private void goodevaluationOrder(String token, String orderNo, String goodsUUID, String content) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        JSONObject object = new JSONObject();
        object.put("orderNo", orderNo);
        object.put("goodsUUID", goodsUUID);
        object.put("content", content);
        RetrofitHttp.getRetrofit(builder.build()).goodvaluation(token, StringUtil.getBody(object))
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
                                        JSONObject jsonObject = JSON.parseObject(result);
                                        if (jsonObject.getString("code").equals("success")) {
                                            // 取消行程成功
                                            DialogUIUtils.showToast(jsonObject.getString("message"));
                                            onRefresh();
                                        } else {
                                            DialogUIUtils.showToast(jsonObject.getString("message"));
                                        }
                                    }
                                    break;
                                default:
                                    String err=response.errorBody().string();
                                    DialogUIUtils.showToast(JSON.parseObject(err).getString("message"));
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
    /**
     * 获取购物订单
     *
     * @param token 用户token
     */
    private void getOrderData(String token, String status, int mypage, int size) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).getGodsList(token, status, mypage, size)
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
                                        System.out.println("我的购物列表数据" + result);
                                        JSONObject jsonObject = JSON.parseObject(result);
                                        if (jsonObject.getString("code").equals("success")) {
                                            JSONObject data = JSON.parseObject(jsonObject.getString("data"));
                                            JSONArray list_temp = JSON.parseArray(data.getString("list"));
                                            if (page == 1) {
                                                list.clear();
                                                for (int i = 0; i < list_temp.size(); i++) {
                                                    JSONObject json = list_temp
                                                            .getJSONObject(i);
                                                    list.add(JSON.parseObject(json.toJSONString(), GoodsOrderModel.class));
                                                }
                                            } else {
                                                // 分页
                                                List<GoodsOrderModel> goodsOrderModels = new ArrayList<>();
                                                for (int i = 0; i < list_temp.size(); i++) {
                                                    JSONObject json = list_temp
                                                            .getJSONObject(i);
                                                    goodsOrderModels.add(JSON.parseObject(json.toJSONString(), GoodsOrderModel.class));
                                                }
                                                list.addAll(goodsOrderModels);
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

    /**
     * 删除订单
     *
     * @param token 用户token
     */
    private void deleteOrderData(String token, String no) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).deleteOrder(token,no)
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
                                        JSONObject jsonObject = JSON.parseObject(result);
                                        if (jsonObject.getString("code").equals("success")) {
                                            DialogUIUtils.showToast(jsonObject.getString("message"));
                                            onRefresh();
                                        } else {
                                            DialogUIUtils.showToast(jsonObject.getString("message"));
                                        }
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            default:
                                String err=response.errorBody().toString();
                                DialogUIUtils.showToast(err);
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        DialogUIUtils.showToast(t.getMessage());
                    }
                });
    }

    /**
     * 确认收货
     *
     * @param token   用户token
     * @param orderNo 订单ID
     */
    private void ConfirmReceipt(String token, String orderNo) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        JSONObject object = new JSONObject();
        object.put("orderNo", orderNo);
        RetrofitHttp.getRetrofit(builder.build()).ConfirmReceipt(token, StringUtil.getBody(object))
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
                                        JSONObject jsonObject = JSON.parseObject(result);
                                        if (jsonObject.getString("code").equals("success")) {
                                            // 确认收货成功
                                            DialogUIUtils.showToast(jsonObject.getString("message"));
                                            onRefresh();
                                        } else {
                                            DialogUIUtils.showToast(jsonObject.getString("message"));
                                        }
                                    }
                                    break;
                                default:
                                    String err=response.errorBody().string();
                                    DialogUIUtils.showToast(JSON.parseObject(err).getString("message"));
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
