package com.yxclient.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.yxclient.app.activity.YXLoginActivity;
import com.yxclient.app.activity.YXMainActivity;
import com.yxclient.app.activity.YXWelcomeActivity;
import com.yxclient.app.app.DemoApplication;
import com.yxclient.app.utils.PreferenceUtils;
import com.yxclient.app.utils.StringUtil;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author：Anumbrella
 * Date：16/5/29 上午9:36
 */
public class SplashActiivty extends AppCompatActivity {

    /**
     * 定义三个切换动画
     */
    private Animation mFadeIn;

    private Animation mFadeOut;

    private Animation mFadeInScale;


    @BindView(R.id.application_bg)
    ImageView applicationBg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DoAction();
    }

    /**
     * 建立监听事件
     */
    private void setAnimListener() {
        mFadeIn.setAnimationListener(new Animation.AnimationListener() {

            public void onAnimationStart(Animation animation) {

            }

            public void onAnimationRepeat(Animation animation) {

            }

            public void onAnimationEnd(Animation animation) {
                applicationBg.startAnimation(mFadeInScale);
            }
        });
        mFadeInScale.setAnimationListener(new Animation.AnimationListener() {

            public void onAnimationStart(Animation animation) {


            }

            public void onAnimationRepeat(Animation animation) {

            }

            public void onAnimationEnd(Animation animation) {
                applicationBg.startAnimation(mFadeOut);
            }
        });
        mFadeOut.setAnimationListener(new Animation.AnimationListener() {

            public void onAnimationStart(Animation animation) {
                if (!StringUtil.isNullOrEmpty(DemoApplication.getInstance().getMyToken())) {
                    Intent intent = new Intent();
                    intent.setClass(SplashActiivty.this, YXMainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent();
                    intent.setClass(SplashActiivty.this, YXLoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            public void onAnimationRepeat(Animation animation) {

            }

            public void onAnimationEnd(Animation animation) {

            }
        });
    }

    /**
     * 初始化动画
     */
    private void initAnim() {
        mFadeIn = AnimationUtils.loadAnimation(this, R.anim.application_fade_in);
        mFadeIn.setDuration(500);
        mFadeInScale = AnimationUtils.loadAnimation(this,
                R.anim.application_fade_in_scale);
        mFadeInScale.setDuration(2000);
        mFadeOut = AnimationUtils.loadAnimation(this, R.anim.application_fade_out);
        mFadeOut.setDuration(500);
        applicationBg.setAnimation(mFadeIn);
    }

    /**
     * 随机选择背景图片
     */
    private void RandomApplicationBg() {
        int index = new Random().nextInt(2);
        if (index == 1) {
            applicationBg.setImageResource(R.drawable.qi_shi_ye);
        } else {
            applicationBg.setImageResource(R.drawable.qi_shi_ye);
        }
    }


    private void DoAction() {
        boolean isFirst = PreferenceUtils.readBoolean(this, "First", "isFirst", true);
        if (isFirst == true) {
            PreferenceUtils.write(this, "First", "isFirst", false);
            Welcome();
        } else {
            ComeingApp();
        }
    }

    /**
     * 进入欢迎界面
     */
    private void Welcome() {
        Intent intent = new Intent();
        intent.setClass(this, YXWelcomeActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 直接进入app
     */
    private void ComeingApp() {
        setContentView(R.layout.activity_application);
        ButterKnife.bind(this);
        RandomApplicationBg();
        initAnim();
        setAnimListener();
    }
}
