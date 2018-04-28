package com.yxdriver.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dou361.dialogui.DialogUIUtils;
import com.yxdriver.app.R;
import com.yxdriver.app.model.bean.GoodsOrderModel;
import com.yxdriver.app.utils.StringUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zuo.biao.library.base.BaseActivity;

/**
 * Created by mac on 2017/11/23.
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

public class YXShopEvaluateActivity extends BaseActivity {
    private GoodsOrderModel model;
    // 商品图片
    @BindView(R.id.goods_img)
    ImageView imageView;
    // 商品名称
    @BindView(R.id.goods_title)
    TextView title;
    // 价格
    @BindView(R.id.goods_price)
    TextView price;
    // 评论内容
    @BindView(R.id.goods_content)
    EditText editText;


    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yx_shop_evaluate);
        ButterKnife.bind(this);
        initView();
        initData();
        initEvent();
    }

    public static Intent createIntent(Context context, GoodsOrderModel data) {
        return new Intent(context, YXShopEvaluateActivity.class).putExtra(RESULT_DATA, data);
    }

    @Override
    public void initView() {
        model = (GoodsOrderModel) getIntent().getSerializableExtra(RESULT_DATA);
        assert model != null;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }

    @OnClick({R.id.goods_btn})
    void myClick(View view) {
        switch (view.getId()) {
            case R.id.goods_btn:
                String content = editText.getText().toString();
                if (StringUtil.isNullOrEmpty(content)) {
                    DialogUIUtils.showToast("请输入评价内容");
                } else {
                    doEvaluate();
                }
                break;
            default:
                break;
        }
    }

    private void doEvaluate() {
    }
}
