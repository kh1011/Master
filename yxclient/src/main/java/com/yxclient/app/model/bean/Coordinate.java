package com.yxclient.app.model.bean;

import java.io.Serializable;

/**
 * Created by mac on 2017/8/17.
 * 位置信息
 */

public class Coordinate implements Serializable {

    private static final long serialVersionUID = -2659373305439291841L;
    private double lng;
    private double lat;

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
