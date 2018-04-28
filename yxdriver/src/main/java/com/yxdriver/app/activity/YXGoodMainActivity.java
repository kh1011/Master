package com.yxdriver.app.activity;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.dou361.dialogui.DialogUIUtils;
import com.jpeng.jptabbar.BadgeDismissListener;
import com.jpeng.jptabbar.OnTabSelectListener;
import com.yxdriver.app.R;
import com.yxdriver.app.adapter.GoodMainViewPagerAdapter;
import com.yxdriver.app.adapter.ItemAdapter;
import com.yxdriver.app.app.DemoApplication;
import com.yxdriver.app.fragment.GoodDetailsPager;
import com.yxdriver.app.fragment.GoodEvaluatePager;
import com.yxdriver.app.fragment.GoodMainPager;
import com.yxdriver.app.http.RetrofitHttp;
import com.yxdriver.app.model.bean.GoodPruductModel;
import com.yxdriver.app.view.MyGridView;
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
import zuo.biao.library.base.BaseActivity;


/**
 * 功能：商品详情页面
 * Created by yun.zhang on 2017/9/24 0024.
 * email:zy19930321@163.com
 */
public class YXGoodMainActivity extends BaseActivity implements BadgeDismissListener, OnTabSelectListener {
    static final String EXTRA_KEY = "goodProductModel";
    static final String EXTRA_FLOG = "flog";
    @BindView(R.id.iv_favorite)
    ImageView ivFavorite;
    @BindView(R.id.in_tab)
    LinearLayout li_tab;
    Button btnReduce;
    Button btnResult;
    Button btnAdd;
    private int flg=1;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private List<Fragment> list;
    private SlidingTabLayout tabLayout;
    GoodPruductModel model;
    PopupWindow pop;
    LinearLayout ll_popup;

    //购买量，默认为1
    int buyAllNum = 1;
    List<String> color_list=new ArrayList<>();
    //选择颜色
    String goodcolor;
    //重量
    String goodweight;

    int flog=1;
    @Override
    public Activity getActivity() {
        return this;
    }

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**
     * 启动这个Activity的Intent
     *
     * @param context
     * @return
     */
    public static Intent createIntent(Context context, GoodPruductModel model,int flog) {
        return new Intent(context, YXGoodMainActivity.class)
                .putExtra(EXTRA_KEY, model)
                .putExtra(EXTRA_FLOG,flog);
    }
    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_shop_details);
        ButterKnife.bind(this);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (SlidingTabLayout) findViewById(R.id.tabLayout);
        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        initViewDate();
    }

    @Override
    public void initView() {
        model = (GoodPruductModel) getIntent().getSerializableExtra(EXTRA_KEY);
        flog  = getIntent().getIntExtra(EXTRA_FLOG,1);
    }

    @Override
    public void initData() {
        // i will give you some color see see
    }

    @Override
    public void initEvent() {

    }

    private void initViewDate() {
        list = new ArrayList<>();
        // 不能通过构造函数的方式来传递参数
        Bundle bundle = new Bundle();
        bundle.putSerializable("model", model);

        GoodMainPager manPage = new GoodMainPager();
        manPage.setArguments(bundle);

        GoodDetailsPager detailsPager = new GoodDetailsPager();
        detailsPager.setArguments(bundle);

        GoodEvaluatePager evaluatePager = new GoodEvaluatePager();
        evaluatePager.setArguments(bundle);
        list.add(manPage);
        list.add(detailsPager);
        list.add(evaluatePager);
//        IndexViewPagerAdapter adapter = new IndexViewPagerAdapter(getChildFragmentManager(), list, view.getContext());
        GoodMainViewPagerAdapter adapter = new GoodMainViewPagerAdapter(fragmentManager, list, this);
        viewPager.setAdapter(adapter);
        tabLayout.setCustomTabView(R.layout.custom_tab, 0);
        tabLayout.setDistributeEvenly(true);
        tabLayout.setViewPager(viewPager);
        tabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                //return Color.WHITE;
                return ContextCompat.getColor(context, R.color.main_blue);
            }
        });
    }

    @Override
    public void onDismiss(int position) {
        if (position == 0) {
            //mTab1.clearCount();
        }
    }

    @Override
    public void onTabSelect(int index) {

    }

    @OnClick({R.id.ll_favorite, R.id.ll_buy})
    void viewOnclick(View view) {
        switch (view.getId()) {
            case R.id.ll_favorite:
                //TODO 收藏
                saveFavorite(DemoApplication.getInstance().getMyToken(), model.getUuid());
                break;
            case R.id.ll_buy:
                //TODO 立即购买
                showPopupWindow();
                ll_popup.startAnimation(AnimationUtils.loadAnimation(
                        context, R.anim.activity_translate_in));
                pop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                break;
        }
    }

    /**
     * 收藏商品
     *
     * @param token
     * @param id
     */
    private void saveFavorite(String token, String id) {
        final Dialog dialog = DialogUIUtils.showMdLoading(context, "数据加载中...", true, true, true, true).show();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).saveFavorite(token, id).enqueue(new Callback<ResponseBody>() {
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
                                    saveFavoriteSuccess();
                                } else {
                                    DialogUIUtils.showToast(jsonObject.getString("message"));
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    default:
                        try {
                            String err=response.errorBody().string();
                            DialogUIUtils.showToast(JSON.parseObject(err).getString("message"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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
     * 收藏成功
     */
    private void saveFavoriteSuccess() {
        ivFavorite.setImageResource(R.mipmap.ic_favorite_pressed);
    }

    /****
     * 购买提示框
     */
    TextView goodPrice;
    TextView weight;
    LinearLayout weight_;
    public void showPopupWindow() {
        pop = new PopupWindow(context);
        View view = getLayoutInflater().inflate(R.layout.item_pop_buy,
                null);
        ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);
        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
        ImageView titleImage = (ImageView) view.findViewById(R.id.iv_title_image);
        goodPrice = (TextView) view.findViewById(R.id.tv_good_price);
        TextView tvMonthSales = (TextView) view.findViewById(R.id.tv_month_sales);
        ImageView ivClose = (ImageView) view.findViewById(R.id.iv_close);
        btnReduce = (Button) view.findViewById(R.id.btn_reduce);
        btnResult = (Button) view.findViewById(R.id.btn_result);
        btnAdd = (Button) view.findViewById(R.id.btn_add);
        Button btnOk = (Button) view.findViewById(R.id.btn_ok);
        weight= (TextView) view.findViewById(R.id.weight);
        weight_= (LinearLayout) view.findViewById(R.id.weight_);
        MyGridView gr_color=(MyGridView) view.findViewById(R.id.grid_color);
        Glide.with(context).load(model.getTitleImage()).into(titleImage);
        goodPrice.setText(String.format("¥ %s", String.format("%.2f",model.getPrice() / 100.0)));
        if (model.getWeight()==null){
            weight_.setVisibility(View.GONE);
        }else {
            weight.setText(model.getWeight()+"kg");
        }
        tvMonthSales.setText("月销量" + model.getMonthSales() + model.getUnit());
        appenedBuyNum();
        btnReduce.setBackgroundResource(R.drawable.shape_buy_left_normal);
        //默认不能做减法
        btnReduce.setClickable(false);
        if (Integer.parseInt(btnResult.getText().toString())>=model.getStock()){
            btnAdd.setClickable(false);
        }
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pop.dismiss();
                //ll_popup.clearAnimation();
            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        btnReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //减法
                doReduce();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //做加法
                if (flg==1){
                    doAdd();
                }
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //购买商品
                doBuy();
            }
        });
        String[] color_=model.getColor().split("色");
        color_list.clear();
        for (int i=0;i<color_.length;i++){
            color_list.add(color_[i]+"色");
        }
        if (color_.equals("")||color_==null||color_[0].equals("")){
            gr_color.setVisibility(View.GONE);
            goodcolor="";
        }else {
            goodcolor=color_list.get(0);
            final ItemAdapter gridadapter = new ItemAdapter(context,color_list);
            gr_color.setAdapter(gridadapter);
            gr_color.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    gridadapter.changeState(position);
                    goodcolor=color_list.get(position);
                }
            });
        }

    }

    /**
     * 购买量增加
     */
    private void doAdd() {
        getBuyAllNum();
        buyAllNum += 1;
        appenedBuyNum();
    }

    /**
     * 更新购买量
     */
    private void appenedBuyNum() {
        resetButton();
        //购买量大于库存量
        if (buyAllNum >= model.getStock()) {
            btnAdd.setClickable(false);
            flg=0;
        } else {
            flg=1;
            btnAdd.setClickable(true);
            btnAdd.setBackgroundResource(R.drawable.shape_buy_right_pressed);
        }
        if (buyAllNum <= 1) {
            btnReduce.setClickable(false);
        } else {
            btnReduce.setClickable(true);
            btnReduce.setBackgroundResource(R.drawable.shape_buy_left_pressed);
        }
        btnResult.setText(String.valueOf(buyAllNum));
        goodPrice.setText(String.format("¥ %s", String.format("%.2f",model.getPrice() * buyAllNum / 100.0)));
        if (model.getWeight()==null||model.getWeight().equals("0.0")){
            weight_.setVisibility(View.GONE);
            goodweight="";
        }else {
            weight.setText(String.valueOf(Double.valueOf(model.getWeight())*buyAllNum)+"kg");
            goodweight=String.valueOf(Double.valueOf(model.getWeight())*buyAllNum);
        }
    }

    /**
     * 减法
     */
    private void doReduce() {
        getBuyAllNum();
        //购买量为1，
        if (buyAllNum == 1 || buyAllNum == 0) {
            btnReduce.setClickable(false);
            buyAllNum = 1;
        } else {
            buyAllNum -= 1;
        }
        appenedBuyNum();
    }

    /**
     * 获取购买数量
     */
    private void getBuyAllNum() {
        buyAllNum = Integer.valueOf(btnResult.getText().toString());
    }

    /**
     * 重置按钮样式
     */
    private void resetButton() {
        btnAdd.setBackgroundResource(R.drawable.shape_buy_right_normal);
        btnReduce.setBackgroundResource(R.drawable.shape_buy_left_normal);
    }

    /**
     * 点击购买
     */
    private void doBuy() {
        pop.dismiss();
        ll_popup.clearAnimation();
        Intent intent = new Intent(context, YXSubmitOrderActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("goodcolor", goodcolor);
        bundle.putString("goodweight", goodweight);
        bundle.putInt("number", buyAllNum);
        bundle.putSerializable("model", model);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
