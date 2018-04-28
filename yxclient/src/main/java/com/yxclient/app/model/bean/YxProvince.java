package com.yxclient.app.model.bean;


import java.io.Serializable;
import java.util.List;

/**
 * Created by mac on 2017/9/7.
 * 省份
 */

public class YxProvince implements Serializable {


    private static final long serialVersionUID = 8103898867040376923L;
    private String province;
    private String lsprefix;
    private String lsnum;
    private String carorg;
    private String frameno;
    private String engineno;
    private List<YxCity> list;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getLsprefix() {
        return lsprefix;
    }

    public void setLsprefix(String lsprefix) {
        this.lsprefix = lsprefix;
    }

    public String getLsnum() {
        return lsnum;
    }

    public void setLsnum(String lsnum) {
        this.lsnum = lsnum;
    }

    public String getCarorg() {
        return carorg;
    }

    public void setCarorg(String carorg) {
        this.carorg = carorg;
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

    public List<YxCity> getList() {
        return list;
    }

    public void setList(List<YxCity> list) {
        this.list = list;
    }
}
