package com.yxclient.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.OptionsPickerView;
import com.dou361.dialogui.DialogUIUtils;
import com.google.gson.Gson;
import com.yxclient.app.R;
import com.yxclient.app.app.DemoApplication;
import com.yxclient.app.http.RetrofitHttp;
import com.yxclient.app.model.bean.JsonBean;
import com.yxclient.app.model.bean.ReceiptAddressModel;
import com.yxclient.app.utils.JsonFileReader;
import com.yxclient.app.utils.ParseUtils;
import com.yxclient.app.utils.StringUtil;

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

/**
 * 功能：
 * Created by yun.zhang on 2017/10/7 0007.
 * email:zy19930321@163.com
 */
public class YXEditAddressActivity extends BaseActivity {
    private static final String EXTRA_DEFAULT = "isDefault";
    private static final String PARAMS_KEY_PROVINCE = "province";
    private static final String PARAMS_KEY_CITY = "city";
    private static final String PARAMS_KEY_AREA = "county";

    @BindView(R.id.et_user_name)
    EditText etUserName;
    @BindView(R.id.et_user_phone)
    EditText etUserPhone;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.et_street)
    EditText etStreet;
    @BindView(R.id.et_postal_code)
    EditText etPostalCode;
    @BindView(R.id.et_address)
    EditText etAddress;

    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private Map<String, String> params = new HashMap<>();
    String token;


    ReceiptAddressModel defaultModel;
    PopupWindow pop;
    LinearLayout ll_popup;

    public static Intent createIntent(Context context, ReceiptAddressModel defaultAddress) {
        return new Intent(context, YXEditAddressActivity.class).putExtra(EXTRA_DEFAULT,defaultAddress);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = LayoutInflater.from(this).inflate(R.layout.yx_add_new_address,null);
        setContentView(R.layout.yx_add_new_address);
        ButterKnife.bind(this);
        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
    }

    @Override
    public void initView() {
        defaultModel = (ReceiptAddressModel) getIntent().getExtras().get(EXTRA_DEFAULT);
        token = DemoApplication.getInstance().getMyToken();
        initJsonData();
    }

    @Override
    public void initData() {
        etAddress.setText(defaultModel.getAddress());
        etUserName.setText(defaultModel.getName());
        etUserPhone.setText(defaultModel.getMobile());
        tvArea.setText(defaultModel.getProvince()+ defaultModel.getCity()+defaultModel.getCounty());
        etStreet.setText(defaultModel.getStreet());
        etPostalCode.setText(defaultModel.getPostcode());
    }

    @Override
    public void initEvent() {

    }

    /*@OnClick({R.id.ll_choose_address,R.id.btn_save})
    void viewOnclick(View view){
        switch (view.getId()){
            case R.id.ll_choose_address:
                //选择地区
                showPickerView();
                break;
            case R.id.btn_save:
                //保存收货地址
                saveAddress();
                break;
            case R.id.tv_delete_address:
                showPopupWindow();
                ll_popup.startAnimation(AnimationUtils.loadAnimation(
                        context, R.anim.activity_translate_in));
                pop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                break;
        }
    }*/

    /****
     * 删除提示框
     */
    public void showPopupWindow() {
        pop = new PopupWindow(context);
        View view = getLayoutInflater().inflate(R.layout.item_pop_delete_address,
                null);
        ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);
        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                doDelete();
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
    }
    private void doDelete(){
        String uuid = defaultModel.getUuid();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).deleteReceiptAddress(token,uuid).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                if(response == null || response.body() == null){
                    DialogUIUtils.showToast("解析数据失败");
                    return;
                }

                try {
                    String result = response.body().string();
                    JSONObject jsonObject = JSON.parseObject(result);
                    if("success".equals(jsonObject.getString("code"))){
                        deleteSuccess();
                    }else{
                        DialogUIUtils.showToast("删除地址失败");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                DialogUIUtils.showToast("删除地址失败");
            }
        });
    }

    /**
     * 地址删除成功
     */
    private void deleteSuccess() {
        Intent intent = new Intent();
        intent.putExtra("data",defaultModel);
        intent.putExtra("type","delete");
        setResult(RESULT_OK,intent);
        finish();
    }


    /**
     * 保存收货地址
     */
    private void saveAddress() {
        String userName = etUserName.getText().toString();
        String userPhone = etUserPhone.getText().toString();
        String area = tvArea.getText().toString();
        String street = etStreet.getText().toString();
        String address = etAddress.getText().toString();
        String postalCode = etPostalCode.getText().toString();
        if(StringUtil.isNullOrEmpty(userName)){
            DialogUIUtils.showToast("请输入姓名");
            return;
        }
        if(StringUtil.isNullOrEmpty(userPhone)){
            DialogUIUtils.showToast("请输入联系方式");
            return;
        }
        if(!StringUtil.isTelPhoneNumber(userPhone)){
            DialogUIUtils.showToast("请输入正确的联系方式");
            return;
        }
        if(StringUtil.isNullOrEmpty(area)){
            DialogUIUtils.showToast("请选择地区");
            return;
        }
        if(StringUtil.isNullOrEmpty(street)){
            DialogUIUtils.showToast("请输入街道");
            return;
        }
        if(StringUtil.isNullOrEmpty(address)){
            DialogUIUtils.showToast("请输入详细收货地址");
            return;
        }
        params.put("uuid",defaultModel.getUuid());
        params.put("name",userName);
        params.put("mobile",userPhone);
        params.put("street",street);
        params.put("address",address);
        params.put("postcode",postalCode);
        params.put("isdefault",String.valueOf(defaultModel.getIsdefault()));
//        ss
//        params.put(PARAMS_KEY_PROVINCE, defaultModel.getProvince());
//        params.put(PARAMS_KEY_CITY, defaultModel.getCity());
//        params.put(PARAMS_KEY_AREA, defaultModel.getCounty());

        final Dialog dialog = DialogUIUtils.showMdLoading(context, "数据提交中", true, true, true, true).show();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).editReceiptAddress(token,params).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                DialogUIUtils.dismiss(dialog);
                if(response == null || response.body() == null){
                    DialogUIUtils.showToast("获取修改结果失败");
                    return;
                }else{
                    try {
                        String result = response.body().string();
                        JSONObject jsonObject = JSON.parseObject(result);
                        if(jsonObject != null && "success".equals(jsonObject.getString("code"))){
                            saveSuccess(jsonObject.getJSONObject("data"));
                        }else{
                            DialogUIUtils.showToast("修改地址失败");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        DialogUIUtils.showToast("解析失败");
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                DialogUIUtils.dismiss(dialog);
                DialogUIUtils.showToast("修改地址失败");
            }
        });
    }

    /**
     * 修改地址成功
     * @param data
     */
    private void saveSuccess(JSONObject data) {
        ReceiptAddressModel model = ParseUtils.parseReceiptAddressModel(data);
        Intent intent = new Intent();
        intent.putExtra("data",model);
        intent.putExtra("type","edit");
        setResult(RESULT_OK,intent);
        finish();
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
                tvArea.setText(address);
            }
        }).setTitleText("")
                .setDividerColor(Color.GRAY)
                .setTextColorCenter(Color.GRAY)
                .setContentTextSize(15)
                .setOutSideCancelable(false)
                .setSubmitText("确定")
                .setCancelText("取消")
                .build();
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

}
