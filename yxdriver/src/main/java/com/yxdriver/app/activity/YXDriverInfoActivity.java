package com.yxdriver.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.yxdriver.app.R;
import com.yxdriver.app.model.bean.UserModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import zuo.biao.library.base.BaseActivity;

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
    UserModel userModel;

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


    /**
     * 启动这个Activity的Intent
     *
     * @param context
     * @param data
     * @return
     */
    public static Intent createIntent(Context context, UserModel data) {
        return new Intent(context, YXDriverInfoActivity.class).
                putExtra(RESULT_DATA, data);
    }

    @Override
    public void initView() {
        userModel = (UserModel) getIntent().getSerializableExtra(RESULT_DATA);
        name.setText(userModel.getName());
        mobile.setText(userModel.getMobile());
        Glide.with(context).load(userModel.getHeadImage()).into(imageView);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }

}