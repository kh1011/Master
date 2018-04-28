package com.yxclient.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.IconHintView;
import com.yxclient.app.R;
import com.yxclient.app.activity.ImageBrowseActivity;
import com.yxclient.app.adapter.GoodDetailsAdapter;
import com.yxclient.app.adapter.rollviewAdapter;
import com.yxclient.app.app.DemoApplication;
import com.yxclient.app.model.bean.GoodPruductModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import zuo.biao.library.base.BaseFragment;

/**
 * 功能：
 * Created by yun.zhang on 2017/9/24 0024.
 * email:zy19930321@163.com
 */
public class GoodDetailsPager extends BaseFragment implements rollviewAdapter.OnImgClickListener {

    @BindView(R.id.desc)
    TextView tvdesc;
    String desc;
    GoodPruductModel model;
    @BindView(R.id.roll_view_pager)
    RollPagerView mRollViewPager;
    GoodDetailsAdapter adapter;
    List<String> images;
    String token;


    /*public GoodDetailsPager(){
    }*/
    rollviewAdapter rollAdapter;
    /*public GoodDetailsPager(GoodPruductModel model) {
        this.model = model;
    }*/


    /**
     * 创建一个Fragment实例
     *
     * @return
     */
    public static IndexPager createInstance() {
        IndexPager fragment = new IndexPager();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //TODO demo_fragment改为你所需要的layout文件
        setContentView(R.layout.yx_fragment_shop_details);
        ButterKnife.bind(this, view);
        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        return view;//返回值必须为view
    }

    @Override
    public void initView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            model = (GoodPruductModel) bundle.getSerializable("model");
        }
        token = DemoApplication.getInstance().getMyToken();
        images = new ArrayList<>();
        images = model.getImages();
        desc=model.getDesc();
        tvdesc.setText(desc);

        //设置播放时间间隔
        mRollViewPager.setPlayDelay(999999999);
        //设置透明度
        mRollViewPager.setAnimationDurtion(500);
        //设置适配器
        rollAdapter=new rollviewAdapter(mRollViewPager,images);
        mRollViewPager.setAdapter(rollAdapter);

        rollAdapter.setOnImgClickListener(this);
        //设置指示器（顺序依次）
        //自定义指示器图片
        //设置圆点指示器颜色
        //设置文字指示器
        //隐藏指示器
        mRollViewPager.setHintView(new IconHintView(context, R.drawable.dot_1, R.drawable.dot_2, 24));



    }

    @Override
    public void initData() {
    }

    @Override
    public void initEvent() {

    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        //获得adapter
        GoodDetailsAdapter adapter = (GoodDetailsAdapter) listView.getAdapter();
        if (adapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(0, 0);
            //计算总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        //计算分割线高度
        params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        //给listview设置高度
        listView.setLayoutParams(params);
    }

    @Override
    public void handleBrowse(int position) {
        ArrayList<String> imgs = new ArrayList<>();

        for (int i=0;i<images.size();i++) {

            imgs.add(images.get(i));
        }

        Intent intent = new Intent(context, ImageBrowseActivity.class);
        intent.putExtra("position", position);
        intent.putStringArrayListExtra("imgs", imgs);
        startActivity(intent);

    }
}
