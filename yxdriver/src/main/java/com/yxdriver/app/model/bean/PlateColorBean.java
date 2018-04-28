package com.yxdriver.app.model.bean;

import java.io.Serializable;

/**
 * Created by mac on 2017/9/11.
 * 车牌颜色
 */

public class PlateColorBean implements Serializable {
    private static final long serialVersionUID = -1105217279897018052L;
    private String name;
    private int res;

    public PlateColorBean(String name, int res) {
        this.name = name;
        this.res = res;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }
}
