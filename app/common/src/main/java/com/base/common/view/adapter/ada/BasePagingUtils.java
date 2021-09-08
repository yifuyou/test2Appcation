package com.base.common.view.adapter.ada;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.base.common.utils.LogUtil;
import com.base.common.utils.UIUtils;
import com.base.common.view.adapter.ada.BaseRVAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 手动分页面 工具
 */
public class BasePagingUtils {

    private List list;
    private int pageNo = 1;

    private int ps;

    public BasePagingUtils() {
    }

    public void setDataList(List list, int ps) {
        this.list = list;
        this.ps = ps;
    }


    public List getData(int pn) {
        if (UIUtils.isNotEmpty(list)) {
            int start = (pn - 1) * ps;
            int end = pn * ps;
            if (start >= list.size()) start = list.size() - 1;
            if (end >= list.size()) end = list.size() - 1;

            if (start < end) {
                pageNo = pn;
                return list.subList(start, end);
            }

        }

        return new ArrayList<>();
    }

}
