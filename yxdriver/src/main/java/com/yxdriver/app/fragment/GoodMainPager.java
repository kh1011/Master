package com.yxdriver.app.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.yxdriver.app.R;
import com.yxdriver.app.activity.ImageBrowseActivity;
import com.yxdriver.app.model.bean.GoodPruductModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zuo.biao.library.base.BaseFragment;

/**
 * 功能：
 * Created by yun.zhang on 2017/9/24 0024.
 * email:zy19930321@163.com
 */
public class GoodMainPager extends BaseFragment {

    GoodPruductModel model;

    @BindView(R.id.iv_image)
    ImageView ivImage;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_good_price)
    TextView tvGoodPrice;
    @BindView(R.id.tv_good_base_price)
    TextView tvGoodBasePrice;
    @BindView(R.id.tv_good_month_sales)
    TextView tvGoodMonthSales;
    // 商家联系电话
    @BindView(R.id.goods_info_mobile)
    TextView tv_Mobile;
    // 倒计时
    @BindView(R.id.tvtime1)
    TextView tvtime1;
    @BindView(R.id.tvtime2)
    TextView tvtime2;
    @BindView(R.id.tvtime3)
    TextView tvtime3;
    private long time = 400;
    @BindView(R.id.promotion)
    LinearLayout li_promotion;
    @BindView(R.id.promotion_content)
    LinearLayout li_promotionContent;
    @BindView(R.id.goods_info_remark)
    TextView tv_remark;
    @BindView(R.id.item_promotion_content)
    TextView tv_promotionContent;
    String titleModel = "【goodProductTag】  goodProductTitle";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //TODO demo_fragment改为你所需要的layout文件
        setContentView(R.layout.yx_fragment_shop_main);
        ButterKnife.bind(this, view);
        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        return view;//返回值必须为view
    }

    @Override
    public void initView() {
        //handler.postDelayed(runnable, 2000);
        Bundle bundle = getArguments();
        if (bundle != null) {
            model = (GoodPruductModel) bundle.getSerializable("model");
            if (model.getPromotion()!=null){
                handler.postDelayed(runnable, 1000);
                Date nowDate = new Date(System.currentTimeMillis());
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                try {
                    Date endDate = format.parse(model.getPromotion().get(0).getEndTime());
                    time = (endDate.getTime() - nowDate.getTime())/1000;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                tv_promotionContent.setText(model.getPromotion().get(0).getDesc());
            }else {
                li_promotion.setVisibility(View.GONE);
                li_promotionContent.setVisibility(View.GONE);
            }
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.default_shop)
                    .error(R.drawable.default_shop)
                    .priority(Priority.HIGH)
                    .diskCacheStrategy(DiskCacheStrategy.NONE);
            Glide.with(getActivity()).load(model.getTitleImage()).apply(options).into(ivImage);
            replaceTitle(model.getTag(), model.getTitle());
            tvGoodPrice.setText(String.format("¥ %s", String.format("%.2f",model.getPrice()/100)));
            if (model.getBasePrice()==0){
                tvGoodBasePrice.setVisibility(View.INVISIBLE);
            }else {
                tvGoodBasePrice.setText(String.format("¥ %s",String.format("%.2f",model.getBasePrice()/100)));

            }
            tvGoodMonthSales.setText("月销量" + model.getMonthSales() + model.getUnit());
            //设置中划线
            tvGoodBasePrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);  // 设置中划线并加清晰
            tv_Mobile.setText(String.format("商家联系电话：%s", model.getBusinessMobile()));
            tvTitle.setText(model.getTag());
            if (!model.getNote().equals("")){
                tv_remark.setText(model.getNote());
            }else {
                tv_remark.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }

    @OnClick({R.id.iv_image})
    void click(View v){
        switch (v.getId()){
            case R.id.iv_image:
                ArrayList<String> imgs = new ArrayList<>();
                imgs.add(model.getTitleImage());
                Intent intent = new Intent(context, ImageBrowseActivity.class);
                intent.putExtra("position", 0);
                intent.putStringArrayListExtra("imgs", imgs);
                startActivity(intent);

                break;
        }
    }

    private String replaceTitle(String tag, String title) {
        String result = titleModel;
        result = result.replace("goodProductTag", tag);
        result = result.replace("goodProductTitle", title);
        return result;
    }

    ///////倒计时效果实现
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            time--;
            String formatLongToTimeStr = formatLongToTimeStr(time);
            String[] split = formatLongToTimeStr.split("：");
            for (int i = 0; i < split.length; i++) {
                if (i == 0) {
                    tvtime1.setText(String.format("%s小时", split[0]));
                }
                if (i == 1) {
                    tvtime2.setText(String.format("%s分钟", split[1]));
                }
                if (i == 2) {
                    tvtime3.setText(String.format("%s秒", split[2]));
                }
            }
            if (time > 0) {
                handler.postDelayed(this, 1000);
            }
        }
    };

    public String formatLongToTimeStr(Long l) {
        int hour = 0;
        int minute = 0;
        int second;
        second = l.intValue();
        if (second > 60) {
            minute = second / 60;         //取整
            second = second % 60;         //取余
        }

        if (minute > 60) {
            hour = minute / 60;
            minute = minute % 60;
        }
        return hour + "：" + minute + "：" + second;
    }


}
