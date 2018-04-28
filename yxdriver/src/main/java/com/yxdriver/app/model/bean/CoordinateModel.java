package com.yxdriver.app.model.bean;

import java.io.Serializable;

/**
 * Created by mac on 2017/8/17.
 * 位置信息
 */

public class CoordinateModel implements Serializable {
    private static final long serialVersionUID = -5116233029965890320L;
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

    @Override
    public String toString() {
        return "{\""+"lng"+"\":"+lng+",\""+"lat"+"\":"+lat+"}";
    }
}
