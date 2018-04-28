package com.yxdriver.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dou361.dialogui.DialogUIUtils;
import com.hys.utils.AppUtils;
import com.yxdriver.app.DemoService;
import com.yxdriver.app.R;
import com.yxdriver.app.app.DemoApplication;
import com.yxdriver.app.http.RetrofitHttp;
import com.yxdriver.app.model.bean.AppVersionModel;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.versionchecklib.core.AllenChecker;
import zuo.biao.library.versionchecklib.core.VersionParams;

/**
 * 功能：
 * Created by yun.zhang on 2017/9/24 0024.
 * email:zy19930321@163.com
 */
public class YXVersionActivity extends BaseActivity {

    @BindView(R.id.versionname)
    TextView tv_versionname;

    private AppVersionModel versionModel;

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_version);
        ButterKnife.bind(this);
        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }
    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    public static Intent createIntent(Context context, String data) {
        return new Intent(context, YXVersionActivity.class).
                putExtra(RESULT_DATA, data);
    }

    @Override
    public void initView() {
        tv_versionname.setText("当前版本 V"+ AppUtils.getVersionName(context));
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }
    @OnClick({R.id.version_btn})
    void myClick(View view) {
        switch (view.getId()) {
            case R.id.version_btn:
                //int curentCode = AppUtils.getAppVersionCode(context);
                //String curentName = AppUtils.getVersionName(context);
                getServiceAppVersion(DemoApplication.getInstance().getMyToken());
                break;
            default:
                break;
        }
    }

    private void getServiceAppVersion(String token) {
        final Dialog dialog = DialogUIUtils.showMdLoading(context, "数据加载中", false, true, true, true).show();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).getAppVersion(token,"driver")
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Response<ResponseBody> response) {
                        DialogUIUtils.dismiss(dialog);
                        switch (response.code()) {
                            case 200:
                                try {
                                    if (response.body() == null) {
                                        DialogUIUtils.showToast(response.message());
                                    } else {
                                        String result = response.body().string();
                                        System.out.println(result);
                                        JSONObject jsonObject = JSON.parseObject(result);
                                        if (jsonObject.getString("code").equals("success")) {
                                            versionModel = JSON.parseObject(jsonObject.getString("data"), AppVersionModel.class);
                                            drawaView(versionModel);
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
                        DialogUIUtils.dismiss(dialog);
                        DialogUIUtils.showToast(t.getMessage());
                    }
                });
    }

    private void drawaView(AppVersionModel model) {
        int curentCode = AppUtils.getAppVersionCode(context);
        if (model.getVersionCode() > curentCode) {
            // 提示更新
            VersionParams.Builder builder = new VersionParams.Builder()
//                .setHttpHeaders(headers)
//                .setRequestMethod(requestMethod)
//                .setRequestParams(httpParams)
                    .setRequestUrl("http://www.baidu.com")
//                .setDownloadAPKPath(getApplicationContext().getFilesDir()+"/")
                    .setService(DemoService.class);
            stopService(new Intent(this, DemoService.class));
            CustomVersionDialogActivity.customVersionDialogIndex = 2;
            builder.setCustomDownloadActivityClass(CustomVersionDialogActivity.class);
            // 下载界面
            CustomVersionDialogActivity.isCustomDownloading = true;
            builder.setCustomDownloadActivityClass(CustomVersionDialogActivity.class);
            // 下载:设置下载地址
            builder.setOnlyDownload(true)
                    .setDownloadUrl(model.getUploadUrl())
                    .setTitle("检测到新版本")
                    .setUpdateMsg(model.getAppDesc());
            // 显示在通知栏
            builder.setShowNotification(true);
            AllenChecker.startVersionCheck(this, builder.build());
        }else {
            DialogUIUtils.showToast("暂无新版本");
        }

    }
}