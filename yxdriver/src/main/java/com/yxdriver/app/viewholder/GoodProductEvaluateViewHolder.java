package com.yxdriver.app.viewholder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.yxdriver.app.R;
import com.yxdriver.app.model.bean.GoodEvaluateModel;
import com.yxdriver.app.model.bean.UserInfo;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 功能：商品信息中的评论的viewholder
 * Created by yun.zhang on 2017/9/29 0029.
 * email:zy19930321@163.com
 */
public class GoodProductEvaluateViewHolder extends BaseViewHolder<GoodEvaluateModel> {

    Context context;

    CircleImageView civUserHeadimg;
    TextView tvUserCName;
    TextView tvTime;
    TextView tvContent;

    public GoodProductEvaluateViewHolder(ViewGroup parent, Context context) {
        super(parent, R.layout.item_good_product_evaluate);
        this.context = context;
        civUserHeadimg = $(R.id.civ_user_headimg);
        tvUserCName = $(R.id.tv_user_c_name);
        tvTime = $(R.id.tv_time);
        tvContent = $(R.id.tv_content);
    }

    @Override
    public void setData(GoodEvaluateModel data) {
        super.setData(data);
        if(data != null){
            UserInfo user = data.getCreateUser();
            if(user != null){
                Glide.with(context).load(user.getHeadImage()).into(civUserHeadimg);
                tvUserCName.setText(user.getName());
            }
            tvTime.setText("购买于" + data.getCreateTime());
            tvContent.setText(data.getContent());
        }
    }
}
