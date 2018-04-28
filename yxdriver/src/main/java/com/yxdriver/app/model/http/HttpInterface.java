package com.yxdriver.app.model.http;


import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * author：Anumbrella
 * Date：16/5/29 下午3:52
 */

public interface HttpInterface {
    /**
     * 获取引导页图片
     *
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @GET("/api/ad/lead/list")
    Call<ResponseBody> getImages();

    /**
     * 获取商品分类列表
     *
     * @param token
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @GET("/api/goods/kinds")
    Call<ResponseBody> getShopClassic(@Header("USER-TOKEN") String token);

    /**
     * 获取验证码
     *
     * @param phone 手机号
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/code/create")
    Call<ResponseBody> getCode(@Query("mobile") String phone,@Query("type") int type);

    /**
     * 司机注册
     *
     * @param mobile   手机号
     * @param password 密码
     * @param code     验证码
     * @param type     用户类型
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/user/register")
    Call<ResponseBody> regist(@Query("mobile") String mobile, @Query("password") String password, @Query("code") String code, @Query("type") String type);

    /**
     * 找回密码
     *
     * @param mobile
     * @param password
     * @param code
     * @param type
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/user/modifyPassword")
    Call<ResponseBody> findPassword(@Header("USER-TOKEN") String token, @Query("mobile") String mobile, @Query("password") String password, @Query("code") String code, @Query("type") String type);

    /**
     * 设置密码
     *
     * @param password 密码
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/user/setPassword")
    Call<ResponseBody> setPwd(@Header("USER-TOKEN") String token, @Query("password") String password);

    /**
     * 登录
     *
     * @param mobile   手机号
     * @param password 密码
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/user/login")
    Call<ResponseBody> login(@Query("mobile") String mobile, @Query("password") String password, @Query("type") String type);

    /**
     * 获取用户信息
     *
     * @param token 用户token
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/user/info")
    Call<ResponseBody> getUserInfo(@Header("USER-TOKEN") String token);

    /**
     * 获取首页轮播图
     *
     * @param token 用户token
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @GET("/api/ad/home/list")
    Call<ResponseBody> getAdlist();

    /**
     * 修改用户信息
     *
     * @param token 用户token
     * @return
     */
    @Multipart
    @POST("/api/user/modify")
    Call<ResponseBody> updateUserInfo(@Header("USER-TOKEN") String token, @PartMap Map<String, RequestBody> params);

    /**
     * 修改头像
     *
     * @param token
     * @param headImage
     * @return
     */
    @Multipart
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/user/uploadImage")
    Call<ResponseBody> updateHeadImage(@Header("USER-TOKEN") String token, @Part("headImage") MultipartBody.Part headImage);


    /**
     * 获取首页订单推荐列表
     *
     * @param token
     * @param infoType
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/express/recommend")
    Call<ResponseBody> indexOrderlist(@Header("USER-TOKEN") String token, @Query("infoType") String infoType);

    /**
     * 出行下单
     *
     * @param token       用户token
     * @param requestBody 参数
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/journey/dispatch")
    Call<ResponseBody> rideOrder(@Header("USER-TOKEN") String token, @Body RequestBody requestBody);

    /**
     * 出行类订单详情
     *
     * @param token
     * @param orderNo
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/trip/info/{orderNo}")
    Call<ResponseBody> tripOrderInfo(@Header("USER-TOKEN") String token, @Path("orderNo") String orderNo);

    /**
     * 货运下单
     *
     * @param token       用户token
     * @param requestBody 参数
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/express/dispatch")
    Call<ResponseBody> goodsOrder(@Header("USER-TOKEN") String token, @Body RequestBody requestBody);

    /**
     * 获取货运推荐列表
     *
     * @param token
     * @param infoType
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/express/recommend")
    Call<ResponseBody> translist(@Header("USER-TOKEN") String token, @Query("infoType") String infoType);


    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/user/uploadHead")
    Call<ResponseBody> uploadHead(@Header("USER-TOKEN") String token, @Body RequestBody requestBody);


    /**
     * 司机拒绝/确认订单
     *
     * @param token
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/trip/accept")
    Call<ResponseBody> selectOrderStatus(@Header("USER-TOKEN") String token, @Body RequestBody requestBody);
    /**
     * 获取商品列表
     *
     * @param token token验证
     * @param kind
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @GET("/api/goods/product/list")
    Call<ResponseBody> getGoodsList(@Header("USER-TOKEN") String token, @Query("kind") String kind, @Query("page") int page, @Query("size") int size);

    /**
     * 搜索商品列表
     *
     * @param kind
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @GET("/api/goods/misSearch")
    Call<ResponseBody> getSearchGoodsList(@Header("USER-TOKEN") String token,@Query("searchValue") String searchValue, @Query("page") int page, @Query("size") int size);
    /**
     * 订单管理
     *
     * @param token
     * @param appId
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @GET("/api/v1/driver/order/history")
    Call<ResponseBody> getHistoryOrderList(@Header("TG-APP-ID") String appId, @Header("TG-USER-TOKEN") String token, @Query("type") String type);

    /**
     * 查询手机号
     *
     * @param token
     * @param appId
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @GET("/api/v1/driver/")
    Call<ResponseBody> findPhone(@Header("TG-APP-ID") String appId, @Header("TG-USER-TOKEN") String token, @Path("phone") String phone);

    /**
     * 查询订单信息
     *
     * @param token
     * @param appId
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @GET("/api/v1/driver/order/getOrder")
    Call<ResponseBody> findOrder(@Header("TG-APP-ID") String appId, @Header("TG-USER-TOKEN") String token, @Query("no") String no);


    /**
     * 获取版本号
     *
     * @param token
     * @param appId
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @GET("/api/v1/driver/app/version")
    Call<ResponseBody> getVersion(@Header("TG-APP-ID") String appId, @Header("TG-USER-TOKEN") String token);


    /**
     * 抢单or拒单
     *
     * @param token
     * @param appId
     * @param no    参数
     * @param type
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/v1/driver/order/getOrRefuse")
    Call<ResponseBody> doOrder(@Header("TG-APP-ID") String appId, @Header("TG-USER-TOKEN") String token, @Query("no") String no, @Query("type") String type);

    /**
     * 上报位置信息
     *
     * @param requestBody 参数
     * @param appId
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/v1/gps/collect")
    Call<ResponseBody> uploadAddr(@Header("TG-APP-ID") String appId, @Header("TG-USER-TOKEN") String token, @Body RequestBody requestBody);

    /**
     * 检票
     *
     * @param token
     * @param appId
     * @param no    票号
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/v1/driver/order/take")
    Call<ResponseBody> checkTicket(@Header("TG-APP-ID") String appId, @Header("TG-USER-TOKEN") String token, @Query("netTicketNo") String no);

    @Multipart
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/user/modify")
    Call<MultipartBody> uploadUserInfo(@Header("USER-TOKEN") String token, @Query("name") String name, @Query("password") String password, @Query("sex") String sex, @Query("birthday") String birthday, @PartMap Map<String, RequestBody> params);

    /**
     * 司机认证照片上传(多张)
     *
     * @param token
     * @param params
     * @return
     */
    @Multipart
    @POST("/api/user/uploadImage")
    Call<ResponseBody> uploadDriverImages(@Header("USER-TOKEN") String token, @PartMap Map<String, RequestBody> params
    );
    /**
     * 司机认证照片上传（单张）
     *
     * @param token
     * @param params
     * @return
     */
    @Multipart
    @POST("/api/user/uploadImage/single")
    Call<ResponseBody> uploadSingleDriverImages(@Header("USER-TOKEN") String token, @PartMap Map<String, RequestBody> params
    );
    /**
     * 提交用户个人信息
     *
     * @param token           token
     * @param name            名字
     * @param province        省份
     * @param city            市
     * @param area            区县
     * @param cardNo          身份证号码
     * @param emergencyName   紧急联系人
     * @param emergencyMobile 紧急联系人号码
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/user/baseInfo")
    Call<ResponseBody> baseInfo(@Header("USER-TOKEN") String token,
                                @Query("name") String name,
                                @Query("province") String province,
                                @Query("city") String city,
                                @Query("area") String area,
                                @Query("cardNo") String cardNo,
                                @Query("emergencyName") String emergencyName,
                                @Query("emergencyMobile") String emergencyMobile);


    /**
     * 获取车辆信息列表
     *
     * @param token
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @GET("/api/car/type")
    Call<ResponseBody> carType(@Header("USER-TOKEN") String token);

    /**
     * @param token
     * @param plateNumber      车牌号
     * @param plateNumberColor 车牌颜色类型
     * @param brand            车辆品牌（帕萨特、沃尔沃）
     * @param color            车辆颜色
     * @param type             车辆类型编码
     * @param seats            核载人数（非货车必传）
     * @param capacity         载重量（货车必传）
     * @param length           车长度（货车必传）
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/car/create/baseInfo")
    Call<ResponseBody> carBaseInfo(@Header("USER-TOKEN") String token,
                                   @Query("plateNumber") String plateNumber,
                                   @Query("plateNumberColor") String plateNumberColor,
                                   @Query("brand") String brand,
                                   @Query("color") String color,
                                   @Query("type") String type,
                                   @Query("seats") int seats,
                                   @Query("capacity") double capacity,
                                   @Query("length") double length);

    /**
     * 获取用户的车辆信息
     *
     * @param token
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/car/list")
    Call<ResponseBody> userCarBaseInfo(@Header("USER-TOKEN") String token);

    /**
     * 获取首页订单推荐列表
     *
     * @param token     用户ID
     * @param infoType  类型（小型货运、大型货运、快递小件、顺风车、专车）
     * @param orderType 订单类型（货运、出行）
     * @param page      第几页
     * @param size      每页几条数据
     * @param curPoint  当前位置 比如昆明
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @GET("/api/trip/recommend")
    Call<ResponseBody> getIndexOrderList(@Header("USER-TOKEN") String token, @Query("infoType") String infoType, @Query("orderType") String orderType, @Query("curPoint") String curPoint, @Query("page") int page, @Query("size") int size);

    /**
     * 出行类订单模糊查询
     *
     * @param token
     * @param searchValue
     * @param page
     * @param size
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @GET("/api/trip/misSearch")
    Call<ResponseBody> getTripSearchList(@Header("USER-TOKEN") String token, @Query("origin") String origin, @Query("site") String site, @Query("infoType") String infoType,
                                         @Query("time") String time, @Query("page") int page, @Query("size") int size);

    /**
     * 获取出行、货运订单列表
     *
     * @param token     用户ID
     * @param infoType  类型（小型货运、大型货运、快递小件、顺风车、专车）
     * @param orderType 订单类型（货运、出行）
     * @param page      第几页
     * @param size      每页几条数据
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @GET("/api/trip/myOrder")
    Call<ResponseBody> getMyTripOrderList(@Header("USER-TOKEN") String token, @Query("infoType") String infoType, @Query("orderType") String orderType, @Query("page") int page, @Query("size") int size);

    /**
     * 获取同城列表
     *
     * @param token
     * @param page
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @GET("/api/news/city/list")
    Call<ResponseBody> getSameCityList(@Header("USER-TOKEN") String token, @Query("page") int page, @Query("size") int size);

    /**
     * 司机抢单
     *
     * @param token   用户token
     * @param orderNo 订单号
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/trip/snatch/{orderNo}")
    Call<ResponseBody> snatchOrder(@Header("USER-TOKEN") String token, @Path("orderNo") String orderNo);

    /**
     * 获取当前司机发布的订单列表
     *
     * @param token
     * @param page
     * @param size
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @GET("/api/trip/myDispatch")
    Call<ResponseBody> getmyDispatchList(@Header("USER-TOKEN") String token, @Query("page") int page, @Query("size") int size);

    /**
     * 上传推送ID
     *
     * @param token
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/user/device")
    Call<ResponseBody> uploadDeviceID(@Header("USER-TOKEN") String token, @Body RequestBody requestBody);


    /**
     * 获取商品详细信息列表
     *
     * @param token
     * @param uuid
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/goods/product/info/{uuid}")
    Call<ResponseBody> getGoodProductDetails(@Header("USER-TOKEN") String token, @Path("uuid") String uuid);


    /**
     * 收藏商品
     *
     * @param token
     * @param uuid
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/goods/favorite/{uuid}")
    Call<ResponseBody> saveFavorite(@Header("USER-TOKEN") String token, @Path("uuid") String uuid);

    /**
     * 获取出行、货运订单列表
     *
     * @param token
     * @param orderType
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/goods/product/info/{uuid}")
    Call<ResponseBody> getMyOrderList(@Header("USER-TOKEN") String token, @Query("orderType") String orderType, @Query("infoType") String infoType, @Query("status") String status, @Query("page") int page, @Query("size") int size);


    /**
     * 获取默认的收货地址
     *
     * @param token
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @GET("/api/user/consignee/getDefault")
    Call<ResponseBody> getDefaultReceiptAddress(@Header("USER-TOKEN") String token);


    /**
     * 获取收货地址列表
     *
     * @param token
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/user/consignee/list")
    Call<ResponseBody> getReceiptAddress(@Header("USER-TOKEN") String token);


    /**
     * 添加收货地址
     *
     * @param token
     * @param addressMap
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/user/consignee/add")
    Call<ResponseBody> saveReceiptAddress(@Header("USER-TOKEN") String token, @Body Map<String, String> addressMap);


    /**
     * 修改收货地址
     *
     * @param token
     * @param addressMap
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/user/consignee/modify")
    Call<ResponseBody> editReceiptAddress(@Header("USER-TOKEN") String token, @Body Map<String, String> addressMap);

    /**
     * 删除收货地址
     *
     * @param token
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/user/consignee/delete/{uuid}")
    Call<ResponseBody> deleteReceiptAddress(@Header("USER-TOKEN") String token, @Path("uuid") String uuid);

    /**
     * 提交订单
     *
     * @param token
     * @param submitMap
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/goods/book")
    Call<ResponseBody> submitOrder(@Header("USER-TOKEN") String token, @Body Map<String, String> submitMap);

    /**
     * 订单支付
     *
     * @param token
     * @param submitMap
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/goods/pay")
    Call<ResponseBody> orderPay(@Header("USER-TOKEN") String token, @Body Map<String, String> submitMap);

    /**
     * 取消订单(行程)
     *
     * @param token
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/trip/cancel")
    Call<ResponseBody> cancelOrder(@Header("USER-TOKEN") String token, @Body RequestBody requestBody);

    /**
     * 开始订单(行程)
     *
     * @param token
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/trip/going")
    Call<ResponseBody> begainOrder(@Header("USER-TOKEN") String token, @Body RequestBody requestBody);

    /**
     * 结束订单(行程)
     *
     * @param token
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/trip/success")
    Call<ResponseBody> endOrder(@Header("USER-TOKEN") String token, @Body RequestBody requestBody);


    /**
     * 获取宝贝收藏列表
     *
     * @param token
     * @param page
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @GET("/api/goods/favorite/list")
    Call<ResponseBody> getFavoriteList(@Header("USER-TOKEN") String token, @Query("page") int page, @Query("size") int size);


    /**
     * 获取用户的商品订单列表
     *
     * @param token
     * @param page
     * @param size   每页总数
     * @param status 订单状态
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @GET("/api/goods/list")
    Call<ResponseBody> getGodsList(@Header("USER-TOKEN") String token, @Query("status") String status, @Query("page") int page, @Query("size") int size);

    @Multipart
    @POST("/api/user/modify")
    Call<ResponseBody> updateHeadIcon(@Header("USER-TOKEN") String token, @Part("file\"; filename=\"head_icon.png") RequestBody file);

    /**
     * 获取二手车列表
     *
     * @param token
     * @param page
     * @param size  每页总数
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @GET("/api/news/car/list")
    Call<ResponseBody> getOldCarList(@Header("USER-TOKEN") String token, @Query("page") int page, @Query("size") int size);

    /**
     * 发布同城信息
     *
     * @param token
     * @param params
     * @return
     */
    @Multipart
    @POST("/api/news/city/create")
    Call<ResponseBody> publishCityInfo(@Header("USER-TOKEN") String token, @PartMap Map<String, RequestBody> params);

    /**
     * 发布二手车信息
     *
     * @param token
     * @param params
     * @return
     */
    @Multipart
    @POST("/api/news/car/create")
    Call<ResponseBody> publishCarInfo(@Header("USER-TOKEN") String token, @PartMap Map<String, RequestBody> params);

    /**
     * 司机验证
     *
     * @param token
     * @param obj   手机号或者车牌号
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/user/verifyDriver")
    Call<ResponseBody> verifyDriver(@Header("USER-TOKEN") String token, @Query("obj") String obj);

    /**
     * 查询购物订单
     *
     * @param token
     * @param no    订单号
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @GET("/api/goods/info")
    Call<ResponseBody> queryGoodsOrder(@Header("USER-TOKEN") String token, @Query("no") String no);

    /**
     * 获取个人二手车信息列表
     *
     * @param token
     * @param page
     * @param size
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @GET("/api/news/car/myList")
    Call<ResponseBody> getMyOldCarList(@Header("USER-TOKEN") String token, @Query("page") int page, @Query("size") int size);

    /**
     * 个人同城信息列表
     *
     * @param token
     * @param page
     * @param size
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @GET("/api/news/city/myList")
    Call<ResponseBody> getMySameCityList(@Header("USER-TOKEN") String token, @Query("page") int page, @Query("size") int size);

    /**
     * 删除信息（二手车、同城）
     *
     * @param token
     * @param uuid
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/news/delete/{uuid}")
    Call<ResponseBody> deleteInfo(@Header("USER-TOKEN") String token, @Path("uuid") String uuid);

    /**
     * 余额充值
     *
     * @param token       用户ID
     * @param requestBody 参数
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/user/recharge")
    Call<ResponseBody> recharge(@Header("USER-TOKEN") String token, @Body RequestBody requestBody);

    /**
     * 获取信息详情：同城信息、二手车信息
     *
     * @param token
     * @param uuid
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/news/info/{uuid}")
    Call<ResponseBody> getInfoDetail(@Header("USER-TOKEN") String token, @Path("uuid") String uuid);

    /**
     * 信息评论
     *
     * @param token
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/news/evaluation")
    Call<ResponseBody> commentInfo(@Header("USER-TOKEN") String token, @Body RequestBody requestBody);

    /**
     * 更换手机号码
     *
     * @param token
     * @param mobile
     * @param code
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/user/modifyMobile")
    Call<ResponseBody> changePhone(@Header("USER-TOKEN") String token, @Query("mobile") String mobile, @Query("code") String code);

    /**
     * 获取购物订单详情
     *
     * @param token 用户token
     * @param no    订单编号
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @GET("/api/goods/info")
    Call<ResponseBody> getShoppingOrder(@Header("USER-TOKEN") String token, @Query("no") String no);

    /**
     * 版本更新
     *
     * @param token
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @GET("/api/app/version")
    Call<ResponseBody> getAppVersion(@Header("USER-TOKEN") String token,@Query("type") String type);
    /**
     * 删除订单
     *
     * @param token
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/goods/delete/{no}")
    Call<ResponseBody> deleteOrder(@Header("USER-TOKEN") String token, @Path("no") String no);

    /**
     * 获取车辆信息
     *
     * @param token
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/car/info")
    Call<ResponseBody> getCarInfo(@Header("USER-TOKEN") String token);

    /**
     * 订单评论
     *
     * @param token
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/trip/evaluation")
    Call<ResponseBody> evaluationOrder(@Header("USER-TOKEN") String token, @Body RequestBody requestBody);

    /**
     * 商城订单确认
     *
     * @param token
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/goods/finish")
    Call<ResponseBody>ConfirmReceipt(@Header("USER-TOKEN") String token, @Body RequestBody requestBody);

    /**
     * 提现
     *
     * @param token
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/user/cash")
    Call<ResponseBody>setCash(@Header("USER-TOKEN") String token, @Body RequestBody requestBody);

    /**
     * 商城评论
     *
     * @param token
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/evaluation/add")
    Call<ResponseBody>goodvaluation(@Header("USER-TOKEN") String token, @Body RequestBody requestBody);

    /**
     * 提交反馈意见
     *
     * @param token
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/idea/add")
    Call<ResponseBody>setIdea(@Header("USER-TOKEN") String token, @Body RequestBody requestBody);
}
