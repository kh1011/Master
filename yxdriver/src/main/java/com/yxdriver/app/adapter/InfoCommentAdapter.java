package com.yxdriver.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yxdriver.app.R;
import com.yxdriver.app.event.ItemInfoCommentListener;
import com.yxdriver.app.model.bean.InfoEvaluationsModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Song on 2016/9/7.
 * x
 */
public class InfoCommentAdapter extends RecyclerView.Adapter<InfoCommentAdapter.MyViewHolder> {

    private Context mContext;
    private List<InfoEvaluationsModel> mDataList;
    private LayoutInflater mInflater;
    private ItemInfoCommentListener onItemListener;

    public InfoCommentAdapter(Context context, List<InfoEvaluationsModel> dataList) {
        this.mContext = context;
        this.mDataList = dataList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = mInflater.inflate(R.layout.item_info_comment, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(rootView);
        return myViewHolder;
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final InfoEvaluationsModel model = mDataList.get(position);
        Glide.with(mContext).load(model.getCreateUser().getHeadImage()).into(holder.imageView);
        holder.tv_Name.setText(model.getCreateUser().getName());
        holder.tv_Content.setText(model.getContent());
        holder.tv_Date.setText(model.getCreateTime());
    }

    public void setOnItemListener(ItemInfoCommentListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    /**
     * ViewHolder
     */
    class MyViewHolder extends RecyclerView.ViewHolder {
        // 评论者头像
        @BindView(R.id.profile_userimage)
        CircleImageView imageView;
        // 评论者姓名
        @BindView(R.id.comment_username)
        TextView tv_Name;
        // 评论内容
        @BindView(R.id.comment_content)
        TextView tv_Content;
        // 评论发布日期
        @BindView(R.id.comment_date)
        TextView tv_Date;


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void remove(InfoEvaluationsModel itemModel) {
        mDataList.remove(itemModel);
    }

    public InfoEvaluationsModel getItem(int pos) {
        return mDataList.get(pos);
    }
}
