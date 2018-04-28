package com.yxclient.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yxclient.app.R;
import com.yxclient.app.event.ItemReceiptAddressOnClickListener;
import com.yxclient.app.model.bean.ReceiptAddressModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 收货地址适配器
 * <p>
 * Created by zhangyun on 2017/10/2 0002
 * EMail -> yun.zhang@chinacreator.com
 */
public class ReceiptAddressAdapter extends BaseAdapter {
    Context context;
    List<ReceiptAddressModel> addressModels;
    LayoutInflater inflater;
    //是否是编辑模式
    boolean isEdit = false;
    int oldDefaultAddressPosition = 0;
    ItemReceiptAddressOnClickListener listener;
    boolean manager = false;

    public ReceiptAddressAdapter(Context context, List<ReceiptAddressModel> addressModels, boolean manager) {
        this.context = context;
        this.addressModels = addressModels;
        inflater = LayoutInflater.from(context);
        this.listener = (ItemReceiptAddressOnClickListener) context;
        this.manager = manager;
        if (addressModels != null && addressModels.size() > 0 && addressModels.get(0) != null) {
            isEdit = addressModels.get(0).isEdit();
        }
    }

    @Override
    public int getCount() {
        return addressModels == null ? 0 : addressModels.size();
    }

    @Override
    public Object getItem(int position) {
        return addressModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_receipt_address, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(R.id.tag_one, viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag(R.id.tag_one);
        }
        /*ReceiptAddressModel model = addressModels.get(position);
        //TODO 填充item中的内容
        viewHolder.tvName.setText(model.getName());
        if (1 == model.getIsdefault()) {
            viewHolder.ivDefaultSet.setImageResource(R.drawable.ok);
            viewHolder.tvAddressDefault.setVisibility(View.VISIBLE);
            viewHolder.tvDefaultSet.setText("默认地址");
            viewHolder.tvDefaultSet.setTextColor(Color.parseColor("#6fa5ff"));
            oldDefaultAddressPosition = position;
        } else {
            viewHolder.ivDefaultSet.setImageResource(R.drawable.no_choose);
            viewHolder.tvAddressDefault.setVisibility(View.GONE);
            viewHolder.tvDefaultSet.setText("设为默认");
            viewHolder.tvDefaultSet.setTextColor(Color.parseColor("#383838"));
        }
        viewHolder.ivSelected.setImageResource(R.drawable.no_choose);
        if (model.isSelected()) {
            viewHolder.ivSelected.setImageResource(R.drawable.ok);
        }
        //处于管理模式的时候，不需要展示选中
        if (manager) {
            viewHolder.ivSelected.setVisibility(View.GONE);
        } else {
            viewHolder.ivSelected.setVisibility(View.VISIBLE);
        }
        viewHolder.tvAddress.setText(model.getAddress());
        viewHolder.tvPhone.setText(model.getMobile());
        if (model.isEdit()) {
            viewHolder.llEdit.setVisibility(View.VISIBLE);
        } else {
            viewHolder.llEdit.setVisibility(View.GONE);
        }
        //编辑地址
        viewHolder.tvEditAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.edit(position);
            }
        });
        //删除收货地址
        viewHolder.tvDeleteAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.delete(position);
            }
        });
        //设置为默认的收货地址
        viewHolder.tvDefaultSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.setDefault(oldDefaultAddressPosition, position);
            }
        });*/

        return convertView;
    }

    /**
     * 获取上一次的默认地址
     *
     * @return
     */
    public int getOldDefaultAddressPosition() {
        return oldDefaultAddressPosition;
    }

    public class ViewHolder {
        /*@BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_phone)
        TextView tvPhone;
        @BindView(R.id.iv_selected)
        ImageView ivSelected;
        @BindView(R.id.tv_address_default)
        TextView tvAddressDefault;
        @BindView(R.id.tv_address)
        TextView tvAddress;
        @BindView(R.id.iv_default_set)
        ImageView ivDefaultSet;
        @BindView(R.id.tv_default_set)
        TextView tvDefaultSet;
        @BindView(R.id.tv_edit_address)
        TextView tvEditAddress;
        @BindView(R.id.tv_delete_address)
        TextView tvDeleteAddress;
        @BindView(R.id.ll_edit)
        LinearLayout llEdit;*/

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
