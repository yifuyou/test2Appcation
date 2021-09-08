package com.base.common.view.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.base.common.R;
import com.base.common.utils.DensityUtil;
import com.base.common.utils.JavaMethod;
import com.base.common.utils.LogUtil;
import com.base.common.viewmodel.BaseViewModel;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;


/**
 * 生命周期
 *
 * @param <DB>
 * @param <VM>
 */

public abstract class BaseFragment<DB extends ViewDataBinding, VM extends BaseViewModel> extends BaseInitDataFragment {


    protected View mRoot;


    protected DB binding;
    protected VM viewModel;
    protected BaseViewUtils viewUtils; //统一处理view的工具类

    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d("BaseFragment", getClass().getSimpleName());
        if (null == viewModel) {
            Class<?> cls = null;
            //如果是BaseFragment的直接子类
            if (getClass().getSuperclass() == BaseFragment.class) {
                cls = getClass();
            } else {
                cls = getChildClass(getClass());
            }

            if (cls == null) return;
            cls = JavaMethod.getGenericClass(cls, 1);
            //如果被继承的是获取不到
            if (null == cls)
                viewModel = null;
            else{
                viewModel = new ViewModelProvider(this).get((Class<VM>) cls);
//                viewModel = ViewModelProviders.of(this).get((Class<VM>) cls);
            }

        }


    }

    protected abstract DB initDataBinding(LayoutInflater inflater, ViewGroup container);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.AppTheme);
        // clone the inflater using the ContextThemeWrapper
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
//        binding = DataBindingUtil.inflate(localInflater, getLayoutId(), container, false);
        binding = initDataBinding(localInflater, container);
        binding.setLifecycleOwner(this);
        mRoot = binding.getRoot();
        viewUtils = new BaseViewUtils(this, this, viewModel);
        return mRoot;
    }


    /**
     * @return 返回父类是baseFragment的类型 或父类是泛型的类
     */
    private Class<?> getChildClass(Class<?> cls) {
        if (cls != null) {
            if (cls.getSuperclass() == BaseFragment.class) {
                return cls;
            } else {
                //获得带有泛型的父类
                Type genType = cls.getGenericSuperclass();
                //ParameterizedType参数化类型，即泛型
                if (genType instanceof ParameterizedType) {
                    return cls;
                } else {
                    return getChildClass(cls.getSuperclass());
                }
            }
        } else {
            return null;
        }
    }


    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }


    public void addFragment(@IdRes int containerViewId, Fragment fragment) {
        mActivity.getSupportFragmentManager()
                .beginTransaction()
                .add(containerViewId, fragment).commitAllowingStateLoss();
    }

    public void addFragment(Fragment fragment) {
        mActivity.getSupportFragmentManager()
                .beginTransaction()
                .add(fragment, fragment.getClass().getSimpleName()).commitAllowingStateLoss();
    }

    public void removeFragment(Fragment fragment) {
        mActivity.getSupportFragmentManager()
                .beginTransaction()
                .remove(fragment).commit();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (viewModel != null) {
            viewModel.onDestroy();
        }
        mRoot = null;
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
            } else if (fragment instanceof BaseDialogFragment) {
                BaseDialogFragment baseFragment = (BaseDialogFragment) fragment;
                isCos = baseFragment.onBackPressed();
                if (isCos) break;
            }
        }
        return isCos;
    }



    public View getRootView() {
        return mRoot;
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