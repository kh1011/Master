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
import android.widget.Button;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIListener;
import com.yxclient.app.R;
import com.yxclient.app.adapter.ReceipterListAdapter;
import com.yxclient.app.app.DemoApplication;
import com.yxclient.app.event.ReceipterItemListener;
import com.yxclient.app.http.RetrofitHttp;
import com.yxclient.app.model.bean.ReceiptAddressModel;
import com.yxclient.app.utils.StringUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;
import zuo.biao.library.base.BaseActivity;

/**
 * 收货地址管理
 */
public class YXReceiptAddressManagerActivity extends BaseActivity {
    // 收货地址列表控件
    @BindView(R.id.rv_list)
    RecyclerView recyclerView;
    // 适配器
    private ReceipterListAdapter adapter;
    // 收货地址列表
    List<ReceiptAddressModel> list = new ArrayList<>();
    @BindView(R.id.btn_manage)
    Button btn;

    @Override
    public Activity getActivity() {
        return this;
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, YXReceiptAddressManagerActivity.class);
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
        btn.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(
                context, DividerItemDecoration.VERTICAL));
    }

    @Override
    public void initData() {
        adapter = new ReceipterListAdapter(context, list, true);
        recyclerView.setAdapter(adapter);
        getAddrList(DemoApplication.getInstance().getMyToken());
    }

    @Override
    public void initEvent() {
        adapter.setOnItemListener(new ReceipterItemListener() {
            @Override
            public void setDefault(int position) {
                // 设置默认地址
                setDefaultAddr(DemoApplication.getInstance().getMyToken(), adapter.getItem(position).getUuid());
            }

            @Override
            public void delete(final int position) {
                // 删除
                DialogUIUtils.showMdAlert(context, "提示", "确定删除当前收货地址吗？", new DialogUIListener() {
                    @Override
                    public void onPositive() {
                        // 确定注销
                        doDelete(DemoApplication.getInstance().getMyToken(), adapter.getItem(position).getUuid());
                        adapter.remove(adapter.getItem(position));
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onNegative() {
                        // 取消注销
                    }

                }).show();
            }

            @Override
            public void edite(int position) {
                // 编辑;;; 跳转到编辑页面
                toActivity(YXAddNewAddressActivity.createIntent(context, adapter.getItem(position)));
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
     * 删除收货地址
     *
     * @param token  用户token
     * @param addrId 地址ID
     */
    private void doDelete(String token, String addrId) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).deleteReceiptAddress(token, addrId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                switch (response.code()) {
                    case 200:
                        if (response.body() == null) {
                            DialogUIUtils.showToast("解析数据失败");
                        } else {
                            try {
                                String result = response.body().string();
                                JSONObject jsonObject = JSON.parseObject(result);
                                if ("success".equals(jsonObject.getString("code"))) {
                                    //地址删除成功
                                    DialogUIUtils.showToast(jsonObject.getString("message"));
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
                DialogUIUtils.showToast("删除地址失败");
            }
        });
    }


    /**
     * 设置默认收货地址
     *
     * @param token  用户token
     * @param addrId 收货地址ID
     */
    private void setDefaultAddr(String token, String addrId) {
        final Dialog dialog = DialogUIUtils.showMdLoading(context, "数据加载中...", true, true, true, true).show();
        Map<String, String> params = new HashMap<>();
        params.put("uuid", addrId);
        params.put("isdefault", "1");
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).editReceiptAddress(token, params).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                DialogUIUtils.dismiss(dialog);
                switch (response.code()) {
                    case 200:
                        if (response.body() == null) {
                            DialogUIUtils.showToast(response.message());
                        } else {
                            try {
                                String result = response.body().string();
                                JSONObject jsonObject = JSON.parseObject(result);
                                if ("success".equals(jsonObject.getString("code"))) {
                                    DialogUIUtils.showToast(jsonObject.getString("message"));
                                    getAddrList(DemoApplication.getInstance().getMyToken());
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
                DialogUIUtils.showToast(t.getMessage());
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
    protected void onResume() {
        super.onResume();
        getAddrList(DemoApplication.getInstance().getMyToken());
    }
}
