package com.yxclient.app.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.yxclient.app.R;
import com.yxclient.app.adapter.Adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 乘车人数弹出框
 */
public class NumDialogUtils implements
        OnClickListener {
    private Context activity;
    private Dialog dialog;
    private TextView num;
    private ListView listView;
    private List<String> numArrays;

    public NumDialogUtils(Context activity,
                          TextView num) {
        this.num = num;
        this.activity = activity;
    }

    private void initListView() {
        // numArrays = new String[]{"1人", "2人", "3人", "4人"};
        numArrays = new ArrayList<>();
        numArrays.add("1人");
        numArrays.add("2人");
        numArrays.add("3人");
        numArrays.add("4人");
        listView.setAdapter(new MyAdapter(numArrays, activity));
    }

    /**
     * 弹出日期时间选择框方法
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    public Dialog dateTimePicKDialog() {
        View dateTimeLayout = View.inflate(activity, R.layout.common_num,
                null);
        dateTimeLayout.findViewById(R.id.tv_cancel).setOnClickListener(this);
        dateTimeLayout.findViewById(R.id.tv_confirm).setOnClickListener(this);
        //tv_time = (TextView) dateTimeLayout.findViewById(R.id.tv_time);
        listView = (ListView) dateTimeLayout.findViewById(R.id.num_list);
        initListView();
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dateTimeLayout, new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = ((Activity) activity).getWindowManager().getDefaultDisplay()
                .getHeight();
        wl.width = LayoutParams.MATCH_PARENT;
        wl.height = LayoutParams.WRAP_CONTENT;
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围隐藏
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                num.setText(numArrays.get(position));
                dialog.dismiss();
            }
        });
        return dialog;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                dialog.dismiss();
                break;
            default:
                break;
        }
    }

    private class MyAdapter extends BaseAdapter {
        private List<String> stuList;
        private LayoutInflater inflater;

        public MyAdapter() {
        }

        public MyAdapter(List<String> stuList, Context context) {
            this.stuList = stuList;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return stuList == null ? 0 : stuList.size();
        }

        @Override
        public Object getItem(int position) {
            return stuList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //加载布局为一个视图
            View view = inflater.inflate(R.layout.item_num, null);
            String student = (String) getItem(position);
            //在view视图中查找id为image_photo的控件
            TextView tv_name = (TextView) view.findViewById(R.id.num_item_value);
            tv_name.setText(student);
            return view;
        }
    }

}
