package com.yxdriver.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIListener;
import com.yxdriver.app.R;
import com.yxdriver.app.app.DemoApplication;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.util.CleanMessageUtil;

/**
 * 功能：用户设置
 * Created by yun.zhang on 2017/9/24 0024.
 * email:zy19930321@163.com
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


    /**
     * 启动这个Activity的Intent
     *
     * @param context
     * @param data
     * @return
     */
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

    @OnClick({R.id.setting_version, R.id.setting_about, R.id.setting_cleanCache, R.id.setting_change_phone, R.id.rl_address_manager, R.id.setting_btn, R.id.setting_copyright, R.id.setting_find_pwd})
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
            case R.id.rl_address_manager://管理地址
                toActivity(YXReceiptAddressActivity.createIntent(context, null));
                break;
            case R.id.setting_btn:
                // 退出登录
                DialogUIUtils.showMdAlert(context, "提示", "确定注销登录吗？", new DialogUIListener() {
                    @Override
                    public void onPositive() {
                        // 确定注销
                        DemoApplication.getInstance().setName("");
                        DemoApplication.getInstance().setPwd("");
                        DemoApplication.getInstance().setMyToken("");
                        DemoApplication.getInstance().setUserId("");
                        Intent logoutIntent = new Intent(context, YXLoginActivity.class);
                        logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(logoutIntent);
                    }

                    @Override
                    public void onNegative() {
                        // 取消注销
                    }

                }).show();
                break;
            case R.id.setting_copyright:
                toActivity(YXCopyrightActivity.createIntent(context, ""));
                break;
            case R.id.setting_find_pwd:
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
                    //Toast.makeText(context, "清除缓存成功!", Toast.LENGTH_SHORT).show();
                    tv_Cache.setText(CleanMessageUtil.getTotalCacheSize(context));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
