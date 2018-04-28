package com.yxdriver.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationQualityReport;
import com.bumptech.glide.Glide;
import com.dou361.dialogui.DialogUIUtils;
import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.jude.rollviewpager.hintview.IconHintView;
import com.yxdriver.app.R;
import com.yxdriver.app.activity.ChooseCityActivity;
import com.yxdriver.app.activity.ImageBrowseActivity;
import com.yxdriver.app.activity.YXQueryIndexActivity;
import com.yxdriver.app.adapter.IndexViewPagerAdapter;
import com.yxdriver.app.app.DemoApplication;
import com.yxdriver.app.http.RetrofitHttp;
import com.yxdriver.app.model.bean.HeadImagesModel;
import com.yxdriver.app.poisearch.util.CityModel;
import com.yxdriver.app.poisearch.util.CityUtil;
import com.yxdriver.app.utils.LocUtils;
import com.yxdriver.app.utils.StringUtil;
import com.yxdriver.app.view.SlidingTabLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;
import zuo.biao.library.base.BaseFragment;

/**
 * Created by jpeng on 16-11-14.
 * 首页
 */
public class IndexPager extends BaseFragment {
    private static final String TAG = "IndexPager";
    private ViewPager viewPager;
    private Toolbar toolbar;
    private List<Fragment> list;
    private SlidingTabLayout tabLayout;
    private List<JSONObject> adlist = new ArrayList<>();
    private List<HeadImagesModel> list2 = new ArrayList<>();
    @BindView(R.id.view_pager)
    RollPagerView mViewPager;
    @BindView(R.id.index_city)
    TextView tv_City;
    private TestLoopAdapter mLoopAdapter;
    public static int MAIN_ACTIVITY_REQUEST_CHOOSE_CITY_ADDRESS_CODE = 2;
    private CityModel mCurrCity;// 当前城市
    // 定位相关
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;


    /**
     * 创建一个Fragment实例
     *
     * @param userId
     * @return
     */
    public static IndexPager createInstance(long userId) {
        return createInstance(userId, null);
    }

    /**
     * 创建一个Fragment实例
     *
     * @param userId
     * @param userName
     * @return
     */
    public static IndexPager createInstance(long userId, String userName) {
        IndexPager fragment = new IndexPager();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //TODO demo_fragment改为你所需要的layout文件
        setContentView(R.layout.yx_index_fragment);
        ButterKnife.bind(this, view);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        tabLayout = (SlidingTabLayout) view.findViewById(R.id.tabLayout);
        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        initViewDate();
        //功能归类分区方法，必须调用>>>>>>>>>>
        return view;//返回值必须为view
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //加载数据
            //getAdList(DemoApplication.getInstance().getMyToken());
        }
    }

    @Override
    public void initView() {
        mCurrCity = CityUtil.getDefCityModel(context);
        tv_City.setText(mCurrCity.getCity());
        //初始化定位
        initLocation();
        mViewPager.setPlayDelay(3000);
        mViewPager.setAdapter(mLoopAdapter = new TestLoopAdapter(mViewPager));
        mViewPager.setHintView(new IconHintView(context, R.drawable.shape_point_select, R.drawable.shape_point_unselect));
        mViewPager.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //toActivity(AdDetailActivity.createIntent(context, "广告详情", mLoopAdapter.imgs[position]));
                handleBrowse(mLoopAdapter.imgs[position]);
            }
        });
    }

    @Override
    public void initData() {
        ArrayList<String> images = new ArrayList<>();
        images.add("http://ohyfvxowv.bkt.clouddn.com/image/shunH-APP/add/home/home1_2x.png");
        images.add("http://ohyfvxowv.bkt.clouddn.com/image/shunH-APP/add/home/home1_2x.png");
        images.add("http://ohyfvxowv.bkt.clouddn.com/image/shunH-APP/add/home/home1_2x.png");
        images.add("http://ohyfvxowv.bkt.clouddn.com/image/shunH-APP/add/home/home1_2x.png");
        getAdList();
        startLocation();
    }


    @Override
    public void initEvent() {

    }


    private void initViewDate() {
        list = new ArrayList<>();
        list.add(new IndexListFragment());
        list.add(new IndexDoOrderFragment());
        IndexViewPagerAdapter adapter = new IndexViewPagerAdapter(getChildFragmentManager(), list, view.getContext());
        viewPager.setAdapter(adapter);
        tabLayout.setCustomTabView(R.layout.custom_tab, 0);
        tabLayout.setDistributeEvenly(true);
        tabLayout.setViewPager(viewPager);
        tabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                //return Color.WHITE;
                return ContextCompat.getColor(getActivity(), R.color.main_blue);
            }
        });
    }

    /**
     * 获取轮播图
     *
     */
    private void getAdList() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).getAdlist()
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Response<ResponseBody> response) {
                        try {
                            switch (response.code()) {
                                case 200:
                                    if (response.body() == null) {
                                        DialogUIUtils.showToast("接口请求错误");
                                    } else {
                                        String result = response.body().string();
                                        System.out.println("广告数据:" + result);
                                        JSONObject jsonObject = JSON.parseObject(result);
                                        if (jsonObject.getString("code").equals("success")) {
                                            JSONArray list_temp = JSON.parseArray(jsonObject.getString("data"));
                                            list2.clear();
                                            for (int i = 0; i < list_temp.size(); i++) {
                                                JSONObject json = list_temp
                                                        .getJSONObject(i);
                                                list2.add(JSON.parseObject(json.toJSONString(), HeadImagesModel.class));
                                            }
                                            final String[] imgs = new String[list2.size()];
                                            for (int i = 0; i < list2.size(); i++) {
                                                imgs[i] = list2.get(i).getImage();
                                            }
                                            mLoopAdapter.setImgs(imgs);
                                            /*context.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    mLoopAdapter.setImgs(imgs);
                                                }
                                            });*/
                                            mLoopAdapter.setImgs(imgs);
                                        } else {
                                            DialogUIUtils.showToast(jsonObject.getString("message"));
                                        }
                                    }
                                    break;
                                case 401:
                                    DialogUIUtils.showToast("" + response.code());
                                    break;
                                default:
                                    break;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        DialogUIUtils.showToast("网络不给力");
                    }
                });
    }


    @OnClick({R.id.index_query, R.id.index_left})
    void myClick(View view) {
        switch (view.getId()) {
            case R.id.index_query:
                if (StringUtil.isNullOrEmpty(DemoApplication.getInstance().getMyToken())) {
                    DialogUIUtils.showToast("请先登录");
                } else {
                    toActivity(YXQueryIndexActivity.createIntent(context, ""));
                }
                break;
            case R.id.index_left:
                // 切换城市
                choicseCity();
                break;
            default:
                break;
        }
    }

    private void choicseCity() {
        Intent intent = new Intent();
        intent.setClass(context, ChooseCityActivity.class);
        intent.putExtra(ChooseCityActivity.CURR_CITY_KEY, tv_City.getText().toString());
        startActivityForResult(intent, MAIN_ACTIVITY_REQUEST_CHOOSE_CITY_ADDRESS_CODE);
    }

    /**
     * 轮播图适配器
     */
    private class TestLoopAdapter extends LoopPagerAdapter {
        String[] imgs = new String[0];

        public void setImgs(String[] imgs) {
            this.imgs = imgs;
            notifyDataSetChanged();
        }


        public TestLoopAdapter(RollPagerView viewPager) {
            super(viewPager);
        }

        @Override
        public View getView(ViewGroup container, int position) {
            Log.i("RollViewPager", "getView:" + imgs[position]);

            ImageView view = new ImageView(container.getContext());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("RollViewPager", "onClick");
                }
            });
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            Glide.with(context)
                    .load(imgs[position])
                    .into(view);
            return view;
        }

        @Override
        public int getRealCount() {
            return imgs.length;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println(String.valueOf(resultCode));
        if (data != null) {
            if (MAIN_ACTIVITY_REQUEST_CHOOSE_CITY_ADDRESS_CODE == requestCode && resultCode == RESULT_OK) {
                CityModel cityModel = data.getParcelableExtra(ChooseCityActivity.CURR_CITY_KEY);
                //改变城市显示
                mCurrCity = cityModel;
                tv_City.setText(mCurrCity.getCity());
                // 刷新推荐数据
            }
        }
    }

    private void initLocation() {
        //初始化client
        locationClient = new AMapLocationClient(context);
        locationOption = getDefaultOption();
        //设置定位参数
        locationClient.setLocationOption(locationOption);
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
    }

    /**
     * 默认的定位参数
     *
     * @return
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(true);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }

    /**
     * 设置城市。如果定位结果返回的城市和当前城市不同，则进行修正
     */
    private void resetCityModelByLocation(AMapLocation location) {
        if (location == null || location.getCityCode() == null) {
            return;
        }
        CityModel cityModel = CityUtil.getCityByCode(context, location.getCityCode());
        if (!mCurrCity.getAdcode().equals(cityModel.getAdcode())) {
            mCurrCity = cityModel;
        }
        tv_City.setText(mCurrCity.getCity());
    }

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (null != location) {
                StringBuffer sb = new StringBuffer();
                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if (location.getErrorCode() == 0) {
                    // 定位成功
                    resetCityModelByLocation(location);
                } else {
                    //定位失败
                    sb.append("定位失败" + "\n");
                    sb.append("错误码:" + location.getErrorCode() + "\n");
                    sb.append("错误信息:" + location.getErrorInfo() + "\n");
                    sb.append("错误描述:" + location.getLocationDetail() + "\n");
                }
                sb.append("***定位质量报告***").append("\n");
                sb.append("* WIFI开关：").append(location.getLocationQualityReport().isWifiAble() ? "开启" : "关闭").append("\n");
                sb.append("* GPS状态：").append(getGPSStatusString(location.getLocationQualityReport().getGPSStatus())).append("\n");
                sb.append("* GPS星数：").append(location.getLocationQualityReport().getGPSSatellites()).append("\n");
                sb.append("****************").append("\n");
                //定位之后的回调时间
                sb.append("回调时间: " + LocUtils.formatUTC(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "\n");
                //解析定位结果，
                String result = sb.toString();
                System.out.println("定位结果:" + result);
            } else {
                System.out.println("定位失败，loc is null");
            }
        }
    };

    /**
     * 获取GPS状态的字符串
     *
     * @param statusCode GPS状态码
     * @return
     */
    private String getGPSStatusString(int statusCode) {
        String str = "";
        switch (statusCode) {
            case AMapLocationQualityReport.GPS_STATUS_OK:
                str = "GPS状态正常";
                break;
            case AMapLocationQualityReport.GPS_STATUS_NOGPSPROVIDER:
                str = "手机中没有GPS Provider，无法进行GPS定位";
                break;
            case AMapLocationQualityReport.GPS_STATUS_OFF:
                str = "GPS关闭，建议开启GPS，提高定位质量";
                break;
            case AMapLocationQualityReport.GPS_STATUS_MODE_SAVING:
                str = "选择的定位模式中不包含GPS定位，建议选择包含GPS定位的模式，提高定位质量";
                break;
            case AMapLocationQualityReport.GPS_STATUS_NOGPSPERMISSION:
                str = "没有GPS定位权限，建议开启gps定位权限";
                break;
        }
        return str;
    }

    // 根据控件的选择，重新设置定位参数
    private void resetOption() {
        // 设置是否需要显示地址信息
        locationOption.setNeedAddress(true);
        /**
         * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
         * 注意：只有在高精度模式下的单次定位有效，其他方式无效
         */
        locationOption.setGpsFirst(false);
        // 设置是否开启缓存
        locationOption.setLocationCacheEnable(false);
        // 设置是否单次定位
        locationOption.setOnceLocation(true);
        //设置是否等待设备wifi刷新，如果设置为true,会自动变为单次定位，持续定位时不要使用
        locationOption.setOnceLocationLatest(false);
        //设置是否使用传感器
        locationOption.setSensorEnable(true);
        //locationOption.setHttpTimeOut(2000);
        // locationOption.setInterval(5000);
    }

    /**
     * 开始定位
     *
     * @since 2.8.0
     */
    private void startLocation() {
        //根据控件的选择，重新设置定位参数
        resetOption();
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }

    /**
     * 停止定位
     *
     * @since 2.8.0
     */
    private void stopLocation() {
        // 停止定位
        locationClient.stopLocation();
    }

    /**
     * 销毁定位
     *
     * @since 2.8.0
     */
    private void destroyLocation() {
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyLocation();
    }

    public void handleBrowse(String images) {
        ArrayList<String> imgs = new ArrayList<>();
        imgs.add(images);

        Intent intent = new Intent(context, ImageBrowseActivity.class);
        intent.putExtra("position", 0);
        intent.putStringArrayListExtra("imgs", imgs);
        startActivity(intent);

    }


}
