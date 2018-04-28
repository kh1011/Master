package com.yxclient.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIListener;
import com.yxclient.app.R;
import com.yxclient.app.app.DemoApplication;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.util.CleanMessageUtil;

/**
 * Created by mac on 2017/8/21.
 * 设置
 */

public class YXSettingActivity extends BaseActivity {
    @BindView(R.id.setting_cache)
    TextView tv_Cache;

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_setting);
        ButterKnife.bind(this);
        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }
    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


    public static Intent createIntent(Context context, String data) {
        return new Intent(context, YXSettingActivity.class).
                putExtra(RESULT_DATA, data);
    }

    @Override
    public void initView() {
        try {
            tv_Cache.setText(CleanMessageUtil.getTotalCacheSize(context));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }

    @OnClick({R.id.setting_foregetpwd, R.id.setting_btn, R.id.setting_addr_manager, R.id.setting_version, R.id.setting_about, R.id.setting_cleanCache, R.id.setting_change_phone})
    void myClick(View view) {
        switch (view.getId()) {
            case R.id.setting_version:
                toActivity(YXVersionActivity.createIntent(context, ""));
                break;
            case R.id.setting_about:
                toActivity(YXAboutusActivity.createIntent(context, ""));
                break;
            case R.id.setting_cleanCache:
                // 清除缓存
                cleanCache();
                break;
            case R.id.setting_change_phone:
                toActivity(YXChangePhoneActivity.createIntent(context, ""));
                break;
            case R.id.setting_addr_manager:
                // 地址管理
                toActivity(YXReceiptAddressActivity.createIntent(context, null));
                break;
            case R.id.setting_btn:
                // 退出登录
                DialogUIUtils.showMdAlert(context, "提示", "确定退出吗？", new DialogUIListener() {
                    @Override
                    public void onPositive() {
                        DemoApplication.getInstance().setMyToken("");
                        DemoApplication.getInstance().setUserId("");
                        Intent logoutIntent = new Intent(context, YXMainActivity.class);
                        logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(logoutIntent);
                    }

                    @Override
                    public void onNegative() {
                    }

                }).show();
                break;
            case R.id.setting_foregetpwd:
                // 找回密码
                toActivity(YXForegetPwdActivity.createIntent(context, ""));
                break;
            default:
                break;
        }
    }

    private void cleanCache() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(2000);
                    CleanMessageUtil.clearAllCache(context);
                    //HORT).show();
                    tv_Cache.setText(CleanMessageUtil.getTotalCacheSize(context));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
