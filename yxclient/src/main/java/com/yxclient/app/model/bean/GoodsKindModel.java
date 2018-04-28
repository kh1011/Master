package com.yxclient.app.model.bean;

import java.io.Serializable;

/**
 * Created by mac on 2017/9/29.
 * 获取类型
 */

public class GoodsKindModel implements Serializable {
    private static final long serialVersionUID = 7767501674137847474L;
    private String id;

    private String content;


    public GoodsKindModel(String id, String content) {
        super();
        this.id = id;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
