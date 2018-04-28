package com.yxclient.app.model.bean;

import java.io.Serializable;

/**
 * 经纬度
 *
 * Created by zhangyun on 2017/10/2 0002
 * EMail -> yun.zhang@chinacreator.com
 */
public class LocaltionModel implements Serializable {
    double lat;//经度
    double lng;//纬度

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
