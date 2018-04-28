package com.yxdriver.app.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yxdriver.app.R;

import uk.co.senab.photoview.PhotoViewAttacher;
import zuo.biao.library.view.ZoomImageView;

//ViewPager
public class BigImageViewPagerAdapter extends PagerAdapter {
    // Declare Variables
    Context context;
    LayoutInflater inflater;
    String[] images;
    PhotoViewAttacher mAttacher;

    public BigImageViewPagerAdapter(Context context, String[] images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        ImageView imageview;
        final ImageView heartAnim;
        final TextView likeCount;
        TextView whichOfThem;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.viewpager_item, container,
                false);
        imageview = (ImageView) itemView.findViewById(R.id.image);
        heartAnim = (ImageView) itemView.findViewById(R.id.heart_anim);
        likeCount = (TextView) itemView.findViewById(R.id.item_comment_like_count);
        whichOfThem = (TextView) itemView.findViewById(R.id.which);

        whichOfThem.setText((position + 1) + "/" + images.length);
        Glide.with(context).load(images[position]).into(imageview);
        mAttacher = new PhotoViewAttacher(imageview);
        mAttacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float v, float v1) {
                //
            }
        });
        final GestureDetector gd = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {

                Animation pulse_fade = AnimationUtils.loadAnimation(context, R.anim.pulse_fade_in);
                pulse_fade.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        heartAnim.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        heartAnim.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                heartAnim.startAnimation(pulse_fade);
                likeCount.setText("3 Likes");
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);

            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                return true;
            }
        });

        imageview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return gd.onTouchEvent(event);
            }
        });


        // Add viewpager_item.xml to ViewPager
        (container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((RelativeLayout) object);

    }

}
