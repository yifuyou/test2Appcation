package com.base.common.view.widget.statelayout;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


import androidx.annotation.DimenRes;

import com.base.common.R;
import com.base.common.utils.UIUtils;
import com.base.common.view.widget.statelayout.anim.ViewAnimProvider;
import com.base.common.view.widget.statelayout.bean.ItemInfo;
import com.base.common.view.widget.statelayout.helper.AnimationHelper;
import com.base.common.view.widget.statelayout.helper.LayoutHelper;
import com.base.common.view.widget.statelayout.holder.ItemViewHolder;
import com.base.common.view.widget.statelayout.holder.LoadingViewHolder;


/**
 * 状态布局
 */

public class StateLayout extends FrameLayout {

    public static final int SUCCESS = 0;
    public static final int ERROR = 1;
    public static final int EMPTY = 2;
    public static final int TIMEOUT = 3;
    public static final int NOT_NETWORK = 4;
    public static final int LOADING = 5;
    public static final int LOGIN = 6;

    private View contentView;
    private View emptyView;
    private View errorView;
    private View loadingView;
    private View timeOutView;
    private View notNetworkView;
    private View loginView;

    private ItemInfo errorItem;
    private ItemInfo noNetworkItem;
    private ItemInfo emptyItem;
    private ItemInfo loadingItem;
    private ItemInfo timeOutItem;
    private ItemInfo loginItem;

    private OnViewRefreshListener mListener;

    private ViewAnimProvider viewAnimProvider;
    private boolean useAnimation = false;

    private boolean isRefreshLayout = false;//是否和下拉刷新合用
    private boolean isShowLoadingLayout = false;//是否已显示过正在加载页面

    private int state = SUCCESS;//当前 StateLayout所处的状态

    public int getState() {
        return state;
    }

    //重置加载状态
    public void resetLoadingState() {
        isShowLoadingLayout = false;
    }


    public OnViewRefreshListener getRefreshLListener() {
        return mListener;
    }

    public StateLayout setRefreshListener(OnViewRefreshListener listener) {
        this.mListener = listener;
        return this;
    }

    public void setRefreshLayout(boolean refreshLayout) {
        isRefreshLayout = refreshLayout;
    }

    public void setErrorItem(ItemInfo errorItem) {
        this.errorItem = errorItem;
    }

    public void setNoNetworkItem(ItemInfo noNetworkItem) {
        this.noNetworkItem = noNetworkItem;
    }

    public void setEmptyItem(ItemInfo emptyItem) {
        this.emptyItem = emptyItem;
    }

    public void setLoadingItem(ItemInfo loadingItem) {
        this.loadingItem = loadingItem;
    }

    public void setTimeOutItem(ItemInfo timeOutItem) {
        this.timeOutItem = timeOutItem;
    }

    public void setLoginItem(ItemInfo loginItem) {
        this.loginItem = loginItem;
    }

    private View currentShowingView;

    public StateLayout(Context context) {
        this(context, null);
    }

    public StateLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public StateLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutHelper.parseAttr(context, attrs, this);

        LayoutInflater inflater = LayoutInflater.from(context);

        errorView = LayoutHelper.getErrorView(inflater, errorItem, this);

        emptyView = LayoutHelper.getEmptyView(inflater, emptyItem, this);

        notNetworkView = LayoutHelper.getNoNetworkView(inflater, noNetworkItem, this);

        timeOutView = LayoutHelper.getTimeOutView(inflater, timeOutItem, this);

        loadingView = LayoutHelper.getLoadingView(inflater, loadingItem);

        loginView = LayoutHelper.getLoginView(inflater, loginItem, this);
    }

    private void checkIsContentView(View view) {
        if (view != errorView && view != notNetworkView
                && view != loadingView && view != timeOutView && view != loginView
                && view != emptyView) {
            contentView = view;
            currentShowingView = contentView;
        }
    }

    /**
     * 展示错误的界面
     */
    public void showErrorView() {
        state = ERROR;
        if (errorView.getParent() == null) {
            addView(errorView);
        }
        AnimationHelper.switchViewByAnim(useAnimation, viewAnimProvider, currentShowingView, errorView);
        currentShowingView = errorView;
    }

    /**
     * 展示错误的界面
     *
     * @param msgId 提示语
     */
    public void showErrorView(int msgId) {
        if (errorView.getParent() == null) {
            addView(errorView);
        }
        setTipText(ERROR, msgId);
        AnimationHelper.switchViewByAnim(useAnimation, viewAnimProvider, currentShowingView, errorView);
        currentShowingView = errorView;
    }

    /**
     * 展示错误的界面
     *
     * @param msg 提示语
     */
    public void showErrorView(String msg) {
        if (errorView.getParent() == null) {
            addView(errorView);
        }
        setTipText(ERROR, msg);
        AnimationHelper.switchViewByAnim(useAnimation, viewAnimProvider, currentShowingView, errorView);
        currentShowingView = errorView;
    }

    /**
     * 展示错误的界面
     *
     * @param msgId 提示语
     * @param imgId 图片Id
     */
    public void showErrorView(int msgId, int imgId) {
        if (errorView.getParent() == null) {
            addView(errorView);
        }
        setTipText(ERROR, msgId);
        setTipImg(ERROR, imgId);
        AnimationHelper.switchViewByAnim(useAnimation, viewAnimProvider, currentShowingView, errorView);
        currentShowingView = errorView;
    }

    /**
     * 展示错误的界面
     *
     * @param msg   提示语
     * @param imgId 图片Id
     */
    public void showErrorView(String msg, int imgId) {
        if (errorView.getParent() == null) {
            addView(errorView);
        }
        setTipText(ERROR, msg);
        setTipImg(ERROR, imgId);
        AnimationHelper.switchViewByAnim(useAnimation, viewAnimProvider, currentShowingView, errorView);
        currentShowingView = errorView;
    }

    /**
     * 展示空数据的界面
     */
    public void showEmptyView() {
        state = EMPTY;
        if (emptyItem != null) {
            showEmptyView(emptyItem.getTip(), emptyItem.getResId());
            return;
        }
        if (emptyView.getParent() == null) {
            addView(emptyView);
        }
        AnimationHelper.switchViewByAnim(useAnimation, viewAnimProvider, currentShowingView, emptyView);
        currentShowingView = emptyView;
    }

    /**
     * 展示空数据的界面
     *
     * @param msgId 提示语
     */
    public void showEmptyView(int msgId) {
        if (emptyView.getParent() == null) {
            addView(emptyView);
        }
        setTipText(EMPTY, msgId);
        AnimationHelper.switchViewByAnim(useAnimation, viewAnimProvider, currentShowingView, emptyView);
        currentShowingView = emptyView;
    }

    /**
     * 展示空数据的界面
     *
     * @param msg 提示语
     */
    public void showEmptyView(CharSequence msg) {
        if (emptyView.getParent() == null) {
            addView(emptyView);
        }
        setTipText(EMPTY, msg);
        AnimationHelper.switchViewByAnim(useAnimation, viewAnimProvider, currentShowingView, emptyView);
        currentShowingView = emptyView;
    }

    /**
     * 展示空数据的界面
     *
     * @param msgId 提示语
     * @param imgId 图片Id
     */
    public void showEmptyView(int msgId, int imgId) {
        if (emptyView.getParent() == null) {
            addView(emptyView);
        }
        setTipText(EMPTY, msgId);
        setTipImg(EMPTY, imgId);
        AnimationHelper.switchViewByAnim(useAnimation, viewAnimProvider, currentShowingView, emptyView);
        currentShowingView = emptyView;
    }

    /**
     * 展示空数据的界面
     *
     * @param msg   提示语
     * @param imgId 图片Id
     */
    public void showEmptyView(String msg, int imgId) {
        if (emptyView.getParent() == null) {
            addView(emptyView);
        }
        setTipText(EMPTY, msg);
        setTipImg(EMPTY, imgId);
        AnimationHelper.switchViewByAnim(useAnimation, viewAnimProvider, currentShowingView, emptyView);
        currentShowingView = emptyView;
    }

    /**
     * 展示超时的界面
     */
    public void showTimeoutView() {
        if (timeOutView.getParent() == null) {
            addView(timeOutView);
        }
        AnimationHelper.switchViewByAnim(useAnimation, viewAnimProvider, currentShowingView, timeOutView);
        currentShowingView = timeOutView;
    }

    /**
     * 展示超时的界面
     *
     * @param msgId 提示语
     */
    public void showTimeoutView(int msgId) {
        if (timeOutView.getParent() == null) {
            addView(timeOutView);
        }
        setTipText(TIMEOUT, msgId);
        AnimationHelper.switchViewByAnim(useAnimation, viewAnimProvider, currentShowingView, timeOutView);
        currentShowingView = timeOutView;
    }

    /**
     * 展示超时的界面
     *
     * @param msg 提示语
     */
    public void showTimeoutView(String msg) {
        if (timeOutView.getParent() == null) {
            addView(timeOutView);
        }
        setTipText(TIMEOUT, msg);
        AnimationHelper.switchViewByAnim(useAnimation, viewAnimProvider, currentShowingView, timeOutView);
        currentShowingView = timeOutView;
    }

    /**
     * 展示超时的界面
     *
     * @param msgId 提示语
     * @param imgId 图片Id
     */
    public void showTimeoutView(int msgId, int imgId) {
        if (timeOutView.getParent() == null) {
            addView(timeOutView);
        }
        setTipText(TIMEOUT, msgId);
        setTipImg(TIMEOUT, imgId);
        AnimationHelper.switchViewByAnim(useAnimation, viewAnimProvider, currentShowingView, timeOutView);
        currentShowingView = timeOutView;
    }

    /**
     * 展示超时的界面
     *
     * @param msg   提示语
     * @param imgId 图片Id
     */
    public void showTimeoutView(String msg, int imgId) {
        if (timeOutView.getParent() == null) {
            addView(timeOutView);
        }
        setTipText(TIMEOUT, msg);
        setTipImg(TIMEOUT, imgId);
        AnimationHelper.switchViewByAnim(useAnimation, viewAnimProvider, currentShowingView, timeOutView);
        currentShowingView = timeOutView;
    }

    /**
     * 展示没有网络的界面
     */
    public void showNoNetworkView() {
        state = NOT_NETWORK;
        if (notNetworkView.getParent() == null) {
            addView(notNetworkView);
        }
        AnimationHelper.switchViewByAnim(useAnimation, viewAnimProvider, currentShowingView, notNetworkView);
        currentShowingView = notNetworkView;
    }

    /**
     * 展示没有网络的界面
     *
     * @param msgId 提示语
     */
    public void showNoNetworkView(int msgId) {
        if (notNetworkView.getParent() == null) {
            addView(notNetworkView);
        }
        setTipText(NOT_NETWORK, msgId);
        AnimationHelper.switchViewByAnim(useAnimation, viewAnimProvider, currentShowingView, notNetworkView);
        currentShowingView = notNetworkView;
    }

    /**
     * 展示没有网络的界面
     *
     * @param msg 提示语
     */
    public void showNoNetworkView(String msg) {
        if (notNetworkView.getParent() == null) {
            addView(notNetworkView);
        }
        setTipText(NOT_NETWORK, msg);
        AnimationHelper.switchViewByAnim(useAnimation, viewAnimProvider, currentShowingView, notNetworkView);
        currentShowingView = notNetworkView;
    }

    /**
     * 展示没有网络的界面
     *
     * @param msgId 提示语
     * @param imgId 图片Id
     */
    public void showNoNetworkView(int msgId, int imgId) {
        if (notNetworkView.getParent() == null) {
            addView(notNetworkView);
        }
        setTipText(NOT_NETWORK, msgId);
        setTipImg(NOT_NETWORK, imgId);
        AnimationHelper.switchViewByAnim(useAnimation, viewAnimProvider, currentShowingView, notNetworkView);
        currentShowingView = notNetworkView;
    }

    /**
     * 展示登录的界面
     */
    public void showLoginView() {
        state = LOGIN;
        if (loginView.getParent() == null) {
            addView(loginView);
        }
        AnimationHelper.switchViewByAnim(useAnimation, viewAnimProvider, currentShowingView, loginView);
        currentShowingView = loginView;
    }

    /**
     * 展示登录的界面
     *
     * @param msgId 提示语
     */
    public void showLoginView(int msgId) {
        if (loginView.getParent() == null) {
            addView(loginView);
        }
        setTipText(LOGIN, msgId);
        AnimationHelper.switchViewByAnim(useAnimation, viewAnimProvider, currentShowingView, loginView);
        currentShowingView = loginView;
    }

    /**
     * 展示登录的界面
     *
     * @param msg 提示语
     */
    public void showLoginView(String msg) {
        if (loginView.getParent() == null) {
            addView(loginView);
        }
        setTipText(LOGIN, msg);
        AnimationHelper.switchViewByAnim(useAnimation, viewAnimProvider, currentShowingView, loginView);
        currentShowingView = loginView;
    }

    /**
     * 展示登录的界面
     *
     * @param msgId 提示语
     * @param imgId 图片Id
     */
    public void showLoginView(int msgId, int imgId) {
        if (loginView.getParent() == null) {
            addView(loginView);
        }
        setTipText(LOGIN, msgId);
        setTipImg(LOGIN, imgId);
        AnimationHelper.switchViewByAnim(useAnimation, viewAnimProvider, currentShowingView, loginView);
        currentShowingView = loginView;
    }

    /**
     * 展示登录的界面
     *
     * @param msg   提示语
     * @param imgId 图片Id
     */
    public void showLoginView(String msg, int imgId) {
        if (loginView.getParent() == null) {
            addView(loginView);
        }
        setTipText(LOGIN, msg);
        setTipImg(LOGIN, imgId);
        AnimationHelper.switchViewByAnim(useAnimation, viewAnimProvider, currentShowingView, loginView);
        currentShowingView = loginView;
    }


    /**
     * 展示加载中的界面
     */
    public void showLoadingView() {

        //如果是和下拉刷新合用，且已显示过一次后直接返回
        if (isRefreshLayout && isShowLoadingLayout && state == SUCCESS) {
            return;
        }
        state = LOADING;
        isShowLoadingLayout = true;
        if (loadingView.getParent() == null) {
            addView(loadingView);
        }
        setLoadingView(null, true);
        AnimationHelper.switchViewByAnim(useAnimation, viewAnimProvider, currentShowingView, loadingView);
        currentShowingView = loadingView;
    }

    /**
     * 展示加载中的界面
     *
     * @param view 进度条部分
     */
    public void showLoadingView(View view) {
        if (loadingView.getParent() == null) {
            addView(loadingView);
        }
        setLoadingView(view, true);
        AnimationHelper.switchViewByAnim(useAnimation, viewAnimProvider, currentShowingView, loadingView);
        currentShowingView = loadingView;
    }

    /**
     * 展示加载中的界面
     *
     * @param view    进度条部分
     * @param showTip 是否显示提示
     */
    public void showLoadingView(View view, boolean showTip) {
        if (loadingView.getParent() == null) {
            addView(loadingView);
        }
        setLoadingView(view, showTip);
        AnimationHelper.switchViewByAnim(useAnimation, viewAnimProvider, currentShowingView, loadingView);
        currentShowingView = loadingView;
    }


    /**
     * 展示加载中的界面
     *
     * @param msgId 提示语
     */
    public void showLoadingView(int msgId) {
        if (loadingView.getParent() == null) {
            addView(loadingView);
        }
        setTipText(LOADING, msgId);
        AnimationHelper.switchViewByAnim(useAnimation, viewAnimProvider, currentShowingView, loadingView);
        currentShowingView = loadingView;
    }

    /**
     * 展示加载中的界面
     *
     * @param msg 提示语
     */
    public void showLoadingView(String msg) {
        if (loadingView.getParent() == null) {
            addView(loadingView);
        }
        setTipText(LOADING, msg);
        AnimationHelper.switchViewByAnim(useAnimation, viewAnimProvider, currentShowingView, loadingView);
        currentShowingView = loadingView;
    }


    /**
     * 展示内容的界面
     */
    public void showContentView() {
        state = SUCCESS;
        AnimationHelper.switchViewByAnim(useAnimation, viewAnimProvider, currentShowingView, contentView);
        currentShowingView = contentView;
    }

    /**
     * 添加用户自定义的View
     *
     * @param view 自定义View
     */
    public void showCustomView(View view) {
        if (view.getParent() == null) { //当前的view没有父类
            addView(view);
        } else {
            view.setLayoutParams(this.getLayoutParams());
        }
        AnimationHelper.switchViewByAnim(useAnimation, viewAnimProvider, currentShowingView, view);
        currentShowingView = view;
    }

    //************ update ************//

    /**
     * 修改提示文字
     *
     * @param type 传入修改哪个
     * @param text 文字
     */
    public void setTipText(int type, CharSequence text) {
        if (text == null) { //text is null
            return;
        }
        switch (type) {
            case ERROR:
                ((ItemViewHolder) errorView.getTag()).tvTip.setText(text);
                break;
            case EMPTY:
                ((ItemViewHolder) emptyView.getTag()).tvTip.setText(text);
                break;
            case TIMEOUT:
                ((ItemViewHolder) timeOutView.getTag()).tvTip.setText(text);
                break;
            case NOT_NETWORK:
                ((ItemViewHolder) notNetworkView.getTag()).tvTip.setText(text);
                break;
            case LOADING:
                ((LoadingViewHolder) loadingView.getTag()).tvTip.setText(text);
                break;
            case LOGIN:
                ((ItemViewHolder) loginView.getTag()).tvTip.setText(text);
                break;
        }
    }

    /**
     * 修改提示文字
     *
     * @param type   传入修改哪个
     * @param textId 文字资源id
     */
    public void setTipText(int type, int textId) {
        switch (type) {
            case ERROR:
                ((ItemViewHolder) errorView.getTag()).tvTip.setText(textId);
                break;
            case EMPTY:
                ((ItemViewHolder) emptyView.getTag()).tvTip.setText(textId);
                break;
            case TIMEOUT:
                ((ItemViewHolder) timeOutView.getTag()).tvTip.setText(textId);
                break;
            case NOT_NETWORK:
                ((ItemViewHolder) notNetworkView.getTag()).tvTip.setText(textId);
                break;
            case LOADING:
                ((LoadingViewHolder) loadingView.getTag()).tvTip.setText(textId);
                break;
            case LOGIN:
                ((ItemViewHolder) loginView.getTag()).tvTip.setText(textId);
                break;
            default:
                break;
        }
    }

    /**
     * 设置提示图片资源
     *
     * @param type     传入修改哪个，除了Loading
     * @param drawable 图片资源drawable
     */
    public void setTipImg(int type, Drawable drawable) {
        switch (type) {
            case ERROR:
                ((ItemViewHolder) errorView.getTag()).ivImg.setBackgroundDrawable(drawable);
                break;
            case EMPTY:
                ((ItemViewHolder) emptyView.getTag()).ivImg.setBackgroundDrawable(drawable);
                break;
            case TIMEOUT:
                ((ItemViewHolder) timeOutView.getTag()).ivImg.setBackgroundDrawable(drawable);
                break;
            case NOT_NETWORK:
                ((ItemViewHolder) notNetworkView.getTag()).ivImg.setBackgroundDrawable(drawable);
                break;
            case LOGIN:
                ((ItemViewHolder) loginView.getTag()).ivImg.setBackgroundDrawable(drawable);
                break;
            default:
                break;
        }
    }

    /**
     * 设置提示图片资源
     *
     * @param type  传入修改哪个，除了Loading
     * @param imgId 图片资源id
     */
    public void setTipImg(int type, int imgId) {
        if (imgId <= 0) return;

        switch (type) {
            case ERROR:
                ((ItemViewHolder) errorView.getTag()).ivImg.setImageResource(imgId);
                break;
            case EMPTY:
                ((ItemViewHolder) emptyView.getTag()).ivImg.setImageResource(imgId);
                break;
            case TIMEOUT:
                ((ItemViewHolder) timeOutView.getTag()).ivImg.setImageResource(imgId);
                break;
            case NOT_NETWORK:
                ((ItemViewHolder) notNetworkView.getTag()).ivImg.setImageResource(imgId);
                break;
            case LOGIN:
                ((ItemViewHolder) loginView.getTag()).ivImg.setImageResource(imgId);
                break;
        }
    }

    /**
     * 设置加载界面的View
     *
     * @param view 显示的View
     */
    public void setLoadingView(View view, boolean showTip) {
        LoadingViewHolder holder = (LoadingViewHolder) loadingView.getTag();
        if (view != null) {
            holder.frameLayout.removeAllViews();
            holder.frameLayout.addView(view);
        }
        if (showTip) {
            holder.tvTip.setVisibility(VISIBLE);
        } else {
            holder.tvTip.setVisibility(GONE);
        }
    }


    public void setMarginBottom(int px) {
        if (emptyView != null) {
            setMarginBottom(emptyView.findViewById(R.id.llBodyContent), px);
        }
        if (errorView != null) {
            setMarginBottom(errorView.findViewById(R.id.llBodyContent), px);
        }
        if (loadingView != null) {
            setMarginBottom(loadingView.findViewById(R.id.llBodyContent), px);
        }
        if (timeOutView != null) {
            setMarginBottom(timeOutView.findViewById(R.id.llBodyContent), px);
        }
        if (notNetworkView != null) {
            setMarginBottom(notNetworkView.findViewById(R.id.llBodyContent), px);
        }
        if (loginView != null) {
            setMarginBottom(loginView.findViewById(R.id.llBodyContent), px);
        }
    }


    public void setMarginTop(int px) {
        if (emptyView != null) {
            setMarginTop(emptyView.findViewById(R.id.llBodyContent), px);
        }
        if (errorView != null) {
            setMarginTop(errorView.findViewById(R.id.llBodyContent), px);
        }
        if (loadingView != null) {
            setMarginTop(loadingView.findViewById(R.id.llBodyContent), px);
        }
        if (timeOutView != null) {
            setMarginTop(timeOutView.findViewById(R.id.llBodyContent), px);
        }
        if (notNetworkView != null) {
            setMarginTop(notNetworkView.findViewById(R.id.llBodyContent), px);
        }
        if (loginView != null) {
            setMarginTop(loginView.findViewById(R.id.llBodyContent), px);
        }
    }

    public void setMarginStart(int px) {
        if (emptyView != null) {
            setMarginStart(emptyView.findViewById(R.id.llBodyContent), px);
        }
        if (errorView != null) {
            setMarginStart(errorView.findViewById(R.id.llBodyContent), px);
        }
        if (loadingView != null) {
            setMarginStart(loadingView.findViewById(R.id.llBodyContent), px);
        }
        if (timeOutView != null) {
            setMarginStart(timeOutView.findViewById(R.id.llBodyContent), px);
        }
        if (notNetworkView != null) {
            setMarginStart(notNetworkView.findViewById(R.id.llBodyContent), px);
        }
        if (loginView != null) {
            setMarginStart(loginView.findViewById(R.id.llBodyContent), px);
        }
    }

    public void setMarginEnd(int px) {
        if (emptyView != null) {
            setMarginEnd(emptyView.findViewById(R.id.llBodyContent), px);
        }
        if (errorView != null) {
            setMarginEnd(errorView.findViewById(R.id.llBodyContent), px);
        }
        if (loadingView != null) {
            setMarginEnd(loadingView.findViewById(R.id.llBodyContent), px);
        }
        if (timeOutView != null) {
            setMarginEnd(timeOutView.findViewById(R.id.llBodyContent), px);
        }
        if (notNetworkView != null) {
            setMarginEnd(notNetworkView.findViewById(R.id.llBodyContent), px);
        }
        if (loginView != null) {
            setMarginEnd(loginView.findViewById(R.id.llBodyContent), px);
        }
    }


    private void setMarginBottom(View view, int px) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof MarginLayoutParams) {
            ((MarginLayoutParams) layoutParams).bottomMargin = px;
        }
    }

    private void setMarginTop(View view, int px) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof MarginLayoutParams) {
            ((MarginLayoutParams) layoutParams).topMargin = px;
        }
    }

    private void setMarginEnd(View view, int px) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof MarginLayoutParams) {
            ((MarginLayoutParams) layoutParams).rightMargin = px;
        }
    }

    private void setMarginStart(View view, int px) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof MarginLayoutParams) {
            ((MarginLayoutParams) layoutParams).leftMargin = px;
        }
    }


    //************ animation ************//

    public void setViewSwitchAnimProvider(ViewAnimProvider animProvider) {
        if (animProvider != null) {
            this.viewAnimProvider = animProvider;
        }
    }

    public ViewAnimProvider getViewAnimProvider() {
        return viewAnimProvider;
    }

    public boolean isUseAnimation() {
        return useAnimation;
    }

    public void setUseAnimation(boolean useAnimation) {
        this.useAnimation = useAnimation;
    }

    //************ callBack ************//
    public interface OnViewRefreshListener {
        //刷新界面
        void onRefreshClick();

        //登录点击
        void onLoginClick(Context context);
    }

    //************ addView ************//
    @Override
    public void addView(View child) {
        checkIsContentView(child);
        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        checkIsContentView(child);
        super.addView(child, index);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        checkIsContentView(child);
        super.addView(child, index, params);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        checkIsContentView(child);
        super.addView(child, params);
    }

    @Override
    public void addView(View child, int width, int height) {
        checkIsContentView(child);
        super.addView(child, width, height);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params) {
        checkIsContentView(child);
        return super.addViewInLayout(child, index, params);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params, boolean preventRequestLayout) {
        checkIsContentView(child);
        return super.addViewInLayout(child, index, params, preventRequestLayout);
    }

}
