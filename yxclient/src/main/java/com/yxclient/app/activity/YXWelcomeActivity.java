package com.yxclient.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.view.SimpleDraweeView;
import com.yxclient.app.R;
import com.yxclient.app.adapter.ViewPagerAdapter;
import com.yxclient.app.config.YXConfig;
import com.yxclient.app.http.RetrofitHttp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * author：Anumbrella
 * Date：16/5/23 下午6:03
 */
public class YXWelcomeActivity extends AppCompatActivity implements View.OnClickListener {


    @BindView(R.id.goComing)
    TextView goComing;

   /* @BindView(R.id.go)
    TextView goBtn;*/

    @BindView(R.id.goLayout)
    RelativeLayout goLayout;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private ViewPagerAdapter adapter;

    private ArrayList<SimpleDraweeView> views = new ArrayList<SimpleDraweeView>();

    private int length;


    /**
     * 底部指示器(小圆点)的图片
     */
    private ImageView[] points;

    /**
     * 当前选中的指针
     */
    private int currentIndex;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        initData(list);
        //getData();

    }

    private int page = 1;
    private List<JSONObject> list = new ArrayList<>();

    private void getData() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).getImages()
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Response<ResponseBody> response) {
                        try {
                            if (response.body() == null) {
                                Toast.makeText(YXWelcomeActivity.this, "接口请求错误!", Toast.LENGTH_SHORT).show();
                            } else {
                                String result = response.body().string();
                                JSONObject jsonObject = JSON.parseObject(result);
                                if (jsonObject.getString("code").equals("success")) {
                                    JSONArray list_temp = JSON.parseArray(jsonObject.getString("data"));
                                    if (page == 1) {
                                        list.clear();
                                        for (int i = 0; i < list_temp.size(); i++) {
                                            JSONObject json = list_temp
                                                    .getJSONObject(i);
                                            list.add(json);
                                        }
                                    } else {
                                        // 分页
                                        for (int i = 0; i < list_temp.size(); i++) {
                                            JSONObject json = list_temp
                                                    .getJSONObject(i);
                                            list.add(json);
                                            page++;
                                        }

                                    }
                                    // 绘制UI
                                    initData(list);
                                } else {
                                    Toast.makeText(YXWelcomeActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(YXWelcomeActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initData(List<JSONObject> jsonObjectList) {
        adapter = new ViewPagerAdapter(views);
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        /*if (!JUtils.isNetWorkAvilable()) {
            for (int i = 0; i < YXConfig.pics.length; i++) {
                length = YXConfig.pics.length;
                SimpleDraweeView iv = new SimpleDraweeView(this);
                iv.setLayoutParams(mParams);
                //防止图片不能填满屏幕
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                //加载图片资源
                iv.setImageResource(YXConfig.pics[i]);
                views.add(iv);
            }
        } else {
            for (int i = 0; i < jsonObjectList.size(); i++) {
                length = jsonObjectList.size();
                SimpleDraweeView iv = new SimpleDraweeView(this);
                iv.setLayoutParams(mParams);
                //防止图片不能填满屏幕
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                //加载图片资源
                Glide.with(this).load(jsonObjectList.get(i).getString("image")).into(iv);
                //iv.setImageURI(Uri.parse(YXConfig.pricUrls[i]));
                views.add(iv);
            }
        }*/
        for (int i = 0; i < YXConfig.pics.length; i++) {
            length = YXConfig.pics.length;
            SimpleDraweeView iv = new SimpleDraweeView(this);
            iv.setLayoutParams(mParams);
            //防止图片不能填满屏幕
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            //加载图片资源
            iv.setImageResource(YXConfig.pics[i]);
            views.add(iv);
        }
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new OnPageChangeListener());
        initPoint();
    }

    /**
     * 初始化小圆点
     */
    private void initPoint() {
        //设定一个布局的参数
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.point_layout);
        //生成指示器个数
        points = new ImageView[length];

        for (int i = 0; i < length; i++) {
            points[i] = (ImageView) linearLayout.getChildAt(i);
            // 默认都设为灰色
            points[i].setEnabled(true);
            // 给每个小点设置监听
            points[i].setOnClickListener(this);
            // 设置位置tag，方便取出与当前位置对应
            points[i].setTag(i);
        }
        //设置当前默认的位置
        currentIndex = 0;
        //设置为白色即选中的状态
        points[currentIndex].setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        setCurrentView(position);
        setCurrentDot(position);
    }

    private final class OnPageChangeListener implements ViewPager.OnPageChangeListener {


        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            setCurrentDot(position);
            if (position == length - 1) {
                goLayout.setVisibility(View.VISIBLE);
                goComing.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(YXWelcomeActivity.this, YXMainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            } else {
                goLayout.setVisibility(View.GONE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }


    /**
     * 设置当前页面视图的状态
     *
     * @param position
     */
    private void setCurrentView(int position) {
        if (position < 0 || position >= length) {
            return;
        }
        viewPager.setCurrentItem(position);
    }


    private void setCurrentDot(int position) {
        if (position < 0 || position >= length) {
            return;
        }
        points[position].setEnabled(false);
        points[currentIndex].setEnabled(true);
        currentIndex = position;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
