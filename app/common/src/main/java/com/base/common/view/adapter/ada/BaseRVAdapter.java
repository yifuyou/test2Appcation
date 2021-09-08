package com.base.common.view.adapter.ada;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.collection.SparseArrayCompat;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.GridLayoutManager;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.base.common.R;
import com.base.common.utils.DensityUtil;
import com.base.common.utils.JavaMethod;
import com.base.common.utils.LogUtil;
import com.base.common.utils.ViewUtils;
import com.base.common.view.adapter.BaseDraggableModule;
import com.base.common.view.adapter.DraggableModule;
import com.base.common.view.adapter.connector.BaseViewHolder;
import com.base.common.view.adapter.connector.BaseItemTypeInterface;
import com.base.common.view.adapter.connector.OnItemClickInterface;
import com.base.common.view.adapter.splite_line.FixedItemDecoration;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * RecyclerViewAdapter 的基类，一般不直接使用，
 * 根据其功能使用其子类
 * <p>
 * <p>
 * 回调顺序
 * getItemViewType(获取显示类型，返回值可在onCreateViewHolder中拿到，以决定加载哪种ViewHolder)
 * <p>
 * onCreateViewHolder(加载ViewHolder的布局)
 * <p>
 * onViewAttachedToWindow（当Item进入这个页面的时候调用）
 * <p>
 * onBindViewHolder(将数据绑定到布局上，以及一些逻辑的控制就写这啦)
 * <p>
 * onViewDetachedFromWindow（当Item离开这个页面的时候调用）
 * <p>
 * onViewRecycled(当Item被回收的时候调用)
 */
public abstract class BaseRVAdapter extends RecyclerView.Adapter<BaseViewHolder> implements OnItemClickInterface {


    //存储类型信息的集合  itemType 为key
    private SparseArrayCompat<BaseItemTypeInterface> mMultiItemTypeList;
    //存储类型信息的集合  class 为key  itemType 为value   谁先添加谁先判断
    private HashMap<Class<?>, List<Integer>> mMultiItemTypeHashMap;

    //缓存需要缓存的BaseViewHolder数量的集合, 减少onCreateViewHolder的消耗
    private SparseArrayCompat<Integer> cacheViewHolder;


    private List mDatas = new ArrayList();

    //    protected BaseRVAdapter.ListChangedCallback mItemsChangeCallback;
    //被添加的recyclerView
    protected RecyclerView recyclerView;

    private boolean isOpenDefaultAnimator = true;//是否打开加载动画

    private RecyclerView.LayoutManager layoutManager;
    /**
     * 需要传出去的点击事件,可拦截 adapter{@link #onItemClick(View, BaseRVAdapter, int, int, int, Object)}
     * 和@{@link com.base.common.view.adapter.connector.BaseItemMultiType#onItemClick(View, BaseRVAdapter, BaseViewHolder, ViewDataBinding, int, int, Object)}  的点击事件
     */
    private OnItemClickInterface onItemClickInterface;

    /**
     * 点击事件的回调，在{@link com.base.common.view.adapter.connector.BaseItemMultiType#onItemTypeClick(View, BaseRVAdapter, int, int, Object)}   点击事件后回调
     */
    private OnItemClickInterface onItemClickCallback;


    private RecyclerView.RecycledViewPool recycledViewPool;//共享的缓存
    /**
     * android 生命周期
     */
    protected WeakReference<LifecycleOwner> lifecycleOwner;


    private int typeFixed = 0;//固定到头部的类型
    private int movePosition;//移动到的位置


    //可拖动的类型
    private int mDraggableItemType = BaseItemTypeInterface.TYPE_CHILD;
    //可拖动的view id
    private int mDraggableToggleViewId = 0;
    //可以拖动的列表项
    private BaseDraggableModule mDraggableModule;
    //是否开启拖动
    public boolean isDraggable() {
        return false;
    }

    /**
     * @param typeFixed 设置固定到头部的类型
     */
    public void setTypeFixed(int typeFixed) {
        this.typeFixed = typeFixed;
    }



    public BaseRVAdapter() {
        init();
    }

    public BaseRVAdapter(LifecycleOwner lifecycleOwner) {
        if (lifecycleOwner != null) {
            this.lifecycleOwner = new WeakReference<>(lifecycleOwner);
        }
        init();
    }

    public LifecycleOwner getLifecycleOwner() {
        if (lifecycleOwner == null) return null;
        return lifecycleOwner.get();
    }


    public void init() {
        cacheViewHolder = new SparseArrayCompat<>();
        mMultiItemTypeList = new SparseArrayCompat<>();
        mMultiItemTypeHashMap = new HashMap<>();
//        mItemsChangeCallback = new BaseRVAdapter.ListChangedCallback();
//        setHasStableIds(true);

        if (isDraggable()) {
            mDraggableModule = addDraggableModule(this);
        }

        initMultiItemType();
    }

    public abstract void initMultiItemType();




    public void setDraggableItemType(int mDraggableItemType) {
        this.mDraggableItemType = mDraggableItemType;
    }

    public void setDraggableToggleViewId(int draggableToggleViewId) {
        this.mDraggableToggleViewId = draggableToggleViewId;
        if (mDraggableModule != null) {
            mDraggableModule.setToggleViewId(draggableToggleViewId);
        }
    }

    public BaseDraggableModule addDraggableModule(BaseRVAdapter baseRVAdapter) {
        return new BaseDraggableModule(baseRVAdapter);
    }

    @Override
    public int getItemViewType(int position) {
        Object obj = getItemBean(position);
        if (obj != null) {
            List<Integer> list = mMultiItemTypeHashMap.get(obj.getClass());
            if (list != null) {
                for (Integer integer : list) {
                    BaseItemTypeInterface baseItemMultiTypeInterface = mMultiItemTypeList.get(integer);
                    if (baseItemMultiTypeInterface.isCurrentItemType(position, obj)) {
                        return baseItemMultiTypeInterface.getItemType();
                    }
                }
            }
        }
        return BaseItemTypeInterface.TYPE_CHILD;
    }


    public <T extends BaseItemTypeInterface> T getMultiItemType(int... viewType) {
        if (viewType.length == 0)
            return (T) mMultiItemTypeList.get(BaseItemTypeInterface.TYPE_CHILD);
        return (T) mMultiItemTypeList.get(viewType[0]);
    }


    public <T, D extends ViewDataBinding> void putMultiItemType(int itemType, @NonNull BaseItemTypeInterface<T, D> baseItemMultiTypeInterface) {
        baseItemMultiTypeInterface.setItemType(itemType);
        Class<?> cls = JavaMethod.getSuperGenericClass(baseItemMultiTypeInterface, 0);
        List<Integer> list = mMultiItemTypeHashMap.get(cls);
        if (list == null) {
            list = new ArrayList<>();
            mMultiItemTypeHashMap.put(cls, list);
        }
        list.add(baseItemMultiTypeInterface.getItemType());
        mMultiItemTypeList.put(baseItemMultiTypeInterface.getItemType(), baseItemMultiTypeInterface);
    }


    /**
     * 只能获取到显示在屏幕上的，未显示的则不显示
     *
     * @param position
     * @param <D>
     * @return
     */
    public <D extends ViewDataBinding> D getDataBinding(int position) {
        BaseViewHolder viewHolder = getViewHolder(position);
        if (viewHolder != null) return (D) viewHolder.getBinding();
        return null;
    }


    public <T> List<T> getDataList(int... itemType) {
        if (itemType.length == 0) {
            itemType = new int[]{BaseItemTypeInterface.TYPE_CHILD};
        }
        ArrayList<T> arrayList = new ArrayList<>();
        for (int i = 0; i < mDatas.size(); i++) {
            boolean bb = false;
            for (int type : itemType) {
                if (getItemViewType(i) == type) {
                    bb = true;
                    break;
                }
            }
            if (bb) {
                arrayList.add((T) getItemBean(i));
            }
        }
        return arrayList;
    }

    public List getDatas() {
        return mDatas;
    }

    /**
     * 异步缓存几个ViewHolder  以防止首次滚动时卡顿
     *
     * @param viewType
     * @param count
     */
    public void cacheViewHolder(final int viewType, int... count) {
        final int cou;
        if (count.length == 0 || count[0] < 1) cou = 3;
        else cou = count[0];
        cacheViewHolder.put(viewType, cou);
    }


    @NonNull
    @Override
    public final BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseItemTypeInterface baseItemMultiTypeInterface = getMultiItemType(viewType);
        BaseViewHolder viewHolder = new BaseViewHolder(parent, baseItemMultiTypeInterface, this);
        if (mDraggableModule != null && isDraggable() && baseItemMultiTypeInterface.getItemType() == mDraggableItemType) {
            mDraggableModule.initView(viewHolder);
        }
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
//        MultiItemType multiItemType = getMultiItemType(getItemViewType(position));
        BaseItemTypeInterface baseItemMultiTypeInterface = holder.getBaseItemMultiTypeInterface();

        LogUtil.d(this.getClass().getSimpleName() + "  onBindViewHolder  position " + position + "  itemType: " + baseItemMultiTypeInterface.getItemType() + "   class:" + getItemBean(position).getClass().getName());

        //databing绑定数据
        holder.onBaseBindViewHolder(this, getItemBean(position));
        //MultiItemType中绑定数据
        baseItemMultiTypeInterface.onBindViewHolder(holder.getBinding(), position, getItemBean(position));
        baseItemMultiTypeInterface.onBindViewHolder(holder.getBinding(), position, holder, getItemBean(position));
    }


//    @Override
//    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position, @NonNull List<Object> payloads) {
//        if (!payloads.isEmpty()) {
//            //如果是局部刷新
//            Map payload = (Map) payloads.get(0);
//            //根据key值调用set方法
//            for (Object o : payload.keySet()) {
//                JavaMethod.setMethodSetByFieldName(getItemBean(position), o.toString(), payload.get(o.toString()));
//            }
//        } else super.onBindViewHolder(holder, position, payloads);
//    }


    public Object getItemBean(int position) {
        if (position < 0 || position >= mDatas.size()) return null;
        return mDatas.get(position);
    }


    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public int getChildCount() {
        return getItemCount();
    }

    public int getItemCountByType(int... itemType) {

        int type = BaseItemTypeInterface.TYPE_CHILD;
        if (itemType.length > 0) type = itemType[0];
        int count = 0;
        for (int i = 0; i < getItemCount(); i++) {
            if (getItemViewType(i) == type) count++;
        }

        return count;
    }

    public int getFirstPosition() {
        return 0;
    }

    /**
     * @param itemType
     * @param formIndex 从该位置起，寻找itemType的第一个位置
     * @return
     */
    public int getFirstPosition(int itemType, int formIndex) {
        if (formIndex < getItemCount()) {
            for (int i = formIndex; i < getItemCount(); i++) {
                if (getItemViewType(i) == itemType) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 去掉尾部的最后一个
     *
     * @return 有可能为-1
     */
    public int getLastPosition() {
        return getItemCount() - 1;
    }

    /**
     * 最后一个
     *
     * @return
     */
    public final int getEndPosition() {
        return getItemCount() - 1;
    }



    public BaseViewHolder getViewHolder(int position) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        View view = layoutManager.findViewByPosition(position);
        if (view == null) return null;
        return (BaseViewHolder) recyclerView.getChildViewHolder(view);
    }

    public int getPosition(View view) {
        return recyclerView.getChildAdapterPosition(view);
    }

    public void setTopPosition(int position) {
        if (getLastPosition() == -1) return;
        movePosition = position;

        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                //先移动到指定位置
                moveToPosition(movePosition);
                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        //如果滚动停止，检查是否滚动到了指定位置
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            //只检查一次，就移除掉监听效果
                            recyclerView.removeOnScrollListener(this);
                            moveToPosition(movePosition);
                        }
                    }
                });
            }
        }, 100);

    }


    private void swap(int fromPosition, int toPosition) {

    }

    /**
     * RecyclerView 移动到当前位置，
     *
     * @param position 要跳转的位置
     *                 只对LinearLayoutManager  有效
     */
    public void moveToPosition(int position) {
        if (getItemCount() == 0 || getLastPosition() < 0) return;

        if (position > getLastPosition()) position = getLastPosition();

        if (layoutManager instanceof LinearLayoutManager) {

            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            int firstItem = linearLayoutManager.findFirstVisibleItemPosition();
            int lastItem = linearLayoutManager.findLastVisibleItemPosition();

            //当要置顶的项在当前显示的第一个项的前面时
            if (position <= firstItem) {
                recyclerView.smoothScrollToPosition(position);
            }
            //当要置顶的项已经在屏幕上显示时
            else if (position < lastItem) {
                if (linearLayoutManager.getOrientation() == RecyclerView.VERTICAL) {
                    int top = recyclerView.getChildAt(position - firstItem).getTop();
                    recyclerView.smoothScrollBy(0, top);
//                    recyclerView.scrollBy(0, top);
                } else {
                    int left = recyclerView.getChildAt(position - firstItem).getLeft();
                    recyclerView.smoothScrollBy(left, 0);
//                    recyclerView.scrollBy(left, 0);
                }

            } else {
                //当要置顶的项在当前显示的最后一项的后面时
                recyclerView.smoothScrollToPosition(position);
            }
        }
    }


    @Override
    public void onViewAttachedToWindow(@NonNull BaseViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        if (isFullSpanType(holder.baseItemMultiTypeInterface)) {
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager.LayoutParams) layoutParams;
                lp.setFullSpan(true);
            }
        }
        holder.getBaseItemMultiTypeInterface().onViewAttachedToWindow(holder.getBinding(), holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull BaseViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.getBaseItemMultiTypeInterface().onViewDetachedFromWindow(holder.getBinding(), holder);
    }


    @Override
    public void onAttachedToRecyclerView(@NonNull final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
//        this.mDatas.addOnListChangedCallback(mItemsChangeCallback);

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        this.recyclerView = recyclerView;
        this.layoutManager = layoutManager;
        if (isDraggable() && mDraggableModule != null) {
            mDraggableModule.attachToRecyclerView(recyclerView);
        }


        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (isFullSpanType(getMultiItemType(getItemViewType(position)))) {
                        return gridLayoutManager.getSpanCount();
                    }
                    return 1;
                }
            });
        }

        if (isOpenDefaultAnimator) {
            openDefaultAnimator();
        } else {
            closeDefaultAnimator();
        }


        if (recycledViewPool == null)
            recycledViewPool = new RecyclerView.RecycledViewPool();

        recyclerView.setRecycledViewPool(recycledViewPool);

        if (typeFixed != 0) {
            int count = recyclerView.getItemDecorationCount();
            boolean isHave = false;
            if (count > 0) {
                for (int i = 0; i < count; i++) {
                    RecyclerView.ItemDecoration item = recyclerView.getItemDecorationAt(i);
                    if (item instanceof FixedItemDecoration) {
                        isHave = true;
                        FixedItemDecoration fixedItemDecoration = (FixedItemDecoration) item;
                        fixedItemDecoration.setTypeFixed(typeFixed);
                        break;
                    }
                }
            }

            if (!isHave) {
                FixedItemDecoration fixedItemDecoration = new FixedItemDecoration();
                fixedItemDecoration.setTypeFixed(typeFixed);
                recyclerView.addItemDecoration(fixedItemDecoration);
            }

        }

        if (cacheViewHolder.size() > 0) {
            //在recyclerView布局完成之后1秒后，开始缓存
            recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    recyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < cacheViewHolder.size(); i++) {
                                int viewType = cacheViewHolder.keyAt(i);
                                int cou = cacheViewHolder.get(viewType, 0);

                                for (int ci = 0; ci < cou; ci++) {
                                    LogUtil.d("cacheViewHolder   type " + viewType + "  pos  " + ci);
                                    recycledViewPool.putRecycledView(createViewHolder(recyclerView, viewType));
                                }

                            }
                        }
                    }, 1000);
                }
            });
        }

    }


    protected boolean isFullSpanType(BaseItemTypeInterface type) {
        return type.isFullSpanType();
    }


    /**
     * @param bean Bean已对equals 方法进行了重写  不然会找不到
     * @return true  数据源包含了这个Bean
     */
    public boolean contains(@NonNull Object bean) {
        return mDatas.contains(bean);
    }

    /**
     * @param bean Bean已对equals 方法进行了重写  不然会找不到
     * @return Bean 在数据源的位置
     */
    public int getPosition(@NonNull Object bean) {
        return mDatas.indexOf(bean);
    }

    /**
     * 更换数据源
     *
     * @param list
     * @param startPosition
     * @param endPosition
     */
    protected final void setList(List list, int startPosition, int endPosition) {
        if (list != null) {
            if (getChildCount() > 0) {
                mDatas.subList(startPosition, endPosition + 1).clear();
            }

            mDatas.addAll(startPosition, list);
//            notifyItemRangeChanged(startPosition, list.size());
            notifyDataSetChanged();
        }
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    /**
     * 更换数据源
     *
     * @param list
     */
    public void setDataList(List list) {
        setList(list, getFirstPosition(), getLastPosition());
    }

    public void setDataList(int startPos, List list) {
        setList(list, startPos, getLastPosition());
    }

    /**
     * 更新指定的项,局部更新直接更新数据源
     *
     * @param position
     * @param bean
     * @param <T>
     */
    public final <T> void update(int position, T bean) {
        if (bean == null) return;
        mDatas.set(position, bean);
        notifyItemChanged(position);
    }


    /**
     * 在指定的位置添加一项
     *
     * @param position
     * @param bean
     * @param <T>
     */
    public final <T> void add(int position, T bean) {
        if (bean == null) return;
        mDatas.add(position, bean);
        notifyItemInserted(position);
    }

    public final void addList(int position, List list) {
        if (list == null) return;
        mDatas.addAll(position, list);
        notifyDataSetChanged();
    }


    /**
     * 在末尾添加一项
     *
     * @param bean
     * @param <T>
     */
    public final <T> void add(T bean) {
        add(getLastPosition() + 1, bean);
    }

    public void move(int fromPosition, int toPosition) {
        move(fromPosition, toPosition, true);
    }

    public void move(int fromPosition, int toPosition, boolean isTop) {
        if (fromPosition > getLastPosition()) fromPosition = getLastPosition();
        if (toPosition > getLastPosition()) toPosition = getLastPosition();

        if (fromPosition != toPosition) {
            Collections.swap(mDatas, fromPosition, toPosition);
            notifyItemMoved(fromPosition, toPosition);

//            if (fromPosition != getLastPosition()) {
//                notifyItemMoved(fromPosition, toPosition);
//                notifyItemInserted(fromPosition);
//            } else {
//                notifyItemMoved(fromPosition, toPosition);
//                notifyItemRemoved(fromPosition);
//            }
//
        }


        if (isTop) {
            recyclerView.smoothScrollToPosition(0);
        }
    }

    /**
     * 移除一项
     *
     * @param position
     */
    public void remove(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    public void removeData(int position) {
        mDatas.remove(position);
    }


    public void remove(int startPosition, int toPosition) {
        if (startPosition < 0) startPosition = 0;
        if (toPosition < 0) return;
        mDatas.subList(startPosition, toPosition + 1).clear();
        notifyItemRangeRemoved(startPosition, toPosition - startPosition + 1);
    }

    /**
     * 清除多项
     *
     * @param startPosition
     * @param endPosition
     */
    public void clear(int startPosition, int endPosition) {
        if (mDatas.size() == 0 || endPosition < startPosition) return;
        notifyItemRangeRemoved(startPosition, endPosition - startPosition + 1);
        this.mDatas.subList(startPosition, endPosition + 1).clear();
    }

    /**
     * 清除所有项
     */
    public void clearChild() {
        clear(getFirstPosition(), getLastPosition());
    }

    /**
     * 清除所有项
     */
    public void clear() {
        mDatas.clear();
        notifyDataSetChanged();
    }

    /**
     * @param newItems
     * @param offset   往上偏移多少个
     */
    public final void loadMore(List newItems, int offset) {
        if (newItems == null) return;
        int size = mDatas.size();
        if (size > offset) {
            int start = size - offset;
            this.mDatas.addAll(start, newItems);
            notifyItemRangeInserted(start, size);
        }
    }

    /**
     * 加载更多
     *
     * @param newItems
     */
    public void loadMore(List newItems) {
        loadMore(newItems, 0);
    }


    /**
     * @param startPosition
     * @param newList
     * @param offset        计算末尾的数量向前偏移多少
     */
    protected final void updateRange(int startPosition, List newList, int offset) {
        if (newList == null) return;
        int count = newList.size();
        int itemCount = getItemCount() - offset;
        //需要判断是否超出原有列表，超出部分做加载到末尾

        int setCou;//更新的数量
        if (count <= itemCount - startPosition) {
            setCou = count;
        } else {
            setCou = itemCount - startPosition;
        }

        for (int i = 0; i < setCou; i++) {
            mDatas.set(startPosition + i, newList.get(i));
        }

        //超出部份添加到末尾
        if (count > setCou) {
            List footList = newList.subList(setCou, count);
            mDatas.addAll(startPosition + setCou, footList);
        }

        notifyDataSetChanged();
    }

    public void setOnItemClickInterface(OnItemClickInterface onItemClickInterface) {
        this.onItemClickInterface = onItemClickInterface;
    }

    public void setOnItemClickCallback(OnItemClickInterface onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public OnItemClickInterface getOnItemClickCallback() {
        return onItemClickCallback;
    }

    /**
     * 打开默认局部刷新动画
     */
    public void openDefaultAnimator() {
        isOpenDefaultAnimator = true;
        if (recyclerView != null && recyclerView.getItemAnimator() != null) {
            recyclerView.getItemAnimator().setAddDuration(120);
            recyclerView.getItemAnimator().setChangeDuration(250);
            recyclerView.getItemAnimator().setMoveDuration(250);
            recyclerView.getItemAnimator().setRemoveDuration(120);
            ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(true);
        }

    }

    /**
     * 关闭默认局部刷新动画
     */
    public void closeDefaultAnimator() {
        isOpenDefaultAnimator = false;
        if (recyclerView != null && recyclerView.getItemAnimator() != null) {
            recyclerView.getItemAnimator().setAddDuration(0);
            recyclerView.getItemAnimator().setChangeDuration(0);
            recyclerView.getItemAnimator().setMoveDuration(0);
            recyclerView.getItemAnimator().setRemoveDuration(0);
            ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        }
    }

    /**
     * 传入到了BaseViewHolder
     *
     * @param view        //点击的view
     * @param onclickType //点击事件类型
     * @param itemType    当前的类型
     * @param position    当前的位置
     * @param bean        当前的数据
     * @return
     */
    @Override
    public final boolean onItemClick(View view, int onclickType, int itemType, int position, Object bean) {
        if (onItemClickInterface != null) {
            boolean bb = onItemClickInterface.onItemClick(view, onclickType, itemType, position, bean);
            if (bb) return true;
        }
        boolean b1 = onItemClick(view, this, onclickType, itemType, position, bean);
        if (b1) return true;

        return false;
    }


    /**
     * adapter自已的点击事件
     *
     * @param view
     * @param baseRVAdapter
     * @param onclickType
     * @param itemType
     * @param position
     * @param obj
     * @return
     */
    public boolean onItemClick(View view, BaseRVAdapter baseRVAdapter, int onclickType, int itemType, int position, Object obj) {
        return false;
    }


    public RecyclerView.RecycledViewPool getRecycledViewPool() {
        return recycledViewPool;
    }

    public void setRecycledViewPool(RecyclerView.RecycledViewPool recycledViewPool) {
        this.recycledViewPool = recycledViewPool;
    }


    public void setViewMarginBottom(View itemView, boolean isHave, @DimenRes int... bottom) {
        @DimenRes int one = R.dimen.dp_0;
        @DimenRes int two = R.dimen.dp_0;

        if (bottom.length > 0) {
            one = bottom[0];
        }

        if (bottom.length > 1) {
            two = bottom[1];
        }
        if (isHave) {
            ViewUtils.setViewMarginBottom(itemView, DensityUtil.getDimens(one));
        } else {
            ViewUtils.setViewMarginBottom(itemView, DensityUtil.getDimens(two));
        }

    }


    //endregion
    class ListChangedCallback extends ObservableArrayList.OnListChangedCallback<ObservableArrayList> {

        @Override
        public void onChanged(ObservableArrayList newItems) {
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(ObservableArrayList newItems, int positionStart, int itemCount) {
            notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeInserted(ObservableArrayList newItems, int positionStart, int itemCount) {
            notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(ObservableArrayList newItems, int fromPosition, int toPosition, int itemCount) {
            if (itemCount == 1) {
                notifyItemMoved(fromPosition, toPosition);
            } else {
                notifyDataSetChanged();
            }
        }

        @Override
        public void onItemRangeRemoved(ObservableArrayList sender, int positionStart, int itemCount) {
            notifyItemRangeRemoved(positionStart, itemCount);
        }

    }


}
