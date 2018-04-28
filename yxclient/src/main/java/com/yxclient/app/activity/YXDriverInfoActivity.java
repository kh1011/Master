package com.yxclient.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iflytek.cloud.thirdparty.S;
import com.yxclient.app.R;
import com.yxclient.app.adapter.PostArticleImgAdapter;
import com.yxclient.app.listener.OnRecyclerItemClickListener;
import com.yxclient.app.model.bean.UserModel;
import com.yxclient.app.model.bean.verifyDriverModel;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import zuo.biao.library.base.BaseActivity;

import static com.yxclient.app.R.id.driver_carNo;

/**
 * 功能：司机信息详情
 * Created by yun.zhang on 2017/9/24 0024.
 * email:zy19930321@163.com
 */
public class YXDriverInfoActivity extends BaseActivity {
    @BindView(R.id.profile_userimage)
    CircleImageView imageView;
    @BindView(R.id.driver_name)
    TextView name;
    @BindView(R.id.driver_mobile)
    TextView mobile;
    @BindView(R.id.driver_carNo)
    TextView code;
    verifyDriverModel cardriverModel;
    @BindView(R.id.rcv_img)
    RecyclerView rcvImg;
    @BindView(R.id.car_img)
    TextView tv_img;
    // 图片适配器
    private PostArticleImgAdapter postArticleImgAdapter;
    private ArrayList<String> originImages;

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_driver_info);
        ButterKnife.bind(this);
        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    public static Intent createIntent(Context context, verifyDriverModel data) {
        return new Intent(context, YXDriverInfoActivity.class).
                putExtra(RESULT_DATA, data);
    }

    @Override
    public void initView() {
        cardriverModel = (verifyDriverModel) getIntent().getSerializableExtra(RESULT_DATA);
        // 测试数据
        cardriverModel.getDriver().getCardImages();
        String[] images = new String[1];
        images[0] = "http://ohyfvxowv.bkt.clouddn.com/image/shunH-APP/add/home/home1_2x.png";
        //images[1] = "http://ohyfvxowv.bkt.clouddn.com/image/shunH-APP/add/home/home1_2x.png";
        //images[2] = "http://ohyfvxowv.bkt.clouddn.com/image/shunH-APP/add/home/home1_2x.png";
        String[]  img=cardriverModel.getCar().getCarImages();
        if (img==null){
            originImages = new ArrayList<String>(Arrays.asList(images));
            rcvImg.setVisibility(View.GONE);
            tv_img.setVisibility(View.VISIBLE);
        }else {
            originImages = new ArrayList<String>(Arrays.asList(cardriverModel.getCar().getCarImages()));
        }

        initRcv();
        name.setText(cardriverModel.getDriver().getName());
        mobile.setText(cardriverModel.getDriver().getMobile());
        if (cardriverModel.getCar()==null){
            code.setText("未知");
        }else {
            code.setText(cardriverModel.getCar().getPlateNumber());
        }
        String headimg=cardriverModel.getDriver().getHeadImage();
        if (headimg==null){
            Glide.with(context).load("http://ohyfvxowv.bkt.clouddn.com/image/shunH-APP/add/home/home1_2x.png").into(imageView);
        }else {
            Glide.with(context).load(headimg).into(imageView);
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }

    private void initRcv() {
        postArticleImgAdapter = new PostArticleImgAdapter(context, originImages);
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

}