package com.yxclient.app.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/11/6.
 */

public class rollviewAdapter extends LoopPagerAdapter {

    List<String> imgs;
    private OnImgClickListener onImgClickListener;

    public void setOnImgClickListener(OnImgClickListener onImgClickListener){
        this.onImgClickListener = onImgClickListener;
    }

    public rollviewAdapter(RollPagerView viewPager, List<String> imgs) {
        super(viewPager);
        this.imgs = imgs;
    }

    @Override
    public View getView(ViewGroup container, final int position) {
        ImageView view = new ImageView(container.getContext());
        //view.setImageResource(imgs[position]);
        Glide.with(container.getContext()).load(imgs.get(position)).into(view);
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showToast("点击了第"+position+"个");
                onImgClickListener.handleBrowse(position);
            }
        });
        return view;

    }


    @Override
    public int getRealCount() {
        return imgs.size();
    }


    public interface OnImgClickListener{

        void handleBrowse(int position);
    }
}
