package com.yxdriver.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.OptionsPickerView;
import com.dou361.dialogui.DialogUIUtils;
import com.google.gson.Gson;
import com.yxdriver.app.R;
import com.yxdriver.app.app.DemoApplication;
import com.yxdriver.app.http.RetrofitHttp;
import com.yxdriver.app.model.bean.JsonBean;
import com.yxdriver.app.utils.IDCardUtil;
import com.yxdriver.app.utils.JsonFileReader;
import com.yxdriver.app.utils.StringUtil;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
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
import zuo.biao.library.util.Log;

/**
 * Created by mac on 2017/9/11.
 * 司机上传个人信息
 */

public class YXSetPersonalActivity extends BaseActivity {
    public static final String EXTRAL_KEY1 = "fromWhere";

    private static final String TAG = "YXSetPersonalActivity";
    private static final String PARAMS_KEY_PROVINCE = "province";
    private static final String PARAMS_KEY_CITY = "city";
    private static final String PARAMS_KEY_AREA = "area";
    @BindView(R.id.tv_input_error)
    TextView tvInputError;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.et_real_name)
    EditText etRealName;
    @BindView(R.id.et_id_card)
    EditText etIdCard;
    @BindView(R.id.et_contact_urgent_name)
    EditText etContactUrgentName;
    @BindView(R.id.et_contact_urgent_phone)
    EditText etContactUrgentPhone;
    @BindView(R.id.personal_jump)
    Button btnPersonalJump;

    private String token;
    private boolean change = false;
    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private Map<String, String> params = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 判断如果登录过，则进入主页
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_activity_setpersonal);
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
//        return new Intent(context, YXSetPersonalActivity.class);
        return createIntent(context, null);
    }


    /**
     * 带参数启动这个Activity的Intent
     *
     * @param context
     * @return
     */
    public static Intent createIntent(Context context, Map<String, String> extras) {
        Intent intent = new Intent(context, YXSetPersonalActivity.class);
        if (extras != null) {
            for (String key : extras.keySet()) {
                intent.putExtra(key, extras.get(key));
            }
        }
        return intent;
    }


    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void initView() {
        String s = getIntent().getStringExtra(EXTRAL_KEY1);
        //不显示  跳过  的按钮
        if (!StringUtil.isNullOrEmpty(s)) {
            btnPersonalJump.setVisibility(View.GONE);
        }
        resetView();
        initJsonData();
    }

    /**
     * 重置view
     */
    private void resetView() {
        tvInputError.setText("");
    }

    @Override
    public void initData() {
        token = DemoApplication.getInstance().getMyToken();
        getUserInfo();
    }

    /**
     * 获取用户信息
     */
    private void getUserInfo() {
        if (StringUtil.isNullOrEmpty(token)) {
            showError("用户未登录");
            finish();
        }
        final Dialog dialog = DialogUIUtils.showMdLoading(context, "用户信息拉取中……", false, true, true, true).show();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).getUserInfo(token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                try {
                    DialogUIUtils.dismiss(dialog);
                    if (response.body() == null) {
                        showError("用户信息拉取失败");
                    } else {
                        String data = response.body().string();
                        Log.i(TAG, "拉取用户信息成功 --> " + data);
                        JSONObject jsonObject = JSON.parseObject(data);
                        if ("success".equals(jsonObject.getString("code"))) {
                            showOldDatas(jsonObject.getJSONObject("data"));
                        } else {
                            showError("数据解析失败");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                DialogUIUtils.dismiss(dialog);
                showError("用户信息拉取失败");
            }
        });

    }

    /**
     * 展示之前的旧数据
     *
     * @param data
     */
    private void showOldDatas(JSONObject data) {
        String name = data.getString("name");
        String cardNo = data.getString("cardNo");
        String province = data.getString("province");
        String city = data.getString("city");
        String area = data.getString("area");
        String emergencyName = data.getString("emergencyName");
        String emergencyMobile = data.getString("emergencyMobile");
        if (province==null||city==null||area==null){
            tvCity.setText("");
        }else {
            tvCity.setText(province + "-" + city + "-" + area);
        }

        etRealName.setText(name);
        etIdCard.setText(cardNo);
        etContactUrgentName.setText(emergencyName);
        etContactUrgentPhone.setText(emergencyMobile);
    }

    @Override
    public void initEvent() {

    }

    @OnClick({R.id.personal_re_city, R.id.personal_btn, R.id.personal_jump})
    void myClick(View view) {
        switch (view.getId()) {
            case R.id.personal_re_city:
                showPickerView();
                break;
            case R.id.personal_btn:
//                toActivity(YXSetCarInfoActivity.createIntent(context));
                submitToService();
                break;
            case R.id.personal_jump:
                onBackPressed();
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 提交本页的数据到服务器中
     */
    private void submitToService() {
        //重置错误信息
        resetView();
        String address = tvCity.getText().toString();
        String cname = etRealName.getText().toString();
        String idCard = etIdCard.getText().toString();
        String contactName = etContactUrgentName.getText().toString();
        String contactPhone = etContactUrgentPhone.getText().toString();
        if (StringUtil.isNullOrEmpty(address)) {
            //showError("请选择地区");
            DialogUIUtils.showToast("请选择地区");
            return;
        }
        if (StringUtil.isNullOrEmpty(cname)) {
            //showError("请输入名字");
            DialogUIUtils.showToast("请输入真实姓名");
            return;
        }
        if (StringUtil.isNullOrEmpty(idCard)) {
            //showError("请输入身份证号");
            DialogUIUtils.showToast("请输入身份证号码");
            return;
        }
        if (!IDCardUtil.validateIDCard(idCard)) {
            //showError("请输入正确的身份证号码");
            DialogUIUtils.showToast("请输入正确的身份证号码");
            return;
        }
        if (StringUtil.isNullOrEmpty(contactName)) {
            //showError("请输入紧急联系人名字");
            DialogUIUtils.showToast("请输入紧急联系人名字");
            return;
        }
        if (StringUtil.isNullOrEmpty(contactPhone)) {
            //showError("请输入紧急联系人号码");
            DialogUIUtils.showToast("请输入紧急联系人号码");
            return;
        }
        if (StringUtil.isNullOrEmpty(token)) {
            //showError("用户未登录");
            DialogUIUtils.showToast("用户未登录");
            return;
        }
        //检查完毕，开始上传信息
        final Dialog dialog = DialogUIUtils.showMdLoading(context, "数据加载中", false, true, true, true).show();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).baseInfo(
                token, cname, params.get(PARAMS_KEY_PROVINCE),
                params.get(PARAMS_KEY_CITY), params.get(PARAMS_KEY_AREA),
                idCard, contactName, contactPhone).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                DialogUIUtils.dismiss(dialog);
                try {
                    if (response.body() == null) {
                        showError("接口返回数据错误");
                    } else {
                        String result = null;
                        result = response.body().string();
                        Log.e(TAG, "注册返回结果：" + result);
                        JSONObject jsonObject = JSON.parseObject(result);
                        if (jsonObject.getString("code").equals("success")) {
                            //提交成功  跳转到下一页
                            DialogUIUtils.showToast(jsonObject.getString("message"));
                            toActivity(YXSetCarInfoActivity.createIntent(context));
                            finish();
                        } else {
                            DialogUIUtils.showToast(jsonObject.getString("message"));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                DialogUIUtils.dismiss(dialog);
                Log.e(TAG, t.getMessage());
            }
        });
    }

    /**
     * 显示错误信息
     *
     * @param msg
     */
    private void showError(String msg) {
//        DialogUIUtils.showToast(msg);
        Log.d(TAG, msg);
        tvInputError.setText(msg);
    }

    private void showPickerView() {
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //省份
                String province = options1Items.get(options1).getPickerViewText();
                //市
                String city = options2Items.get(options1).get(options2);
                //区县
                String area = options3Items.get(options1).get(options2).get(options3);
                String address = province + "-" + city + "-" + area;
                //返回的分别是三个级别的选中位置
//                String city = options1Items.get(options1).getPickerViewText() +
//                        options2Items.get(options1).get(options2) +
//                        options3Items.get(options1).get(options2).get(options3);
                params.put(PARAMS_KEY_PROVINCE, province);
                params.put(PARAMS_KEY_CITY, city);
                params.put(PARAMS_KEY_AREA, area);
                tvCity.setText(address);
            }
        }).setTitleText("")
                .setDividerColor(Color.GRAY)
                .setTextColorCenter(Color.GRAY)
                .setContentTextSize(15)
                .setOutSideCancelable(false)
                .build();
          /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    /**
     * 获取国家地区的json数据
     */
    private void initJsonData() {   //解析数据
        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        //  获取json数据
        String JsonData = JsonFileReader.getJson(this, "province_data.json");
        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体
        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;
        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {

                    for (int d = 0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);

                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }
    }

    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }
}
