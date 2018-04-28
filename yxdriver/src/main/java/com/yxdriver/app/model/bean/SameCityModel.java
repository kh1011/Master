package com.yxdriver.app.model.bean;

import java.util.List;

/**
 * 功能：同城列表的实体对象
 * Created by yun.zhang on 2017/9/26 0026.
 * email:zy19930321@163.com
 */
public class SameCityModel {
    String uuid;
    String title;
    String content;
    String type;
    List<String> images;
    String createTime;
    UserInfo createUser;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public UserInfo getCreateUser() {
        return createUser;
    }

    public void setCreateUser(UserInfo createUser) {
        this.createUser = createUser;
    }
}
