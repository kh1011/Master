package com.yxdriver.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.yxdriver.app.R;
import com.yxdriver.app.utils.CodeUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zuo.biao.library.base.BaseActivity;

/**
 * 车牌所在地点
 */
public class YXPlatePointActivity extends BaseActivity {
    @BindView(R.id.gv_1)
    GridView gv;

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_plate_point);
        ButterKnife.bind(this);
        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }
    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


    /**
     * 启动这个Activity的Intent
     *
     * @param context
     * @param data
     * @return
     */
    public static Intent createIntent(Context context, String data) {
        return new Intent(context, YXPlatePointActivity.class).
                putExtra(RESULT_DATA, data);
    }

    @Override
    public void initView() {
        Bundle bundle = getIntent().getExtras();
        final String short_name = bundle.getString("select_short_name");
        Log.d("select_short_name...", short_name);
    }

    @Override
    public void initData() {
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.csy_listitem_shortname, getDate());
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String txt = adapter.getItem(position);
                if (txt.length() > 0) {
                    // 选择之后再打开一个 显示城市的 Activity；
                    Intent intent = new Intent();
                    intent.putExtra("short_name", txt);
                    setResult(0, intent);
                    finish();
                }
            }
        });
    }

    @Override
    public void initEvent() {

    }

    private String[] getDate() {
        return new String[]{"京", "津", "沪", "川", "鄂", "甘", "赣", "桂", "贵", "黑",
                "吉", "翼", "晋", "辽", "鲁", "蒙", "闽", "宁", "青", "琼", "陕", "苏",
                "皖", "湘", "新", "渝", "豫", "粤", "云", "藏", "浙", ""};
    }
}