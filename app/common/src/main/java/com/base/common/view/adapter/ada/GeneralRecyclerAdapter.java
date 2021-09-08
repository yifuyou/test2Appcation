package com.base.common.view.adapter.ada;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.base.common.view.adapter.bean.ChildBaseBean;
import com.base.common.view.adapter.bean.FooterBean;
import com.base.common.view.adapter.bean.HeaderBean;
import com.base.common.view.adapter.connector.BaseItemTypeInterface;
import com.base.common.view.adapter.splite_line.ItemSlideHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用的适配器
 * 1，添加了侧滑的功能
 * 2，子项需要继承继承 {@link com.base.common.view.adapter.bean.ChildBaseBean} 则可以使用
 */
public abstract class GeneralRecyclerAdapter extends BaseAddHeadAndFootAdapter {

    private boolean sideslip = false;//是否侧滑

    private boolean isRadio = false;  //是否是单项选择

    public void setRadio(boolean radio) {
        isRadio = radio;
    }

    //是否是左滑菜单
    public void setSideslip(boolean sideslip) {
        this.sideslip = sideslip;
    }


    public GeneralRecyclerAdapter() {

    }

    public GeneralRecyclerAdapter(LifecycleOwner lifecycleOwner) {
        super(lifecycleOwner);
    }

    /**
     * 对侧滑菜单进行初始化
     *
     * @param recyclerView
     */
    @Override
    public void onAttachedToRecyclerView(@NonNull final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        //侧滑菜单
        if (sideslip) {
            recyclerView.addOnItemTouchListener(new ItemSlideHelper(recyclerView.getContext(), new ItemSlideHelper.Callback() {
                public int getHorizontalRange(RecyclerView.ViewHolder holder) {
                    if (holder.itemView instanceof LinearLayout) {
                        ViewGroup viewGroup = (ViewGroup) holder.itemView;
                        if (viewGroup.getChildCount() == 2) {
                            return viewGroup.getChildAt(1).getLayoutParams().width;
                        }
                    }
                    return 0;
                }

                /**
                 * @param childView 未显示的是获取不到的
                 * @return
                 */
                public RecyclerView.ViewHolder getChildViewHolder(View childView) {
                    return recyclerView.getChildViewHolder(childView);
                }

                public View findTargetView(float x, float y) {
                    return recyclerView.findChildViewUnder(x, y);
                }
            }));
        }

    }

    /**
     * 获取当前选中的数量
     *
     * @param itemType 如果不为空则获取指定类型的选中的数量，如果为空则获取{@link BaseItemTypeInterface#TYPE_CHILD }
     * @return
     */
    public int getSelectedCount(int... itemType) {
        int type = BaseItemTypeInterface.TYPE_CHILD;
        if (itemType.length > 0) type = itemType[0];
        int count = 0;
        for (int i = 0; i < getItemCount(); i++) {
            if (getItemViewType(i) == type && getItemBean(i) instanceof ChildBaseBean) {
                ChildBaseBean baseBean = (ChildBaseBean) getItemBean(i);
                if (baseBean.getSelected()) {
                    count++;
                }

            }
        }
        return count;
    }


    public int getSelectPosition(int... itemType) {
        if (itemType.length == 0) {
            itemType = new int[1];
            itemType[0] = BaseItemTypeInterface.TYPE_CHILD;
        }

        for (int i = 0; i < getItemCount(); i++) {
            if (getItemBean(i) instanceof ChildBaseBean) {
                boolean isCheckedType = false;
                int itemTT = getItemViewType(i);
                for (int type : itemType) {
                    if (itemTT == type) {
                        isCheckedType = true;
                        break;
                    }
                }
                if (!isCheckedType) continue;
                ChildBaseBean baseBean = (ChildBaseBean) getItemBean(i);
                if (baseBean.getSelected()) {
                    return i;
                }

            }
        }
        return -1;
    }


    public List getSelectedList(int... itemType) {
        int type = BaseItemTypeInterface.TYPE_CHILD;
        if (itemType.length > 0) type = itemType[0];
        List list = new ArrayList();
        for (int i = 0; i < getItemCount(); i++) {
            if (getItemViewType(i) == type && getItemBean(i) instanceof ChildBaseBean) {
                ChildBaseBean baseBean = (ChildBaseBean) getItemBean(i);
                if (baseBean.getSelected()) {
                    list.add(baseBean);
                }
            }
        }
        return list;
    }


    public <T> T getSelectBean(int... itemType) {
        int type = BaseItemTypeInterface.TYPE_CHILD;
        if (itemType.length > 0) type = itemType[0];

        for (int i = 0; i < getItemCount(); i++) {
            if (getItemViewType(i) == type && getItemBean(i) instanceof ChildBaseBean) {
                ChildBaseBean baseBean = (ChildBaseBean) getItemBean(i);
                if (baseBean.getSelected()) {
                    return (T) baseBean;
                }
            }
        }

        return null;
    }

    public <T> T getCheckedBean(int... itemType) {
        int type = BaseItemTypeInterface.TYPE_CHILD;
        if (itemType.length > 0) type = itemType[0];

        for (int i = 0; i < getItemCount(); i++) {
            if (getItemViewType(i) == type && getItemBean(i) instanceof ChildBaseBean) {
                ChildBaseBean baseBean = (ChildBaseBean) getItemBean(i);
                if (baseBean.getChecked()) {
                    return (T) baseBean;
                }
            }
        }

        return null;
    }


    public void setSelectPosition(int position, boolean select, int... itemType) {
        if (select) {
            setSelectPosition(position, itemType);
        } else {
            if (getItemBean(position) instanceof ChildBaseBean) {
                ((ChildBaseBean) getItemBean(position)).setSelected(false);
            }
        }
    }


    /**
     * 设置选中
     *
     * @param itemType 如果是单先且不为空则这种添型只能选中一个,如果为空则只能选中一个{@link BaseItemTypeInterface#TYPE_CHILD}
     * @param position
     */
    public void setSelectPosition(int position, int... itemType) {
        if (itemType.length == 0) {
            itemType = new int[1];
            itemType[0] = BaseItemTypeInterface.TYPE_CHILD;
        }

        //设置当前选中
        if (getItemBean(position) instanceof ChildBaseBean) {
            ((ChildBaseBean) getItemBean(position)).setSelected(true);
        }

        if (isRadio) {
            for (int i = 0; i < getItemCount(); i++) {
                if (i == position) continue;
                if (getItemBean(i) instanceof ChildBaseBean) {
                    boolean isCheckedType = false;
                    int itemTT = getItemViewType(i);
                    for (int type : itemType) {
                        if (itemTT == type) {
                            isCheckedType = true;
                            break;
                        }
                    }
                    if (!isCheckedType) continue;
                    ChildBaseBean baseBean = (ChildBaseBean) getItemBean(i);
                    if (baseBean.getSelected()) {
                        baseBean.setSelected(false);
                    }
                }
            }
        }
    }

    /**
     * @param isSelect true 选中
     * @param itemType 选中的类型，未设置默认TYPE_CHILD
     * @return 选中或取消的数量
     */
    public int setSelectAll(boolean isSelect, int... itemType) {
        if (isRadio) return 0;
        int type = BaseItemTypeInterface.TYPE_CHILD;
        if (itemType.length > 0) type = itemType[0];
        int count = 0;
        for (int i = 0; i < getItemCount(); i++) {
            if (getItemViewType(i) == type && getItemBean(i) instanceof ChildBaseBean) {
                ChildBaseBean baseBean = (ChildBaseBean) getItemBean(i);
                baseBean.setSelected(isSelect);
                count++;
            }
        }
        return count;
    }


    /**
     * 设置当前类型是否选中，可以设置不选中
     *
     * @param position
     * @param isSelect
     * @param itemType
     */
    public void setSelectPositionByUpdate(int position, boolean isSelect, int... itemType) {

        if (getItemBean(position) instanceof ChildBaseBean) {
            ((ChildBaseBean) getItemBean(position)).setSelected(isSelect);
            notifyItemChanged(position);
        }

        if (isSelect) {

            if (itemType.length == 0) {
                itemType = new int[1];
                itemType[0] = BaseItemTypeInterface.TYPE_CHILD;
            }


            //设置当前选中
            if (isRadio) {
                for (int i = 0; i < getItemCount(); i++) {
                    if (i == position) continue;
                    if (getItemBean(i) instanceof ChildBaseBean) {
                        boolean isCheckedType = false;
                        int itemTT = getItemViewType(i);
                        for (int type : itemType) {
                            if (itemTT == type) {
                                isCheckedType = true;
                                break;
                            }
                        }
                        if (!isCheckedType) continue;
                        ChildBaseBean baseBean = (ChildBaseBean) getItemBean(i);
                        if (baseBean.getSelected()) {
                            baseBean.setSelected(false);
                            notifyItemChanged(i);
                        }
                    }
                }
            }
        }
    }


    public void setPositionState(int position, int state) {
        if (getItemBean(position) instanceof ChildBaseBean) {
            ((ChildBaseBean) getItemBean(position)).setState(state);
        } else if (getItemBean(position) instanceof HeaderBean) {
            ((HeaderBean) getItemBean(position)).setState(state);
        } else if (getItemBean(position) instanceof FooterBean) {
            ((FooterBean) getItemBean(position)).setState(state);
        }
    }

    public void setCheckedPosition(int position, int... itemType) {
        int type = BaseItemTypeInterface.TYPE_CHILD;
        if (itemType.length > 0) type = itemType[0];

        //设置当前选中
        if (getItemBean(position) instanceof ChildBaseBean) {
            ((ChildBaseBean) getItemBean(position)).setChecked(true);
        }

        if (isRadio) {
            for (int i = 0; i < getItemCount(); i++) {
                if (i == position) continue;
                if (getItemViewType(i) == type && getItemBean(i) instanceof ChildBaseBean) {
                    ChildBaseBean baseBean = (ChildBaseBean) getItemBean(i);
                    if (baseBean.getChecked()) {
                        baseBean.setChecked(false);
                    }
                }
            }
        }
    }


}
