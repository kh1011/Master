package com.yxclient.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dou361.dialogui.DialogUIUtils;
import com.yxclient.app.R;
import com.yxclient.app.adapter.ReceipterListAdapter;
import com.yxclient.app.app.DemoApplication;
import com.yxclient.app.event.ReceipterItemListener;
import com.yxclient.app.http.RetrofitHttp;
import com.yxclient.app.model.bean.ReceiptAddressModel;
import com.yxclient.app.utils.StringUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;
import zuo.biao.library.base.BaseActivity;

/**
 * 选择收货地址
 */
public class YXReceiptAddressActivity extends BaseActivity {
    private static final String EXTRA_MODEL = "model";
    // 收货地址列表控件
    @BindView(R.id.rv_list)
    RecyclerView recyclerView;
    // 适配器
    private ReceipterListAdapter adapter;
    // 收货地址列表
    List<ReceiptAddressModel> list = new ArrayList<>();

    @Override
    public Activity getActivity() {
        return this;
    }

    public static Intent createIntent(Context context, ReceiptAddressModel model) {
        return new Intent(context, YXReceiptAddressActivity.class).putExtra(EXTRA_MODEL, model);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_receipt_address);
        ButterKnife.bind(this);
        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
    }

    @Override
    public void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(
                context, DividerItemDecoration.VERTICAL));
    }

    @Override
    public void initData() {
        adapter = new ReceipterListAdapter(context, list, false);
        recyclerView.setAdapter(adapter);
        getAddrList(DemoApplication.getInstance().getMyToken());
    }

    @Override
    public void initEvent() {
        adapter.setOnItemListener(new ReceipterItemListener() {
            @Override
            public void setDefault(int position) {
                // 设置默认地址
            }

            @Override
            public void delete(int position) {
                // 删除
            }

            @Override
            public void edite(int position) {
                // 编辑
            }

            @Override
            public void selected(int position) {
                // 选择收货地址
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("model", adapter.getItem(position));
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    /**
     * 获取收货地址列表
     *
     * @param token 用户token
     */
    private void getAddrList(String token) {
        final Dialog dialog = DialogUIUtils.showMdLoading(context, "列表加载中", true, true, true, true).show();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).getReceiptAddress(token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                DialogUIUtils.dismiss(dialog);
                switch (response.code()) {
                    case 200:
                        if (response.body() == null) {
                            DialogUIUtils.showToast("获取列表失败");
                        } else {
                            try {
                                String result = response.body().string();
                                System.out.println("收货地址数据：" + result);
                                JSONObject jsonObject = JSON.parseObject(result);
                                if ("success".equals(jsonObject.getString("code"))) {
                                    if (StringUtil.isNullOrEmpty(jsonObject.getString("data"))) {
                                        DialogUIUtils.showToast("您暂时没有收货地址");
                                    } else {
                                        list.clear();
                                        List<ReceiptAddressModel> receiptAddressModels = JSON.parseArray(jsonObject.getString("data"), ReceiptAddressModel.class);
                                        list.addAll(receiptAddressModels);
                                        adapter.notifyDataSetChanged();
                                    }
                                } else {
                                    DialogUIUtils.showToast(jsonObject.getString("message"));
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    default:
                        DialogUIUtils.showToast(response.message());
                        break;
                }
            }

            @Override
            public void onFailure(Throwable t) {
                DialogUIUtils.dismiss(dialog);
                DialogUIUtils.showToast("获取列表失败");
            }
        });
    }

    @OnClick({R.id.receipt_addr_addbtn})
    void viewOnclick(View view) {
        switch (view.getId()) {
            case R.id.receipt_addr_addbtn:
                //添加收获地址
                toActivity(YXAddNewAddressActivity.createIntent(context, null));
                break;
        }
    }

    @Override
    public void onForwardClick(View v) {
        super.onForwardClick(v);
        // 管理
        toActivity(YXReceiptAddressManagerActivity.createIntent(context));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAddrList(DemoApplication.getInstance().getMyToken());
    }
}
