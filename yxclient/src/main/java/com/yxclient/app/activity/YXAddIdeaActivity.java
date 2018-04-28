package com.yxclient.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dou361.dialogui.DialogUIUtils;
import com.iflytek.cloud.thirdparty.V;
import com.yxclient.app.R;
import com.yxclient.app.app.DemoApplication;
import com.yxclient.app.http.RetrofitHttp;
import com.yxclient.app.utils.StringUtil;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;
import zuo.biao.library.base.BaseActivity;

public class YXAddIdeaActivity extends BaseActivity {

    public static Intent createIntent(Context context, String data) {
        return new Intent(context, YXAddIdeaActivity.class).
                putExtra(RESULT_DATA, data);
    }

    @BindView(R.id.add_itea_content)
    EditText ed_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_add_idea);
        ButterKnife.bind(this);
        initView();
        initData();
        initEvent();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }

    @OnClick({R.id.submit_btn})
    void click(View v){
        switch (v.getId()){
            case R.id.submit_btn:
                String idea=ed_content.getText().toString();
                if (StringUtil.isNullOrEmpty(idea)){
                    DialogUIUtils.showToast("反馈意见不能为空");
                }else {
                    setIdea(DemoApplication.getInstance().getMyToken(),idea);
                }
                break;
        }
    }
    /**
     * 提交反馈意见
     *
     * @param token   用户token
     * @param content 反馈意见
     */
    private void setIdea(String token,String content) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        JSONObject object = new JSONObject();
        object.put("content", content);
        RetrofitHttp.getRetrofit(builder.build()).setIdea(token, StringUtil.getBody(object))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Response<ResponseBody> response) {
                        try {
                            switch (response.code()) {
                                case 200:
                                    if (response.body() == null) {
                                        DialogUIUtils.showToast(response.message());
                                    } else {
                                        String result = response.body().string();
                                        JSONObject jsonObject = JSON.parseObject(result);
                                        if (jsonObject.getString("code").equals("success")) {
                                            // 提交反馈意见成功
                                            DialogUIUtils.showToast(jsonObject.getString("message"));
                                            onBackPressed();
                                            finish();
                                        } else {
                                            DialogUIUtils.showToast(jsonObject.getString("message"));
                                        }
                                    }
                                    break;
                                default:
                                    String err=response.errorBody().string();
                                    DialogUIUtils.showToast(JSON.parseObject(err).getString("message"));
                                    break;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        DialogUIUtils.showToast(t.getMessage());
                    }
                });
    }


}
