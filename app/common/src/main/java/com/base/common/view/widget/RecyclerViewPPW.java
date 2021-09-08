package com.base.common.view.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.base.common.databinding.PpwRecyclerItemBinding;
import com.base.common.model.bean.ADInfo;
import com.base.common.view.adapter.MyLinearLayoutManager;

import androidx.recyclerview.widget.RecyclerView;

import com.base.common.R;
import com.base.common.utils.DrawableUtil;
import com.base.common.utils.ViewUtils;
import com.base.common.view.adapter.ada.BaseRVAdapter;
import com.base.common.view.adapter.ada.GeneralRecyclerAdapter;
import com.base.common.view.adapter.connector.BaseViewHolder;
import com.base.common.view.adapter.connector.BaseItemTypeInterface;

import java.util.List;


public class RecyclerViewPPW extends PopupWindow implements BaseItemTypeInterface<ADInfo, PpwRecyclerItemBinding> {

    private BaseRVAdapter baseRVAdapter;

    /**
     * @param context
     * @param onclickView   和这里的view一样宽
     * @param baseRVAdapter
     * @param height
     */
    public RecyclerViewPPW(Context context, View onclickView, BaseRVAdapter baseRVAdapter, int... height) {
        if (baseRVAdapter == null) {
            baseRVAdapter = getBaseRVAdapter();
        }
        this.baseRVAdapter = baseRVAdapter;
        init(context, baseRVAdapter, onclickView.getMeasuredWidth(), height);
    }

    /**
     * @param context
     * @param baseRVAdapter
     * @param width         指定宽度
     * @param height
     */
    public RecyclerViewPPW(Context context, BaseRVAdapter baseRVAdapter, int width, int... height) {
        if (baseRVAdapter == null) {
            baseRVAdapter = getBaseRVAdapter();
        }
        this.baseRVAdapter = baseRVAdapter;
        init(context, baseRVAdapter, width, height);
    }


    private void init(Context context, BaseRVAdapter baseRVAdapter, int width, int... height) {
        View view = ViewUtils.getLayoutView(context, getLayout());

        setContentView(view);
        setWidth(width);
        setHeight(height.length == 0 ? LinearLayout.LayoutParams.WRAP_CONTENT : height[0]);

        setFocusable(true);
        setOutsideTouchable(true);
        //防止虚拟软键盘被弹出菜单遮住
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // 设置SelectPicPopupWindow弹出窗体可点击
        setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        if (getAnimation()) setAnimationStyle(R.style.AnimBottomPopupWindow);


        ColorDrawable dw;
        if (isTransparent()) {
            // 实例化一个ColorDrawable颜色为透明
            dw = DrawableUtil.getColorDrawableRes(android.R.color.transparent);
        } else {
            // 实例化一个ColorDrawable颜色为半透明
            dw = new ColorDrawable(0xa0000000);
//            dw = DrawableUtil.getColorDrawableRes(R.color.C_DBDBDB);
        }
        // 设置SelectPicPopupWindow弹出窗体的背景
        setBackgroundDrawable(dw);

        if (baseRVAdapter != null) {
            RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new MyLinearLayoutManager(context));
//        recyclerView.addItemDecoration(new ItemDividerDecoration(5, R.color.C_F2F2F2));
            recyclerView.setAdapter(baseRVAdapter);
        }
        initView(view);
        initAdapter(baseRVAdapter);
    }


    public void setDataList(List<ADInfo> list) {
        if (baseRVAdapter != null) {
            baseRVAdapter.setDataList(list);
        }
    }


    protected void initView(View mainView) {

    }

    protected void initAdapter(BaseRVAdapter baseRVAdapter) {

    }

    private BaseRVAdapter getBaseRVAdapter() {
        return new GeneralRecyclerAdapter() {
            @Override
            public void initMultiItemType() {
                putMultiItemType(BaseItemTypeInterface.TYPE_CHILD, RecyclerViewPPW.this);
            }
        };
    }


    protected int getLayout() {
        return R.layout.ppw_recycler_view;
    }

    public boolean getAnimation() {
        return false;
    }

    public boolean isTransparent() {
        return true;
    }


    /**
     * ------------------------------------------------------------------------------------------------------------------------
     *
     * @return
     */
    @Override
    public int getItemType() {
        return TYPE_CHILD;
    }


    @Override
    public void setItemType(int itemType) {

    }

    @Override
    public boolean isCurrentItemType(int position, ADInfo bean) {
        return true;
    }

    @Override
    public final int getLayoutId() {
        return R.layout.ppw_recycler_item;
    }

    @Override
    public boolean isFullSpanType() {
        return true;
    }

    @Override
    public void onBindViewHolder(PpwRecyclerItemBinding binding, int position, ADInfo bean) {
//        binding.imageName.setText(bean.getImageName());
        if (baseRVAdapter != null) {
            if (position == baseRVAdapter.getLastPosition()) {
                binding.vLine.setVisibility(View.GONE);
            } else {
                binding.vLine.setVisibility(View.VISIBLE);
            }
        }

    }


    @Override
    public void onBindViewHolder(PpwRecyclerItemBinding binding, int position, BaseViewHolder viewHolder, ADInfo bean) {

    }

    @Override
    public void onCreateViewHolder(PpwRecyclerItemBinding binding, BaseViewHolder viewHolder) {

    }

    @Override
    public void onViewDetachedFromWindow(PpwRecyclerItemBinding binding, BaseViewHolder viewHolder) {

    }

    @Override
    public void onViewAttachedToWindow(PpwRecyclerItemBinding binding, BaseViewHolder viewHolder) {

    }


    @Override
    public void onItemClick(View view, BaseRVAdapter baseRVAdapter, BaseViewHolder viewHolder, PpwRecyclerItemBinding binding, int onclickType, int position, ADInfo bean) {
        dismiss();
    }

}
