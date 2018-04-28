package com.yxclient.app.viewholder;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIListener;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.yxclient.app.R;
import com.yxclient.app.app.DemoApplication;
import com.yxclient.app.http.RetrofitHttp;
import com.yxclient.app.model.bean.OldCarModel;
import com.yxclient.app.model.bean.UserModel;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 商城中商品列表的viewholder
 * <p>
 * Created by zhangyun on 2017/9/29 0029
 * EMail -> yun.zhang@chinacreator.com
 */
public class CarListViewHoder extends BaseViewHolder<OldCarModel> {
    private OldCarModel model;
    TextView title;
    ImageView imageView;
    TextView content;
    TextView price;
    TextView delete;

    public CarListViewHoder(ViewGroup parent) {
        super(parent, R.layout.item_old_car);
        title = $(R.id.item_car_title);
        imageView = $(R.id.item_car_img);
        content = $(R.id.item_car_content);
        price = $(R.id.item_car_price);
        delete = $(R.id.item_car_delete);
    }

    @Override
    public void setData(OldCarModel data) {
        super.setData(data);
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.icon_car_img)
                .error(R.drawable.icon_car_img)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        model = data;
        UserModel userModel = model.getCreateUser();
        if (DemoApplication.getInstance().getUserId().equals(userModel.getUuid())) {
            delete.setVisibility(View.VISIBLE);
        } else {
            delete.setVisibility(View.GONE);
        }
        title.setText(model.getTitle());
        content.setText(model.getContent());
        price.setText(String.format("¥ %s", model.getPrice() / 100.0));
        if (model.getImages() != null && model.getImages().length > 0) {
            Glide.with(getContext()).load(model.getImages()[0]).apply(options).into(imageView);
        }
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 调用删除信息
                DialogUIUtils.showMdAlert((Activity) getContext(), "提示", "确定删除当前信息吗？", new DialogUIListener() {
                    @Override
                    public void onPositive() {
                        // 确定删除
                        doDelete(DemoApplication.getInstance().getMyToken(), model.getUuid());
                        getOwnerAdapter().notifyItemRemoved(getAdapterPosition());
                    }

                    @Override
                    public void onNegative() {

                    }

                }).show();

            }
        });
    }

    /**
     * 删除二手车信息
     *
     * @param token  用户token
     * @param infoId 信息ID
     */
    private void doDelete(String token, String infoId) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).deleteInfo(token, infoId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                switch (response.code()) {
                    case 200:
                        try {
                            if (response.body() == null) {
                                DialogUIUtils.showToast(response.message());
                            } else {
                                String result = response.body().string();
                                System.out.println("删除信息数据" + result);
                                JSONObject jsonObject = JSON.parseObject(result);
                                if (jsonObject.getString("code").equals("success")) {
                                    // 提示删除信息成功
                                    DialogUIUtils.showToast(jsonObject.getString("message"));
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
}
