package com.yxdriver.app.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.dou361.dialogui.DialogUIUtils;
import com.yxdriver.app.R;
import com.yxdriver.app.activity.YXAddIdeaActivity;
import com.yxdriver.app.activity.YXBalanceActivity;
import com.yxdriver.app.activity.YXCutomerActivity;
import com.yxdriver.app.activity.YXOldCarActivity;
import com.yxdriver.app.activity.YXOrderManagerActivity;
import com.yxdriver.app.activity.YXQueryActivity;
import com.yxdriver.app.activity.YXSameCityActivity;
import com.yxdriver.app.activity.YXSetPersonalActivity;
import com.yxdriver.app.activity.YXSettingActivity;
import com.yxdriver.app.activity.YXUserDocActivity;
import com.yxdriver.app.activity.YXUserFavoriteActivity;
import com.yxdriver.app.activity.YXVerificationActivity;
import com.yxdriver.app.app.DemoApplication;
import com.yxdriver.app.http.RetrofitHttp;
import com.yxdriver.app.model.bean.UserModel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
    // 用户认证
    @BindView(R.id.ll_certified)
    LinearLayout ll_Certified;
    // 账户余额
    @BindView(R.id.user_blance_val)
    TextView tv_Blance;

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
            getUserInfo(DemoApplication.getInstance().getMyToken());
        }
    }

    @OnClick({R.id.user_customer, R.id.user_setting, R.id.user_verification,
            R.id.user_query, R.id.user_blance, R.id.profile_userimage, R.id.ll_certified,
            R.id.ll_same_city, R.id.ll_favorite, R.id.user_oldcar, R.id.ll_user_goods_list,
            R.id.user_updatecar,R.id.user_add_idea})
    void myClick(View view) {
        switch (view.getId()) {
            case R.id.user_blance:
                // 用户余额
                toActivity(YXBalanceActivity.createIntent(context));
                break;
            case R.id.profile_userimage:
                // 用户中心
                toActivity(YXUserDocActivity.createIntent(context));
                break;
            case R.id.user_customer://客服中心
                toActivity(YXCutomerActivity.createIntent(context, ""));
                break;
            case R.id.ll_user_goods_list:
                // 订单管理
                toActivity(YXOrderManagerActivity.createIntent(context, ""));
                break;
            case R.id.user_setting:
                // 用户设置
                toActivity(YXSettingActivity.createIntent(context, ""));
                break;
            case R.id.user_verification:
                // 验证司机
                toActivity(YXVerificationActivity.createIntent(context, ""));
                break;
            case R.id.user_query:
                // 违章查询
                toActivity(YXQueryActivity.createIntent(context, ""));
                break;
            case R.id.ll_certified:
                // 去认证
                Map<String, String> extras = new HashMap<>();
                extras.put(YXSetPersonalActivity.EXTRAL_KEY1, "UserPager");
                toActivity(YXSetPersonalActivity.createIntent(context));
                break;
            case R.id.ll_same_city:
                // 同城信息
                toActivity(YXSameCityActivity.createIntent(context));
                break;
            case R.id.user_oldcar:
                // 二手车
                toActivity(YXOldCarActivity.createIntent(context, ""));
                break;
            case R.id.ll_favorite:
                // 我的收藏
                toActivity(YXUserFavoriteActivity.createIntent(context));
                break;
            case R.id.user_updatecar:
                toActivity(YXSetPersonalActivity.createIntent(context));
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
    }

    @Override
    public void initData() {
        //
    }

    @Override
    public void initEvent() {

    }

    /**
     * 获取用户信息
     *
     * @param token usertoken
     */
    private void getUserInfo(String token) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).getUserInfo(token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
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
                                    Log.d("onResponse: ",jsonObject.getString("data"));
                                    getUserInfoSuccess(JSON.parseObject(jsonObject.getString("data"), UserModel.class));
                                } else {
                                    DialogUIUtils.showToast(jsonObject.getString("message"));
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        default:
                            DialogUIUtils.showToast("数据获取失败");
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
        if (model.getVerify() == 1) {
            ll_Certified.setVisibility(View.GONE);
        }
        if (model.getType() == 2) {
            btnType.setText("普通用户");
        } else {
            btnType.setText("顺心司机");
        }
        tvName.setText(model.getAlias());
        tv_Blance.setText(String.valueOf(model.getBalance()/100));
        Glide.with(context).load(model.getHeadImage()).into(profileUserimage);
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserInfo(DemoApplication.getInstance().getMyToken());
    }
}
