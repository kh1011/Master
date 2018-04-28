package com.yxdriver.app.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dou361.dialogui.DialogUIUtils;
import com.hys.utils.AppUtils;
import com.jpeng.jptabbar.BadgeDismissListener;
import com.jpeng.jptabbar.JPTabBar;
import com.jpeng.jptabbar.OnTabSelectListener;
import com.jpeng.jptabbar.anno.NorIcons;
import com.jpeng.jptabbar.anno.SeleIcons;
import com.jpeng.jptabbar.anno.Titles;
import com.jude.utils.JActivityManager;
import com.yxdriver.app.DemoService;
import com.yxdriver.app.R;
import com.yxdriver.app.adapter.Adapter;
import com.yxdriver.app.app.DemoApplication;
import com.yxdriver.app.fragment.IndexPager;
import com.yxdriver.app.fragment.ShoppingPager;
import com.yxdriver.app.fragment.TransportPager;
import com.yxdriver.app.fragment.UserPager;
import com.yxdriver.app.http.RetrofitHttp;
import com.yxdriver.app.model.bean.AppVersionModel;
import com.yxdriver.app.utils.ExitUtils;
import com.yxdriver.app.utils.StringUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.versionchecklib.core.AllenChecker;
import zuo.biao.library.versionchecklib.core.VersionParams;

import static com.yxdriver.app.R.id.tabbar;

/**
 * Created by mac on 2017/7/3.
 * 主页
 */

public class YXMainActivity extends BaseActivity implements BadgeDismissListener, OnTabSelectListener {
    private ExitUtils exit = new ExitUtils();
    public static boolean isForeground = false;
    @Titles
    private static final String[] mTitles = {"出行", "订单", "商城", "我要"};
    @SeleIcons
    private static final int[] mSeleIcons = {R.drawable.tab1_selected, R.drawable.tab2_selected, R.drawable.tab3_selected, R.drawable.tab4_selected};

    @NorIcons
    private static final int[] mNormalIcons = {R.drawable.tab1_normal, R.drawable.tab2_normal, R.drawable.tab3_normal, R.drawable.tab4_normal};

    private List<Fragment> list = new ArrayList<>();

    private ViewPager mPager;

    private JPTabBar mTabbar;

    private IndexPager mTab1;

    private TransportPager mTab2;

    private ShoppingPager mTab3;

    private UserPager mTab4;

    private AppVersionModel versionModel;
    @Override
    public Activity getActivity() {
        return this;
    }

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**
     * 启动这个Activity的Intent
     *
     * @param context
     * @return
     */
    public static Intent createIntent(Context context) {
        return new Intent(context, YXMainActivity.class);
    }
    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_main);
        if (shouldAskPermissions()) {
            askPermissions();
        }
        String token = DemoApplication.getInstance().getMyToken();
        mTabbar = (JPTabBar) findViewById(tabbar);
        mPager = (ViewPager) findViewById(R.id.view_pager);
        mTabbar.setTitles("qwe", "asd", "qwe", "asdsa").setNormalIcons(R.drawable.tab1_normal, R.drawable.tab2_normal, R.drawable.tab3_normal, R.drawable.tab4_normal)
                .setSelectedIcons(R.drawable.tab1_selected, R.drawable.tab2_selected, R.drawable.tab3_selected, R.drawable.tab4_selected).generate();
        mTab1 = new IndexPager();
        mTab2 = new TransportPager();
        mTab3 = new ShoppingPager();
        mTab4 = new UserPager();
        list.add(mTab1);
        list.add(mTab2);
        list.add(mTab3);
        list.add(mTab4);
        mPager.setAdapter(new Adapter(getSupportFragmentManager(), list));
        mTabbar.setContainer(mPager);
        mTabbar.setDismissListener(this);
        //显示圆点模式的徽章
        //设置容器
        mTabbar.showBadge(0, 50);
        mTabbar.hideBadge(0);
        //设置Badge消失的代理
        mTabbar.setTabListener(this);
        initView();
        initData();
        initEvent();
        getServiceAppVersion(DemoApplication.getInstance().getMyToken());
    }

    @Override
    public void initView() {
        setTitle("主页");
        // 接收自定义消息（即时通讯）
        registerMessageReceiver();  // used for receive msg
        String testId = JPushInterface.getRegistrationID(this);
        System.out.println("Main极光注册ID：" + testId);
        // 上传极光注册ID
        uploadID(DemoApplication.getInstance().getMyToken(), testId, "");
    }

    @Override
    public void initData() {
        // i will give you some color see see
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void onDismiss(int position) {
        if (position == 0) {
            //mTab1.clearCount();
        }
    }


    @Override
    public void onTabSelect(int index) {

    }


    public JPTabBar getTabbar() {
        return mTabbar;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            pressAgainExit();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    /**
     * 双击返回键离开
     */
    private void pressAgainExit() {
        if (exit.isExit()) {
            for (Activity activity : JActivityManager.getActivityStack()) {
                activity.finish();
            }
        } else {
            Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
            exit.doExitAction();
        }
    }

    //动态申请权限
    protected boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(23)
    protected void askPermissions() {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE",
                "android.permission.ACCESS_COARSE_LOCATION"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }

    /**
     * 上传ID
     *
     * @param token
     * @param deviceId
     */
    private void uploadID(String token, String deviceId, String phoneType) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        JSONObject object = new JSONObject();
        object.put("registrationID", deviceId);
        object.put("phoneType", phoneType);
        RetrofitHttp.getRetrofit(builder.build()).uploadDeviceID(token, StringUtil.getBody(object))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Response<ResponseBody> response) {
                        try {
                            if (response.body() == null) {
                                Toast.makeText(context, "接口请求错误!", Toast.LENGTH_SHORT).show();
                            } else {
                                String result = response.body().string();
                                System.out.println("上传ID返回结果：" + result);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        DialogUIUtils.showToast("网络不给力");
                    }
                });
    }

    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.yxdriver.app.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                    if (!StringUtil.isNullOrEmpty(extras)) {
                        showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                    }
                    setCostomMsg(showMsg.toString());
                }
            } catch (Exception e) {
            }
        }
    }

    private void setCostomMsg(String msg) {
        DialogUIUtils.showToast(msg);
    }

    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
    }


    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
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
                                            DemoApplication.getInstance().setApkUrl(versionModel.getUploadUrl());
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
                    .setRequestUrl("http://www.baidu.com")
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
            //DialogUIUtils.showToast("暂无新版本");
        }

    }

}
