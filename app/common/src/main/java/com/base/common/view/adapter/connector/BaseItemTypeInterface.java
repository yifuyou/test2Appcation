package com.base.common.view.adapter.connector;


import androidx.annotation.LayoutRes;
import androidx.databinding.ViewDataBinding;

public interface BaseItemTypeInterface<T, D extends ViewDataBinding> extends OnItemTypeClickListener<T, D> {

    int TYPE_CHILD = 0;//默认的项目
    int TYPE_CHILD_ONE = 1;
    int TYPE_CHILD_TWO = 2;
    int TYPE_CHILD_THREE = 3;
    int TYPE_CHILD_FOUR = 4;
    int TYPE_CHILD_FIVE = 5;


    int TYPE_GROUP = 10;


    int TYPE_HEAD = 100000;
    int TYPE_HEAD_ONE = 100001;
    int TYPE_HEAD_TWO = 100002;
    int TYPE_HEAD_THREE = 100003;
    int TYPE_HEAD_FOUR = 100004;
    int TYPE_HEAD_FIVE = 100005;
    int TYPE_HEAD_SIX = 100006;
    int TYPE_HEAD_SEVEN = 100007;
    int TYPE_HEAD_EIGHT = 100008;
    int TYPE_HEAD_NINE = 100009;


    int TYPE_FOOT = 900000;
    int TYPE_FOOT_ONE = 900001;
    int TYPE_FOOT_TWO = 900002;
    int TYPE_FOOT_THREE = 900003;
    int TYPE_FOOT_FOUR = 900004;
    int TYPE_FOOT_FIVE = 900005;

    /**
     * 当前类型
     *
     * @return
     */
    int getItemType();

    /**
     * 更换类型
     *
     * @param itemType
     */
    void setItemType(int itemType);

    /**
     * 判断数据源是否是当前类型
     *
     * @param bean
     * @return
     */
    boolean isCurrentItemType(int position, T bean);

    /**
     * 返回layoutId
     *
     * @return
     */
    @LayoutRes
    int getLayoutId();


    /**
     * 是否占用一整行
     *
     * @return
     */
    boolean isFullSpanType();

    /**
     * 数据绑定
     * 比下面的 onBindViewHolder  先执行
     *
     * @param binding
     * @param position
     * @param bean
     */
    void onBindViewHolder(D binding, int position, T bean);

    /**
     * 在上面的方法后执行
     *
     * @param binding
     * @param position
     * @param viewHolder
     * @param bean
     */
    void onBindViewHolder(D binding, int position, BaseViewHolder viewHolder, T bean);


    /**
     * 在ViewHolder 创建完成时回调
     */
    void onCreateViewHolder(D binding, BaseViewHolder viewHolder);

    void onViewDetachedFromWindow(D binding, BaseViewHolder viewHolder);

    void onViewAttachedToWindow(D binding, BaseViewHolder viewHolder);

}
