package com.base.common.view.adapter.ada;

import com.base.common.view.adapter.connector.BaseItemTypeInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * 一个分组的adapter 主要用于数据源是分组包含子项的数据源
 *
 * @param <G> {@link BaseItemTypeInterface#TYPE_GROUP}
 * @param <C> {@link BaseItemTypeInterface#TYPE_CHILD}
 */
public abstract class GroupAdapter<G, C> extends GeneralRecyclerAdapter {


    /**
     * 获取分组的子项数据源
     *
     * @param gBean
     * @return
     */
    public abstract List<C> getChildListFromGroup(G gBean);

    /**
     * 判断是否是同一个分组
     *
     * @return
     */
    public abstract boolean isSameGroup(G g1, G g2);

    /**
     * 更换数据源
     *
     * @param list 分组的数据源
     */
    @Override
    public void setDataList(List list) {
        super.setDataList(iniList(list, true));
    }

    /**
     * 加载更多
     *
     * @param newItems 分组 的数据源
     */
    @Override
    public void loadMore(List newItems) {
        super.loadMore(iniList(newItems, false));
    }

    /**
     * @param listGroup 分组数据源
     * @param isRefresh 是否是刷新  如果是刷新则完全返回数据源
     * @return
     */

    private List iniList(List listGroup, boolean isRefresh) {
        List dataList = new ArrayList();
        if (listGroup != null && !listGroup.isEmpty()) {
            //如果不是上一个分组的数据则要添加第一个分组数据
            G g = (G) listGroup.get(0);
            if (isRefresh || !isLastGroupData(listGroup)) {
                dataList.add(g);
            }
            //添加第一个的子项
            dataList.addAll(getChildListFromGroup(g));

            //添加后续
            for (int i = 1; i < listGroup.size(); i++) {
                //先添加头部
                G g1 = (G) listGroup.get(i);
                dataList.add(g1);
                dataList.addAll(getChildListFromGroup(g1));
            }

        }
        return dataList;
    }


    /**
     * 是否是上一个分组的数据源
     *
     * @return
     */
    private boolean isLastGroupData(List listGroup) {
        //首先判断新进的数据源第一个和本来的数据源是否是同一个分组
        int posLast = getLastGroupPosition();
        if (posLast == -1) {
            return false;
        }
        G g1 = (G) listGroup.get(0);
        G g2 = (G) getItemBean(posLast);
        return isSameGroup(g1, g2);
    }

    /**
     * 获取上一个的分组位置
     *
     * @return
     */
    private int getLastGroupPosition() {
        if (getChildCount() == 0) return -1;
        for (int itemCount = getItemCount() - 1; itemCount >= 0; itemCount--) {
            if (getItemViewType(itemCount) == BaseItemTypeInterface.TYPE_GROUP) {
                return itemCount;
            }
        }
        return -1;
    }

    public int getGroupPosition(int position) {
        if (getChildCount() == 0 || position < 0) return -1;
        if (position >= getItemCount()) {
            position = getLastPosition();
        }

        for (int i = position; i >= 0; i--) {
            if (getItemViewType(i) == BaseItemTypeInterface.TYPE_GROUP) {
                return i;
            }
        }

        return -1;
    }


    @Override
    public void remove(int position) {
        int itemType = getItemViewType(position);
        if (itemType == BaseItemTypeInterface.TYPE_GROUP) {

        } else {
            int posG = getGroupPosition(position);
            if (posG > -1) {
                List<C> listCh = getChildListFromGroup((G) getItemBean(posG));
                Object obj = getItemBean(position);
                listCh.remove(obj);
            }
            super.remove(position);
        }

    }


    @Override
    public void remove(int startPosition, int toPosition) {
        super.remove(startPosition, toPosition);

    }


}
