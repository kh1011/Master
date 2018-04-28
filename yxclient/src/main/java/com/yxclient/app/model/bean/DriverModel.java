package com.yxclient.app.model.bean;

import com.iflytek.cloud.thirdparty.S;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mac on 2017/9/27.
 * 订单(出行、货运)
 */

public class DriverModel implements Serializable {

    private String uuid;
    private String name;
    private String mobile;
    private String headImage;
    private String password;
    private String createTime;
    private String sex;
    private String birthday;
    private String cardNo;
    private String province;
    private String city;
    private String area;
    private String emergencyName;
    private String emergencyMobile;
    private String id;
    private int type;
    private int status;
    private int verify;
    private int balance;
    private int realCurrency;
    private int virtualCurrency;
    private String[] cardImages;
    private String[] driverImages;
    public DriverModel() {

    }

    public DriverModel(String uuid, String name, String mobile, String headImage, String password, String createTime, String sex
                            , String birthday, String cardNo, String province, String city, String area, String emergencyName
                            , String emergencyMobile, String id, int type, int status, int verify, int balance, int realCurrency,
                       int virtualCurrency, String[] cardImages, String[] driverImages) {
        this.uuid=uuid;
        this.name=name;
        this.mobile=mobile;
        this.headImage=headImage;
        this.password=password;
        this.createTime=createTime;
        this.sex=sex;
        this.birthday=birthday;
        this.cardNo=cardNo;
        this.province=province;
        this.city=city;
        this.area=area;
        this.emergencyName=emergencyName;
        this.emergencyMobile=emergencyMobile;
        this.id=id;
        this.type=type;
        this.status=status;
        this.verify=verify;
        this.balance=balance;
        this.realCurrency=realCurrency;
        this.virtualCurrency=virtualCurrency;
        this.cardImages=cardImages;
        this.driverImages=driverImages;
    }

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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getEmergencyName() {
        return emergencyName;
    }

    public void setEmergencyName(String emergencyName) {
        this.emergencyName = emergencyName;
    }

    public String getEmergencyMobile() {
        return emergencyMobile;
    }

    public void setEmergencyMobile(String emergencyMobile) {
        this.emergencyMobile = emergencyMobile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getVerify() {
        return verify;
    }

    public void setVerify(int verify) {
        this.verify = verify;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getRealCurrency() {
        return realCurrency;
    }

    public void setRealCurrency(int realCurrency) {
        this.realCurrency = realCurrency;
    }

    public int getVirtualCurrency() {
        return virtualCurrency;
    }

    public void setVirtualCurrency(int virtualCurrency) {
        this.virtualCurrency = virtualCurrency;
    }

    public String[] getCardImages() {
        return cardImages;
    }

    public void setCardImages(String[] cardImages) {
        this.cardImages = cardImages;
    }

    public String[] getDriverImages() {
        return driverImages;
    }

    public void setDriverImages(String[] driverImages) {
        this.driverImages = driverImages;
    }







}
