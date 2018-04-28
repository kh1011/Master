package com.yxclient.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.dou361.dialogui.DialogUIUtils;
import com.yxclient.app.R;
import com.yxclient.app.adapter.QueryGridAdapter;
import com.yxclient.app.utils.DateTimePickOrderUtils;
import com.yxclient.app.utils.StringUtil;
import com.yxclient.app.view.MyGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zuo.biao.library.base.BaseActivity;

/**
 * 出行类综合查询
 */
public class YXQueryIndexActivity extends BaseActivity {
    @BindView(R.id.gridview)
    MyGridView gridView;
    @BindView(R.id.query_index_orign)
    EditText ed_orign;
    @BindView(R.id.query_index_site)
    EditText ed_site;
    @BindView(R.id.query_index)
    TextView tv_Time;

    private List<String> list;
    private String infoType;
    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_query_index);
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
        return new Intent(context, YXQueryIndexActivity.class).
                putExtra(RESULT_DATA, data);
    }

    @Override
    public void initView() {
        //
    }

    @Override
    public void initData() {
        list = new ArrayList<>();
        list.add("顺风车");
        list.add("专车");
        list.add("快递小件");
        list.add("大型货运");
        list.add("小型货运");
        infoType=list.get(0);
        int[] img={R.drawable.index_car1,R.drawable.index_car2,R.drawable.trans_icon3,R.drawable.trans_icon2,R.drawable.trans_icon1};
        final QueryGridAdapter adapter=new QueryGridAdapter(context, list,img);
        gridView.setAdapter(adapter);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.changeState(position);
                infoType=list.get(position);
            }
        });
    }

    @Override
    public void initEvent() {
    }

    @OnClick({R.id.query_btn,R.id.query_index})
    void myClick(View view) {
        switch (view.getId()) {
            case R.id.query_btn:
                String orign = ed_orign.getText().toString();
                String site = ed_site.getText().toString();
                if (StringUtil.isNullOrEmpty(orign)) {
                    DialogUIUtils.showToast("请输入出发地");
                }else if (StringUtil.isNullOrEmpty(site)){
                    DialogUIUtils.showToast("请输入");
                }else {
                    DialogUIUtils.showToast("搜索成功");
                    //toActivity(YXTripQueryListActivity.createIntent(context, val));
                    toActivity(YXTripQueryListActivity.createIntent(context,orign,site,infoType,tv_Time.getText().toString()));

                }
                break;
            case R.id.query_index:
                // 弹出日期选择
                DateTimePickOrderUtils dateTimePicKDialog = new DateTimePickOrderUtils(
                        YXQueryIndexActivity.this, null, tv_Time,4);
                dateTimePicKDialog.dateTimePicKDialog();
                break;
            default:
                break;
        }
    }
}