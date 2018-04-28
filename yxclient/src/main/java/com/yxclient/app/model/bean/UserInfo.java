package com.yxclient.app.model.bean;

import java.io.Serializable;

/**
 * Created by mac on 2017/7/3.
 * 联系人
 */

public class UserInfo implements Serializable {
    private static final long serialVersionUID = 877912393857335936L;
    private String name;
    private String mobile;
    private String headImage;
    private String uuid;

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public UserInfo() {
    }
    
    public UserInfo(String name, String mobile) {
        this.name = name;
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
