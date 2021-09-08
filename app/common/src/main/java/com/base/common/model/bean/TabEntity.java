package com.base.common.model.bean;

import com.base.common.view.base.BaseFragment;
import com.base.common.view.flycotablayout.listener.CustomTabEntity;

public class TabEntity implements CustomTabEntity {

    public String title;
    private int state;
    private BaseFragment fragment;

    public int selectedIcon;
    public int unSelectedIcon;

    public TabEntity(String title, int state) {
        this.title = title;
        this.state = state;
    }


    public TabEntity(String title, BaseFragment fragment) {
        fragment.setTitle(title);
        this.title = title;
        this.fragment = fragment;
    }

    public TabEntity(String title, int selectedIcon, int unSelectedIcon, BaseFragment fragment) {
        this.title = title;
        this.selectedIcon = selectedIcon;
        this.unSelectedIcon = unSelectedIcon;
        this.fragment = fragment;
        fragment.setTitle(title);
    }


    @Override
    public BaseFragment getFragment() {
        return fragment;
    }

    public String getTabTitle() {
        return title;
    }

    @Override
    public int getTabSelectedIcon() {
        return selectedIcon;
    }

    @Override
    public int getTabUnselectedIcon() {
        return unSelectedIcon;
    }


    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
