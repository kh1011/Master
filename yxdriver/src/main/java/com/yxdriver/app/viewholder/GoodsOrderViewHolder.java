package com.yxdriver.app.viewholder;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.yxdriver.app.R;
import com.yxdriver.app.fragment.IndexDoOrderFragment;
import com.yxdriver.app.model.bean.GoodsOrderModel;
import com.yxdriver.app.model.bean.OrderModel;
import com.yxdriver.app.model.bean.PointModel;
import com.yxdriver.app.model.bean.UserModel;
import com.yxdriver.app.utils.ImageViewUtil;
import com.yxdriver.app.utils.OrderUtils;
import com.yxdriver.app.utils.StringUtil;

/**
 * Created by mac on 2017/4/17.
 * 我的购物订单
 */

public class GoodsOrderViewHolder extends BaseViewHolder<GoodsOrderModel> {
    private GoodsOrderModel data;
    private TextView status;
    private TextView no;
    private ImageView imageView;
    private TextView goodsName;
    private TextView goodsDesc;
    private TextView price;
    private TextView count;
    private TextView count2;
    private TextView totlePrice;
    private ImageView delete;       // 删除订单
    private Button btn_Content;     // 评价
    private Button btn_Pay;         // 再次购买


    public GoodsOrderViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_shoping_order);
        status = $(R.id.item_goods_order_status);
        no = $(R.id.item_goods_order_no);
        imageView = $(R.id.item_shop_order_img);
        goodsName = $(R.id.item_goods_order_name);
        goodsDesc = $(R.id.item_goods_order_desc);
        price = $(R.id.item_goods_order_price);
        count = $(R.id.item_goods_order_num);
        count2 = $(R.id.item_goods_order_count);
        totlePrice = $(R.id.item_goods_order_total);
        delete = $(R.id.item_goods_order_delete);
        btn_Content = $(R.id.item_goods_order_btn_content);
        btn_Pay = $(R.id.item_goods_order_pay);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void setData(GoodsOrderModel model) {
        super.setData(model);
        this.data = model;
        status.setText(OrderUtils.getGoodsStatusStr(data.getStatus()));
        no.setText("订单编号：" + data.getNo());
        Glide.with(getContext()).load(data.getProduct().getTitleImage()).into(imageView);
        goodsName.setText(data.getProduct().getTitle());
        goodsDesc.setText(data.getProduct().getDesc());
        price.setText("¥ " + data.getProduct().getPrice());
        count.setText("" + data.getNumber());
        count2.setText("共" + data.getNumber() + "件商品");
        totlePrice.setText("¥ " + data.getAmount());
        // 按钮点击事件
        btn_Pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 再次购买
            }
        });
        btn_Content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  晒单评论
            }
        });
    }
}
