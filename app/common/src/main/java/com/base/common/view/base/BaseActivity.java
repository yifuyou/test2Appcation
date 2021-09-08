package com.base.common.view.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.os.ResultReceiver;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.base.common.R;
import com.base.common.app.HomeKeyListener;
import com.base.common.app.LoginUtils;
import com.base.common.utils.DialogUtils;
import com.base.common.utils.JavaMethod;
import com.base.common.utils.LogUtil;
import com.base.common.utils.matisse.ImageEditUtils;
import com.base.common.view.widget.statelayout.StateLayout;
import com.base.common.viewmodel.BaseViewModel;
import com.gyf.immersionbar.ImmersionBar;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;

import java.lang.reflect.Method;
import java.util.List;

import androidx.annotation.ColorRes;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


public abstract class BaseActivity<DB extends ViewDataBinding, VM extends BaseViewModel> extends RxAppCompatActivity implements StateLayout.OnViewRefreshListener {

    protected DB binding;
    protected VM viewModel;
    protected BaseActivity mContext;
    protected BaseViewUtils viewUtils; //统一处理view的工具类
    private boolean isPauseRevert = false;//是否从暂停恢复
    private HomeKeyListener homeKeyListener;//对home键的监听

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        if (null == viewModel) {
            Class<?> cls = JavaMethod.getGenericClass(this, 1);
            if (null == cls)
                viewModel = null;
            else {
                viewModel = new ViewModelProvider(this).get((Class<VM>) cls);
//                viewModel = ViewModelProviders.of(this).get((Class<VM>) cls);
            }
        }

        viewUtils = new BaseViewUtils(this, this, viewModel);
    }

    /**
     * 在{@link  #onCreate(Bundle)}  } 中调用
     *
     * @param layoutResID
     */
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        initStatusBar();
        //对binding 初始华
        initContentView(layoutResID);
        //接收事件通知
        initNotificationReceipt();
        initView();
        //数据初始化
        initData(getIntent());
    }

    /**
     * 初始化 binding
     *
     * @param layoutResID 主要布局
     */
    protected void initContentView(@LayoutRes int layoutResID) {
//                binding = DataBindingUtil.setContentView(this, getLayoutId());
        binding = DataBindingUtil.inflate(getLayoutInflater(), layoutResID, null, false);
        binding.setLifecycleOwner(this);
        setContentView(binding.getRoot());
    }


    /**
     * 状态栏初始化
     */
    protected void initStatusBar() {
        ImmersionBar in = ImmersionBar.with(this)
                .statusBarColor(getStatusBarColor())
                .statusBarDarkFont(isStatusBarDarkFont())
                .fitsSystemWindows(fitsSystemWindows())
                .transparentStatusBar()
                .navigationBarColor(getNavigationBarColor())
                .navigationBarDarkIcon(false);
        setStatusBar(in);
        //不弹出键盘
        if (keyboardEnable()) {
            in.keyboardEnable(true);
//            in.keyboardEnable(true, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }
        in.init();
    }

    protected void setStatusBar(ImmersionBar in) {

    }


    protected boolean keyboardEnable() {
        return false;
    }

    @ColorRes
    protected int getNavigationBarColor() {
        return R.color.C_666666;
    }

    //默认透明色
    @ColorRes
    protected int getStatusBarColor() {
        return R.color.colorPrimary;
    }


    //暂时未用到
    protected boolean fitsSystemWindows() {
        return false;
    }

    /**
     * 状态栏字体深色或亮色
     *
     * @return
     */
    protected boolean isStatusBarDarkFont() {
        return true;
    }

    public View getRootView() {
        if (binding == null) return getWindow().getDecorView();
        return binding.getRoot();
    }

    public ViewGroup getRootViewGroup() {
        return (ViewGroup) getWindow().getDecorView();
    }

    /**
     * 接收事件通知
     */
    protected void initNotificationReceipt() {

    }


    /**
     * 视图初始化
     */
    protected void initView() {

    }

    /**
     * 数据初始化
     */
    protected void initData(Intent intent) {
        initData();
    }

    protected void initData() {

    }

    //数据显示后，从弹出Activity页面返回后   会在initData()后面执行  AlertDialog  弹起也会调用
    public void onPauseRevert() {
        LogUtil.d(this.getClass().getSimpleName(), "onPauseRevert() ");
    }


    public void setHomeKeyListener(HomeKeyListener.OnHomePressedListener onHomePressedListener) {
        homeKeyListener = new HomeKeyListener(this);
        homeKeyListener.setOnHomePressedListener(onHomePressedListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isPauseRevert) {
            isPauseRevert = false;
            onPauseRevert();
        }
        LogUtil.d(this.getClass().getSimpleName(), "onStart() ");
    }

    @Override
    protected void onStop() {
        isPauseRevert = true;
        super.onStop();
        LogUtil.d(this.getClass().getSimpleName(), "onStop() ");
    }


    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.d(this.getClass().getSimpleName(), "onPause() ");
        if (homeKeyListener != null)
            homeKeyListener.stopWatch();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.d(this.getClass().getSimpleName(), "onResume() ");
        if (homeKeyListener != null)
            homeKeyListener.startWatch();

    }


    /**
     * 网络请求失败，重新请求按扭的点击
     * {@link com.base.common.view.widget.statelayout.StateLayout.OnViewRefreshListener}
     */
    @Override
    public void onRefreshClick() {
        initData(getIntent());
    }

    //未登录按扭点击
    @Override
    public void onLoginClick(Context context) {
        //        LoginActivity   在app层发送登录事件，首页接收
        LoginUtils.loginIn();
    }


    public BaseActivity getActivity() {
        return this;
    }

    public void showLoad() {
        DialogUtils.waitingDialog(this);
    }

    public void dismissLoad() {
        DialogUtils.dismiss(this);
    }

    @Override
    protected void onDestroy() {

        LogUtil.d(getClass().getSimpleName() + " onDestroy");
//        DialogUtils.onDestroy();
        if (viewModel != null) {
            viewModel.onDestroy();
        }
        if (binding != null)
            binding.unbind();
        dismissLoad();
        super.onDestroy();

    }


    @Override
    public void onBackPressed() {
        List<Fragment> list = getSupportFragmentManager().getFragments();
        boolean isCos = false;
        for (Fragment fragment : list) {
            if (fragment instanceof BaseFragment) {
                BaseFragment baseFragment = (BaseFragment) fragment;
                isCos = baseFragment.onBackPressed();
                if (isCos) break;
            } else if (fragment instanceof BaseDialogFragment) {
                BaseDialogFragment baseFragment = (BaseDialogFragment) fragment;
                isCos = baseFragment.onBackPressed();
                if (isCos) break;
            }
        }
        if (isCos) return;
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //图片选择返回，回调
        if (ImageEditUtils.getInstance().getImageCallBack() != null) {
            ImageEditUtils.getInstance().getImageCallBack().onActivityResult(requestCode, resultCode, data);
        }
    }

    public void addFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(fragment, fragment.getClass().getSimpleName()).commitAllowingStateLoss();
    }

    public void addFragment(@IdRes int id, Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(id, fragment).commitAllowingStateLoss();
    }

    public void removeFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .remove(fragment).commit();
    }

    /**
     * 设置 app 不随着系统字体的调整而变化
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();

        //config.setToDefaults();
        config.fontScale = 1.0f;
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }


    /**
     * 隐藏键盘
     */
    public void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View v = getWindow().peekDecorView();
        if (null != v && imm != null) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    // 隐藏软键盘
    public final void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    // 隐藏软键盘
    public void hideSoftInput(EditText view) {
        if (view.getWindowToken() != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void onHideSoftInput() {

    }


    /**
     * @return 本次点击 是否 隐藏软键盘  默认 true 隐藏
     */
    public boolean isHideSoftInput(int x, int y) {
        return true;
    }


    /**
     * @return 本次点击 是否 隐藏软键盘  默认
     */
    private boolean isHideInput(int x, int y) {
        boolean isCos = true;
        isCos = isHideSoftInput(x, y);
        if (!isCos) return false;

        List<Fragment> list = getSupportFragmentManager().getFragments();
        for (Fragment fragment : list) {
            if (fragment instanceof BaseFragment) {
                BaseFragment baseFragment = (BaseFragment) fragment;
                isCos = baseFragment.isHideSoftInput(x, y);
                if (!isCos) break;
            } else if (fragment instanceof BaseDialogFragment) {
                BaseDialogFragment baseFragment = (BaseDialogFragment) fragment;
                isCos = baseFragment.isHideSoftInput(x, y);
                if (!isCos) break;
            }
        }

        return isCos;
    }

    // 获取点击事件  点击空白隐藏软键盘
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (isHideInput(view, ev)) {
                if (isHideInput((int) ev.getX(), (int) ev.getY())) {
                    hideSoftInput(view.getWindowToken());
                    onHideSoftInput();
                    List<Fragment> list = getSupportFragmentManager().getFragments();
                    for (Fragment fragment : list) {
                        if (fragment instanceof BaseFragment) {
                            BaseFragment baseFragment = (BaseFragment) fragment;
                            baseFragment.onHideSoftInput();
                        } else if (fragment instanceof BaseDialogFragment) {
                            BaseDialogFragment baseFragment = (BaseDialogFragment) fragment;
                            baseFragment.onHideSoftInput();
                        }
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    // 判定是否需要隐藏
    public boolean isHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            if (ev.getX() > left && ev.getX() < right && ev.getY() > top && ev.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public boolean showInputMethod(View focus) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        try {

            Object mService = JavaMethod.getFieldValue(imm, "mService");
            Object mClient = JavaMethod.getFieldValue(imm, "mClient");
            Class<?> clientClass = ClassLoader.getSystemClassLoader().loadClass("com.android.internal.view.IInputMethodClient");
            Method showSoftInput = JavaMethod.getMethod(mService, "showSoftInput", clientClass, int.class, ResultReceiver.class);
            return (Boolean) showSoftInput.invoke(mService, mClient, 0, null);


        } catch (Exception e) {
            e.printStackTrace();
        }
        if (focus == null) {
            focus = getWindow().getCurrentFocus();
        }
        return imm.showSoftInput(focus, 0);
    }

    public static boolean isServiceAlive(Context context, String serviceName) {
        boolean flag = false;
        int size = 0;
        ActivityManager am = (ActivityManager) context
                .getSystemService(Activity.ACTIVITY_SERVICE);
        //返回100个服务
        List<ActivityManager.RunningServiceInfo> runningServices = am.getRunningServices(100);
        for (ActivityManager.RunningServiceInfo runningServiceInfo : runningServices) {
            String allServiceName = runningServiceInfo.service.getClassName();
            if (allServiceName.indexOf(serviceName) != -1) {
                size++;
                flag = true;
            }
        }
        LogUtil.d("WebSocketService", "WebSocketService 数量：" + size);
        return flag;
    }

}
