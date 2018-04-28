package com.yxclient.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dou361.dialogui.DialogUIUtils;
import com.yxclient.app.R;
import com.yxclient.app.activity.YXAddIdeaActivity;
import com.yxclient.app.activity.YXBalanceActivity;
import com.yxclient.app.activity.YXCutomerActivity;
import com.yxclient.app.activity.YXLoginActivity;
import com.yxclient.app.activity.YXOldCarActivity;
import com.yxclient.app.activity.YXOrderManagerActivity;
import com.yxclient.app.activity.YXQueryActivity;
import com.yxclient.app.activity.YXSameCityActivity;
import com.yxclient.app.activity.YXSettingActivity;
import com.yxclient.app.activity.YXUserDocActivity;
import com.yxclient.app.activity.YXUserFavoriteActivity;
import com.yxclient.app.activity.YXVerificationActivity;
import com.yxclient.app.app.DemoApplication;
import com.yxclient.app.http.RetrofitHttp;
import com.yxclient.app.model.bean.UserModel;
import com.yxclient.app.utils.StringUtil;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;
import zuo.biao.library.base.BaseFragment;

/**
 * Created by jpeng on 16-11-14.
 * 我要
 */
public class UserPager extends BaseFragment {
    @BindView(R.id.profile_userimage)
    CircleImageView profileUserimage;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.btn_type)
    Button btnType;
    // 账户余额
    @BindView(R.id.user_blance_val)
    TextView tv_Blance;
    // 登录、注册
    @BindView(R.id.user_login_register)
    TextView tv_login;

    @BindView(R.id.user_info_ll)
    LinearLayout ll_user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //TODO demo_fragment改为你所需要的layout文件
        setContentView(R.layout.yx_activity_my);
        ButterKnife.bind(this, view);
        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
        return view;//返回值必须为view
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //加载数据
            if (!validateUser()) {
                getUserInfo(DemoApplication.getInstance().getMyToken());
            }
        }
    }

    @OnClick({R.id.user_customer, R.id.user_setting, R.id.user_verification, R.id.user_query,
            R.id.user_ll_order, R.id.profile_userimage, R.id.user_blance, R.id.my_colloect,
            R.id.user_oldcar, R.id.ll_same_city,R.id.user_add_idea})
    void myClick(View view) {
        switch (view.getId()) {
            case R.id.profile_userimage:
                // 个人中心
                if (!validateUser()) {
                    toActivity(YXUserDocActivity.createIntent(context));
                } else {
                    gotoLogin();
                }
                break;
            case R.id.user_blance:
                // 用户余额管理
                if (!validateUser()) {
                    toActivity(YXBalanceActivity.createIntent(context));
                } else {
                    gotoLogin();
                }
                break;
            case R.id.my_colloect:
                // 我的收藏
                if (!validateUser()) {
                    toActivity(YXUserFavoriteActivity.createIntent(context));
                } else {
                    gotoLogin();
                }
                break;
            case R.id.ll_same_city:
                // 同城信息
                if (!validateUser()) {
                    toActivity(YXSameCityActivity.createIntent(context));
                } else {
                    gotoLogin();
                }
                break;
            case R.id.user_oldcar:
                // 二手车
                if (!validateUser()) {
                    toActivity(YXOldCarActivity.createIntent(context, ""));
                } else {
                    gotoLogin();
                }
                break;
            case R.id.user_customer:
                // 联系客服
                toActivity(YXCutomerActivity.createIntent(context, ""));
                break;
            case R.id.user_setting:
                // 账户设置
                toActivity(YXSettingActivity.createIntent(context, ""));
                break;
            case R.id.user_verification:
                // 司机验证
                if (!validateUser()) {
                    toActivity(YXVerificationActivity.createIntent(context, ""));
                } else {
                    gotoLogin();
                }
                break;
            case R.id.user_query:
                // 违章查询
                if (!validateUser()) {
                    toActivity(YXQueryActivity.createIntent(context, ""));
                } else {
                    gotoLogin();
                }
                break;
            case R.id.user_ll_order:
                // 我的订单
                if (!validateUser()) {
                    toActivity(YXOrderManagerActivity.createIntent(context, ""));
                } else {
                    gotoLogin();
                }
                break;
            case R.id.user_add_idea:
                //反馈意见
                toActivity(YXAddIdeaActivity.createIntent(context,""));
                break;
            default:
                break;
        }
    }

    @Override
    public void initView() {
        if (validateUser()) {
            ll_user.setVisibility(View.GONE);
            tv_login.setVisibility(View.VISIBLE);
        } else {
            ll_user.setVisibility(View.VISIBLE);
            tv_login.setVisibility(View.GONE);
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoLogin();
            }
        });
    }

    /**
     * 用户验证
     *
     * @return
     */
    private boolean validateUser() {
        return StringUtil.isNullOrEmpty(DemoApplication.getInstance().getMyToken());
    }

    /**
     * 跳转到登录页面
     */
    private void gotoLogin() {
        toActivity(YXLoginActivity.createIntent(context));
    }

    /**
     * 获取用户信息
     *
     * @param token
     */
    private void getUserInfo(String token) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).getUserInfo(token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                System.out.println("xxx");
                if (response == null) {
                    DialogUIUtils.showToast(response.message());
                } else {
                    switch (response.code()) {
                        case 200:
                            try {
                                String result = response.body().string();
                                System.out.println("获取用户信息:" + result);
                                JSONObject jsonObject = JSON.parseObject(result);
                                if ("success".equals(jsonObject.getString("code"))) {
                                    getUserInfoSuccess(JSON.parseObject(jsonObject.getString("data"), UserModel.class));
                                } else {
                                    DialogUIUtils.showToast(jsonObject.getString("message"));
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        default:
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                DialogUIUtils.showToast(t.getMessage());
            }
        });
    }

    /**
     * 绘制页面
     *
     * @param model 用户对象
     */
    private void getUserInfoSuccess(UserModel model) {
        tv_login.setVisibility(View.GONE);
        ll_user.setVisibility(View.VISIBLE);
        tvName.setText(model.getName());
        if (model.getType() == 2) {
            btnType.setText("普通用户");
        } else {
            btnType.setText("顺心司机");
        }
        tv_Blance.setText(String.valueOf(model.getBalance()/100));
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.default_head)
                .error(R.drawable.default_head)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(context).load(model.getHeadImage()).apply(options).into(profileUserimage);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!validateUser()) {
            ll_user.setVisibility(View.VISIBLE);
            getUserInfo(DemoApplication.getInstance().getMyToken());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
