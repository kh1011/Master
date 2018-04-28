package com.yxdriver.app.viewholder;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dou361.dialogui.DialogUIUtils;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.yxdriver.app.R;
import com.yxdriver.app.activity.YXOrderDetailActivity;
import com.yxdriver.app.app.DemoApplication;
import com.yxdriver.app.http.RetrofitHttp;
import com.yxdriver.app.model.bean.CustomerModel;
import com.yxdriver.app.model.bean.OrderModel;
import com.yxdriver.app.model.bean.PointModel;
import com.yxdriver.app.model.bean.UserModel;
import com.yxdriver.app.model.bean.newOrderModel;
import com.yxdriver.app.utils.DateUtils;
import com.yxdriver.app.utils.StringUtil;

import java.io.IOException;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yxdriver.app.utils.StringUtil.changeObject;

/**
 * Created by mac on 2017/4/17.
 * order
 */

public class IndexSearchViewHolder extends BaseViewHolder<newOrderModel> implements View.OnClickListener {
    private Context context;
    private newOrderModel data;

    private CircleImageView imageView;
    private TextView name;
    private TextView time;
    private TextView dateTime;
    private TextView origin;
    private TextView site;
    private TextView price;
    private TextView type;

    private Button btn;
    private View view;
    private RelativeLayout qd;

    public IndexSearchViewHolder(ViewGroup parent, Context context) {
        super(parent, R.layout.item_index);
        this.context = context;
        imageView = $(R.id.profile_image);
        name = $(R.id.item_index_name);
        time = $(R.id.item_index_time);
        dateTime = $(R.id.item_index_datetime);
        origin = $(R.id.item_index_origin);
        site = $(R.id.item_index_site);
        price = $(R.id.item_index_price);
        type = $(R.id.item_index_type);
        btn = $(R.id.item_index_btn);
        view=$(R.id.vv);
        qd=$(R.id.qd);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void setData(newOrderModel model) {
        super.setData(model);
        this.data = model;

        // 出发地点
        newOrderModel.OriginBean originModel = data.getOrigin();
        newOrderModel.SiteBean siteModel = data.getSite();
        if (StringUtil.isNullOrEmpty(model.getUser().getName())) {
            model.getUser().setName("未知用户");
        }
        name.setText(model.getUser().getName());
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.default_head)
                .error(R.drawable.default_head)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.NONE);

        Glide.with(context).load(model.getUser().getHeadImage()).apply(options).into(imageView);
        dateTime.setText(data.getDatetime());
        origin.setText(originModel.getProvince()+originModel.getCity()+originModel.getCounty()+originModel.getAddress());
        site.setText(siteModel.getProvince()+siteModel.getCity()+siteModel.getCounty()+siteModel.getAddress());
        type.setText(data.getInfoType());
        Date createdate = DateUtils.strToDate(data.getCreateTime());
        time.setText(DateUtils.twoDateDistance(String.valueOf(createdate.getTime())));
        view.setVisibility(View.GONE);
        qd.setVisibility(View.GONE);
        btn.setOnClickListener(this);

    }

    /**
     * 司机抢单
     *
     * @param token 用户token
     * @param no    订单号
     */
    private void snatchOrder(String token, String no) {
        final Dialog dialog = DialogUIUtils.showMdLoading(context, "数据加载中", true, true, true, true).show();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).snatchOrder(token, no)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Response<ResponseBody> response) {
                        DialogUIUtils.dismiss(dialog);
                        switch (response.code()) {
                            case 200:
                                try {
                                    if (response.body() == null) {
                                        try {
                                            DialogUIUtils.showToast(JSON.parseObject(response.errorBody().string()).getString("message"));
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        String result = response.body().string();
                                        System.out.println("抢单返回数据" + result);
                                        JSONObject object = JSON.parseObject(result);
                                        if (object.getString("code").equals("success")) {
                                            DialogUIUtils.showToast(object.getString("message"));
                                            Intent intent = new Intent();
                                            intent.putExtra("RESULT_DATA", changeObject(JSON.parseObject(object.getString("data"), OrderModel.class)));
                                            intent.setClass(getContext(), YXOrderDetailActivity.class);
                                            getContext().startActivity(intent);
                                        } else {
                                            DialogUIUtils.showToast(object.getString("message"));
                                        }
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            default:
                                try {
                                    DialogUIUtils.showToast(JSON.parseObject(response.errorBody().string()).getString("message"));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_index_btn:
                snatchOrder(DemoApplication.getInstance().getMyToken(), data.getExtraInfoUUID());
                break;
            default:
                break;
        }
    }
}
