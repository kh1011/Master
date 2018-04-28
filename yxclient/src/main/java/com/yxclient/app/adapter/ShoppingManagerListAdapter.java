package com.yxclient.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.yxclient.app.R;
import com.yxclient.app.event.ShopingOrderItemListener;
import com.yxclient.app.model.bean.GoodsOrderModel;
import com.yxclient.app.utils.OrderUtils;
import com.yxclient.app.utils.StringUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 购物订单列表适配器
 */
public class ShoppingManagerListAdapter extends RecyclerView.Adapter<ShoppingManagerListAdapter.MyViewHolder> {

    private List<GoodsOrderModel> mDataList;
    private LayoutInflater mInflater;
    private ShopingOrderItemListener mOnItemListener;
    private Context mContext;

    public ShoppingManagerListAdapter(Context context, List<GoodsOrderModel> dataList) {
        this.mDataList = dataList;
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = mInflater.inflate(R.layout.item_shoping_order, parent, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final GoodsOrderModel data = mDataList.get(position);
        holder.status.setText(OrderUtils.getGoodsStatusStr(data.getStatus()));
        holder.no.setText(String.format("订单编号：%s", data.getNo()));
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.default_shop)
                .error(R.drawable.default_shop)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(mContext).load(data.getProduct().getTitleImage()).apply(options).into(holder.imageView);
        holder.goodsName.setText(data.getProduct().getTitle());
        holder.goodsDesc.setVisibility(View.GONE);
        holder.price.setText(String.format("¥%s", data.getProduct().getPrice() / 100.0));
        holder.count.setText(String.format("x%s", String.valueOf(data.getNumber())));
        holder.count2.setText("共" + data.getNumber() + "件商品");
        holder.totlePrice.setText(String.format("¥%s", data.getAmount() / 100.0));
        if (!data.getProduct().getWeight().equals("0.0")&&data.getProduct().getWeight()!=null&& !StringUtil.isNullOrEmpty(data.getProduct().getWeight())){
            holder.tv_weight.setVisibility(View.VISIBLE);
            holder.tv_weight.setText("商品:"+Double.valueOf( data.getProduct().getWeight())*data.getNumber()+"kg");
        }
        if (data.getProduct().getColor()!=null&&!StringUtil.isNullOrEmpty(data.getProduct().getColor())){
            holder.tv_color.setVisibility(View.VISIBLE);
            holder.tv_color.setText("所选颜色:"+data.getProduct().getColor());
        }
        //////////////////////按钮显示、隐藏规则///////////////////////
        /*
          *1、当状态为待付款（10）的时候，只显示去支付按钮和删除按钮
          *2、当状态处于已支付（12、14）、并且处于未签收状态时，显示"确认收货"按钮。
          *3、当状态处于签收、送达时（15）、显示评价按钮、再次购买按钮
          *4、当订单评论之后，变为已完成，这时候显示删除按钮和再次购买按钮

         */
        switch (data.getStatus()) {
            case 10:
                // 待支付
                holder.delete.setVisibility(View.VISIBLE);
                holder.btn_Buy.setVisibility(View.GONE);
                holder.btn_Content.setVisibility(View.GONE);
                holder.btn_Pay.setVisibility(View.VISIBLE);
                holder.btn_Yes.setVisibility(View.GONE);
                break;
            case 12:
                // 已支付、未发货
                holder.delete.setVisibility(View.GONE);
                holder.btn_Buy.setVisibility(View.VISIBLE);
                holder.btn_Content.setVisibility(View.GONE);
                holder.btn_Pay.setVisibility(View.GONE);
                holder.btn_Yes.setVisibility(View.GONE);
                break;
            case 14:
                // 已发货、未签收
                holder.delete.setVisibility(View.GONE);
                holder.btn_Buy.setVisibility(View.GONE);
                holder.btn_Content.setVisibility(View.GONE);
                holder.btn_Pay.setVisibility(View.GONE);
                holder.btn_Yes.setVisibility(View.VISIBLE);
                break;
            case 15:
                // 已签收、待评价
                holder.delete.setVisibility(View.GONE);
                holder.btn_Buy.setVisibility(View.VISIBLE);
                holder.btn_Content.setVisibility(View.VISIBLE);
                holder.btn_Pay.setVisibility(View.GONE);
                holder.btn_Yes.setVisibility(View.GONE);
                break;
            case 19:
                // 订单完成
                holder.delete.setVisibility(View.VISIBLE);
                holder.btn_Buy.setVisibility(View.VISIBLE);
                holder.btn_Content.setVisibility(View.GONE);
                holder.btn_Pay.setVisibility(View.GONE);
                holder.btn_Yes.setVisibility(View.GONE);
                break;
            default:
                break;
        }
        // 按钮点击事件
        holder.btn_Pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 再次购买
                mOnItemListener.pay(position);
            }
        });
        holder.btn_Content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  晒单评论
                mOnItemListener.evaluate(position);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemListener.delete(position);
            }
        });
        holder.btn_Buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemListener.buy(position);
            }
        });
        holder.btn_Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemListener.confirm(position);
            }
        });
        holder.re_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemListener.goMain(position);
            }
        });
    }

    public void setOnItemListener(ShopingOrderItemListener onItemListener) {
        this.mOnItemListener = onItemListener;
    }

    /**
     * ViewHolder
     */
    class MyViewHolder extends RecyclerView.ViewHolder {
        // 订单状态
        @BindView(R.id.item_goods_order_status)
        TextView status;
        // 订单编号
        @BindView(R.id.item_goods_order_no)
        TextView no;
        // 商品图片
        @BindView(R.id.item_shop_order_img)
        ImageView imageView;
        // 商品名称
        @BindView(R.id.item_goods_order_name)
        TextView goodsName;
        // 商品描述
        @BindView(R.id.item_goods_order_desc)
        TextView goodsDesc;
        // 商品单价
        @BindView(R.id.item_goods_order_price)
        TextView price;
        // 购买数量
        @BindView(R.id.item_goods_order_num)
        TextView count;
        // 商品数量
        @BindView(R.id.item_goods_order_count)
        TextView count2;
        // 总金额
        @BindView(R.id.item_goods_order_total)
        TextView totlePrice;
        // 删除订单
        @BindView(R.id.item_goods_order_delete)
        ImageView delete;       // 删除订单
        // 评价
        @BindView(R.id.item_goods_order_btn_content)
        Button btn_Content;     // 评价
        // 购买
        @BindView(R.id.item_goods_order_buy)
        Button btn_Buy;
        // 支付
        @BindView(R.id.item_goods_order_pay)
        Button btn_Pay;
        // 确认收货
        @BindView(R.id.item_goods_order_yes)
        Button btn_Yes;
        @BindView(R.id.item_shop_order_content)
        RelativeLayout re_content;
        //所选颜色
        @BindView(R.id.item_goods_order_color)
        TextView tv_color;
        //商品重量
        @BindView(R.id.item_goods_order_weight)
        TextView tv_weight;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void remove(GoodsOrderModel itemModel) {
        mDataList.remove(itemModel);
    }

    public GoodsOrderModel getItem(int pos) {
        return mDataList.get(pos);
    }
}
