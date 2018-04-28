/*Copyright ©2015 TommyLemon(https://github.com/TommyLemon)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package com.yxdriver.app.app;

import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.support.multidex.MultiDex;

import com.bumptech.glide.request.target.ViewTarget;
import com.dou361.dialogui.DialogUIUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.jude.utils.JActivityManager;
import com.jude.utils.JFileManager;
import com.jude.utils.JUtils;
import com.yxdriver.app.R;
import com.yxdriver.app.utils.ImageLoaderUtil;
import com.yxdriver.app.utils.SharedPreUtil;
import com.yxdriver.app.utils.StringUtil;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import zuo.biao.library.base.BaseApplication;
import zuo.biao.library.pays.sdk.CommonPayConfig;

/**
 * Application
 *
 * @author Lemon
 */


public class DemoApplication extends BaseApplication {
    private static final String TAG = "DemoApplication";

    private static DemoApplication context;
    private String accessToken;
    private String name;
    private String pwd;
    private String carUuid;
    private String verify;// 司机认证状态
    private String userId;

    private String isBegain;
    //车辆信息
    private String carInfo;

    private String carList;

    private String apkUrl;  //安装包下载地址

    public static DemoApplication getInstance() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }

        ImageLoaderUtil.init(context);
        ViewTarget.setTagId(R.id.glide_tag);
        DialogUIUtils.init(context);
        Fresco.initialize(this);
        JUtils.initialize(this);
        //生成文件夹
        JFileManager.getInstance().init(this, Dir.values());
        //初始化sdk
        JPushInterface.setDebugMode(true);//正式版的时候设置false，关闭调试
        JPushInterface.init(this);
        //建议添加tag标签，发送消息的之后就可以指定tag标签来发送了
        Set<String> set = new HashSet<>();
        set.add("andfixdemo");//名字任意，可多添加几个
        JPushInterface.setTags(this, set, null);//设置标签
        String testId = JPushInterface.getRegistrationID(this);
        System.out.println("极光注册ID：" + testId);
        // 微信支付注册
        CommonPayConfig.WX_APP_ID = "wxb4bbf0651d312ab6";
        registerActivityLifecycleCallbacks(JActivityManager.getActivityLifecycleCallbacks());

    }

    public enum Dir {
        Object
    }

    public String getMyToken() {
        if (StringUtil.isNullOrEmpty(accessToken)) {
            return SharedPreUtil.getValue(context, "accessToken", "");
        }
        return accessToken;
    }

    public void setMyToken(String accessToken) {
        if (!StringUtil.isNullOrEmpty(accessToken)) {
            SharedPreUtil.putValue(context, "accessToken", accessToken);
            this.accessToken = accessToken;
        } else {
            SharedPreUtil.putValue(context, "accessToken", "");
            this.accessToken = "";
        }
    }

    public String getUserId() {
        if (StringUtil.isNullOrEmpty(userId)) {
            return SharedPreUtil.getValue(context, "userId", "");
        }
        return userId;
    }

    public void setUserId(String userId) {
        if (!StringUtil.isNullOrEmpty(userId)) {
            SharedPreUtil.putValue(context, "userId", userId);
            this.userId = userId;
        } else {
            SharedPreUtil.putValue(context, "userId", "");
            this.userId = "";
        }
    }

    public String getVerify() {
        if (StringUtil.isNullOrEmpty(verify)) {
            return SharedPreUtil.getValue(context, "verify", "");
        }
        return verify;
    }

    public void setVerify(String verify) {
        if (!StringUtil.isNullOrEmpty(verify)) {
            SharedPreUtil.putValue(context, "verify", verify);
            this.verify = verify;
        } else {
            SharedPreUtil.putValue(context, "verify", "");
            this.verify = "";
        }
    }

    public String getName() {
        if (StringUtil.isNullOrEmpty(name)) {
            return SharedPreUtil.getValue(context, "name", "");
        }
        return name;
    }

    public void setName(String name) {
        if (!StringUtil.isNullOrEmpty(name)) {
            SharedPreUtil.putValue(context, "name", name);
            this.name = name;
        } else {
            SharedPreUtil.putValue(context, "name", "");
            this.name = "";
        }
    }

    public String getPwd() {
        if (StringUtil.isNullOrEmpty(pwd)) {
            return SharedPreUtil.getValue(context, "pwd", "");
        }
        return pwd;
    }

    public void setPwd(String pwd) {
        if (!StringUtil.isNullOrEmpty(pwd)) {
            SharedPreUtil.putValue(context, "pwd", pwd);
            this.pwd = pwd;
        } else {
            SharedPreUtil.putValue(context, "pwd", "");
            this.pwd = "";
        }
    }
    public String getcarInfo() {
        if (StringUtil.isNullOrEmpty(carInfo)) {
            return SharedPreUtil.getValue(context, "carInfo", "");
        }
        return carInfo;
    }

    public void setcarInfo(String carInfo) {
        if (!StringUtil.isNullOrEmpty(carInfo)) {
            SharedPreUtil.putValue(context, "carInfo", carInfo);
            this.carInfo = carInfo;
        } else {
            SharedPreUtil.putValue(context, "carInfo", "");
            this.carInfo = "";
        }
    }
    public void setCarUuid(String carUuid) {
        if (!StringUtil.isNullOrEmpty(carUuid)) {
            SharedPreUtil.putValue(context, "carUuid", carUuid);
            this.carUuid = carUuid;
        } else {
            SharedPreUtil.putValue(context, "carUuid", "");
            this.carUuid = "";
        }
    }

    public String getCarUuid() {
        if (StringUtil.isNullOrEmpty(carUuid)) {
            return SharedPreUtil.getValue(context, "carUuid", "");
        }
        return carUuid;
    }

    public String getisBegain() {
        if (StringUtil.isNullOrEmpty(isBegain)) {
            return SharedPreUtil.getValue(context, "isBegain", "");
        }
        return isBegain;
    }

    public void setisBegain(String isBegain) {
        if (!StringUtil.isNullOrEmpty(isBegain)) {
            SharedPreUtil.putValue(context, "isBegain", isBegain);
            this.isBegain = isBegain;
        } else {
            SharedPreUtil.putValue(context, "isBegain", "");
            this.isBegain = "";
        }
    }

    public String getcarList() {
        if (StringUtil.isNullOrEmpty(carList)) {
            return SharedPreUtil.getValue(context, "carList", "");
        }
        return carList;
    }

    public void setcarList(String carList) {
        if (!StringUtil.isNullOrEmpty(carList)) {
            SharedPreUtil.putValue(context, "carList", carList);
            this.carList = carList;
        } else {
            SharedPreUtil.putValue(context, "carList", "");
            this.carList = "";
        }
    }

    public String getApkUrl() {
        if (StringUtil.isNullOrEmpty(apkUrl)) {
            return SharedPreUtil.getValue(context, "apkUrl", "");
        }
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        if (!StringUtil.isNullOrEmpty(apkUrl)) {
            SharedPreUtil.putValue(context, "apkUrl", apkUrl);
            this.apkUrl = apkUrl;
        } else {
            SharedPreUtil.putValue(context, "apkUrl", "");
            this.apkUrl = "";
        }
    }

    /**
     * 防止出现he number of method references in a .dex file cannot exceed 64K.异常
     *
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
