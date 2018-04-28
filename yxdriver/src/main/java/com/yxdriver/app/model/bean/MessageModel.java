package com.yxdriver.app.model.bean;

import java.io.Serializable;

/**
 * Created by mac on 2017/9/28.
 * 消息
 */

public class MessageModel implements Serializable {
    private static final long serialVersionUID = 3251808027492846789L;
    private String uuid;
    private String title;
    private String content;
    private String createTime;
    private int status;

    public MessageModel() {
    }

    public MessageModel(String title, String content, String createTime) {
        this.title = title;
        this.content = content;
        this.createTime = createTime;
    }

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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
