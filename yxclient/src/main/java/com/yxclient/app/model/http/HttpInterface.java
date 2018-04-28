package com.yxclient.app.model.http;


import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
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
     * 获取首页轮播图
     *
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @GET("/api/ad/home/list")
    Call<ResponseBody> getHeadImages();

    /**
     * 获取商品分类列表
     *
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @GET("/api/goods/kinds")
    Call<ResponseBody> getShopClassic();

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
     * 用户注册
     *
     * @param mobile   手机号
     * @param password 密码
     * @param code     验证码
     * @param type     用户类型
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/user/register")
    Call<ResponseBody> userRegister(@Query("mobile") String mobile, @Query("password") String password, @Query("code") String code, @Query("type") String type);

    /**
     * 登录
     *
     * @param mobile   手机号
     * @param password 密码
     * @param type     用户类型
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/user/login")
    Call<ResponseBody> userlogin(@Query("mobile") String mobile, @Query("password") String password, @Query("type") String type);

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

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/trip/pay")
    Call<ResponseBody> tripPay(@Header("USER-TOKEN") String token, @Body RequestBody requestBody);

    /**
     * 货运下单
     *
     * @param token       用户token
     * @param requestBody 参数
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/trip/express/dispatch")
    Call<ResponseBody> transOrder(@Header("USER-TOKEN") String token, @Body RequestBody requestBody);

    /**
     * 获取出行推荐列表
     *
     * @param orderType
     * @param page
     * @param size
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @GET("/api/trip/user/recommend")
    Call<ResponseBody> ridelist(@Query("orderType") String orderType, @Query("page") int page, @Query("size") int size);

    /**
     * 获取货运推荐列表
     *
     * @param token
     * @param infoType
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @GET("/api/express/recommend")
    Call<ResponseBody> translist(@Header("USER-TOKEN") String token, @Query("infoType") String infoType);


    /**
     * 出行类订单模糊查询
     *
     * @param token
     * @param page
     * @param size
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @GET("/api/trip/misSearch")
    Call<ResponseBody> getTripSearchList(@Header("USER-TOKEN") String token, @Query("origin") String origin, @Query("site") String site, @Query("infoType") String infoType,
                                         @Query("time") String time, @Query("page") int page, @Query("size") int size);

    //Call<ResponseBody> getTripSearchList(@Header("USER-TOKEN") String token, @Query("searchValue") String searchValue, @Query("page") int page, @Query("size") int size);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/user/uploadHead")
    Call<ResponseBody> uploadHead(@Header("USER-TOKEN") String token, @Body RequestBody requestBody);

    /**
     * 获取商品列表
     *
     * @param kind
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @GET("/api/goods/product/list")
    Call<ResponseBody> getGoodsList(@Header("USER-TOKEN") String token,@Query("kind") String kind, @Query("page") int page, @Query("size") int size);


    /**
     * 搜索商品列表
     *

     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @GET("/api/goods/misSearch")
    Call<ResponseBody> getSearchGoodsList(@Header("USER-TOKEN") String token,@Query("searchValue") String searchValue, @Query("page") int page, @Query("size") int size);
    /**
     * 获取商品详细信息列表
     *
     * @param uuid
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/goods/product/info/{uuid}")
    Call<ResponseBody> getGoodProductDetails(@Path("uuid") String uuid);

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
     * 获取货物类型列表
     *
     * @param token
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/express/kind")
    Call<ResponseBody> transKindlist(@Header("USER-TOKEN") String token);

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
     * 修改用户信息
     *
     * @param token 用户token
     * @return
     */
        @Multipart
        @POST("/api/user/modify")
        Call<ResponseBody> updateUserInfo(@Header("USER-TOKEN") String token, @PartMap Map<String, RequestBody> params);

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
     * 删除订单
     *
     * @param token
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/goods/delete/{no}")
    Call<ResponseBody> deleteOrder(@Header("USER-TOKEN") String token, @Path("no") String no);


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
     * 获取收货地址列表
     *
     * @param token
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/user/consignee/list")
    Call<ResponseBody> getReceiptAddress(@Header("USER-TOKEN") String token);

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
     * 用户出行抢单
     *
     * @param token
     * @param orderNo
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/trip/snatch/{orderNo}")
    Call<ResponseBody> snatchOrder(@Header("USER-TOKEN") String token, @Path("orderNo") String orderNo, @Body RequestBody requestBody);

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
     * 获取订单详情
     *
     * @param token
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/trip/info/{orderNo}")
    Call<ResponseBody> getTripOrder(@Header("USER-TOKEN") String token,@Path("orderNo")String orderNo);

    /**
     * 找回密码
     *
     * @param mobile
     * @param password
     * @param code
     * @param type
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/user/modifyPassword")
    Call<ResponseBody> findPassword(@Header("USER-TOKEN") String token, @Query("mobile") String mobile, @Query("password") String password, @Query("code") String code, @Query("type") String type);


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
     * 版本更新
     *
     * @param token
     * @return
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @GET("/api/app/version")
    Call<ResponseBody> getAppVersion(@Header("USER-TOKEN") String token,@Query("type") String type);

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
