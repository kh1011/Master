package com.yxdriver.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yxdriver.app.R;
import com.yxdriver.app.event.ReceipterItemListener;
import com.yxdriver.app.model.bean.ReceiptAddressModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 收货地址列表适配器
 */
public class ReceipterListAdapter extends RecyclerView.Adapter<ReceipterListAdapter.MyViewHolder> {

    private List<ReceiptAddressModel> mDataList;
    private LayoutInflater mInflater;
    private ReceipterItemListener mOnItemListener;
    // 是否是管理收货地址
    private boolean isManager = false;

    public ReceipterListAdapter(Context context, List<ReceiptAddressModel> dataList, boolean isManager) {
        this.mDataList = dataList;
        mInflater = LayoutInflater.from(context);
        this.isManager = isManager;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = mInflater.inflate(R.layout.item_receipt_address, parent, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ReceiptAddressModel item = mDataList.get(position);
        holder.tvName.setText(item.getName());
        holder.tvMobile.setText(item.getMobile());
        String[] s=item.getAddress().split(item.getStreet());
        holder.tvAddress.setText(item.getProvince() + "省" + item.getCity() + "市" + item.getCounty() + item.getStreet()+s[1]);
        if (item.getIsdefault() == 1) {
            holder.defCheckBox.setChecked(true);
        } else {
            holder.defCheckBox.setChecked(false);
        }
        if (item.getIsdefault() == 1) {
            holder.tvDefault.setVisibility(View.VISIBLE);
        }
        if (isManager) {
            holder.linearLayout.setVisibility(View.VISIBLE);
            holder.checkBox.setVisibility(View.GONE);
        }
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // 选中
                    mOnItemListener.selected(position);
                }
            }
        });
        holder.defCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // 设置默认地址
                    mOnItemListener.setDefault(position);
                }
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 删除当前收货地址
                mOnItemListener.delete(position);
            }
        });
        holder.edite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 编辑当前收货地址
                mOnItemListener.edite(position);
            }
        });
    }

    public void setOnItemListener(ReceipterItemListener onItemListener) {
        this.mOnItemListener = onItemListener;
    }

    /**
     * ViewHolder
     */
    class MyViewHolder extends RecyclerView.ViewHolder {
        // 收货人姓名
        @BindView(R.id.item_recetper_name)
        TextView tvName;
        // 收货人电话号码
        @BindView(R.id.item_recetper_mobile)
        TextView tvMobile;
        // 是否是默认收货地址
        @BindView(R.id.item_recetper_isDefault)
        TextView tvDefault;
        // 收货地址
        @BindView(R.id.item_recetper_address)
        TextView tvAddress;
        // 选择
        @BindView(R.id.item_recetper_ck)
        CheckBox checkBox;
        // 地址管理区域
        @BindView(R.id.item_recetper_manager)
        LinearLayout linearLayout;
        // 设置默认地址
        @BindView(R.id.item_recetper_setdef)
        CheckBox defCheckBox;
        // 编辑
        @BindView(R.id.item_recetper_edit)
        Button edite;
        // 删除
        @BindView(R.id.item_recetper_delete)
        Button delete;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void remove(ReceiptAddressModel itemModel) {
        mDataList.remove(itemModel);
    }

    public ReceiptAddressModel getItem(int pos) {
        return mDataList.get(pos);
    }
}
