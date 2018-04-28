package com.yxdriver.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.dou361.dialogui.bean.TieBean;
import com.dou361.dialogui.listener.DialogUIItemListener;
import com.iflytek.cloud.thirdparty.S;
import com.yxdriver.app.R;
import com.yxdriver.app.adapter.CarTypeAdapter;
import com.yxdriver.app.app.DemoApplication;
import com.yxdriver.app.http.RetrofitHttp;
import com.yxdriver.app.model.bean.CarType;
import com.yxdriver.app.model.bean.uploadCarModel;
import com.yxdriver.app.utils.StringUtil;
import com.yxdriver.app.view.MyGridView;

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
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.util.Log;

/**
 * Created by mac on 2017/9/11.
 * 上传车辆信息
 */

/**
 * ==============================================================
 * 1、车牌颜色：蓝牌、黄牌
 * 2、车辆类型：面包车、轿车、SUV、MPV（此四种车型不需要设置车长度和载重,但是需要设置荷载人数）
 * <p>
 * 微型货车（长<3.3m，载重<=1800kg）
 * 轻型货车（长<6m，载重<=4500kg）
 * 中型货车（长>=6m，4500kg<=载重<=12000kg）
 * 大型货车（长>=6m，载重>=12000kg）
 * 货车不需要设置荷载人数
 * ==============================================================
 */

public class YXSetCarInfoActivity extends BaseActivity {
    private static final String TAG = "YXSetCarInfoActivity";

    @BindView(R.id.tv_plate_number_color)
    TextView tvPlateNumberColor;
    @BindView(R.id.tv_car_type)
    TextView tvCarType;
    @BindView(R.id.et_car_length)
    EditText etCarLength;
    @BindView(R.id.et_car_load)
    EditText etCarLoad;
    @BindView(R.id.et_car_seats)
    EditText etCarSeats;
    @BindView(R.id.et_plate_number)
    EditText etPlateNumber;
    // 载重量
    @BindView(R.id.tv_car_loadvalue)
    TextView tv_loadValue;
    // 座位数
    @BindView(R.id.tv_car_seatsvalue)
    TextView tv_sitesValue;

    @BindView(R.id.tv_error_msg)
    TextView tvErrorMsg;

    @BindView(R.id.short_name)
    TextView tv_shortname;
    String token;

    // 车辆相关信息
    private String carBrandColor;//车牌颜色
    private String carNumber;// 车牌号码
    private String carType;// 车辆类型
    private String carWeight; //车辆载重
    private String carSites;// 车辆载人数
    private double carLength;// 车身长度
    private int seats = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 判断如果登录过，则进入主页
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_activity_setcar);
        ButterKnife.bind(this);
        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }
    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**
     * 启动这个Activity的Intent
     *
     * @param context
     * @return
     */
    public static Intent createIntent(Context context) {
        return new Intent(context, YXSetCarInfoActivity.class);
    }
    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void initView() {
        resetErrorMsg();

    }

    @Override
    public void initData() {
        token = DemoApplication.getInstance().getMyToken();

        setData(DemoApplication.getInstance().getcarInfo());
//        getUserCarInfo();
        //获取车型列表
        //getCarTypeFromService();
    }


    @Override
    public void initEvent() {

    }

    @OnClick({R.id.setcar_re_plate, R.id.setcar_btn, R.id.rl_car_type, R.id.setcar_re_seats, R.id.setcar_re_weight, R.id.setcar_txt})
    void myClick(View view) {
        switch (view.getId()) {
            case R.id.setcar_re_plate:
                // 弹出车牌颜色选择框
                showCarColortypeDialog();
                break;
            case R.id.setcar_btn:
                //提交车辆基本信息
//                toActivity(YXSetInfoPhotoActivity.createIntent(context));
                // 提交之前验证
                // 车牌颜色
                carBrandColor = tvPlateNumberColor.getText().toString();
                carNumber = tv_shortname.getText().toString() + etPlateNumber.getText().toString();
                carWeight = tv_loadValue.getText().toString();
                carSites = tv_sitesValue.getText().toString();
                if (StringUtil.isNullOrEmpty(carBrandColor)) {
                    DialogUIUtils.showToast("请选择车牌颜色");
                } else if (StringUtil.isNullOrEmpty(carNumber)) {
                    DialogUIUtils.showToast("请输入车牌号码");
                } else if (StringUtil.isNullOrEmpty(carType)) {
                    DialogUIUtils.showToast("请选择车辆类型");
                } else {
                    //uploadToService();
                    if (StringUtil.isNullOrEmpty(carWeight)) {
                        carWeight = "0.0";
                    } else if (StringUtil.isNullOrEmpty(carSites)) {
                        seats = 2;
                    }
                    JSONObject car=new JSONObject();
                    car.put("NumberColor",carBrandColor);
                    car.put("Carnumber",carNumber);
                    car.put("CarType",carType);
                    car.put("Capacity",carWeight);
                    car.put("CarSeats",seats);
                    DemoApplication.getInstance().setcarInfo(String.valueOf(car));
                    uploadCarInfo(DemoApplication.getInstance().getMyToken(), carNumber, carBrandColor, "", "", carType, seats, Double.parseDouble(carWeight.substring(0,carWeight.length()-2)), carLength);
                }
                break;
            case R.id.rl_car_type:
                List<String> cartypeList = new ArrayList<>();
                cartypeList.add("面包车");
                cartypeList.add("轿车");
                cartypeList.add("SUV");
                cartypeList.add("MPV");
                cartypeList.add("微型货车");
                cartypeList.add("轻型货车");
                cartypeList.add("中型货车");
                cartypeList.add("大型货车");
                showCartypeDialog(cartypeList);
                break;

            case R.id.setcar_re_seats:
                // 设置车辆载人数量
                if (StringUtil.isNullOrEmpty(carType)) {
                    DialogUIUtils.showToast("请先选择车辆类型");
                } else {
                    if ("面包车".equals(carType) || "轿车".equals(carType) || "SUV".equals(carType) || "MPV".equals(carType)) {
                        showSeatsDialog();
                    } else {
                        DialogUIUtils.showToast("货车不需设置");
                    }
                }
                break;
            case R.id.setcar_re_weight:
                // 设置车辆载重
                if (StringUtil.isNullOrEmpty(carType)) {
                    DialogUIUtils.showToast("请先选择车辆类型");
                } else {
                    if ("微型货车".equals(carType) || "轻型货车".equals(carType) || "中型货车".equals(carType) || "大型货车".equals(carType)) {
                        switch (carType) {
                            case "微型货车":
                                carLength = 3.0;
                                break;
                            case "轻型货车":
                                carLength = 5.0;
                                break;
                            case "中型货车":
                                carLength = 6.0;
                                break;
                            case "大型货车":
                                carLength = 7.0;
                                break;
                            default:
                                carLength = 0.0;
                                break;
                        }
                        showWeightDialog();
                    } else {
                        DialogUIUtils.showToast("非货车不需设置");
                    }
                }
                break;
            case R.id.setcar_txt:
                // 选择车牌所在地
                Intent intent = new Intent();
                intent.setClass(context, YXPlatePointActivity.class);
                intent.putExtra("select_short_name", tv_shortname.getText());
                startActivityForResult(intent, 0);
                break;
            default:
                break;
        }

    }

    private void resetErrorMsg() {
        tvErrorMsg.setText("");
    }

    /**
     * 上传车辆信息
     *
     * @param carnumber   车牌号
     * @param numberColor 车牌颜色类型
     * @param brand       车辆品牌（帕萨特、沃尔沃）
     * @param color       车辆颜色
     * @param carType     车辆类型编码
     * @param carSeats    核载人数（非货车必传）
     * @param capacity    载重
     * @param length      车长
     */
    private void uploadCarInfo(String tokenStr, String carnumber, String numberColor, String brand, String color, String carType, int carSeats, double capacity, double length) {
        final Dialog dialog = DialogUIUtils.showMdLoading(context, "数据加载中", false, true, true, true).show();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).carBaseInfo(tokenStr, carnumber, numberColor,
                brand, color, carType, carSeats, capacity, length).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                DialogUIUtils.dismiss(dialog);
                switch (response.code()) {
                    case 200:
                        try {
                            if (response.body() == null) {
                                DialogUIUtils.showToast(response.message());
                            }
                            String result = response.body().string();
                            System.out.println("上传车辆信息返回结果:" + result);
                            JSONObject jsonObject = JSON.parseObject(result);
                            if ("success".equals(jsonObject.getString("code"))) {
                                doLocalSomething(jsonObject.getJSONObject("data"));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        try {
                            String result = response.errorBody().string();
                            DialogUIUtils.showToast(JSON.parseObject(result).getString("message"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }

            @Override
            public void onFailure(Throwable t) {
                DialogUIUtils.dismiss(dialog);
                Log.e(TAG, "添加车辆信息失败 --> " + t.getMessage());
            }
        });
    }

    /**
     * 添加车辆信息成功后，做一些存储操作
     *
     * @param data
     */
    private void doLocalSomething(JSONObject data) {
        //TODO 添加车辆信息成功后，做一些存储操作
//        String carUuid = data.getString("uuid");
//        DemoApplication.getInstance().setCarUuid(carUuid);
        toActivity(YXSetInfoPhotoActivity.createIntent(context));
    }

    /**
     * 判断是不是货车，是货车时返回true
     *
     * @param caType
     * @return
     */
    private boolean isTruck(String caType) {
        if (StringUtil.isNullOrEmpty(caType)) {
            return false;
        }
        int length = caType.length();
        //截取最后两位
        String parseStr = caType.substring(length - 3, length - 1);
        if ("货车".equals(parseStr)) {
            return true;
        }
        return false;
    }

    /**
     * 从服务器中获取车辆类型数据
     */
    private void getCarTypeFromService() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).carType(token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                try {
                    if (response.body() == null) {
                        DialogUIUtils.showToast("获取车辆类型数据失败");
                        finish();
                    } else {
                        String result = response.body().string();
                        JSONObject jsonObject = JSON.parseObject(result);
                        if ("success".equals(jsonObject.getString("code"))) {
                            //fillCarTypeList(jsonObject.getJSONArray("data"));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                //TODO
                Log.e("tag", t.getMessage());
            }
        });
    }

    /**
     * 车牌颜色选择框
     */
    private void showCarColortypeDialog() {
        List<TieBean> strings = new ArrayList<TieBean>();
        strings.add(new TieBean("黄色车牌"));
        strings.add(new TieBean("蓝色车牌"));
        DialogUIUtils.showSheet(context, strings, "取消", Gravity.BOTTOM, true, true, new DialogUIItemListener() {
            @Override
            public void onItemClick(CharSequence text, int position) {
                tvPlateNumberColor.setText(text);
            }

            @Override
            public void onBottomBtnClick() {
                System.out.println("不选择车牌颜色");
            }
        }).show();

    }

    /**
     * 获取车辆类型弹出框
     *
     * @param kinds
     */
    private void showCartypeDialog(final List<String> kinds) {
        View rootView = View.inflate(context, R.layout.custom_cartype_layout, null);
        final BuildBean buildBean = DialogUIUtils.showCustomBottomAlert(context, rootView);
        buildBean.show();
        final MyGridView gridView = (MyGridView) rootView.findViewById(R.id.gv_reply);
        final CarTypeAdapter goodsKindAdapter = new CarTypeAdapter(context, kinds);
        gridView.setAdapter(goodsKindAdapter);
        rootView.findViewById(R.id.txt_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUIUtils.dismiss(buildBean);
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goodsKindAdapter.changeState(position);
                tvCarType.setText(kinds.get(position));
                tv_loadValue.setText("");
                tv_sitesValue.setText("");
                carType = kinds.get(position);
                DialogUIUtils.dismiss(buildBean);
            }
        });
    }

    /**
     * 车辆载重设置
     */
    private void showWeightDialog() {
        //自定义
        View rootView = View.inflate(context, R.layout.custom_weight_dialog_layout, null);
        final BuildBean buildBean = DialogUIUtils.showCustomBottomAlert(context, rootView);
        buildBean.show();
        final EditText ed_Weight = (EditText) rootView.findViewById(R.id.car_weight_weight);
        rootView.findViewById(R.id.txt_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUIUtils.dismiss(buildBean);
            }
        });
        rootView.findViewById(R.id.txt_down).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String weight = ed_Weight.getText().toString();
                if (StringUtil.isNullOrEmpty(weight)) {
                    DialogUIUtils.showToast("请设置车辆载重");
                } else {
                    DialogUIUtils.dismiss(buildBean);
                    tv_loadValue.setText(weight + " kg");
                }
            }
        });
    }

    /**
     * 车辆载人数设置
     */
    private void showSeatsDialog() {
        //自定义
        View rootView = View.inflate(context, R.layout.custom_seats_dialog_layout, null);
        final BuildBean buildBean = DialogUIUtils.showCustomBottomAlert(context, rootView);
        buildBean.show();
        final EditText ed_Weight = (EditText) rootView.findViewById(R.id.car_seats_weight);
        rootView.findViewById(R.id.txt_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUIUtils.dismiss(buildBean);
            }
        });
        rootView.findViewById(R.id.txt_down).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String weight = ed_Weight.getText().toString();
                if (StringUtil.isNullOrEmpty(weight)) {
                    DialogUIUtils.showToast("请设置车辆荷载数");
                } else {
                    DialogUIUtils.dismiss(buildBean);
                    seats = Integer.parseInt(weight);
                    tv_sitesValue.setText(weight + " 人");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null)
            return;

        switch (requestCode) {
            case 0:
                Bundle bundle = data.getExtras();
                String ShortName = bundle.getString("short_name");
                tv_shortname.setText(ShortName);
                break;
            default:
                break;
        }
    }

    //加载上次数据
    private void setData(String carInfo){
        JSONObject car=JSON.parseObject(carInfo);
        if (car!=null) {
            tvPlateNumberColor.setText(car.getString("NumberColor"));
            etPlateNumber.setText(car.getString("Carnumber").substring(1));
            tvCarType.setText(car.getString("CarType"));
            carType = car.getString("CarType");
            String carType = car.getString("CarType");
            if ("微型货车".equals(carType) || "轻型货车".equals(carType) || "中型货车".equals(carType) || "大型货车".equals(carType)) {
                tv_loadValue.setText(car.getString("Capacity"));
                tv_sitesValue.setText(" ");
            } else {
                tv_loadValue.setText(" ");
                tv_sitesValue.setText(car.getString("CarSeats")+"人");
            }
        }
    }
}
