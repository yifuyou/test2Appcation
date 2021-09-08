package com.base.common.utils;


import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;

import com.base.common.view.base.BaseActivity;

/**
 * 导航提示页面的工具
 */
public class HelpViewUtils {

    /**
     * @param activity  要显示的activity
     * @param spKeyName 判断是否显示的sp字段
     * @param btnId     添加点击事件的id
     * @param layRes    要添加的布局
     */
    public static void show(BaseActivity activity, String spKeyName, @IdRes int btnId, @LayoutRes int... layRes) {
        if (layRes.length > 0) {

//            SPUtils.putBoolean(spKeyName, true);


            boolean bb = SPUtils.getBoolean(spKeyName, true);
            if (bb) {
                show(activity, spKeyName, 0, btnId, layRes);
            }
        }
    }

    /**
     * @param activity
     * @param pos      显示第几个
     * @param layRes
     */
    private static void show(BaseActivity activity, String spKeyName, int pos, @IdRes int btnId, @LayoutRes int... layRes) {
        if (pos < layRes.length) {
            View viewRoot = ViewUtils.getLayoutViewMatch(activity, layRes[pos]);
            activity.getRootViewGroup().addView(viewRoot);
            View tvOK = viewRoot.findViewById(btnId);
            if (tvOK != null) {
                tvOK.setOnClickListener(new OnClickCheckedUtil() {
                    @Override
                    public void onClicked(View view) {
                        ViewUtils.removeSelfFromParent(viewRoot);
                        //最后一个了
                        if (pos + 1 == layRes.length) {
                            SPUtils.putBoolean(spKeyName, false);
                        } else {
                            show(activity, spKeyName, pos + 1, btnId, layRes);
                        }
                    }
                });
            }
        }
    }


}
