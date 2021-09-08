package com.base.common.view.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;

import com.base.common.app.LoginUtils;
import com.base.common.utils.DialogUtils;
import com.base.common.utils.LogUtil;
import com.base.common.utils.matisse.ImageEditUtils;
import com.base.common.view.widget.statelayout.StateLayout;
import com.trello.rxlifecycle3.components.support.RxFragment;

import java.util.ArrayList;
import java.util.List;

public class BaseInitDataFragment extends RxFragment implements StateLayout.OnViewRefreshListener {


    private boolean isPauseRevert = false;//是否从暂停恢复
    private boolean isFirst = true;//是否是第一次加载
    private boolean isViewPagerRevert = false;//是否是和viewPager合用，并从回收后恢复
    private boolean isInitView = false;//是否已初始化view
    protected BaseActivity mActivity;
    private String title = "";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (BaseActivity) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在配置变化的时候将这个fragment保存下来  Failure saving state: active xxxFragment{53e882a} has cleared index: -1
//        setRetainInstance(true);

        LogUtil.d(this.getClass().getSimpleName() + ":" + getTitle(), "onCreate() ");
    }

    public LifecycleOwner getLifecycleOwner() {
        return this;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LogUtil.d(this.getClass().getSimpleName() + ":" + getTitle(), "onViewCreated() ");
        isInitView = true;
        initNotificationReceipt();
        initView();
        if (isViewPagerRevert) {
            initData();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtil.d(this.getClass().getSimpleName() + ":" + getTitle(), "onActivityCreated() ");

        if (getUserVisibleHint() && isVisibleToUser(this)) {
            if (isFirst) {
                isFirst = false;
                initData();
            }
        }


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //getActivity() == null fragment尚未添加到activity或fragment已被回收，忽略
        if (getActivity() != null) {
            if (isVisibleToUser) {
                if (isVisibleToUser(this)) {
                    //先执行该方法，但是没有初始化view  是回收后重新恢复
                    if (!isInitView) {
                        isViewPagerRevert = true;
                    }
                    if (isFirst) {
                        isFirst = false;
                        LogUtil.d(this.getClass().getSimpleName() + ":" + getTitle(), "setUserVisibleHint() true ");
                        initData();
                    } else {
                        onUserVisible();
                    }
                }
            } else {
                onInvisible();
            }
        }
    }


    @Override
    public void onStop() {
        isPauseRevert = true;
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isPauseRevert && isVisibleToUser(this)) {
            isPauseRevert = false;
            onPauseRevert();
        }
    }

    /**
     * 接收事件通知
     */
    protected void initNotificationReceipt() {

    }

    public void initView() {
        LogUtil.d(this.getClass().getSimpleName() + ":" + getTitle(), "initView()");
    }

    //数据显示后，从弹出Activity页面返回后   会在initData()后面执行  AlertDialog  弹起也会调用
    public void onPauseRevert() {
        LogUtil.d(this.getClass().getSimpleName() + ":" + getTitle(), "onPauseRevert() ");
    }

    //第一次显示的时侯加载数据,只会调用一次
    public void initData() {
        LogUtil.d(this.getClass().getSimpleName() + ":" + getTitle(), "initData() ");

        //子fragment，排在第一位的也要加载数据
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        if (!fragments.isEmpty()) {
            for (Fragment fragment : fragments) {
                if (fragment instanceof BaseInitDataFragment && fragment.getUserVisibleHint() && fragment.isVisible()) {
                    BaseInitDataFragment baseInitDataFragment = (BaseInitDataFragment) fragment;
                    baseInitDataFragment.initData();
                }
            }
        }
    }

    //可见时调用
    public void onUserVisible() {
        LogUtil.d(this.getClass().getSimpleName() + ":" + getTitle(), "onUserVisible() ");

        //子fragment，排在第一位的也要加载数据
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        if (!fragments.isEmpty()) {
            for (Fragment fragment : fragments) {
                if (fragment instanceof BaseInitDataFragment && fragment.getUserVisibleHint()) {
                    BaseInitDataFragment baseInitDataFragment = (BaseInitDataFragment) fragment;
                    baseInitDataFragment.onUserVisible();
                }
            }
        }
    }

    //不可见时调用
    public void onInvisible() {
        LogUtil.d(this.getClass().getSimpleName() + ":" + getTitle(), "onInvisible()");
        //子fragment，排在第一位的也要加载数据
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        if (!fragments.isEmpty()) {
            for (Fragment fragment : fragments) {
                if (fragment instanceof BaseInitDataFragment && fragment.getUserVisibleHint()) {
                    BaseInitDataFragment baseInitDataFragment = (BaseInitDataFragment) fragment;
                    baseInitDataFragment.onInvisible();
                }
            }
        }
    }


    @Override
    @Deprecated
    public final void onDestroyView() {
        super.onDestroyView();
        LogUtil.d(this.getClass().getSimpleName() + ":" + getTitle(), "onDestroyView()");
        isPauseRevert = false;
        isInitView = false;
        isFirst = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d(this.getClass().getSimpleName() + ":" + getTitle(), "onDestroy()");
        isPauseRevert = false;
        isInitView = false;
        isFirst = true;
    }

    /**
     * 网络请求失败，重新请求按扭的点击
     * {@link com.base.common.view.widget.statelayout.StateLayout.OnViewRefreshListener}
     */
    @Override
    public void onRefreshClick() {
        initData();
    }

    //未登录按扭点击
    @Override
    public void onLoginClick(Context context) {
        //        LoginActivity   在app层发送登录事件，首页接收
        LoginUtils.loginIn();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //图片选择返回，回调
        if (ImageEditUtils.getInstance().getImageCallBack() != null) {
            ImageEditUtils.getInstance().getImageCallBack().onActivityResult(requestCode, resultCode, data);
        }
    }


    public void showLoad() {
        DialogUtils.waitingDialog(getActivity());
    }

    public void dismissLoad() {
        DialogUtils.dismiss(getActivity());
    }

    //获取当前显示在第一位的fragment ps:被覆盖的不会显示
    public List<Fragment> getFirstVisibleFragments() {
        List<Fragment> list = new ArrayList<>();
        if (!isAdded()) return list;

        FragmentManager fragmentManager = getChildFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments == null) return list;
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible() && fragment.getUserVisibleHint()) {
                getAllChildFrament(list, fragment);
            }
        }
        return list;
    }

    //获取当前显示的fragment  ps:被覆盖的也会显示
    public List<Fragment> getVisibleFragments() {
        FragmentManager fragmentManager = getChildFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        List<Fragment> list = new ArrayList<>();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible()) {
                getAllChildFrament(list, fragment);
            }
        }
        return list;
    }

    //递归调用找出所有的Fragment
    public void getAllChildFrament(List<Fragment> list, Fragment fragment) {
        list.add(fragment);
        List<Fragment> frags = fragment.getChildFragmentManager().getFragments();
        if (frags != null) {
            for (Fragment f : frags) {
                if (f != null) {
                    getAllChildFrament(list, f);
                }
            }
        }
    }

    //判断fragment是否显示在了用户面前，如果有嵌套也会判断上层的显示状态
    public boolean isVisibleToUser(Fragment fragment) {
        Fragment frag = fragment.getParentFragment();
        if (fragment.getUserVisibleHint() && frag != null) {
            return frag.getUserVisibleHint() && isVisibleToUser(frag);
        } else {
            return fragment.getUserVisibleHint();
        }
    }

}
