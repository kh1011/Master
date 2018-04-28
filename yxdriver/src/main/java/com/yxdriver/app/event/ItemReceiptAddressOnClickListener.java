package com.yxdriver.app.event;

/**
 * 收货地址的适配器点击事件接口
 *
 * Created by zhangyun on 2017/10/2 0002
 * EMail -> yun.zhang@chinacreator.com
 */
public interface ItemReceiptAddressOnClickListener {
    /**
     * 编辑按钮点击事件
     * @param position
     */
    void edit(int position);

    /**
     * 删除按钮点击事件
     * @param position
     */
    void delete(int position);

    /**
     * 将地址设置为默认地址
     * @param oldDefault
     * @param newDefault
     */
    void setDefault(int oldDefault,int newDefault);
}
