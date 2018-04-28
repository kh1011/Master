package com.yxdriver.app.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.yxdriver.app.R;

/**
 * 雷达效果
 */
public class Radar extends View {
    private int w, h;
    private Paint mCirclePaint;

    Bitmap mBitmap;
    private int image_half_width;
    private Paint mGradientCirclePaint;


    Handler mHandler = new Handler();
    Matrix matrix;
    private float degrees = 0;
    private long delayMillis = 20;
    private Runnable runnable = new Runnable() {

        @Override
        public void run() {

            matrix.postRotate(++degrees, w / 2, h / 2);
            Radar.this.invalidate();
            mHandler.postDelayed(runnable, delayMillis);
        }
    };

    public Radar(Context context, AttributeSet attrs) {
        super(context, attrs);
        // setBackgroundResource(resid);
        w = context.getResources().getDisplayMetrics().widthPixels;
        h = context.getResources().getDisplayMetrics().heightPixels;
        matrix = new Matrix();
        mCirclePaint = new Paint();
        mCirclePaint.setColor(Color.WHITE);
        mCirclePaint.setStrokeWidth(3);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setAntiAlias(true);

        mBitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_default_head))
                .getBitmap();
        image_half_width = ((w / 6) * 1) / 2;

        float sx = (float) 2 * image_half_width / mBitmap.getWidth();
        float sy = (float) 2 * image_half_width / mBitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.setScale(sx, sy);
        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(),
                mBitmap.getHeight(), matrix, false);
        Shader mShader = new SweepGradient(w / 2, h / 2, Color.TRANSPARENT,
                Color.parseColor("#AAAAAAAA"));
        mGradientCirclePaint = new Paint();
        mGradientCirclePaint.setColor(Color.WHITE);
//		mGradientCirclePaint.setStrokeWidth(3);
        mGradientCirclePaint.setStyle(Paint.Style.FILL);
        mGradientCirclePaint.setAntiAlias(true);
        mGradientCirclePaint.setShader(mShader);
        mHandler.post(runnable);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        canvas.drawCircle(w / 2, h / 2, w / 6, mCirclePaint);
        canvas.drawCircle(w / 2, h / 2, w / 3, mCirclePaint);
        canvas.drawCircle(w / 2, h / 2, 11 * w / 20, mCirclePaint);
        canvas.drawCircle(w / 2, h / 2, 7 * h / 16, mCirclePaint);
        canvas.drawBitmap(mBitmap, w / 2 - image_half_width, h / 2
                - image_half_width, null);

        canvas.concat(matrix);
        canvas.drawCircle(w / 2, h / 2, 7 * h / 16, mGradientCirclePaint);

        matrix.reset();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        return super.onTouchEvent(event);

    }
}
