package com.yxdriver.app.model.bean;

import com.dou361.dialogui.bean.TieBean;

import java.io.Serializable;

/**
 * 车辆类型model
 * Created by yun.zhang on 2017/9/21 0021.
 * email:zy19930321@163.com
 */
public class CarType implements Serializable {
    String uuid;
    String name;
    String desc;
    String image;
    int state;


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
