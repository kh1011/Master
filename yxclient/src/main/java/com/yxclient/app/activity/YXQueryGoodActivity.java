package com.yxclient.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.dou361.dialogui.DialogUIUtils;
import com.yxclient.app.R;
import com.yxclient.app.utils.StringUtil;
import com.yxclient.app.view.MyGridView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zuo.biao.library.base.BaseActivity;

/**
 * 商城搜索
 */
public class YXQueryGoodActivity extends BaseActivity {
    @BindView(R.id.gridview)
    MyGridView gridView;
    @BindView(R.id.query_index_ed)
    EditText editText;


    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_query_good);
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
        return new Intent(context, YXQueryGoodActivity.class).
                putExtra(RESULT_DATA, data);
    }

    @Override
    public void initView() {
        //
    }

    @Override
    public void initData() {
        /*final List<String> list = new ArrayList<>();
        list.add("顺风车");
        list.add("专车");
        list.add("快递小件");
        list.add("大型货物");
        list.add("小型货物");
        gridView.setAdapter(new QueryGridAdapter(context, list));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toActivity(YXTripQueryListActivity.createIntent(context, list.get(position)));
            }
        });*/
    }

    @Override
    public void initEvent() {
    }

    @OnClick({R.id.query_btn})
    void myClick(View view) {
        switch (view.getId()) {
            case R.id.query_btn:
                String val = editText.getText().toString();
                if (StringUtil.isNullOrEmpty(val)) {
                    DialogUIUtils.showToast("请输入关键字");
                } else {
                    toActivity(YXGoodQueryListActivity.createIntent(context, val));
                }
                break;
            default:
                break;
        }
    }
}