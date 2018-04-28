package com.yxdriver.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.dou361.dialogui.DialogUIUtils;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.yxdriver.app.R;
import com.yxdriver.app.adapter.AddrListAdapter;
import com.yxdriver.app.model.bean.CoordinateModel;
import com.yxdriver.app.model.bean.OriginAndSite;
import com.yxdriver.app.poisearch.util.CityModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zuo.biao.library.base.BaseActivity;

/**
 * Created by mac on 2017/11/11.
 * ////////////////////////////////////////////////////////////////////
 * //                            _ooOoo_                             //
 * //                           o8888888o                            //
 * //                           88" . "88                            //
 * //                           (| ^_^ |)                            //
 * //                           O\  =  /O                            //
 * //                        ____/`---'\____                         //
 * //                      .'  \\|     |//  `.                       //
 * //                     /  \\|||  :  |||//  \                      //
 * //                    /  _||||| -:- |||||-  \                     //
 * //                    |   | \\\  -  /// |   |                     //
 * //                    | \_|  ''\---/''  |   |                     //
 * //                    \  .-\__  `-`  ___/-. /                     //
 * //                  ___`. .'  /--.--\  `. . ___                   //
 * //                ."" '<  `.___\_<|>_/___.'  >'"".                //
 * //              | | :  `- \`.;`\ _ /`;.`/ - ` : | |               //
 * //              \  \ `-.   \_ __\ /__ _/   .-` /  /               //
 * //        ========`-.____`-.___\_____/___.-`____.-'========       //
 * //                             `=---='                            //
 * //        ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^      //
 * //         佛祖保佑       永无BUG        永不修改                    //
 * ////////////////////////////////////////////////////////////////////
 */
// 选择出发地点、目的地、途经地
public class YXChooseAddrActivity extends BaseActivity implements TextWatcher, Inputtips.InputtipsListener {
    // 城市
    private String city = "";
    @BindView(R.id.input_edittext)
    AutoCompleteTextView mKeywordText;
    @BindView(R.id.rv_list)
    RecyclerView recyclerView;
    @BindView(R.id.choose_addr_city)
    TextView tv_City;
    @BindView(R.id.choose_addr_city_icon)
    ImageView icon;
    // 切换城市请求码
    private static final int CHOOSE_CITY = 1000;
    // 地址列表
    private List<OriginAndSite> list = new ArrayList<>();
    private AddrListAdapter addrListAdapter;

    @Override
    public Activity getActivity() {
        return this;
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, YXChooseAddrActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_activity_choose_addr);
        ButterKnife.bind(this);
        initView();
        initData();
        initEvent();
    }

    @Override
    public void initView() {
        city = getIntent().getStringExtra("city");
        tv_City.setText(city);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(
                context, DividerItemDecoration.VERTICAL));
        addrListAdapter = new AddrListAdapter(context);
        recyclerView.setAdapter(addrListAdapter);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        mKeywordText.addTextChangedListener(this);
        addrListAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // 选中的地址
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("model", addrListAdapter.getItem(position));
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void onForwardClick(View v) {
        super.onForwardClick(v);
        // 切换城市
    }

    @OnClick({R.id.choose_addr_change_city})
    void myClick(View view) {
        switch (view.getId()) {
            case R.id.choose_addr_change_city:
                // 切换城市
                icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.up));
                Intent intent = new Intent();
                intent.setClass(context, ChooseCityActivity.class);
                intent.putExtra(ChooseCityActivity.CURR_CITY_KEY, tv_City.getText().toString());
                startActivityForResult(intent, CHOOSE_CITY);
                break;
            default:
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String newText = s.toString().trim();
        InputtipsQuery inputquery = new InputtipsQuery(newText, city);
        inputquery.setCityLimit(true);
        Inputtips inputTips = new Inputtips(context, inputquery);
        inputTips.setInputtipsListener(this);
        inputTips.requestInputtipsAsyn();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /**
     * 输入提示结果的回调
     *
     * @param tipList list
     * @param rCode   错误信息
     */
    @Override
    public void onGetInputtips(final List<Tip> tipList, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            list.clear();
            addrListAdapter.clear();
            List<OriginAndSite> mylist = new ArrayList<>();
            for (int i = 0; i < tipList.size(); i++) {
                Tip tip = tipList.get(i);
                LatLonPoint point = tip.getPoint();
                OriginAndSite model = new OriginAndSite();
                String provinc,city,county;
                String[] provinc_ = new String[0],city_;
                if (tip.getDistrict().contains("自治区")){
                    provinc_=tip.getDistrict().split("自治区");
                    provinc=provinc_[0]+"自治区";
                }else if (tip.getDistrict().contains("省")) {
                    provinc_ = tip.getDistrict().split("省");
                    provinc = provinc_[0] + "省";
                }else {
                    String str="云南省"+tip.getDistrict();
                    provinc_ = str.split("省");
                    provinc="";
                }
                if (provinc_[1].contains("地区")){
                    city_=provinc_[1].split("地区");
                    city=city_[0]+"地区";
                    if (city_.length==1){
                        county="";
                        continue;
                    }else {
                        county=city_[1];
                    }
                }else if (provinc_[1].contains("自治州")){
                    city_=provinc_[1].split("自治州");
                    city=city_[0]+"自治州";
                    if (city_.length==1){
                        county="";
                        continue;
                    }else {
                        county=city_[1];
                    }
                } else if (provinc_[1].contains("行政区")){
                    city_=provinc_[1].split("行政区");
                    city=city_[0]+"行政区";
                    if (city_.length==1){
                        county="";
                        continue;
                    }else {
                        county=city_[1];
                    }
                }else if (provinc_[1].contains("自治县")){
                    city_=provinc_[1].split("自治县");
                    city=city_[0]+"自治县";
                    if (city_.length==1){
                        county="";
                    }else {
                        county=city_[1];
                    }
                }else if (provinc_[1].contains("林区")){
                    city_=provinc_[1].split("林区");
                    city=city_[0]+"林区";
                    if (city_.length==1){
                        county="";
                        continue;
                    }else {
                        county=city_[1];
                    }
                }else if (provinc_[1].contains("阿拉善盟")){
                    city_=provinc_[1].split("阿拉善盟");
                    city=city_[0]+"阿拉善盟";
                    if (city_.length==1){
                        county="";
                        continue;
                    }else {
                        county=city_[1];
                    }
                }else if (provinc_[1].contains("锡林郭勒盟")){
                    city_=provinc_[1].split("锡林郭勒盟");
                    city=city_[0]+"锡林郭勒盟";
                    if (city_.length==1){
                        county="";
                        continue;
                    }else {
                        county=city_[1];
                    }
                }else if (provinc_[1].contains("县")){
                    city_=provinc_[1].split("县");
                    city=city_[0]+"县";
                    if (city_.length==1){
                        county="";
                    }else {
                        county=city_[1];
                    }
                }
                else {
                    city_=provinc_[1].split("市");
                    city=city_[0]+"市";
                    if (city_.length==1){
                        county="";
                        continue;
                    }else {
                        county=city_[1];
                    }
                }
                model.setName(tip.getAddress());
                model.setAddress(tip.getName());
                model.setProvince(provinc);
                model.setCity(city);
                model.setCounty(county);
                if (point != null) {
                    CoordinateModel coordinateModel = new CoordinateModel();
                    coordinateModel.setLat(point.getLatitude());
                    coordinateModel.setLng(point.getLongitude());
                    model.setCoordinate(coordinateModel);
                }else{
                    continue;
                }
                mylist.add(model);
            }
            list.addAll(mylist);
            addrListAdapter.addAll(list);
            addrListAdapter.notifyDataSetChanged();
        } else {
            DialogUIUtils.showToast(rCode);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (CHOOSE_CITY == requestCode && resultCode == RESULT_OK) {
                CityModel cityModel = data.getParcelableExtra(ChooseCityActivity.CURR_CITY_KEY);
                // 改变城市显示
                city = cityModel.getCity();
                tv_City.setText(city);
                icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.down));
            }
        }
    }
}
