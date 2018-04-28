package com.yxclient.app.model.bean;

import java.io.Serializable;

/**
 * Created by mac on 2017/9/27.
 * 用户model
 */

public class UserModel implements Serializable {
    private static final long serialVersionUID = 7694129150438352459L;
    private String uuid;
    // 真实姓名
    private String name;
    //昵称
    private String alias;
    // 性别
    private String sex;
    // 生日
    private String birthday;
    // 电话号码
    private String mobile;
    // 用户类型
    private int type;
    // 用户状态
    private int status;
    // 审核状态
    private int verify;
    // 头像
    private String headImage;
    // 账户余额
    private float balance;
    // 真实账户资金（可提现或退货）
    private int realCurrency;
    // 虚拟账户资金
    private int virtualCurrency;
    // 身份证号码
    private String cardNo;
    // 证件照
    private String[] cardImages;
    // 车辆图片
    private String[] carImages;
    // 所属省份
    private String province;
    // 所属城市
    private String city;
    // 所属区域--县城
    private String area;
    // 紧急联系人
    private String emergencyName;
    // 紧急联系人电话
    private String emergencyMobile;


    public UserModel() {

    }

    public UserModel(String name, String mobile, int type, String headImage) {
        this.name = name;
        this.mobile = mobile;
        this.type = type;
        this.headImage = headImage;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
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

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
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

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String[] getCardImages() {
        return cardImages;
    }

    public void setCardImages(String[] cardImages) {
        this.cardImages = cardImages;
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

    public String[] getCarImages() {
        return carImages;
    }

    public void setCarImages(String[] carImages) {
        this.carImages = carImages;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
