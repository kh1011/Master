package com.yxdriver.app.viewholder;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.yxdriver.app.model.bean.OrderModel;
import com.yxdriver.app.model.bean.newOrderModel;
import com.yxdriver.app.utils.DateUtils;

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

public class IndexViewHolder extends BaseViewHolder<newOrderModel> implements View.OnClickListener {
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
    private TextView weight;
    private TextView number;
    private Button btn;
    private TextView fragile;

    public IndexViewHolder(ViewGroup parent, Context context) {
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
        weight = $(R.id.item_index_weight);
        btn = $(R.id.item_index_btn);
        number = $(R.id.item_index_number);
        fragile=$(R.id.item_index_fragile);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void setData(newOrderModel model) {
        super.setData(model);


        this.data = model;
            // 下单客户信息
            //customerModel = data.getInfo().get(0);
            // 下单用户
        number.setVisibility(View.GONE);
        weight.setVisibility(View.GONE);
        fragile.setVisibility(View.GONE);
        number.setText(String.format("乘车人数：%s", String.valueOf(model.getNumber())));
        // 出发地点
        newOrderModel.OriginBean originModel = data.getOrigin();
        newOrderModel.SiteBean siteModel = data.getSite();
        name.setText(data.getUser().getName());
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.default_head)
                .error(R.drawable.default_head)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.NONE);

        Glide.with(context).load(data.getUser().getHeadImage()).apply(options).into(imageView);
        dateTime.setText(data.getDatetime());
        origin.setText(originModel.getProvince()+originModel.getCity()+originModel.getCounty()+originModel.getAddress());
        site.setText(siteModel.getProvince()+siteModel.getCity()+siteModel.getCounty()+siteModel.getAddress());
        type.setText(data.getInfoType());
        Date createdate = DateUtils.strToDate(data.getCreateTime());
        time.setText(DateUtils.twoDateDistance(String.valueOf(createdate.getTime())));
        if (data.getOrderType().equals("express")) {
            if (data.getInfoType().equals("快递小件")){
                //number.setVisibility(View.GONE);
                //weight.setVisibility(View.GONE);
                fragile.setVisibility(View.VISIBLE);
                fragile.setText(getFragile(data.getFragile())+"  "+getWet(data.getWet()));
            }else {
                //number.setVisibility(View.GONE);
                weight.setVisibility(View.VISIBLE);
                weight.setText(String.format("货物重量：%s" + "千克", String.valueOf(data.getWeight())));
                //fragile.setVisibility(View.GONE);
            }
        } else {
            //weight.setVisibility(View.GONE);
            number.setVisibility(View.VISIBLE);
            //fragile.setVisibility(View.GONE);
        }
        btn.setOnClickListener(this);
    }
    private String getFragile(int status){
        String fragile;
        if (status==1){
            fragile="易碎";
        }else {
            fragile="非易碎";
        }
        return fragile;
    }
    private String getWet(int status){
        String wet;
        if (status==1){
            wet="易湿";
        }else {
            wet="非易湿";
        }
        return wet;
    }

    /**
     * 司机抢单
     *
     * @param token 用户token
     * @param no    订单号express
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
                                            intent.putExtra("RESULT_DATA", changeObject(JSON.parseObject(object.getString("data"), OrderModel.class)) );
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
                getCarInfo(data);
                break;
            default:
                break;
        }
    }

    /**
     * 获取车辆信息
     *
     */
    private void getCarInfo(final newOrderModel data_) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).getCarInfo(DemoApplication.getInstance().getMyToken())
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
                                        System.out.println("发布订单列表数据" + result);
                                        JSONObject jsonObject = JSON.parseObject(result);
                                        if (jsonObject.getString("code").equals("success")) {
                                            JSONObject data = JSON.parseObject(jsonObject.getString("data"));
                                            if (data.getString("type").equals("面包车")||data.getString("type").equals("轿车")||
                                                    data.getString("type").equals("SUV")||data.getString("type").equals("MPV")) {
                                                    if (data_.getInfoType().equals("顺风车")||data_.getInfoType().equals("快递小件")||data_.getInfoType().equals("专车")){
                                                        snatchOrder(DemoApplication.getInstance().getMyToken(), data_.getExtraInfoUUID());
                                                    }else {
                                                        DialogUIUtils.showToast("司机类型不符");
                                                    }
                                            }else if (data.getString("type").equals("微型货车")||data.getString("type").equals("轻型货车")||
                                                    data.getString("type").equals("中型货车")||data.getString("type").equals("大型货车")){
                                                if (data_.getInfoType().equals("小型货运")||data_.getInfoType().equals("快递小件")||data_.getInfoType().equals("大型货运")){
                                                    snatchOrder(DemoApplication.getInstance().getMyToken(), data_.getExtraInfoUUID());
                                                }else {
                                                    DialogUIUtils.showToast("司机类型不符");
                                                }

                                            }else {
                                                DialogUIUtils.showToast("错误");
                                            }
                                            //snatchOrder(DemoApplication.getInstance().getMyToken(), data_.getNo());
                                        } else {
                                            DialogUIUtils.showToast(jsonObject.getString("message"));
                                        }
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            default:
                                String err= null;
                                try {
                                    err = response.errorBody().string();
                                    DialogUIUtils.showToast(JSON.parseObject(err).getString("message"));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                break;
                        }
                    }
                    @Override
                    public void onFailure(Throwable t) {
                        DialogUIUtils.showToast(t.getMessage());
                    }
                });
    }
}
