package com.yxclient.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yxclient.app.R;
import com.yxclient.app.model.bean.GoodPruductModel;
import com.yxclient.app.model.bean.SubmitOrderModel;
import com.yxclient.app.view.SubmitOrderView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 提交订单的适配器
 * <p>
 * Created by zhangyun on 2017/9/30 0030
 * EMail -> yun.zhang@chinacreator.com
 */
public class SubmitOrderAdapter extends BaseAdapter {
    public static final String KEY_MODEL = "GoodProductModel";
    public static final String KEY_BUY_ALL_NUM = "BuyAllNum";

    ViewHolder viewHolder;
    Context context;
    List<Map<String, Object>> orders;
    Map<String,SubmitOrderModel> chooseSubmitOrderMap;
    int buyAllNum;
    SubmitOrderView submitOrderView;
    int selectPosition = 0;

    public SubmitOrderAdapter(Context context, List<Map<String, Object>> orders) {
        this.context = context;
        this.orders = orders;
        chooseSubmitOrderMap = new HashMap<>();
        buyAllNum = 1;
        submitOrderView = (SubmitOrderView) context;
    }

    @Override
    public int getCount() {
        return orders == null ? 0 : orders.size();
    }

    @Override
    public Object getItem(int position) {
        return orders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_submit_order, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(R.id.tag_one,viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag(R.id.tag_one);
        }
        resetViewHolder(viewHolder);
        final GoodPruductModel model = (GoodPruductModel) orders.get(position).get(KEY_MODEL);
        buyAllNum = (int) orders.get(position).get(KEY_BUY_ALL_NUM);
        Glide.with(context).load(model.getTitleImage()).into(viewHolder.ivTitleImage);
        viewHolder.tvTitle.setText("【" + model.getTag() + "】" + model.getTitle());
        viewHolder.tvPrice.setText(String.valueOf(model.getPrice()));
        viewHolder.btnResult.setText(String.valueOf(buyAllNum));
        double itemResult = model.getPrice() * buyAllNum;
        viewHolder.tvItemPrice.setText("￥" + itemResult);
        viewHolder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPosition = position;
                doAdd(model);
                setChooseed(model,position);
            }
        });
        viewHolder.btnReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPosition = position;
                doReduce(model);
                setChooseed(model,position);
            }
        });
        setChooseed(model,position);
        return convertView;
    }

    /**
     * 设置购买的订单列表中的参数，这些参数需要传到服务器
     * @param model
     */
    private void setChooseed(GoodPruductModel model,int position) {
        //TODO 编辑单个item中的订单
        SubmitOrderModel orderModel = new SubmitOrderModel();
//        orderModel.setConsignee();//收货地址
//        orderModel.setNote();//备注
        orderModel.setNumber(Integer.valueOf(viewHolder.btnResult.getText().toString()));//购买量
        double itemResult = model.getPrice() * buyAllNum;
//        orderModel.setProduct(String.valueOf(itemResult));
        orderModel.setProduct(model.getUuid());
        orderModel.setPrice(itemResult);
        chooseSubmitOrderMap.put(String.valueOf(position),orderModel);
        //调用接口，返回到Activity，提示Activity更新界面
        submitOrderView.itemChange();
    }

    /**
     * 获取订单中需要购买的列表
     * @return
     */
    public List<SubmitOrderModel> getSelectedOrder(){
        Collection<SubmitOrderModel> valueCollection = chooseSubmitOrderMap.values();
        List<SubmitOrderModel> valueList = new ArrayList<SubmitOrderModel>(valueCollection);
        return valueList;
    }

    /**
     * 添加
     * @param model
     */
    private void doAdd(GoodPruductModel model) {
        getBuyAllNum();
        buyAllNum += 1;
        appenedBuyNum(model);
    }

    /**
     * 减法
     */
    private void doReduce(GoodPruductModel model) {
        getBuyAllNum();
        //购买量为1，
        if(buyAllNum == 1 || buyAllNum == 0 ){
            viewHolder.btnReduce.setClickable(false);
            buyAllNum = 1;
        }else{
            buyAllNum -= 1;
        }
        appenedBuyNum(model);
    }

    /**
     * 更新购买量
     */
    private void appenedBuyNum(GoodPruductModel model) {
        resetButton();
        //购买量大于库存量
        if(buyAllNum > model.getStock()){
            viewHolder.btnAdd.setClickable(false);
            return;
        }else{
            viewHolder.btnAdd.setClickable(true);
            viewHolder.btnAdd.setBackgroundResource(R.drawable.shape_buy_right_pressed);
        }
        if(buyAllNum < 1){
            viewHolder.btnReduce.setClickable(false);
            return;
        }else{
            viewHolder.btnReduce.setClickable(true);
            viewHolder.btnReduce.setBackgroundResource(R.drawable.shape_buy_left_pressed);
        }

        Map<String,Object> oldMap = orders.get(selectPosition);
        oldMap.put(KEY_BUY_ALL_NUM,buyAllNum);
        orders.remove(selectPosition);
        orders.add(selectPosition,oldMap);
        viewHolder.btnResult.setText(String.valueOf(buyAllNum));
        double itemResult = model.getPrice() * buyAllNum;
        viewHolder.tvItemPrice.setText("￥" + itemResult);
        notifyDataSetChanged();
    }


    /**
     * 获取购买数量
     */
    private void getBuyAllNum(){
        buyAllNum = Integer.valueOf(viewHolder.btnResult.getText().toString());
    }

    /**
     * 重置按钮样式
     */
    private void resetButton(){
        viewHolder.btnAdd.setBackgroundResource(R.drawable.shape_buy_right_normal);
        viewHolder.btnReduce.setBackgroundResource(R.drawable.shape_buy_left_normal);
    }

    private void resetViewHolder(ViewHolder v){
        v.tvAllBuyNum.setText("1");
        v.tvChooseNum.setText("x1");
        v.tvItemPrice.setText("");
        v.tvPrice.setText("");
        v.tvTitle.setText("");
    }

    static class ViewHolder {
        @BindView(R.id.iv_title_image)
        ImageView ivTitleImage;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.tv_choose_num)
        TextView tvChooseNum;
        @BindView(R.id.btn_reduce)
        Button btnReduce;
        @BindView(R.id.btn_result)
        Button btnResult;
        @BindView(R.id.btn_add)
        Button btnAdd;
        @BindView(R.id.tv_all_buy_num)
        TextView tvAllBuyNum;
        @BindView(R.id.tv_item_price)
        TextView tvItemPrice;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
