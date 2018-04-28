 package com.yxdriver.app.poisearch.searchmodule;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

 /**
  * Created by liangchao_suxun on 2017/4/26.
  */

 public class PoiListView extends ListView {
     public PoiListView(Context context) {
         super(context);
         init();
     }

     public PoiListView(Context context, AttributeSet attrs) {
         super(context, attrs);
         init();
     }

     public PoiListView(Context context, AttributeSet attrs, int defStyleAttr) {
         super(context, attrs, defStyleAttr);
         init();
     }

     private void init(){
         setDividerHeight(0);
     }
 }
