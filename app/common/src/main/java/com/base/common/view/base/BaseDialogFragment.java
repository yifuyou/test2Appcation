package com.base.common.view.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.base.common.R;
import com.base.common.app.LoginUtils;
import com.base.common.utils.DensityUtil;
import com.base.common.utils.JavaMethod;
import com.base.common.utils.LogUtil;
import com.base.common.utils.matisse.ImageEditUtils;
import com.base.common.view.widget.statelayout.StateLayout;
import com.base.common.viewmodel.BaseViewModel;
import com.trello.rxlifecycle3.components.support.RxAppCompatDialogFragment;

import java.util.List;


public abstract class BaseDialogFragment<DB extends ViewDataBinding, VM extends BaseViewModel> extends RxAppCompatDialogFragment implements StateLayout.OnViewRefreshListener {


    private boolean mIsShowAnimation = true;

    protected DB binding;
    protected VM viewModel;
    protected BaseViewUtils viewUtils; //统一处理view的工具类
    private View mRoot;
    private float dimAmount = 0.35f;//不透明度


    @ColorInt
    private int bgDrawableRes = 0;  //背景色
    private Drawable bgDrawable = null;//背景色

    protected BaseActivity mActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //在配置变化的时候将这个fragment保存下来  Failure saving state: active xxxFragment{53e882a} has cleared index: -1
//        setRetainInstance(true);

        LogUtil.d(this.getClass().getSimpleName(), "onCreate() ");
        if (null == viewModel) {
            Class<?> cls = JavaMethod.getGenericClass(this, 1);
            if (null == cls)
                viewModel = null;
            else {
                viewModel = new ViewModelProvider(this).get((Class<VM>) cls);
//                viewModel = ViewModelProviders.of(this).get((Class<VM>) cls);
            }
        }
        mActivity = (BaseActivity) getActivity();

        setStyle(STYLE_NO_TITLE, R.style.dialog_no_bg);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        LogUtil.d(this.getClass().getSimpleName(), "onCreateView() ");
        binding = initDataBinding(inflater, container);
        binding.setLifecycleOwner(this);
        mRoot = binding.getRoot();
        viewUtils = new BaseViewUtils(this, this, viewModel);
        initNotificationReceipt();
        initView();
        initData();
        return mRoot;
    }

    protected abstract DB initDataBinding(LayoutInflater inflater, ViewGroup container);

    /**
     * 接收事件通知
     */
    protected void initNotificationReceipt() {

    }

    public void initView() {
        LogUtil.d(this.getClass().getSimpleName(), "initView() ");

    }

    public void initData() {
        LogUtil.d(this.getClass().getSimpleName(), "initData() ");
    }

    public void finish() {
        mActivity.removeFragment(this);
    }

    public void addFragment(Fragment fragment) {
        mActivity.getSupportFragmentManager()
                .beginTransaction()
                .add(fragment, fragment.getClass().getSimpleName()).commit();
    }

    public void addFragment(@IdRes int containerViewId, Fragment fragment) {
        getChildFragmentManager()
                .beginTransaction()
                .add(containerViewId, fragment).commitAllowingStateLoss();
    }

    public void removeFragment(Fragment fragment) {
        mActivity.getSupportFragmentManager()
                .beginTransaction()
                .remove(fragment).commit();
    }

    public LifecycleOwner getLifecycleOwner() {
        return this;
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtil.d(this.getClass().getSimpleName(), "onStart() ");
        if (getDialog() == null) return;


        getDialog().setCanceledOnTouchOutside(isCancel());
        getDialog().setCancelable(isCancel());
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    // 返回键
                    case KeyEvent.KEYCODE_BACK:
                        if (!isCancel()) {
                            return true;
                        }
                    default:
                        break;
                }
                return false;
            }
        });


        Window window = getDialog().getWindow();
        if (window == null) return;
        if (isShowAnimation()) {
            window.getAttributes().windowAnimations = R.style.Animation_Bottom_Rising;
        }
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = getGravity(); // 紧贴底部

        lp.width = getWidth(); // 宽度持平

        lp.height = getHeight();

        //   当参数值包含Gravity.LEFT时,对话框出现在左边,所以params.x就表示相对左边的偏移
        //   当参数值包含Gravity.RIGHT时,对话框出现在右边,所以params.x就表示相对右边的偏移
        //   当参数值包含Gravity.TOP时,对话框出现在上边,所以params.y就表示相对上边的偏移
        //   当参数值包含Gravity.BOTTOM时,对话框出现在下边,所以params.y就表示相对下边的偏移

        lp.x = skewingX();
        lp.y = skewingY();


        lp.dimAmount = getDimAmount();
        window.setAttributes(lp);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        if (bgDrawable != null) {
            window.setBackgroundDrawable(bgDrawable);
        } else if (getBgDrawableRes() != 0) {
//            window.setBackgroundDrawableResource(getBgDrawableRes());
            window.setBackgroundDrawable(new ColorDrawable(getBgDrawableRes()));
        } else {
            window.setBackgroundDrawable(new BitmapDrawable());
        }
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


    public View getRootView() {
        return mRoot;
    }

    /**
     * 设置位置
     *
     * @return Gravity.TOP
     * Gravity.BOTTOM
     * Gravity.LEFT
     * Gravity.RIGHT
     */
    protected int getGravity() {
        return Gravity.BOTTOM;
    }

    /**
     * 设置宽度
     *
     * @return WindowManager.LayoutParams.MATCH_PARENT
     * WindowManager.LayoutParams.WRAP_CONTENT
     */
    protected int getWidth() {
        return WindowManager.LayoutParams.MATCH_PARENT;
    }

    /**
     * 设置高度
     *
     * @return WindowManager.LayoutParams.MATCH_PARENT
     * WindowManager.LayoutParams.WRAP_CONTENT
     */
    protected int getHeight() {
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }

    /**
     * 设置是否可以cancel
     *
     * @return
     */
    protected boolean isCancel() {
        return true;
    }

    /**
     * 设轩x的偏移方向
     * 当参数值包含Gravity.LEFT时,对话框出现在左边,所以params.x就表示相对左边的偏移
     * 当参数值包含Gravity.RIGHT时,对话框出现在右边,所以params.x就表示相对右边的偏移
     * 当参数值包含Gravity.TOP时,对话框出现在上边,所以params.y就表示相对上边的偏移
     * 当参数值包含Gravity.BOTTOM时,对话框出现在下边,所以params.y就表示相对下边的偏移
     */
    protected int skewingX() {
        return 0;
    }

    protected int skewingY() {
        return 0;
    }

    @Override
    @Deprecated
    public final void onDestroyView() {
        LogUtil.d(this.getClass().getSimpleName(), "onDestroyView() ");
        super.onDestroyView();
        if (viewModel != null) {
            viewModel.onDestroy();
        }
        mRoot = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //打开相册返回的回调
        //图片选择返回，回调
        if (ImageEditUtils.getInstance().getImageCallBack() != null) {
            ImageEditUtils.getInstance().getImageCallBack().onActivityResult(requestCode, resultCode, data);
        }
    }

    //activity 点击空白处 隐藏软键盘的回调
    public void onHideSoftInput() {
        if (!isAdded()) return;
        List<Fragment> list = getChildFragmentManager().getFragments();
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

    public final boolean isHideSoftInput(int x, int y) {
        if (mRoot != null) {
            Rect rect = DensityUtil.getViewRectInWindow(mRoot);
            if (rect.left <= x && rect.right >= x && rect.top <= y && rect.bottom >= y) {
                boolean isCos = true;
                isCos = isHideInput(x, y);
                if (!isCos) return false;

                List<Fragment> list = getChildFragmentManager().getFragments();
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
        }
        return true;
    }


    public boolean isHideInput(int x, int y) {

        return true;
    }

    //按下返回键
    public boolean onBackPressed() {
        if (!isAdded()) return false;
        List<Fragment> list = getChildFragmentManager().getFragments();
        boolean isCos = false;
        for (Fragment fragment : list) {
            if (fragment instanceof BaseFragment) {
                BaseFragment baseFragment = (BaseFragment) fragment;
                isCos = baseFragment.onBackPressed();
                if (isCos) break;
            }
        }
        return isCos;
    }


    public void showAnimation(boolean show) {
        mIsShowAnimation = show;
    }

    public boolean isShowAnimation() {
        return mIsShowAnimation;
    }

    public void setDimAmount(float dimAmount) {
        this.dimAmount = dimAmount;
    }

    public float getDimAmount() {
        return dimAmount;
    }

    public void setBgDrawableRes(@ColorInt int drawableBgRes) {
        this.bgDrawableRes = drawableBgRes;
    }

    public int getBgDrawableRes() {
        return bgDrawableRes;
    }

    public void setBgDrawable(Drawable bgDrawable) {
        this.bgDrawable = bgDrawable;
    }


}
