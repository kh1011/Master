package com.yxdriver.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yxdriver.app.R;
import com.yxdriver.app.config.YXConfig;
import com.yxdriver.app.event.RideOrderItemClickListener;
import com.yxdriver.app.model.bean.CustomerModel;
import com.yxdriver.app.utils.OrderUtils;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Song on 2016/9/7.
 * x
 */
public class OrderCustomerAdapter extends RecyclerView.Adapter<OrderCustomerAdapter.MyViewHolder> {

    private Context mContext;
    private List<CustomerModel> mDataList;
    private LayoutInflater mInflater;
    private RideOrderItemClickListener onItemListener;
    //订单类型
    private String OrderType;
    //行程类型
    private String infoType;

    public OrderCustomerAdapter(Context context, List<CustomerModel> dataList,String OrderType,String infoType) {
        this.mContext = context;
        this.mDataList = dataList;
        this.OrderType=OrderType;
        this.infoType=infoType;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = mInflater.inflate(R.layout.item_trip_order, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(rootView);
        return myViewHolder;
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final CustomerModel customerModel = mDataList.get(position);
        switch (customerModel.getStatus()) {
            case YXConfig.T_ORDER_STATUS_CONFIRM:
               holder.xx.setVisibility(View.GONE);
               holder.xxx.setVisibility(View.GONE);
               holder.re_order.setVisibility(View.VISIBLE);
                break;
            case YXConfig.T_ORDER_STATUS_ACCEPT:
                holder.btn.setBackgroundResource(R.drawable.green_btn);
                holder.btn.setText("开始行程");
                holder.btnPay.setVisibility(View.GONE);
                holder.re_order.setVisibility(View.GONE);
                break;
            case YXConfig.T_ORDER_STATUS_GO:
                holder.btn.setBackgroundResource(R.drawable.grive_btn);
                holder.btn.setText("待支付");
                holder.btnPay.setVisibility(View.VISIBLE);
                holder.re_order.setVisibility(View.GONE);
                break;
            case YXConfig.T_ORDER_STATUS_WAIT_PAY:
                // 待支付
                holder.btn.setBackgroundResource(R.drawable.grive_btn);
                holder.btn.setText("待支付");
                holder.btnPay.setVisibility(View.GONE);
                holder.re_order.setVisibility(View.GONE);
                holder.xx.setVisibility(View.GONE);
                holder.li_mileage.setVisibility(View.VISIBLE);
                break;
            case YXConfig.T_ORDER_STATUS_INFO_SUCCESS:
                holder.btn.setBackgroundResource(R.drawable.red_btn);
                holder.btn.setText("已结束");
                holder.btnPay.setVisibility(View.GONE);
                holder.re_order.setVisibility(View.GONE);
                holder.xx.setVisibility(View.GONE);
                holder.li_mileage.setVisibility(View.VISIBLE);
                break;
            case YXConfig.T_ORDER_STATUS_SUCCESS:
                holder.btn.setBackgroundResource(R.drawable.red_btn);
                holder.btn.setText("已结束");
                holder.btnPay.setVisibility(View.GONE);
                holder.re_order.setVisibility(View.GONE);
                holder.xx.setVisibility(View.GONE);
                holder.li_mileage.setVisibility(View.VISIBLE);
                break;
            case YXConfig.T_ORDER_STATUS_CLOSE:
                holder.btn.setBackgroundResource(R.drawable.red_btn);
                holder.btn.setText("已结束");
                holder.btnPay.setVisibility(View.GONE);
                holder.re_order.setVisibility(View.GONE);
                holder.xx.setVisibility(View.GONE);
                break;
            default:
                holder.re_order.setVisibility(View.GONE);
                break;
        }
        if (mDataList.get(position).getConsignorMobile()!=null&&!OrderType.equals("express")){
            holder.li_mileage.setVisibility(View.GONE);

            holder.li_status.setVisibility(View.VISIBLE);
            holder.tv_fragile.setText(getFragile(customerModel.getFragile()));
            holder.tv_wet.setText(getWet(customerModel.getWet()));

            holder.tv_consignorName.setText("发货人："+customerModel.getName());
            holder.tv_consignorphone.setText("联系方式："+customerModel.getMobile());

            holder.tv_Name.setText("收货人："+customerModel.getConsignorName());
            holder.tv_phone.setText("联系方式："+customerModel.getConsignorMobile());

            holder.tv_money.setText(customerModel.getAmount()/100+"元");

            holder.tv_origin.setText("导航到收货地");
            holder.tv_site.setText("导航到送货地");
            //holder.tv_weight.setText("货物重量："+customerModel.getWeight()+"kg");
            if (customerModel.getWeight()==0){
                holder.tv_weight.setVisibility(View.GONE);
            }else {
                holder.tv_weight.setVisibility(View.VISIBLE);
                holder.tv_weight.setText("货物重量："+customerModel.getWeight()+"kg");
            }
            if (customerModel.getBulk()==null||customerModel.getBulk().equals("")){
                holder.tv_size.setVisibility(View.GONE);
            }else {
                holder.tv_size.setVisibility(View.VISIBLE);
                holder.tv_size.setText("货物尺寸：(米)"+customerModel.getBulk());
            }
            holder.li_number.setVisibility(View.GONE);

            holder.tv_consignorsite.setVisibility(View.VISIBLE);
            holder.tv_consignorsite.setText("发货点："+mDataList.get(position).getOrigin().getAddress());

            holder.tv_receivingsite.setVisibility(View.VISIBLE);
            holder.tv_receivingsite.setText("送货点："+mDataList.get(position).getSite().getAddress());

        }else if (OrderType.equals("express")){
            holder.li_mileage.setVisibility(View.GONE);

            holder.tv_consignorName.setText("发货人："+customerModel.getName());
            holder.tv_consignorphone.setText("联系方式："+customerModel.getMobile());

            holder.tv_Name.setText("收货人："+customerModel.getConsignorName());
            holder.tv_phone.setText("联系方式："+customerModel.getConsignorMobile());

            holder.tv_money.setText(customerModel.getAmount()/100+"元");

            holder.tv_origin.setText("导航到收货地");
            holder.tv_site.setText("导航到送货地");
            //holder.tv_weight.setText("货物重量："+customerModel.getWeight()+"kg");
            if (customerModel.getWeight()==0){
                holder.tv_weight.setVisibility(View.GONE);
            }else {
                holder.tv_weight.setVisibility(View.VISIBLE);
                holder.tv_weight.setText("货物重量："+customerModel.getWeight()+"kg");
            }
            if (customerModel.getBulk()==null||customerModel.getBulk().equals("")){
                holder.tv_size.setVisibility(View.GONE);
            }else {
                holder.tv_size.setVisibility(View.VISIBLE);
                holder.tv_size.setText("货物尺寸：(米)"+customerModel.getBulk());
            }
            holder.li_number.setVisibility(View.GONE);

            holder.tv_consignorsite.setVisibility(View.VISIBLE);
            holder.tv_consignorsite.setText("发货点："+mDataList.get(position).getOrigin().getAddress());

            holder.tv_receivingsite.setVisibility(View.VISIBLE);
            holder.tv_receivingsite.setText("送货点："+mDataList.get(position).getSite().getAddress());

        }else {
            holder.li_consignorName.setVisibility(View.GONE);
            holder.li_money.setVisibility(View.GONE);
            holder.tv_Name.setText(customerModel.getName());
            holder.tv_phone.setText("联系方式："+customerModel.getMobile());
            holder.tv_origin.setText("导航到出发地");
            holder.tv_site.setText("导航到目的地");
            holder.tv_number.setText(String.valueOf(customerModel.getNumber()));
            if (customerModel.getMileage()!=null){
                BigDecimal bd = new BigDecimal(customerModel.getMileage());
                BigDecimal setScale = bd.setScale(1, bd.ROUND_DOWN);
                holder.tv_mileage.setText(String.valueOf(setScale)+"km");
            }

            holder.li_e_origin.setVisibility(View.VISIBLE);
            holder.tv_e_origin.setText(mDataList.get(position).getOrigin().getAddress());

            holder.li_e_site.setVisibility(View.VISIBLE);
            holder.tv_e_site.setText(mDataList.get(position).getSite().getAddress());

        }

        if (customerModel.getNote()==null||customerModel.getNote().equals("无")||customerModel.getNote().equals("")||customerModel.getNote().equals("remark")){
            holder.li_note.setVisibility(View.GONE);
        }else {
            holder.tv_note.setText(customerModel.getNote());
        }

        holder.tv_status.setText(OrderUtils.getStatusStr(customerModel.getStatus()));
        //holder.tv_Num.setText("乘车人数：" + customerModel.getNumber());
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (customerModel.getStatus()) {
                    case YXConfig.T_ORDER_STATUS_ACCEPT:
                        // 开始行程
                        onItemListener.begainOrder(position);
                        holder.btn.setBackgroundResource(R.drawable.grive_btn);
                        holder.btn.setText("待支付");
                        break;
                    case YXConfig.T_ORDER_STATUS_GO:
                        // 改变按钮状态
                        break;
                    default:
                        break;
                }
            }
        });
        holder.btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemListener.endOrder(position);
            }
        });
        holder.iv_Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemListener.callOrder(position);
            }
        });
        holder.tv_origin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemListener.ReceiptPlace(position);
            }
        });
        holder.tv_site.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemListener.DeliveryPlace(position);
            }
        });
        holder.btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemListener.confirmOrder(position);
            }
        });
        holder.btn_refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemListener.refuseOrder(position);
            }
        });
    }

    public void setOnItemListener(RideOrderItemClickListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    /**
     * ViewHolder
     */
    class MyViewHolder extends RecyclerView.ViewHolder {
        // 乘车人姓名
        @BindView(R.id.trip_c_item_name)
        TextView tv_Name;
        @BindView(R.id.trip_c_item_consignorname)
        TextView tv_consignorName;
        @BindView(R.id.trip_c_item_consignorphone)
        TextView tv_consignorphone;
        @BindView(R.id.consignorName)
        LinearLayout li_consignorName;
        // 开始行程
        @BindView(R.id.trip_c_item_btn)
        Button btn;
        // 结束行程
        @BindView(R.id.trip_c_item_paybtn)
        Button btnPay;
        // 拨打电话
        @BindView(R.id.trip_c_item_call)
        ImageView iv_Call;
        @BindView(R.id.trip_c_item_phone)
        TextView tv_phone;
        //导航
        @BindView(R.id.origin)
        TextView tv_origin;
        @BindView(R.id.site)
        TextView tv_site;
        //小订单状态
        @BindView(R.id.trip_c_item_status)
        TextView tv_status;
        //乘客出价
        @BindView(R.id.trip_c_item_money)
        TextView tv_money;
        @BindView(R.id.trip_c_money)
        LinearLayout li_money;
        //订单
        @BindView(R.id.order)
        RelativeLayout re_order;
        //确认订单
        @BindView(R.id.trip_c_item_confirm)
        Button btn_confirm;
        ////拒绝订单
        @BindView(R.id.trip_c_item_refuse)
        Button btn_refuse;
        //支付按钮
        @BindView(R.id.xx)
        LinearLayout xx;
        @BindView(R.id.xxx)
        RelativeLayout xxx;
        //备注信息
        @BindView(R.id.note)
        LinearLayout li_note;
        @BindView(R.id.trip_c_item_note)
        TextView tv_note;
        //货物重量
        @BindView(R.id.trip_c_item_weight)
        TextView tv_weight;
        //货物尺寸
        @BindView(R.id.trip_c_item_size)
        TextView tv_size;
        //乘车人数
        @BindView(R.id.trip_c_number)
        LinearLayout li_number;
        @BindView(R.id.trip_c_item_number)
        TextView tv_number;
        //易碎易湿
        @BindView(R.id.trip_c_status)
        LinearLayout li_status;
        @BindView(R.id.trip_c_item_fragile)
        TextView tv_fragile;
        @BindView(R.id.trip_c_item_wet)
        TextView tv_wet;
        //行驶里程
        @BindView(R.id.mileage)
        LinearLayout li_mileage;
        @BindView(R.id.trip_c_item_mileage)
        TextView tv_mileage;
        //发货点
        @BindView(R.id.trip_c_item_consignorsite)
        TextView tv_consignorsite;
        //收货点
        @BindView(R.id.trip_c_item_site)
        TextView tv_receivingsite;

        //出发地
        @BindView(R.id.trip_c_e_origin)
        LinearLayout li_e_origin;
        @BindView(R.id.trip_c_item_e_origin)
        TextView tv_e_origin;

        //目的地
        @BindView(R.id.trip_c_e_site)
        LinearLayout li_e_site;
        @BindView(R.id.trip_c_item_e_site)
        TextView tv_e_site;


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void remove(CustomerModel itemModel) {
        mDataList.remove(itemModel);
    }

    public CustomerModel getItem(int pos) {
        return mDataList.get(pos);
    }

    private String getFragile(int status){
        String fragile;
        if (status==1){
            fragile="易碎";
        }else {
            fragile="非易碎";
        }
        return fragile;
    }
    private String getWet(int status){
        String wet;
        if (status==1){
            wet="易湿";
        }else {
            wet="非易湿";
        }
        return wet;
    }
}
