package com.yxdriver.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dou361.dialogui.DialogUIUtils;
import com.yxdriver.app.R;
import com.yxdriver.app.app.DemoApplication;
import com.yxdriver.app.http.RetrofitHttp;
import com.yxdriver.app.model.bean.UserModel;
import com.yxdriver.app.utils.PhoneUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;
import zuo.biao.library.base.BaseActivity;

/**
 * Created by mac on 2017/11/11.
 * ////////////////////////////////////////////////////////////////////
 * //                            _ooOoo_                             //
 * //                           o8888888o                            //
 * //                           88" . "88                            //
 * //                           (| ^_^ |)                            //
 * //                           O\  =  /O                            //
 * //                        ____/`---'\____                         //
 * //                      .'  \\|     |//  `.                       //
 * //                     /  \\|||  :  |||//  \                      //
 * //                    /  _||||| -:- |||||-  \                     //
 * //                    |   | \\\  -  /// |   |                     //
 * //                    | \_|  ''\---/''  |   |                     //
 * //                    \  .-\__  `-`  ___/-. /                     //
 * //                  ___`. .'  /--.--\  `. . ___                   //
 * //                ."" '<  `.___\_<|>_/___.'  >'"".                //
 * //              | | :  `- \`.;`\ _ /`;.`/ - ` : | |               //
 * //              \  \ `-.   \_ __\ /__ _/   .-` /  /               //
 * //        ========`-.____`-.___\_____/___.-`____.-'========       //
 * //                             `=---='                            //
 * //        ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^      //
 * //         佛祖保佑       永无BUG        永不修改                    //
 * ////////////////////////////////////////////////////////////////////
 */

public class YXChangePhoneActivity extends BaseActivity {
    private UserModel userModel;
    @BindView(R.id.change_phone_number)
    TextView tv_phoneNumber;


    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_chanage_phone);
        ButterKnife.bind(this);
        initView();
        initData();
        initEvent();
    }

    public static Intent createIntent(Context context, String data) {
        return new Intent(context, YXChangePhoneActivity.class).
                putExtra(RESULT_DATA, data);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        getUserInfo(DemoApplication.getInstance().getMyToken());
    }

    @Override
    public void initEvent() {

    }

    @OnClick({R.id.change_phone_btn})
    void myClick(View view) {
        switch (view.getId()) {
            case R.id.change_phone_btn:
                // 修改手机号码
                toActivity(YXSetNewPhoneActivity.createIntent(context, ""));
                finish();
                break;
            default:
                break;
        }
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
                    DialogUIUtils.showToast("请求错误!");
                } else {
                    switch (response.code()) {
                        case 200:
                            try {
                                String result = response.body().string();
                                System.out.println("获取用户信息:" + result);
                                JSONObject jsonObject = JSON.parseObject(result);
                                if ("success".equals(jsonObject.getString("code"))) {
                                    userModel = JSON.parseObject(jsonObject.getString("data"), UserModel.class);
                                    tv_phoneNumber.setText(PhoneUtils.startNumber(userModel.getMobile()));
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

}
