package com.yxdriver.app.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

/**
 * Created by mac on 2017/9/19.
 * 图片加载工具类
 */

public class ImageViewUtil {
    /**
     * 显示图片
     *
     * @param context
     * @param imageView
     * @param url
     */
    public static void showImageView(Context context, ImageView imageView, String url) {
        GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader("User-Agent",
                        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_2) AppleWebKit / 537.36(KHTML, like Gecko) Chrome  47.0.2526.106 Safari / 537.36")
                .build());
        Glide.with(context).load(glideUrl).into(imageView);
    }
}
