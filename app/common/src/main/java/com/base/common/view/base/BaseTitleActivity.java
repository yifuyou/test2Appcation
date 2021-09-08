package com.base.common.view.base;

import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Px;
import androidx.annotation.StringRes;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.base.common.R;
import com.base.common.databinding.BaseTitleActivityBinding;
import com.base.common.databinding.BaseTitleToolbarBinding;
import com.base.common.utils.ColorUtils;
import com.base.common.utils.DensityUtil;
import com.base.common.utils.OnClickCheckedUtil;
import com.base.common.utils.ViewUtils;
import com.base.common.view.roundview.RoundTextView;
import com.base.common.viewmodel.BaseViewModel;


/**
 * @author sugar
 * @date 2018/11/2
 */

public abstract class BaseTitleActivity<DB extends ViewDataBinding, VM extends BaseViewModel> extends BaseActivity<DB, VM> {

    protected BaseTitleToolbarBinding mTitleBinding;
    protected BaseTitleActivityBinding baseTitleActivityBinding;

    /**
     * toolbar的颜色是否渐变，
     *
     * @return true  用的是渐变色{@link #getStatusBarColorStart()} {{@link #getStatusBarColorEnd()}}
     * false 不用渐变色 {{@link #getStatusBarColor()}}
     */
    public boolean isToolbarColorGradient() {
        return false;
    }

    /**
     * toolbar的文字颜色是否为白色
     *
     * @return
     */
    protected boolean isToolBarWhiteTextTheme() {
        return false;
    }


    @Override
    protected void initContentView(@LayoutRes int layoutResID) {
        baseTitleActivityBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.base_title_activity, null, false);

        binding = DataBindingUtil.inflate(getLayoutInflater(), layoutResID, baseTitleActivityBinding.flContainer, false);
        binding.setLifecycleOwner(this);

        baseTitleActivityBinding.flContainer.addView(binding.getRoot());
        baseTitleActivityBinding.itemView.setBackgroundResource(getBackgroundRootColor());

        setContentView(baseTitleActivityBinding.getRoot());
        mTitleBinding = baseTitleActivityBinding.inToolbar;

        mTitleBinding.ivImageLeftClose.setOnClickListener(new OnClickCheckedUtil() {
            @Override
            public void onClicked(View view) {
                finish();
            }
        });


        //如果toolbar覆盖在Container上面，则toolbar颜色当为透明，
        if (isToolbarCoverage()) {
            mTitleBinding.rllToolbarItemView.getDelegate().setStartColor(0);
            mTitleBinding.rllToolbarItemView.getDelegate().setEndColor(0);
        } else {
            //如果tools渐变色
            if (isToolbarColorGradient()) {
                mTitleBinding.rllToolbarItemView.getDelegate().setStartColor(ColorUtils.getColor(getStatusBarColorStart()));
                mTitleBinding.rllToolbarItemView.getDelegate().setEndColor(ColorUtils.getColor(getStatusBarColorEnd()));
                mTitleBinding.rllToolbarItemView.getDelegate().setGradientOrientation(getGradientOrientation());
            } else {
                mTitleBinding.rllToolbarItemView.getDelegate().setStartColor(0);
                mTitleBinding.rllToolbarItemView.getDelegate().setEndColor(0);
                //和状态栏一样的颜色
                mTitleBinding.rllToolbarItemView.getDelegate().setBackgroundColor(ColorUtils.getColor(getStatusBarColor()));
            }

            ViewUtils.setViewMarginTop(baseTitleActivityBinding.flContainer, DensityUtil.getDimens(R.dimen.dp_87) + topMarginTitle(), 0);
        }

        setTitleTextTheme(isToolBarWhiteTextTheme());
        initToolBar(mTitleBinding);
    }

    public void setBackgroundColor(@ColorInt int colorInt) {
        baseTitleActivityBinding.flContainer.setBackgroundColor(colorInt);
    }

    public void setBackgroundColorResource(@ColorRes int colorRes) {
        baseTitleActivityBinding.flContainer.setBackgroundColor(ColorUtils.getColor(colorRes));
    }


    public void initToolBar(BaseTitleToolbarBinding mTitleBinding) {

    }

    @Override
    protected boolean isStatusBarDarkFont() {
        return true;
    }

    //toolbar是否覆盖在Container上面，return true则toolbar透明
    public boolean isToolbarCoverage() {
        return false;
    }

    /**
     * 内容距toolBar多少距离
     *
     * @return
     */
    @Px
    protected int topMarginTitle() {
        return 0;
    }


    @ColorRes
    protected int getStatusBarColorStart() {
        return R.color.colorStatusBarStart;
    }

    @ColorRes
    protected int getStatusBarColorEnd() {
        return R.color.colorStatusBarEnd;
    }

    //页面背景色
    @ColorRes
    protected int getBackgroundRootColor() {
        return R.color.colorBackground;
    }


    /**
     * toolbar颜色渐变方向
     *
     * @return 0 左右 1 上下  2左上到右下
     */
    protected int getGradientOrientation() {
        return 1;
    }


    public void setToolBarBack(@ColorRes int colorRes) {
        mTitleBinding.clToolbar.setBackgroundColor(ColorUtils.getColor(colorRes));
    }

    public void setVisibilityIvClose(boolean isShow) {
        if (isShow) {
            mTitleBinding.ivImageLeftClose.setVisibility(View.VISIBLE);
            mTitleBinding.tvTextLeft.setVisibility(View.GONE);
        } else mTitleBinding.ivImageLeftClose.setVisibility(View.GONE);
    }

    public void setVisibilityTextLeft(boolean isShow) {
        if (isShow) {
            mTitleBinding.ivImageLeftClose.setVisibility(View.GONE);
            mTitleBinding.tvTextLeft.setVisibility(View.VISIBLE);
        } else mTitleBinding.ivImageLeftClose.setVisibility(View.GONE);
    }

    public void setTextLeft(String text) {
        mTitleBinding.tvTextLeft.setText(text);
    }

    public void setTitle(String title) {
        mTitleBinding.tvTitleCenter.setText(title);
    }

    public void setTitle(@StringRes int resid) {
        mTitleBinding.tvTitleCenter.setText(resid);
    }


    public RoundTextView getTextRight() {
        return mTitleBinding.tvTextRight;
    }

    public void setRightText(String text) {
        mTitleBinding.tvTextRight.setText(text);
    }

    public void setRightTextOnClick(View.OnClickListener onClick) {
        mTitleBinding.tvTextRight.setOnClickListener(onClick);
    }


    public void setRightImg(int resId) {
        mTitleBinding.tvTextRight.setVisibility(View.GONE);
        mTitleBinding.ivTextRight.setVisibility(View.VISIBLE);
        mTitleBinding.ivTextRight.setImageResource(resId);
    }

    public void setRightImgOnClick(View.OnClickListener onClick) {
        mTitleBinding.ivTextRight.setOnClickListener(onClick);
    }

    /**
     * 设尾部分隔线是否显示
     *
     * @param isShow
     */
    public void setBottomLineVisibility(boolean isShow) {
        int visibility = isShow ? View.VISIBLE : View.GONE;
        baseTitleActivityBinding.vBottomLine.setVisibility(visibility);
    }

    public View getBottomView() {
        return baseTitleActivityBinding.llBottom;
    }


    public View getContentView() {
        return baseTitleActivityBinding.flContainer;
    }


    /**
     * 设置标题文字的主题  黑或白
     */
    private void setTitleTextTheme(boolean isWhite) {
        if (isWhite) {
            mTitleBinding.ivImageLeftClose.setImageResource(R.mipmap.ic_back_white);
            mTitleBinding.tvTitleCenter.setTextColor(ColorUtils.getColor(R.color.textColor_FFFFFF));
            mTitleBinding.tvTextRight.setTextColor(ColorUtils.getColor(R.color.textColor_FFFFFF));
            mTitleBinding.tvTextLeft.setTextColor(ColorUtils.getColor(R.color.textColor_FFFFFF));
        } else {
            mTitleBinding.ivImageLeftClose.setImageResource(R.mipmap.ic_back_black);
            mTitleBinding.tvTitleCenter.setTextColor(ColorUtils.getColor(R.color.textColor_333333));
            mTitleBinding.tvTextRight.setTextColor(ColorUtils.getColor(R.color.textColor_333333));
            mTitleBinding.tvTextLeft.setTextColor(ColorUtils.getColor(R.color.textColor_333333));
        }

    }


}
