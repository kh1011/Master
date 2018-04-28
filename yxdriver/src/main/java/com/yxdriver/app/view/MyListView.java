package com.yxdriver.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Administrator on 2018/1/4.
 */

public class MyListView extends ListView {
    public MyListView(Context context) {
                super(context);
            }

        public MyListView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public MyListView(Context context, AttributeSet attrs,
    int defStyle) {
            super(context, attrs, defStyle);
        }

        @Override
    /**
      * 重写该方法，达到使ListView适应ScrollView的效果
      */
             protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                 int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                      MeasureSpec.AT_MOST);
                 super.onMeasure(widthMeasureSpec, expandSpec);
             }



}

    /*public MyListView(Context paramContext) {
        super(paramContext);
    }

    public MyListView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    public MyListView(Context paramContext, AttributeSet paramAttributeSet,
                      int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }*/
