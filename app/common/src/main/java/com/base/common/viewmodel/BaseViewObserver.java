package com.base.common.viewmodel;

import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.base.common.app.LoginUtils;
import com.base.common.model.http.err.ApiException;
import com.base.common.model.http.err.ERROR;
import com.base.common.netBeanPackage.LiveDataWrapper;
import com.base.common.utils.DialogUtils;
import com.base.common.utils.LogUtil;
import com.base.common.utils.UIUtils;
import com.base.common.view.base.BaseViewUtils;
import com.base.common.view.widget.statelayout.StateLayout;

import java.lang.ref.WeakReference;

public abstract class BaseViewObserver<T> implements Observer<T> {

    private WeakReference<BaseViewUtils> viewUtils_WR;
    private WeakReference<Activity> context;

    /**
     * 不对请求状态进行处理
     */
    public BaseViewObserver() {

    }

    public BaseViewObserver(Object object) {
        //只对请求成功和失败进行处理
        if (object instanceof Activity) {
            this.context = new WeakReference<>((Activity) object);
        } else if (object instanceof Fragment) {
            this.context = new WeakReference<>(((Fragment) object).getActivity());
        }
        //传入BaseViewUtils   搭配response自动显示状态
        else if (object instanceof BaseViewUtils) {
            viewUtils_WR = new WeakReference<>((BaseViewUtils) object);
            if (viewUtils_WR.get().getStateLayout() != null) {
                viewUtils_WR.get().getStateLayout().setRefreshLayout(isRefreshLayoutData());
            }
        }
    }


    /**
     * 当出现外部错误时，只有发送一个ERROR  一会结速这一串请求，并不会发射onComplete
     * 当出现内部错误时，请求已返回对这次请求来说是成功的，有可能会发射 onComplete
     * <p>
     * 现对，发射状态调整，不管是内部还是外部错误时，都会发射onComplete,并且只会发射一次，
     */
    @Override
    public void onChanged(T t) {
        if (t instanceof LiveDataWrapper) {
            LiveDataWrapper liveDataWrapper = (LiveDataWrapper) t;
            switch (liveDataWrapper.status) {
                case LOADING:
                    LogUtil.d("BaseViewObserver", "加载中");
                    loadLayout();
                    onStart();
                    break;
                case ERROR:
                    if (liveDataWrapper.error != null) {
                        LogUtil.d("BaseViewObserver", liveDataWrapper.error.getCode() + "  " + liveDataWrapper.error.getMessage());
                        if (liveDataWrapper.error.getCode() >= ERROR.ERR400) {
                            notNetWorkLayout();
                        } else {
                            errorLayout();
                        }
                        //外部网络错误
                        onNetError(liveDataWrapper.error);
                    }

                    //外部错误，只要便会中断，只会发射一次
                    onComplete();
                    break;
                case APP_ERROR:
                    if (liveDataWrapper.error != null) {
                        LogUtil.d("BaseViewObserver", liveDataWrapper.error.getMessage());
                        onAppError(liveDataWrapper.error);
                        if (liveDataWrapper.error.getCode() == ERROR.LOGIN_OUT) {
                            loginLayout();
                        } else {
                            errorLayout();
                        }
                    }
                    statusComplete(liveDataWrapper);
                    break;
                case SUCCESS:
                    LogUtil.d("BaseViewObserver", "成功");
                    if (isEmptyData(t)) {
                        onEmptyLayout();
                        onSuccessResultNull();
                    } else {
                        contentLayout(t);
                        onSuccess(t);
                    }

                    statusComplete(liveDataWrapper);
                    break;
            }
        }
    }


    //请求完成判断
    private void statusComplete(LiveDataWrapper liveDataWrapper) {
        if (liveDataWrapper != null) {
            int count = liveDataWrapper.getCount();
            int current = liveDataWrapper.getCurrent();
            if (count == current) {
                onComplete();
            }
        }
    }

    /**
     * 显示正在加载的布局
     */
    private void loadLayout() {
        if (null != viewUtils_WR && viewUtils_WR.get().getStateLayout() != null) {
            LogUtil.d("BaseViewObserver", "loadLayout()");
            viewUtils_WR.get().getStateLayout().showLoadingView();
        } else if (null != context) {
            DialogUtils.waitingDialog(context.get());
        }
    }

    /**
     * 显示无数据的布局
     */
    protected void onEmptyLayout() {
        if (null != viewUtils_WR && showStateLayoutChecked()) {
            LogUtil.d("BaseViewObserver", "onEmptyLayout()");
            viewUtils_WR.get().getStateLayout().showEmptyView();
        } else if (null != context) {
            DialogUtils.dismiss(context.get());
        }
    }


    /**
     * 显示正文布局
     */
    private void contentLayout(T data) {
        if (null != viewUtils_WR && showStateLayoutChecked()) {
            LogUtil.d("BaseViewObserver", "contentLayout()");
            viewUtils_WR.get().getStateLayout().showContentView();
        } else if (null != context) {
            DialogUtils.dismiss(context.get());
        }
    }


    /**
     * 显示未登录的布局
     */

    private void loginLayout() {
        if (null != viewUtils_WR && showStateLayoutChecked()) {
            LogUtil.d("BaseViewObserver", "loginLayout()");
            viewUtils_WR.get().getStateLayout().showLoginView();
        } else if (null != context) {
            //发送需要弹出登录页面
            if (isSendNeedLogin()) {
                LoginUtils.loginIn();
                UIUtils.showToastSafes("未登录请登录");
            }
            DialogUtils.dismiss(context.get());
        }
    }

    /**
     * 显示网络断开的布局
     */
    private void notNetWorkLayout() {
        if (null != viewUtils_WR && showStateLayoutChecked()) {
            LogUtil.d("BaseViewObserver", "notNetWorkLayout()");
            viewUtils_WR.get().getStateLayout().showNoNetworkView();
        } else if (null != context) {
            DialogUtils.dismiss(context.get());
        }
    }


    /**
     * 显示数据获取错误布局
     */
    private void errorLayout() {
        if (null != viewUtils_WR && showStateLayoutChecked()) {
            LogUtil.d("BaseViewObserver", "errorLayout()");
            viewUtils_WR.get().getStateLayout().showErrorView();
        } else if (null != context) {
            DialogUtils.dismiss(context.get());
        }
    }


    /**
     * StateLayout 是否显示状态布局检查
     *
     * @return true  显示
     */
    private boolean showStateLayoutChecked() {
        if (null != viewUtils_WR) {
            //如果有下拉刷新，则会停止
            viewUtils_WR.get().stopRefreshLayout();
            if (viewUtils_WR.get().getStateLayout() != null) {
                return true;
            }
        }
        return false;
    }


    //是否发送需要登录事件
    public boolean isSendNeedLogin() {
        return true;
    }


    /**
     * 判断数据是否为空
     * {@link #isRefreshLayoutData()}  返回true 时 要判断 第一页的数据是否为空即可，第二页时不用判断是否为空
     *
     * @param data
     * @return true   结果为空
     */
    public boolean isEmptyData(T data) {
        return false;
    }


    /**
     * 是否是下拉刷新数据源,只对 StateLayout  的加载状态有影响
     *
     * @return true 时为下拉刷新的数据源，StateLayout的加载状态，只会回调一次，
     * 除非调用了 {@link StateLayout#resetLoadingState()} 重置加载状态 ，会再次回调一次
     */
    public boolean isRefreshLayoutData() {
        return false;
    }


    // 开始时回调
    protected void onStart() {

    }

    //所有请求都成功时回调,只会回调一次
    protected void onComplete() {
        LogUtil.d("BaseViewObserver", "onComplete()");
        if (null != context) {
            DialogUtils.dismiss(context.get());
        }
    }


    //app内部错误时回调
    protected void onAppError(ApiException error) {
        if (error.getCode() != ERROR.LOGIN_OUT) {
            UIUtils.showToastSafesClose(error.getMessage());
        }
        LogUtil.e(error.getMessage());
        onError(error);
    }

    //服务器外部错误回调
    protected void onNetError(ApiException error) {
        LogUtil.e(error.getMessage() + error.getUrl() == null ? "" : "\nurl: " + error.getUrl());
        UIUtils.showToastSafesClose(error.getMessage());
        onError(error);
    }

    protected void onError(ApiException error) {

    }


    /**
     * 请求成功，但返回数据为空时的回调
     */
    protected void onSuccessResultNull() {

    }

    /**
     * 请求成功，返回数据不为空时的回调
     *
     * @param data
     */
    protected abstract void onSuccess(T data);


}
