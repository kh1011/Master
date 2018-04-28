package com.yxclient.app.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yxclient.app.model.bean.GoodEvaluateModel;
import com.yxclient.app.model.bean.GoodPruductModel;
import com.yxclient.app.model.bean.GoodTypeModel;
import com.yxclient.app.model.bean.LocaltionModel;
import com.yxclient.app.model.bean.ReceiptAddressModel;
import com.yxclient.app.model.bean.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 转换工具类
 *
 * Created by zhangyun on 2017/9/29 0029
 * EMail -> yun.zhang@chinacreator.com
 */
public class ParseUtils {

    /**
     * 将{@link JSONArray} 转换为 {@link List<String>}对象
     * @param array
     * @return
     */
    public static List<String> parseStrings(JSONArray array){
        List<String> result = new ArrayList<>();
        if(array == null || array.size() == 0){
            return result;
        }
        for (int i = 0; i < array.size(); i++) {
            String str = array.getString(i);
            result.add(str);
        }
        return result;
    }


    /**
     * 将{@link JSONObject} 转换为 {@link GoodPruductModel}对象
     * @param obj
     * @return
     */
    public static GoodPruductModel parseToGoodProduct(JSONObject obj){
        GoodPruductModel g = new GoodPruductModel();
        String uuid = obj.getString("uuid");
        String title = obj.getString("title");
        String titleImage = obj.getString("titleImage");
        String desc = obj.getString("desc");
        String unit = obj.getString("unit");
        String tag = obj.getString("tag");
        String note = obj.getString("note");
        String createTime = obj.getString("createTime");
        int sales = obj.getIntValue("sales");
        int monthSales = obj.getIntValue("monthSales");
        int stock = obj.getIntValue("stock");
        float price = obj.getFloatValue("price");
        float basePrice = obj.getFloatValue("basePrice");
        int status = obj.getIntValue("status");
        JSONArray images = obj.getJSONArray("images");
        JSONObject jsonObject = obj.getJSONObject("kind");

        g.setUuid(uuid);
        g.setKind(parseToGoodType(jsonObject));
        g.setTitle(title);
        g.setTitleImage(titleImage);
        g.setDesc(desc);
        g.setSales(sales);
        g.setMonthSales(monthSales);
        g.setStock(stock);
        g.setUnit(unit);
        g.setPrice(price / 100);
        g.setBasePrice(basePrice / 100);
        g.setCreateTime(createTime);
        g.setStatus(status);
        g.setImages(parseStrings(images));
        g.setTag(tag);
        g.setNote(note);
        return g;
    }

    /**
     * 将{@link JSONObject} 转换为 {@link GoodTypeModel}对象
     * @param obj
     * @return
     */
    public static GoodTypeModel parseToGoodType(JSONObject obj){
        GoodTypeModel g = new GoodTypeModel();
        String uuid = obj.getString("uuid");
        String name = obj.getString("name");
        String image = obj.getString("image");
        int status = obj.getIntValue("status");
        g.setImage(image);
        g.setName(name);
        g.setStatus(status);
        g.setUuid(uuid);
        return g;
    }

    /**
     * 将{@link JSONObject} 转换为 {@link UserInfo}对象
     * @param obj
     * @return
     */
    public static UserInfo parseUserInfo(JSONObject obj){
        UserInfo user = new UserInfo();
        String uuid = obj.getString("uuid");
        String name = obj.getString("name");
        String mobile = obj.getString("mobile");
        String headImage = obj.getString("headImage");
        user.setMobile(mobile);
        user.setName(name);
        user.setUuid(uuid);
        user.setHeadImage(headImage);
        return user;
    }

    /**
     * 将{@link JSONObject} 转换为 {@link GoodEvaluateModel}对象
     * @param obj
     * @return
     */
    public static GoodEvaluateModel parseToGoodEvaluate(JSONObject obj){
        GoodEvaluateModel model = new GoodEvaluateModel();
        String uuid = obj.getString("uuid");
        String orderNo = obj.getString("orderNo");
        String goodsUUID = obj.getString("goodsUUID");
        String content = obj.getString("content");
        String createTime = obj.getString("createTime");
        JSONObject userObj = obj.getJSONObject("createUser");
        UserInfo user = parseUserInfo(userObj);
        model.setUuid(uuid);
        model.setContent(content);
        model.setCreateTime(createTime);
        model.setGoodsUUID(goodsUUID);
        model.setCreateUser(user);
        model.setOrderNo(orderNo);
        return model;
    }


    /**
     * 将{@link JSONObject} 转换为 {@link LocaltionModel}对象
     * @param obj
     * @return
     */
    public static LocaltionModel parseLocaltionModel(JSONObject obj){
        if(obj == null){
            return new LocaltionModel();
        }
        LocaltionModel model = new LocaltionModel();
        double lng = obj.getDoubleValue("lng");
        double lat = obj.getDoubleValue("lat");
        model.setLat(lat);
        model.setLng(lng);
        return model;
    }

    /**
     * 将{@link JSONObject} 转换为 {@link ReceiptAddressModel}对象
     * @param obj
     * @return
     */
    public static ReceiptAddressModel parseReceiptAddressModel(JSONObject obj){
        ReceiptAddressModel model = new ReceiptAddressModel();
        String uuid = obj.getString("uuid");
        String userUUID = obj.getString("userUUID");
        String name = obj.getString("name");
        String mobile = obj.getString("mobile");
        String province = obj.getString("province");
        String city = obj.getString("city");
        String county = obj.getString("county");
        String street = obj.getString("street");
        String address = obj.getString("address");
        JSONObject coordinate = obj.getJSONObject("coordinate");
        String postcode = obj.getString("postcode");
        int isdefault = obj.getIntValue("isdefault");
        model.setUuid(uuid);
        model.setUserUUID(userUUID);
        model.setName(name);
        model.setMobile(mobile);
        model.setProvince(province);
        model.setCity(city);
        model.setCounty(county);
        model.setStreet(street);
        model.setAddress(address);
        model.setCoordinate(parseLocaltionModel(coordinate));
        model.setPostcode(postcode);
        model.setIsdefault(isdefault);
        //默认不是编辑模式
        model.setEdit(false);
        if(model.getIsdefault() == 1){
            model.setSelected(true);
        }
        return model;
    }
}
