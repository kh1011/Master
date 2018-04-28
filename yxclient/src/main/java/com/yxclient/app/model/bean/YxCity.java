package com.yxclient.app.model.bean;
import java.io.Serializable;

/**
 * Created by mac on 2017/9/7.
 * 城市
 */

public class YxCity implements Serializable {

    private static final long serialVersionUID = -7733905376935289226L;
    private String city;
    private String lsprefix;
    private String lsnum;
    private String carorg;
    private String frameno;
    private String engineno;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLsprefix() {
        return lsprefix;
    }

    public void setLsprefix(String lsprefix) {
        this.lsprefix = lsprefix;
    }

    public String getCarorg() {
        return carorg;
    }

    public void setCarorg(String carorg) {
        this.carorg = carorg;
    }

    public String getLsnum() {
        return lsnum;
    }

    public void setLsnum(String lsnum) {
        this.lsnum = lsnum;
    }

    public String getFrameno() {
        return frameno;
    }

    public void setFrameno(String frameno) {
        this.frameno = frameno;
    }

    public String getEngineno() {
        return engineno;
    }

    public void setEngineno(String engineno) {
        this.engineno = engineno;
    }
}
