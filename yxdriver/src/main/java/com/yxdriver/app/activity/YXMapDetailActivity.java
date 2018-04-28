package com.yxdriver.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.yxdriver.app.R;
import com.yxdriver.app.model.bean.OrderModel;

import butterknife.ButterKnife;
import zuo.biao.library.base.BaseActivity;

/**
 * Created by mac on 2017/11/12.
 * ////////////////////////////////////////////////////////////////////
 * //                            _ooOoo_                             //
 * //                           o8888888o                            //
 * //                           88" . "88                            //
 * //                           (| ^_^ |)                            //
 * //                           O\  =  /O                            //
 * //                        ____/`---'\____                         //
 * //                      .'  \\|     |//  `.                       //
 * //                     /  \\|||  :  |||//  \                      //
 * //                    /  _||||| -:- |||||-  \                     //
 * //                    |   | \\\  -  /// |   |                     //
 * //                    | \_|  ''\---/''  |   |                     //
 * //                    \  .-\__  `-`  ___/-. /                     //
 * //                  ___`. .'  /--.--\  `. . ___                   //
 * //                ."" '<  `.___\_<|>_/___.'  >'"".                //
 * //              | | :  `- \`.;`\ _ /`;.`/ - ` : | |               //
 * //              \  \ `-.   \_ __\ /__ _/   .-` /  /               //
 * //        ========`-.____`-.___\_____/___.-`____.-'========       //
 * //                             `=---='                            //
 * //        ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^      //
 * //         佛祖保佑       永无BUG        永不修改                    //
 * ////////////////////////////////////////////////////////////////////
 */

public class YXMapDetailActivity extends BaseActivity {
    @Override
    public Activity getActivity() {
        return this;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_order_detail);
        ButterKnife.bind(this);
        initView();
        initData();
        initEvent();
    }

    public static Intent createIntent(Context context, OrderModel data) {
        return new Intent(context, YXOrderDetailActivity.class).
                putExtra(RESULT_DATA, data);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }
}
