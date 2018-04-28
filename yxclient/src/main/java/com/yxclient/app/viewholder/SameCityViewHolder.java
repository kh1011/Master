package com.yxclient.app.viewholder;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIListener;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.yxclient.app.R;
import com.yxclient.app.activity.ImageBrowseActivity;
import com.yxclient.app.adapter.PostArticleImgAdapter;
import com.yxclient.app.app.DemoApplication;
import com.yxclient.app.http.RetrofitHttp;
import com.yxclient.app.listener.OnRecyclerItemClickListener;
import com.yxclient.app.model.bean.OldCarModel;
import com.yxclient.app.model.bean.UserModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 功能：
 * Created by yun.zhang on 2017/9/26 0026.
 * email:zy19930321@163.com
 */
public class SameCityViewHolder extends BaseViewHolder<OldCarModel> implements AdapterView.OnItemClickListener {

    private OldCarModel model;
    private CircleImageView civUserHeadimg;
    private TextView tvUserCName;
    private TextView tvTime;
    private TextView tvContent;
    private RecyclerView rcvImg;
    private TextView tvDelete;
    private PostArticleImgAdapter postArticleImgAdapter;

    public SameCityViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_same_city);
        civUserHeadimg = $(R.id.civ_user_headimg);
        tvUserCName = $(R.id.tv_user_c_name);
        tvTime = $(R.id.tv_time);
        rcvImg = $(R.id.rcv_img);
        tvDelete = $(R.id.tv_delete);
        tvContent = $(R.id.tv_content);
    }

    @Override
    public void setData(OldCarModel data) {
        super.setData(data);
        model = data;
        UserModel user = model.getCreateUser();
        if (user != null) {
            if (user.getHeadImage() != null) {
                Glide.with(getContext()).load(user.getHeadImage()).into(civUserHeadimg);
            }
            if (DemoApplication.getInstance().getUserId().equals(user.getUuid())) {
                tvDelete.setVisibility(View.VISIBLE);
            } else {
                tvDelete.setVisibility(View.GONE);
            }
            tvUserCName.setText(user.getName());
        } else {
            tvDelete.setVisibility(View.GONE);
        }
        tvTime.setText(model.getCreateTime());
        tvContent.setText(model.getContent());
        //为GridView设置适配器
        if (model.getImages() != null && model.getImages().length > 0) {
            initRcv(new ArrayList<String>(Arrays.asList(model.getImages())), new ArrayList<String>(Arrays.asList(model.getImages())));
        }
        //事件监听
        rcvImg.addOnItemTouchListener(new OnRecyclerItemClickListener(rcvImg) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh){
                // 查看大图
                handleBrowse(vh.getPosition(),model);
            }

            @Override
            public void onItemLongClick(RecyclerView.ViewHolder vh) {
                //如果item不是最后一个，则执行拖拽
                /*if (vh.getLayoutPosition() != dragImages.size() - 1) {
                    itemTouchHelper.startDrag(vh);
                }*/
            }
        });
        tvDelete.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DialogUIUtils.showToast(view.getTag().toString());
    }

    private void initRcv(List<String> dragImages, List<String> originImages) {
        postArticleImgAdapter = new PostArticleImgAdapter(getContext(), dragImages);
        rcvImg.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        rcvImg.setAdapter(postArticleImgAdapter);
    }

    /**
     * 删除同城信息
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
    public void handleBrowse(int position,OldCarModel oldCarModel) {
        ArrayList<String> imgs = new ArrayList<>();
        imgs.clear();
        for (int i=0;i<oldCarModel.getImages().length;i++) {

            imgs.add(oldCarModel.getImages()[i]);
        }

        Intent intent = new Intent(getContext(), ImageBrowseActivity.class);
        intent.putExtra("position", position);
        intent.putStringArrayListExtra("imgs", imgs);
        getContext().startActivity(intent);

    }
}
