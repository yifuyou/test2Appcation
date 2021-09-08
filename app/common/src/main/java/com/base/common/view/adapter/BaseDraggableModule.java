package com.base.common.view.adapter;

import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.base.common.R;
import com.base.common.view.adapter.ada.BaseRVAdapter;
import com.base.common.view.adapter.connector.BaseViewHolder;

import java.util.Collections;

public class BaseDraggableModule implements DraggableListenerImp {

    private boolean isDragEnabled;
    private boolean isSwipeEnabled;
    private int toggleViewId;
    public ItemTouchHelper itemTouchHelper;
    public DragAndSwipeCallback itemTouchHelperCallback;
    @Nullable
    private View.OnTouchListener mOnToggleViewTouchListener;
    @Nullable
    private View.OnLongClickListener mOnToggleViewLongClickListener;
    @Nullable
    private OnItemDragListener mOnItemDragListener;
    @Nullable
    private OnItemSwipeListener mOnItemSwipeListener;
    private boolean isDragOnLongPressEnabled;

    private BaseRVAdapter baseQuickAdapter;
    private static final int NO_TOGGLE_VIEW = 0;


    public BaseDraggableModule(BaseRVAdapter baseQuickAdapter) {
        this.baseQuickAdapter = baseQuickAdapter;
        this.isDragOnLongPressEnabled = true;
        this.initItemTouch();
        initDrag();
    }


    public final boolean isDragEnabled() {
        return this.isDragEnabled;
    }

    public final void setDragEnabled(boolean var1) {
        this.isDragEnabled = var1;
    }

    public final boolean isSwipeEnabled() {
        return this.isSwipeEnabled;
    }

    public final void setSwipeEnabled(boolean var1) {
        this.isSwipeEnabled = var1;
    }

    public final int getToggleViewId() {
        return this.toggleViewId;
    }

    public final void setToggleViewId(int var1) {
        this.toggleViewId = var1;
    }


    public final ItemTouchHelper getItemTouchHelper() {
        return itemTouchHelper;
    }

    public final void setItemTouchHelper(ItemTouchHelper var1) {
        this.itemTouchHelper = var1;
    }


    public final DragAndSwipeCallback getItemTouchHelperCallback() {
        return itemTouchHelperCallback;
    }

    public final void setItemTouchHelperCallback(DragAndSwipeCallback var1) {
        this.itemTouchHelperCallback = var1;
    }

    @Nullable
    protected final View.OnTouchListener getMOnToggleViewTouchListener() {
        return this.mOnToggleViewTouchListener;
    }

    protected final void setMOnToggleViewTouchListener(@Nullable View.OnTouchListener var1) {
        this.mOnToggleViewTouchListener = var1;
    }

    @Nullable
    protected final View.OnLongClickListener getMOnToggleViewLongClickListener() {
        return this.mOnToggleViewLongClickListener;
    }

    protected final void setMOnToggleViewLongClickListener(@Nullable View.OnLongClickListener var1) {
        this.mOnToggleViewLongClickListener = var1;
    }

    @Nullable
    protected final OnItemDragListener getMOnItemDragListener() {
        return this.mOnItemDragListener;
    }

    protected final void setMOnItemDragListener(@Nullable OnItemDragListener var1) {
        this.mOnItemDragListener = var1;
    }

    @Nullable
    protected final OnItemSwipeListener getMOnItemSwipeListener() {
        return this.mOnItemSwipeListener;
    }

    protected final void setMOnItemSwipeListener(@Nullable OnItemSwipeListener var1) {
        this.mOnItemSwipeListener = var1;
    }

    private final void initItemTouch() {
        this.itemTouchHelperCallback = new DragAndSwipeCallback(this);
        this.itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
    }


    protected void initDrag() {
        setDragEnabled(true);
        setDragOnLongPressEnabled(true);
        getItemTouchHelperCallback().setSwipeMoveFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN);
        setOnItemDragListener(new OnItemDragListener() {
            @Override
            public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {
                pickUpAnimation(viewHolder.itemView);
            }

            @Override
            public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {
                Log.d("onItemDragMoving", "move from: " + source.getAdapterPosition() + " to: " + target.getAdapterPosition());
            }

            @Override
            public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {
                putDownAnimation(viewHolder.itemView);
            }
        });


    }

    private void pickUpAnimation(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationZ", 1f, 10f);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(300);
        animator.start();
    }

    private void putDownAnimation(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationZ", 10f, 1f);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(300);
        animator.start();
    }


    public final void initView(BaseViewHolder holder) {
        if (this.isDragEnabled && this.hasToggleView()) {
            View toggleView = holder.itemView.findViewById(this.toggleViewId);
            if (toggleView != null) {
                toggleView.setTag(R.id.tag_three, holder);
                if (this.isDragOnLongPressEnabled()) {
                    toggleView.setOnLongClickListener(this.mOnToggleViewLongClickListener);
                } else {
                    toggleView.setOnTouchListener(this.mOnToggleViewTouchListener);
                }
            }
        }

    }


    public final void attachToRecyclerView(RecyclerView recyclerView) {
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public boolean hasToggleView() {
        return this.toggleViewId != 0;
    }

    public boolean isDragOnLongPressEnabled() {
        return this.isDragOnLongPressEnabled;
    }

    public void setDragOnLongPressEnabled(boolean value) {
        this.isDragOnLongPressEnabled = value;
        if (value) {
            this.mOnToggleViewTouchListener = (View.OnTouchListener) null;
            this.mOnToggleViewLongClickListener = new View.OnLongClickListener() {
                public final boolean onLongClick(View v) {
                    if (BaseDraggableModule.this.isDragEnabled()) {
                        ItemTouchHelper var10000 = BaseDraggableModule.this.getItemTouchHelper();
                        Object var10001 = v.getTag(R.id.tag_three);
                        if (var10001 == null) {
                            throw new NullPointerException("null cannot be cast to non-null type androidx.recyclerview.widget.RecyclerView.ViewHolder");
                        }

                        var10000.startDrag((RecyclerView.ViewHolder) var10001);
                    }

                    return true;
                }
            };
        } else {
            this.mOnToggleViewLongClickListener = (View.OnLongClickListener) null;
            this.mOnToggleViewTouchListener = new View.OnTouchListener() {
                public final boolean onTouch(View v, MotionEvent event) {
                    boolean var10000;
                    if (event.getAction() == 0 && !BaseDraggableModule.this.isDragOnLongPressEnabled()) {
                        if (BaseDraggableModule.this.isDragEnabled()) {
                            ItemTouchHelper var3 = BaseDraggableModule.this.getItemTouchHelper();
                            Object var10001 = v.getTag(R.id.tag_three);
                            if (var10001 == null) {
                                throw new NullPointerException("null cannot be cast to non-null type androidx.recyclerview.widget.RecyclerView.ViewHolder");
                            }

                            var3.startDrag((RecyclerView.ViewHolder) var10001);
                        }

                        var10000 = true;
                    } else {
                        var10000 = false;
                    }

                    return var10000;
                }
            };
        }

    }

    protected final int getViewHolderPosition(RecyclerView.ViewHolder viewHolder) {
        return viewHolder.getAdapterPosition();
    }

    public void onItemDragStart(RecyclerView.ViewHolder viewHolder) {
        if (mOnItemDragListener != null) {
            mOnItemDragListener.onItemDragStart(viewHolder, this.getViewHolderPosition(viewHolder));
        }
    }

    public void onItemDragMoving(RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        int from = this.getViewHolderPosition(source);
        int to = this.getViewHolderPosition(target);
        if (this.inRange(from) && this.inRange(to)) {

            if (from < to) {
                for (int i = from; i < to; i++) {
                    Collections.swap(baseQuickAdapter.getDatas(), i, i + 1);
                }
            } else {
                for (int i = from; i > to; i--) {
                    Collections.swap(baseQuickAdapter.getDatas(), i, i - 1);
                }
            }

            this.baseQuickAdapter.notifyItemMoved(from, to);
        }

        OnItemDragListener var10000 = this.mOnItemDragListener;
        if (var10000 != null) {
            var10000.onItemDragMoving(source, from, target, to);
        }

    }

    public void onItemDragEnd(RecyclerView.ViewHolder viewHolder) {
        if (mOnItemDragListener != null) {
            mOnItemDragListener.onItemDragEnd(viewHolder, this.getViewHolderPosition(viewHolder));
        }

    }

    public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder) {
        if (this.isSwipeEnabled) {
            if (mOnItemSwipeListener != null) {
                mOnItemSwipeListener.onItemSwipeStart(viewHolder, this.getViewHolderPosition(viewHolder));
            }
        }

    }

    public void onItemSwipeClear(RecyclerView.ViewHolder viewHolder) {
        if (this.isSwipeEnabled) {
            if (mOnItemSwipeListener != null) {
                mOnItemSwipeListener.clearView(viewHolder, this.getViewHolderPosition(viewHolder));
            }
        }

    }

    public void onItemSwiped(RecyclerView.ViewHolder viewHolder) {
        int pos = this.getViewHolderPosition(viewHolder);
        if (this.inRange(pos)) {
            this.baseQuickAdapter.getDatas().remove(pos);
            this.baseQuickAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            if (this.isSwipeEnabled) {
                if (mOnItemSwipeListener != null) {
                    mOnItemSwipeListener.onItemSwiped(viewHolder, pos);
                }
            }
        }

    }

    public void onItemSwiping(@Nullable Canvas canvas, @Nullable RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {
        if (this.isSwipeEnabled) {
            if (mOnItemSwipeListener != null) {
                mOnItemSwipeListener.onItemSwipeMoving(canvas, viewHolder, dX, dY, isCurrentlyActive);
            }
        }

    }

    private final boolean inRange(int position) {
        return position >= 0 && position < this.baseQuickAdapter.getDatas().size();
    }

    public void setOnItemDragListener(@Nullable OnItemDragListener onItemDragListener) {
        this.mOnItemDragListener = onItemDragListener;
    }

    public void setOnItemSwipeListener(@Nullable OnItemSwipeListener onItemSwipeListener) {
        this.mOnItemSwipeListener = onItemSwipeListener;
    }


}
