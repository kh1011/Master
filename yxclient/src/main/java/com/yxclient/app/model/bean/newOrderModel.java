package com.yxclient.app.model.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/4/11.
 */

public class newOrderModel implements Serializable {

    private int number;  //人数
    private String time;  // 出行时间
    private String mobile;  //联系方式
    private String infoType;  //出行类型（顺风车、专车）
    private SiteBean site;// 目的地点
    private OriginBean origin;// 出发地点
    private String date;//出行日期
    private int logicDel;
    private int once;
    private UserBean user; //用户信息
    private int amount;  // 估计订单金额
    private int status;// 订单状态
    private String datetime;// 出行日期+时间
    private String id;
    private String createTime; //创建时间
    private String name; //用户名称
    private double mileage;  //大约里程
    private String note;// 备注信息
    private String orderType;//订单类型（出行、货运）
    private String extraInfoUUID;  //订单号

    private String no; //货运订单号

    private int fragile; //是否易碎
    private double weight;// 货物重量
    private String consignorMobile;// 发货人电话号码
    private String bulk;// 货物体积
    private String kind; // 货物类别
    private String consignorName;// 发货联系人
    private int wet;//是否易湿
    private Double capacity;                //余载重

    private UserModel driver;
    /**
     * car : {"uuid":"af9ccb75-83b8-4bd4-bda1-436522d203f7","logicDel":0,"carImages":["http://ohyfvxowv.bkt.clouddn.com/image/shunH-APP/1514359478258"],"type":"SUV","plateNumberColor":"蓝色车牌","plateNumber":"云A45678"}
     */

    private CarModel car;


    public UserModel getDriver() {
        return driver;
    }

    public void setDriver(UserModel driver) {
        this.driver = driver;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getInfoType() {
        return infoType;
    }

    public void setInfoType(String infoType) {
        this.infoType = infoType;
    }

    public SiteBean getSite() {
        return site;
    }

    public void setSite(SiteBean site) {
        this.site = site;
    }

    public OriginBean getOrigin() {
        return origin;
    }

    public void setOrigin(OriginBean origin) {
        this.origin = origin;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getLogicDel() {
        return logicDel;
    }

    public void setLogicDel(int logicDel) {
        this.logicDel = logicDel;
    }

    public int getOnce() {
        return once;
    }

    public void setOnce(int once) {
        this.once = once;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getExtraInfoUUID() {
        return extraInfoUUID;
    }

    public void setExtraInfoUUID(String extraInfoUUID) {
        this.extraInfoUUID = extraInfoUUID;
    }

    public CarModel getCar() {
        return car;
    }

    public void setCar(CarModel car) {
        this.car = car;
    }

    public static class SiteBean implements Serializable {
        /**
         * address : 昆明万科金域国际2期
         * city : 昆明市
         * county : 五华区
         * coordinate : {"lat":25.055609,"lng":102.655154}
         * province : 云南省
         * detailAddress : 云南省昆明市五华区昆明万科金域国际2期
         */

        private String address;
        private String city;
        private String county;
        private CoordinateModel coordinate;
        private String province;
        private String detailAddress;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCounty() {
            return county;
        }

        public void setCounty(String county) {
            this.county = county;
        }

        public CoordinateModel getCoordinate() {
            return coordinate;
        }

        public void setCoordinate(CoordinateModel coordinate) {
            this.coordinate = coordinate;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getDetailAddress() {
            return detailAddress;
        }

        public void setDetailAddress(String detailAddress) {
            this.detailAddress = detailAddress;
        }

        public static class CoordinateBean implements Serializable {
            /**
             * lat : 25.055609
             * lng : 102.655154
             */

            private double lat;
            private double lng;

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
    }

    public static class OriginBean implements Serializable {
        /**
         * address : 中国工商银行(昆州路支行)
         * city : 昆明市
         * county : 西山区
         * coordinate : {"lat":25.038192,"lng":102.642648}
         * province : 云南省
         * detailAddress : 云南省昆明市西山区中国工商银行(昆州路支行)
         */

        private String address;
        private String city;
        private String county;
        private CoordinateModel coordinate;
        private String province;
        private String detailAddress;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCounty() {
            return county;
        }

        public void setCounty(String county) {
            this.county = county;
        }

        public CoordinateModel getCoordinate() {
            return coordinate;
        }

        public void setCoordinate(CoordinateModel coordinate) {
            this.coordinate = coordinate;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getDetailAddress() {
            return detailAddress;
        }

        public void setDetailAddress(String detailAddress) {
            this.detailAddress = detailAddress;
        }

        public static class CoordinateBeanX implements Serializable {
            /**
             * lat : 25.038192
             * lng : 102.642648
             */

            private double lat;
            private double lng;

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
    }

    public int getFragile() {
        return fragile;
    }

    public void setFragile(int fragile) {
        this.fragile = fragile;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getConsignorMobile() {
        return consignorMobile;
    }

    public void setConsignorMobile(String consignorMobile) {
        this.consignorMobile = consignorMobile;
    }

    public String getBulk() {
        return bulk;
    }

    public void setBulk(String bulk) {
        this.bulk = bulk;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getConsignorName() {
        return consignorName;
    }

    public void setConsignorName(String consignorName) {
        this.consignorName = consignorName;
    }

    public int getWet() {
        return wet;
    }

    public void setWet(int wet) {
        this.wet = wet;
    }

    public static class UserBean implements Serializable {
        /**
         * alias : 悟空
         * headImage : http://ohyfvxowv.bkt.clouddn.com/image/shunH-APP/1516104484073
         * mobile : 13095303081
         * uuid : 814f7558-d066-4418-a9da-f5708b078261
         * name : 悟空
         * type : 2
         */

        private String alias;
        private String headImage;
        private String mobile;
        private String uuid;
        private String name;
        private int type;

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public String getHeadImage() {
            return headImage;
        }

        public void setHeadImage(String headImage) {
            this.headImage = headImage;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
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

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }



    }

    public static class CarBean  implements Serializable {
        /**
         * uuid : af9ccb75-83b8-4bd4-bda1-436522d203f7
         * logicDel : 0
         * carImages : ["http://ohyfvxowv.bkt.clouddn.com/image/shunH-APP/1514359478258"]
         * type : SUV
         * plateNumberColor : 蓝色车牌
         * plateNumber : 云A45678
         */

        private String uuid;
        @SerializedName("logicDel")
        private int logicDelX;
        private String type;
        private String plateNumberColor;
        private String plateNumber;
        private List<String> carImages;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public int getLogicDelX() {
            return logicDelX;
        }

        public void setLogicDelX(int logicDelX) {
            this.logicDelX = logicDelX;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPlateNumberColor() {
            return plateNumberColor;
        }

        public void setPlateNumberColor(String plateNumberColor) {
            this.plateNumberColor = plateNumberColor;
        }

        public String getPlateNumber() {
            return plateNumber;
        }

        public void setPlateNumber(String plateNumber) {
            this.plateNumber = plateNumber;
        }

        public List<String> getCarImages() {
            return carImages;
        }

        public void setCarImages(List<String> carImages) {
            this.carImages = carImages;
        }
    }

    public Double getCapacity() {
        return capacity;
    }

    public void setCapacity(Double capacity) {
        this.capacity = capacity;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }
}
