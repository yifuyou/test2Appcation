package com.base.common.view.flycotablayout.listener;


import androidx.annotation.DrawableRes;

import com.base.common.view.base.BaseFragment;

public interface CustomTabEntity {
    String getTabTitle();

    int getState();

    BaseFragment getFragment();

    @DrawableRes
    int getTabSelectedIcon();

    @DrawableRes
    int getTabUnselectedIcon();
}