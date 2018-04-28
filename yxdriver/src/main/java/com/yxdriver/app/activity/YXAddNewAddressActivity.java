package com.yxdriver.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dou361.dialogui.DialogUIUtils;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.citywheel.CityPickerView;
import com.yxdriver.app.R;
import com.yxdriver.app.app.DemoApplication;
import com.yxdriver.app.http.RetrofitHttp;
import com.yxdriver.app.model.bean.ReceiptAddressModel;
import com.yxdriver.app.utils.StringUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;
import zuo.biao.library.base.BaseActivity;

public class YXAddNewAddressActivity extends BaseActivity {
    // 收货人姓名
    @BindView(R.id.et_user_name)
    EditText etUserName;
    // 收货人联系电话
    @BindView(R.id.et_user_phone)
    EditText etUserPhone;
    // 收货人省市区
    @BindView(R.id.tv_area)
    TextView tvArea;
    // 收货人街道信息
    @BindView(R.id.et_street)
    EditText etStreet;
    // 收货人当地邮编号码
    @BindView(R.id.et_postal_code)
    EditText etPostalCode;
    // 详细地址
    @BindView(R.id.et_address)
    EditText etAddress;
    ReceiptAddressModel model;  //收货地址对象
    private String provinceVal;
    private String cityVal;
    private String countyVal;

    public static Intent createIntent(Context context, ReceiptAddressModel data) {
        return new Intent(context, YXAddNewAddressActivity.class).putExtra(RESULT_DATA, data);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_add_new_address);
        ButterKnife.bind(this);
        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
    }

    @Override
    public void initView() {
        model = (ReceiptAddressModel) getIntent().getExtras().getSerializable(RESULT_DATA);
        if (model != null) {
            // 属于编辑
            etUserName.setText(model.getName());
            etUserPhone.setText(model.getMobile());
            tvArea.setText(model.getProvince() + model.getCity() + model.getCounty());
            etStreet.setText(model.getStreet());
            etAddress.setText(model.getAddress());
            etPostalCode.setText(model.getPostcode());
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }

    @OnClick({R.id.ll_choose_address, R.id.btn_save})
    void viewOnclick(View view) {
        switch (view.getId()) {
            case R.id.ll_choose_address:
                //选择地区
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                }
                showCityDialog();
                break;
            case R.id.btn_save:
                //保存收货地址
                String name = etUserName.getText().toString();
                String mobile = etUserPhone.getText().toString();
                String area = tvArea.getText().toString();
                String street = etStreet.getText().toString();
                String address = etAddress.getText().toString();
                String postalCode = etPostalCode.getText().toString();
                if (StringUtil.isNullOrEmpty(name)) {
                    DialogUIUtils.showToast("请输入收货人姓名!");
                } else if (StringUtil.isNullOrEmpty(mobile)) {
                    DialogUIUtils.showToast("请输入收货人电话!");
                } else if (StringUtil.isNullOrEmpty(area)) {
                    DialogUIUtils.showToast("请选择所在区域!");
                } else if (StringUtil.isNullOrEmpty(address)) {
                    DialogUIUtils.showToast("请输入详细联系地址!");
                } else {
                    if (model != null) {
                        editeAddr(DemoApplication.getInstance().getMyToken(), model.getUuid(), name, mobile, provinceVal, cityVal, countyVal, street, address, postalCode);
                    } else {
                        addAddr(DemoApplication.getInstance().getMyToken(), name, mobile, provinceVal, cityVal, countyVal, street, address, postalCode);
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 添加收货地址
     *
     * @param token    用户token
     * @param name     收货人姓名
     * @param phone    收货人电话
     * @param province 所在省
     * @param city     所在城市
     * @param county   所在区域
     * @param street   街道信息
     * @param address  详细地址
     * @param postcode 邮政编码
     */
    private void addAddr(String token, String name, String phone, String province, String city, String county, String street, String address, String postcode) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("mobile", phone);
        params.put("street", street);
        params.put("province", province);
        params.put("city", city);
        params.put("county", county);
        params.put("address", address);
        params.put("postcode", postcode);
        final Dialog dialog = DialogUIUtils.showMdLoading(context, "数据提交中", true, true, true, true).show();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).saveReceiptAddress(token, params).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                DialogUIUtils.dismiss(dialog);
                switch (response.code()) {
                    case 200:
                        if (response.body() == null) {
                            DialogUIUtils.showToast(response.message());
                        } else {
                            try {
                                String result = response.body().string();
                                JSONObject jsonObject = JSON.parseObject(result);
                                if ("success".equals(jsonObject.getString("code"))) {
                                    onBackPressed();
                                    finish();
                                } else {
                                    DialogUIUtils.showToast("添加地址失败");
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                DialogUIUtils.showToast("解析失败");
                            }
                        }
                        break;
                    default:
                        DialogUIUtils.showToast(response.message());
                        break;
                }
            }

            @Override
            public void onFailure(Throwable t) {
                DialogUIUtils.dismiss(dialog);
                DialogUIUtils.showToast(t.getMessage());
            }
        });
    }

    /**
     * 添加收货地址
     *
     * @param token    用户token
     * @param name     收货人姓名
     * @param phone    收货人电话
     * @param province 所在省
     * @param city     所在城市
     * @param county   所在区域
     * @param street   街道信息
     * @param address  详细地址
     * @param postcode 邮政编码
     */
    private void editeAddr(String token, String uuid, String name, String phone, String province, String city, String county, String street, String address, String postcode) {
        Map<String, String> params = new HashMap<>();
        params.put("uuid", uuid);
        params.put("name", name);
        params.put("mobile", phone);
        params.put("street", street);
        params.put("province", province);
        params.put("city", city);
        params.put("county", county);
        params.put("address", address);
        params.put("postcode", postcode);
        final Dialog dialog = DialogUIUtils.showMdLoading(context, "数据提交中", true, true, true, true).show();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).editReceiptAddress(token, params).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                DialogUIUtils.dismiss(dialog);
                switch (response.code()) {
                    case 200:
                        if (response.body() == null) {
                            DialogUIUtils.showToast(response.message());
                        } else {
                            try {
                                String result = response.body().string();
                                JSONObject jsonObject = JSON.parseObject(result);
                                if ("success".equals(jsonObject.getString("code"))) {
                                    DialogUIUtils.showToast(jsonObject.getString("message"));
                                    onBackPressed();
                                    finish();
                                } else {
                                    DialogUIUtils.showToast("添加地址失败");
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                DialogUIUtils.showToast("解析失败");
                            }
                        }
                        break;
                    default:
                        DialogUIUtils.showToast(response.message());
                        break;
                }
            }

            @Override
            public void onFailure(Throwable t) {
                DialogUIUtils.dismiss(dialog);
                DialogUIUtils.showToast(t.getMessage());
            }
        });
    }

    private void showCityDialog() {
        CityConfig cityConfig = new CityConfig.Builder(context)
                .title("选择地区")
                .titleBackgroundColor("#E9E9E9")
                .textSize(18)
                .titleTextColor("#585858")
                .textColor("#585858")
                .confirTextColor("#0000FF")
                .cancelTextColor("#000000")
                .province("云南省")
                .city("昆明")
                .district("五华区")
                .visibleItemsCount(5)
                .provinceCyclic(true)
                .cityCyclic(true)
                .showBackground(true)
                .districtCyclic(true)
                .itemPadding(5)
                .setCityInfoType(CityConfig.CityInfoType.BASE)
                .setCityWheelType(CityConfig.WheelType.PRO_CITY_DIS)
                .build();
        CityPickerView cityPicker = new CityPickerView(cityConfig);
        cityPicker.show();
        cityPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
            @Override
            public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                //ProvinceBean 省份信息
                //CityBean     城市信息
                //DistrictBean 区县信息
                //当wheelType==CityConfig.WheelType.PRO时，CityBean和DistrictBean为null
                //当wheelType==CityConfig.WheelType.PRO_CITY时， DistrictBean为null
                //当wheelType==CityConfig.WheelType.PRO_CITY_DIS时， 可取省市区三个对象的值
                //使用之前需判断province、city、district是否等于null
                if (province != null && city != null && district != null) {
                    provinceVal = province.getName();
                    cityVal = city.getName();
                    countyVal = district.getName();
                    tvArea.setText(province.getName() + city.getName() + district.getName());
                }
            }

            @Override
            public void onCancel() {
                // 取消选择
            }
        });
    }

}
