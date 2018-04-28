package com.yxclient.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIListener;
import com.yxclient.app.R;
import com.yxclient.app.adapter.InfoCommentAdapter;
import com.yxclient.app.adapter.PostArticleImgAdapter;
import com.yxclient.app.app.DemoApplication;
import com.yxclient.app.http.RetrofitHttp;
import com.yxclient.app.listener.OnRecyclerItemClickListener;
import com.yxclient.app.model.bean.InfoEvaluationsModel;
import com.yxclient.app.model.bean.OldCarModel;
import com.yxclient.app.utils.StringUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
 * 功能：同城信息、二手车信息详情
 * Created by yun.zhang on 2017/9/24 0024.
 * email:zy19930321@163.com
 */
public class YXInfoDetailActivity extends BaseActivity {
    private OldCarModel oldCarModel;
    @BindView(R.id.textView3)
    TextView title;
    // 标题
    @BindView(R.id.info_detail_title)
    TextView tv_name;
    // 创建者
    @BindView(R.id.info_detail_creater)
    TextView tv_creater;
    // 信息发布日期
    @BindView(R.id.info_detail_datetime)
    TextView tv_datetime;
    // 信息内容
    @BindView(R.id.info_detail_content)
    TextView tv_content;
    // 价格
    @BindView(R.id.info_detail_price)
    TextView tv_price;
    // 评论内容
    @BindView(R.id.info_detail_comment)
    EditText ed_Comment;

    // 图片列表
    @BindView(R.id.rcv_img)
    RecyclerView rcvImg;
    // 评论列表
    @BindView(R.id.rcv_comment)
    RecyclerView rcvComment;

    @BindView(R.id.right_btn)
    Button btn_Del;
    // 信息图片集合
    private ArrayList<String> originImages;
    // 评论集合
    private List<InfoEvaluationsModel> list = new ArrayList<>();
    // 评论列表适配器
    private InfoCommentAdapter commentAdapter;

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_info_detail);
        ButterKnife.bind(this);
        initView();
        initData();
        initEvent();
    }


    public static Intent createIntent(Context context, OldCarModel data) {
        return new Intent(context, YXInfoDetailActivity.class).
                putExtra(RESULT_DATA, data);
    }

    @Override
    public void initView() {
        rcvComment.setLayoutManager(new LinearLayoutManager(context));
        rcvComment.addItemDecoration(new DividerItemDecoration(
                context, DividerItemDecoration.VERTICAL));
        commentAdapter = new InfoCommentAdapter(context, list);
        rcvComment.setAdapter(commentAdapter);
    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        if (StringUtil.isNullOrEmpty(bundle.getString("flag"))) {
            btn_Del.setVisibility(View.GONE);
            oldCarModel = (OldCarModel) getIntent().getSerializableExtra(RESULT_DATA);
        } else {
            btn_Del.setVisibility(View.VISIBLE);
            oldCarModel = (OldCarModel) bundle.getSerializable("info");
        }
        // 判断是不是自己的
        //drawaView(oldCarModel);
        getInfo(DemoApplication.getInstance().getMyToken(), oldCarModel.getUuid());
    }

    @Override
    public void initEvent() {

    }

    private void drawaView(OldCarModel model) {
        assert model != null;
        if (model.getImages() != null) {
            originImages = new ArrayList<>(Arrays.asList(model.getImages()));
            initRcv();
        }
        title.setText(model.getTitle());
        tv_content.setText(model.getContent());
        tv_name.setText(model.getTitle());
        tv_creater.setText(model.getCreateUser().getName());
        tv_datetime.setText(model.getCreateTime());
        if (!model.getType().equals("car")) {
            tv_price.setVisibility(View.GONE);
        } else {
            tv_price.setText(model.getNote());//String.format("¥ %s", model.getPrice() / 100.0)
        }
        // 判断是否有评论列表
        if (model.getEvaluations() != null) {
            list.clear();
            List<InfoEvaluationsModel> modelList = model.getEvaluations();
            list.addAll(modelList);
            commentAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onForwardClick(View v) {
        super.onForwardClick(v);
        // 删除
        DialogUIUtils.showMdAlert(context, "提示", "确定删除当前信息吗？", new DialogUIListener() {
            @Override
            public void onPositive() {
                // 确定删除
                deleteInfo(DemoApplication.getInstance().getMyToken(), oldCarModel.getUuid());
            }

            @Override
            public void onNegative() {
                // 取消注销
            }

        }).show();
    }

    private void initRcv() {
        PostArticleImgAdapter postArticleImgAdapter = new PostArticleImgAdapter(context, originImages);
        rcvImg.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL) {
            @Override
            public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
                if (getChildCount() > 0) {
                    View firstChildView = recycler.getViewForPosition(0);
                    measureChild(firstChildView, widthSpec, heightSpec);
                    setMeasuredDimension(View.MeasureSpec.getSize(widthSpec), firstChildView.getMeasuredHeight() * 3);
                } else {
                    super.onMeasure(recycler, state, widthSpec, heightSpec);
                }
            }
        });
        rcvImg.setAdapter(postArticleImgAdapter);
        //事件监听
        rcvImg.addOnItemTouchListener(new OnRecyclerItemClickListener(rcvImg) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                // 查看大图
                //Intent intent = new Intent();
                //intent.setClass(context, PhotoviewActivity.class);
                //Bundle bundle = new Bundle();
                //bundle.putSerializable("RESULT_DATA", oldCarModel);
                //intent.putExtras(bundle);
                //startActivity(intent);
                handleBrowse(vh.getPosition(),oldCarModel);
            }

            @Override
            public void onItemLongClick(RecyclerView.ViewHolder vh) {
                //如果item不是最后一个，则执行拖拽
                /*if (vh.getLayoutPosition() != dragImages.size() - 1) {
                    itemTouchHelper.startDrag(vh);
                }*/
            }
        });
    }

    private void deleteInfo(String token, String id) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).deleteInfo(token, id)
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
                                        System.out.println("删除信息数据" + result);
                                        JSONObject jsonObject = JSON.parseObject(result);
                                        if (jsonObject.getString("code").equals("success")) {
                                            DialogUIUtils.showToast(jsonObject.getString("message"));
                                            onBackPressed();
                                            finish();
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

    private void getInfo(String token, String id) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).getInfoDetail(token, id)
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
                                        System.out.println("信息数据" + result);
                                        JSONObject jsonObject = JSON.parseObject(result);
                                        if (jsonObject.getString("code").equals("success")) {
                                            oldCarModel = JSON.parseObject(jsonObject.getString("data"), OldCarModel.class);
                                            drawaView(oldCarModel);
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

    @OnClick({R.id.info_detail_btn})
    void myClick(View view) {
        switch (view.getId()) {
            case R.id.info_detail_btn:
                String comment = ed_Comment.getText().toString();
                if (StringUtil.isNullOrEmpty(comment)) {
                    DialogUIUtils.showToast("请输入评论内容");
                } else {
                    commentInfo(DemoApplication.getInstance().getMyToken(), oldCarModel.getUuid(), comment);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 评论信息
     *
     * @param token   用户token
     * @param uuid    信息ID
     * @param content 信息内容
     */
    private void commentInfo(String token, String uuid, String content) {
        JSONObject object = new JSONObject();
        object.put("newsUUID", uuid);
        object.put("content", content);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).commentInfo(token, StringUtil.getBody(object))
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
                                        System.out.println("信息数据" + result);
                                        JSONObject jsonObject = JSON.parseObject(result);
                                        if (jsonObject.getString("code").equals("success")) {
                                            DialogUIUtils.showToast(jsonObject.getString("message"));
                                            getInfo(DemoApplication.getInstance().getMyToken(), oldCarModel.getUuid());
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

    public void handleBrowse(int position,OldCarModel oldCarModel) {
        ArrayList<String> imgs = new ArrayList<>();

        for (int i=0;i<oldCarModel.getImages().length;i++) {

            imgs.add(oldCarModel.getImages()[i]);
        }

        Intent intent = new Intent(context, ImageBrowseActivity.class);
        intent.putExtra("position", position);
        intent.putStringArrayListExtra("imgs", imgs);
        startActivity(intent);

    }
}