package com.yxdriver.app.model;

import com.yxdriver.app.http.RetrofitHttp;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Callback;

/**
 * Created by mac on 2017/9/19.
 * 用户model
 */

public class UserSettingModel {
    /**
     * 修改用户信息
     *
     * @param token    用户token
     * @param callback
     * @param data
     */
    public static void uploadPricture(String token, String name, String password, String sex, String birthday, Callback<MultipartBody> callback, Map<String, RequestBody> data) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).uploadUserInfo(token,name,password,sex,birthday, data)
                .enqueue(callback);
    }

}
