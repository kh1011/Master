package com.yxdriver.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.yxdriver.app.R;
import com.yxdriver.app.activity.PhotoviewActivity;
import com.yxdriver.app.app.DemoApplication;
import com.yxdriver.app.config.YXConfig;
import com.yxdriver.app.event.InfoItemClickListener;
import com.yxdriver.app.event.RideOrderItemClickListener;
import com.yxdriver.app.listener.OnRecyclerItemClickListener;
import com.yxdriver.app.model.bean.CustomerModel;
import com.yxdriver.app.model.bean.OldCarModel;
import com.yxdriver.app.model.bean.UserModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Song on 2016/9/7.
 * x
 */
public class InfoListAdapter extends RecyclerView.Adapter<InfoListAdapter.MyViewHolder> {

    private Context mContext;
    private List<OldCarModel> mDataList;
    private LayoutInflater mInflater;
    private InfoItemClickListener onItemListener;

    public InfoListAdapter(Context context, List<OldCarModel> dataList) {
        this.mContext = context;
        this.mDataList = dataList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = mInflater.inflate(R.layout.item_same_city, parent, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final OldCarModel model = mDataList.get(position);
        UserModel user = model.getCreateUser();
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.default_head)
                .error(R.drawable.default_head)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        if (user.getHeadImage() != null) {
            Glide.with(mContext).load(user.getHeadImage()).apply(options).into(holder.civUserHeadimg);
        }
        holder.tvUserCName.setText(user.getName());
        holder.tvTime.setText(model.getCreateTime());
        holder.tvContent.setText(model.getContent());
        //为GridView设置适配器
        if (model.getImages() != null && model.getImages().length > 0) {
            initRcv(holder.rcvImg, new ArrayList<String>(Arrays.asList(model.getImages())), new ArrayList<String>(Arrays.asList(model.getImages())));
        }
        //事件监听
        holder.rcvImg.addOnItemTouchListener(new OnRecyclerItemClickListener(holder.rcvImg) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                // 查看大图
                onItemListener.lookBigImg(position);
            }

            @Override
            public void onItemLongClick(RecyclerView.ViewHolder vh) {
                //如果item不是最后一个，则执行拖拽
                /*if (vh.getLayoutPosition() != dragImages.size() - 1) {
                    itemTouchHelper.startDrag(vh);
                }*/
            }
        });
        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 删除当前信息
                onItemListener.doDelete(position);
            }
        });
    }

    public void setOnItemListener(InfoItemClickListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    private void initRcv(RecyclerView rcvImg, List<String> dragImages, List<String> originImages) {
        PostArticleImgAdapter postArticleImgAdapter = new PostArticleImgAdapter(mContext, dragImages);
        rcvImg.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        rcvImg.setAdapter(postArticleImgAdapter);
    }

    /**
     * ViewHolder
     */
    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.civ_user_headimg)
        CircleImageView civUserHeadimg;
        @BindView(R.id.tv_user_c_name)
        TextView tvUserCName;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.tv_delete)
        TextView tvDelete;
        @BindView(R.id.rcv_img)
        RecyclerView rcvImg;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void remove(OldCarModel itemModel) {
        mDataList.remove(itemModel);
    }

    public OldCarModel getItem(int pos) {
        return mDataList.get(pos);
    }
}
