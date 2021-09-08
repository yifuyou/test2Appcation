package com.base.common.view.base;

import android.graphics.Typeface;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.TextViewCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

import com.base.common.R;
import com.base.common.app.BaseConstant;
import com.base.common.utils.DensityUtil;
import com.base.common.utils.OnClickCheckedUtil;
import com.base.common.utils.UIUtils;
import com.base.common.utils.ViewUtils;
import com.base.common.view.adapter.ada.BaseRVAdapter;
import com.base.common.view.adapter.connector.OnTabClickListener;
import com.base.common.view.flycotablayout.CommonTabLayout;
import com.base.common.view.flycotablayout.SlidingTabLayout;
import com.base.common.view.flycotablayout.listener.CustomTabEntity;
import com.base.common.view.flycotablayout.listener.OnTabSelectListener;
import com.base.common.view.widget.imageView.GlideImageView;
import com.base.common.view.widget.statelayout.StateLayout;
import com.base.common.viewmodel.BaseViewModel;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class BaseViewUtils {

    /**
     * {@link StateLayout.OnViewRefreshListener}  的刷新接口
     */
    private StateLayout.OnViewRefreshListener onViewRefreshListener;

    /**
     * android 生命周期
     */
    private LifecycleOwner lifecycleOwner;

    private BaseViewModel viewModel;

    private StateLayout stateLayout; //当前的stateLayout

    private BaseLoadMoreUtils baseLoadMoreUtils;

    public BaseViewUtils(@NonNull LifecycleOwner lifecycleOwner, StateLayout.OnViewRefreshListener onViewRefreshListener, @NonNull BaseViewModel viewModel) {
        this.onViewRefreshListener = onViewRefreshListener;
        this.lifecycleOwner = lifecycleOwner;
        this.viewModel = viewModel;
        init();
    }


    private void init() {
        //接收登录成功事件，对StateLayout 处在login状态的 进行数据刷新
        LiveEventBus.get(BaseConstant.EventKey.USER_UPDATE_SUCCESS).observe(lifecycleOwner, new Observer<Object>() {
            @Override
            public void onChanged(Object o) {
                if (viewModel != null && stateLayout != null) {
                    if (stateLayout.getState() == StateLayout.LOGIN) {
                        onViewRefreshListener.onRefreshClick();
                    }
                }

            }
        });
    }


    /**
     * 下拉刷新和StateLayout  嵌套使用
     */
    public void setRefreshStateLayout(@NonNull View view, StateLayout.OnViewRefreshListener mListener, @NonNull OnGetDataListener onGetDataListener, boolean... enableLoadMore) {
        setStateLayout(view, mListener);
        setRefreshLayout(view, onGetDataListener, enableLoadMore);
    }

    /**
     * 设轩置状态   StateLayout
     *
     * @param view      父布局不能是相对布局和约束布局
     * @param mListener
     */
    public void setStateLayout(@NonNull View view, StateLayout.OnViewRefreshListener mListener) {

        //已添加了，不重复再添加
        if (isAddStateLayout(view)) {
            return;
        }

        stateLayout = new StateLayout(view.getContext());

        ViewGroup.LayoutParams lp = view.getLayoutParams();
        stateLayout.setLayoutParams(lp);
        view.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        stateLayout.setRefreshListener(mListener);

        ViewGroup viewGroup = (ViewGroup) view.getParent();
        int index = viewGroup.indexOfChild(view);
        viewGroup.removeViewAt(index);

        stateLayout.addView(view);
        viewGroup.addView(stateLayout, index);
    }


    //检查是否已添加了  StateLayout
    private boolean isAddStateLayout(@NonNull View view) {
        if (view.getParent() == null) {
            return false;
        } else if (view.getParent() instanceof StateLayout) {
            return true;
        } else {
            return isAddStateLayout((View) view.getParent());
        }
    }

    //检查是否已添加了  SmartRefreshLayout
    private boolean isAddSmartRefreshLayout(@NonNull View view) {
        if (view.getParent() == null) {
            return false;
        } else if (view.getParent() instanceof SmartRefreshLayout) {
            return true;
        } else {
            return isAddSmartRefreshLayout((View) view.getParent());
        }
    }

    public StateLayout getStateLayout() {
        return stateLayout;
    }


    //重置加载状态  ps:和下拉刷新合用时，只会有加载状态一次，当需要再次加载时
    public void resetLoadingState() {
        if (stateLayout != null) {
            stateLayout.resetLoadingState();
        }
    }

    /**
     * 设置下拉刷新和上拉加载
     *
     * @param view 父布局不能是相对布局和约束布局,因为view移除后相对该控件的控件会因找不到位置，而布局错乱
     */

    public final void setRefreshLayout(@NonNull View view, @NonNull OnGetDataListener onGetDataListener, boolean... enableLoadMore) {

        //已添加了，不重复再添加
        if (isAddSmartRefreshLayout(view)) {
            return;
        }
        SmartRefreshLayout smartRefreshLayout = new SmartRefreshLayout(view.getContext());

        if (baseLoadMoreUtils == null)
            baseLoadMoreUtils = new BaseLoadMoreUtils(onGetDataListener, smartRefreshLayout, enableLoadMore);

        ViewGroup.LayoutParams lp = view.getLayoutParams();

        smartRefreshLayout.setLayoutParams(lp);
        view.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));

        ViewGroup viewGroup = (ViewGroup) view.getParent();
        int index = viewGroup.indexOfChild(view);
        viewGroup.removeViewAt(index);
        smartRefreshLayout.addView(view);
        viewGroup.addView(smartRefreshLayout, index);
        if (enableLoadMore.length == 0) {
            smartRefreshLayout.setEnableLoadMore(true);
        } else {
            smartRefreshLayout.setEnableLoadMore(enableLoadMore[0]);
        }

        smartRefreshLayout.setEnableAutoLoadMore(false);

        smartRefreshLayout.setOnRefreshLoadMoreListener(baseLoadMoreUtils);

    }

    /**
     * 是否启用下拉刷新（默认启用）
     *
     * @param enabled 是否启用
     * @return SmartRefreshLayout
     */
    public void setRefreshEnable(boolean enabled) {
        if (baseLoadMoreUtils != null) {
            baseLoadMoreUtils.setEnableRefresh(enabled);
        }
    }

    /**
     * Set whether to enable pull-up loading more (enabled by default).
     * 设置是否启用上拉加载更多（默认启用）
     *
     * @param enabled 是否启用
     * @return RefreshLayout
     */
    public void setEnableLoadMore(boolean enabled) {
        if (baseLoadMoreUtils != null) {
            baseLoadMoreUtils.setEnableLoadMore(enabled);
        }
    }

    /**
     * 是否启动预加载 默认不启动预加载
     *
     * @param autoPreload
     */
    public void setAutoPreload(boolean autoPreload) {
        if (baseLoadMoreUtils != null) {
            baseLoadMoreUtils.setAutoPreload(autoPreload);
        }
    }


    /**
     * 设置下拉刷新和上拉加载数据
     */
    public void setDataListRefreshLayout(BaseRVAdapter baseRVAdapter, int pn, int ps, List list) {
        if (baseLoadMoreUtils != null) {
//            stopRefreshLayout();
            baseLoadMoreUtils.setDataListRefreshLayout(baseRVAdapter, pn, ps, list);
        }
    }

    //刷新某一位置的分页
    public void update(int position) {
        if (baseLoadMoreUtils != null) {
            baseLoadMoreUtils.update(position);
        }
    }


    /**
     * 关闭下拉刷新或加载更多
     */
    public void stopRefreshLayout() {
        if (baseLoadMoreUtils != null) {
            baseLoadMoreUtils.stopRefreshLayout();
        }
    }

    /**
     * 是否是预加载数据
     *
     * @return
     */
    public boolean isPreload() {
        if (baseLoadMoreUtils != null) {
            return baseLoadMoreUtils.isPreload();
        }
        return false;
    }

    public void initCommonTabLayout(CommonTabLayout tabLayout, ViewPager viewPager, boolean autoTextSize, int... position) {
        int pos = position.length == 0 ? 0 : position[0];
        tabLayout.setCurrentTab(pos);
        viewPager.setCurrentItem(pos);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        if (autoTextSize) {
            int count = tabLayout.getTabCount();
            for (int i = 0; i < count; i++) {
                TextView textViewAuto = tabLayout.getTitleView(i);
                initAutoSizeText(textViewAuto);
            }
        }

    }


    public void initCommonTabLayout(CommonTabLayout tabLayout, OnTabClickListener onTabClickListener, float selectSize, float unSelectSize, int... position) {
        if (tabLayout == null) return;
        int pos = position.length == 0 ? 0 : position[0];
        tabLayout.setCurrentTab(pos);
        if (tabLayout.getTextBold() == 1) {
            TextView textView = tabLayout.getTitleView(pos);
            textView.setTextSize(selectSize);
            textView.setTypeface(Typeface.DEFAULT_BOLD);
        }

        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                for (int i = 0; i < tabLayout.getTabCount(); i++) {
                    TextView textView = tabLayout.getTitleView(i);
                    if (i == position) {
                        textView.setTextSize(selectSize);
                        if (tabLayout.getTextBold() == 1) {
                            textView.setTypeface(Typeface.DEFAULT_BOLD);
                        }

                    } else {
                        textView.setTextSize(unSelectSize);
                        if (tabLayout.getTextBold() == 1) {
                            textView.setTypeface(Typeface.DEFAULT);
                        }

                    }
                }

                if (onTabClickListener != null) {
                    onTabClickListener.onTabClick(position);
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

//        if (autoTextSize) {
//            int count = tabLayout.getTabCount();
//            for (int i = 0; i < count; i++) {
//                TextView textViewAuto = tabLayout.getTitleView(i);
//                initAutoSizeText(textViewAuto);
//            }
//        }

    }


    /**
     * 选中时字体加粗
     *
     * @param tabLayout
     * @param viewPager
     * @param position
     */
    public void initSlidingTabLayout(SlidingTabLayout tabLayout, ViewPager viewPager, boolean autoTextSize, int... position) {
        int pos = position.length == 0 ? 0 : position[0];
        tabLayout.setViewPager(viewPager);
        tabLayout.setCurrentTab(pos);

        if (pos == 0) {
            TextView textView = tabLayout.getTitleView(0);
            if (textView != null) {
                textView.setTypeface(Typeface.DEFAULT_BOLD);
            }
        }

        if (autoTextSize) {
            int count = tabLayout.getTabCount();
            for (int i = 0; i < count; i++) {
                TextView textViewAuto = tabLayout.getTitleView(i);
                initAutoSizeText(textViewAuto);
            }
        }

    }


    public void initAutoSizeText(TextView textView) {
        if (textView == null) return;
        int textSize = (int) textView.getTextSize();
        int minTextSize = DensityUtil.getDimens(R.dimen.font_9);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(textView, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(textView, minTextSize, textSize, 1, TypedValue.COMPLEX_UNIT_PX);
    }


    public void initPwdEditText(EditText editText, GlideImageView icImage, boolean isPlaintext) {
        if (editText != null && icImage != null) {
            icImage.setSelected(isPlaintext);
            icImage.setOnClickListener(new OnClickCheckedUtil() {
                @Override
                public void onClicked(View view) {
                    view.setSelected(!view.isSelected());
                    if (view.isSelected()) {
                        // 明文
                        editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    } else {
                        // 显示密文
                        editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    }
                }
            });

        }
    }


    /**
     * 实现 SlidingTabLayout  选中时字体加粗放大
     *
     * @param tabLayout
     * @param viewPager    在tabLayoutViewpager.setAdapter  后调用该方法
     * @param selectSize   先中时字体大小
     * @param unSelectSize 未选中时字体大小
     * @param position     默认翻页的位置
     */
    public void initSlidingTabLayout(SlidingTabLayout tabLayout, ViewPager viewPager, boolean autoTextSize, float selectSize, float unSelectSize, int... position) {
        int pos = position.length == 0 ? 0 : position[0];
        tabLayout.setViewPager(viewPager);
        tabLayout.setCurrentTab(pos);

        TextView textView = tabLayout.getTitleView(pos);
        textView.setTextSize(selectSize);
        textView.setTypeface(Typeface.DEFAULT_BOLD);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < tabLayout.getTabCount(); i++) {
                    TextView textView = tabLayout.getTitleView(i);
                    if (i == position) {
                        textView.setTextSize(selectSize);
                        textView.setTypeface(Typeface.DEFAULT_BOLD);
                    } else {
                        textView.setTextSize(unSelectSize);
                        textView.setTypeface(Typeface.DEFAULT);
                    }

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

//        if (autoTextSize) {
//            int count = tabLayout.getTabCount();
//            for (int i = 0; i < count; i++) {
//                TextView textViewAuto = tabLayout.getTitleView(pos);
//                initAutoSizeText(textViewAuto);
//            }
//        }


    }


    public BaseLoadMoreUtils getBaseLoadMoreUtils() {
        return baseLoadMoreUtils;
    }


    public boolean onBackPressed() {
        if (OnClickCheckedUtil.onClickChecked(2000)) {
            UIUtils.showToastSafes("再按一次退出程序");
            return false;
        } else {
            return true;
            //1,杀死自己进程的方法
//            android.os.Process.killProcess(android.os.Process.myPid());  //获取PID}
        }
    }
}
