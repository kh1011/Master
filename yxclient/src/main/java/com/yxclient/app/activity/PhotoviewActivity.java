package com.yxclient.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.yxclient.app.R;
import com.yxclient.app.adapter.PhotoViewPager;
import com.yxclient.app.model.bean.OldCarModel;

import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher.OnViewTapListener;
import zuo.biao.library.base.BaseActivity;

/**
 * @author Cloudsoar(wangyb)
 * @datetime 2015-11-19 20:53 GMT+8
 * @email 395044952@qq.com
 */
public class PhotoviewActivity extends BaseActivity implements OnViewTapListener {
    private PhotoViewPager mViewPager;
    private PhotoView mPhotoView;
    private List<String> mImgUrls;
    private PhotoViewAdapter mAdapter;
    private PhotoViewAttacher mAttacher;
    private OldCarModel oldCarModel;


    @Override
    public void onViewTap(View view, float x, float y) {
        finish();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_viewpager);
        ButterKnife.bind(this);
        initView();
        initData();
        initEvent();
    }


    public static Intent createIntent(Context context, OldCarModel data) {
        return new Intent(context, PhotoviewActivity.class).
                putExtra(RESULT_DATA, data);
    }

    @Override
    public void initView() {
        mViewPager = (PhotoViewPager) findViewById(R.id.view_pager);
        mPhotoView = (PhotoView) findViewById(R.id.photo);
        oldCarModel = (OldCarModel) getIntent().getSerializableExtra(RESULT_DATA);
        mImgUrls = Arrays.asList(oldCarModel.getImages());
        mAdapter = new PhotoViewAdapter();
        mViewPager.setAdapter(mAdapter);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }

    class PhotoViewAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = container.inflate(PhotoviewActivity.this,
                    R.layout.item_photo_view, null);
            mPhotoView = (PhotoView) view.findViewById(R.id.photo);
            //使用ImageLoader加载图片
            Glide.with(PhotoviewActivity.this).load(mImgUrls.get(position)).into(mPhotoView);
            //给图片增加点击事件
            mAttacher = new PhotoViewAttacher(mPhotoView);
            mAttacher.setOnViewTapListener(PhotoviewActivity.this);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            mAttacher = null;
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return mImgUrls.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
