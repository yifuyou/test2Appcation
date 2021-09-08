package com.base.common.viewmodel;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Px;
import androidx.databinding.BindingAdapter;
import androidx.databinding.BindingConversion;
import androidx.recyclerview.widget.RecyclerView;

import com.base.common.utils.DensityUtil;
import com.base.common.utils.FontCache;
import com.base.common.view.adapter.MyFlexboxLayoutManager;
import com.base.common.view.adapter.MyGridLayoutManager;
import com.base.common.view.adapter.MyLinearLayoutManager;
import com.base.common.view.roundview.RoundConstraintLayout;
import com.base.common.view.roundview.RoundTextView;
import com.base.common.view.roundview.RoundViewDelegate;
import com.base.common.view.widget.imageView.GlideImageView;
import com.base.common.view.widget.imageView.ImageLoaderUtils;


public class BindingAdapters {


    @BindingAdapter("android:enabled")
    public static void setEnabled(View view, boolean enable) {
        if (view != null) {
            view.setEnabled(enable);
        }
    }

    @BindingAdapter("android:visibility")
    public static void setVisibility(View view, int visibility) {
        if (view != null) {
            switch (visibility) {
                case 0:
                    view.setVisibility(View.GONE);
                    break;
                case 1:
                    view.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    view.setVisibility(View.INVISIBLE);
                    break;
            }
        }
    }

    @BindingAdapter("android:textTypeface")
    public static void setTypeface(TextView textView, int type) {
        Typeface typeface = FontCache.getTypeface(type == 0 ? FontCache.XI : FontCache.XX, textView.getContext());
        Typeface typ = Typeface.create(typeface, textView.getTypeface().getStyle());
        textView.setTypeface(typ);
    }

    /**
     * public static final int NORMAL = 0;
     * public static final int BOLD = 1;
     * public static final int ITALIC = 2;
     * public static final int BOLD_ITALIC = 3;
     *
     * @param textView
     * @param type
     */
    @BindingAdapter("android:textStyle")
    public static void textStyle(TextView textView, int type) {
        switch (type) {
            case 1:
                textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
                break;
            case 2:
                textView.setTypeface(textView.getTypeface(), Typeface.ITALIC);
                break;
            case 3:
                textView.setTypeface(textView.getTypeface(), Typeface.BOLD_ITALIC);
                break;
            default:
                textView.setTypeface(textView.getTypeface(), Typeface.NORMAL);
                break;
        }
    }

    @BindingAdapter({"android:paddingStart"})
    public static void paddingLeft(View view, int left) {
        int top = view.getPaddingTop();
        int right = view.getPaddingRight();
        int bottom = view.getPaddingBottom();
        view.setPadding(left, top, right, bottom);
    }

    @BindingAdapter({"android:paddingTop"})
    public static void paddingTop(View view, int top) {
        int left = view.getPaddingLeft();
        int right = view.getPaddingRight();
        int bottom = view.getPaddingBottom();
        view.setPadding(left, top, right, bottom);
    }


    @BindingAdapter({"android:paddingEnd"})
    public static void paddingEnd(View view, int right) {
        int left = view.getPaddingLeft();
        int top = view.getPaddingTop();
        int bottom = view.getPaddingBottom();
        view.setPadding(left, top, right, bottom);
    }

    @BindingAdapter({"android:paddingBottom"})
    public static void paddingBottom(View view, int bottom) {
        int left = view.getPaddingLeft();
        int top = view.getPaddingTop();
        int right = view.getPaddingRight();
        view.setPadding(left, top, right, bottom);
    }


    @BindingAdapter({"android:urlString"})
    public static void loadImageByUrl(ImageView image, String url) {
        ImageLoaderUtils.loadImage(image, url);
    }

    @BindingAdapter({"android:urlObj"})
    public static void loadImageByUrl(ImageView image, Object url) {
        ImageLoaderUtils.loadImage(image, url);
    }

    @BindingAdapter({"android:src"})
    public static void loadImage(ImageView image, Object url) {
        ImageLoaderUtils.loadImage(image, url);
    }

    @BindingAdapter({"android:selected"})
    public static void selected(View view, boolean isSelected) {
        view.setSelected(isSelected);
    }

    @BindingAdapter({"android:layout_marginStart"})
    public static void setMarginStart(View view, int marginStart) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        if (params != null) {
            params.setMarginStart(marginStart);
            view.setLayoutParams(params);
        }
    }


    @BindingAdapter({"android:drawableTop"})
    public static void drawableTop(TextView textView, int drawableRes) {
        textView.setCompoundDrawablesWithIntrinsicBounds(0, drawableRes, 0, 0);
    }

    @BindingAdapter({"android:drawableRight"})
    public static void drawableRight(TextView textView, int drawableRes) {
        textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawableRes, 0);
    }

    @BindingAdapter({"android:drawableLeft"})
    public static void drawableLeft(TextView textView, int drawableRes) {
        if (drawableRes != -1) {
            textView.setCompoundDrawablesWithIntrinsicBounds(drawableRes, 0, 0, 0);
        }
    }


    @BindingAdapter("android:GridLayoutManager")
    public static void setLayoutManager(RecyclerView recyclerView, int spanCount) {
        recyclerView.setLayoutManager(new MyGridLayoutManager(recyclerView.getContext(), spanCount));
    }

    @BindingAdapter("android:LinearLayoutManager")
    public static void setLinearLayoutManager(RecyclerView recyclerView, int type) {
        switch (type) {
            case 0:
                recyclerView.setLayoutManager(new MyLinearLayoutManager(recyclerView.getContext(), RecyclerView.HORIZONTAL, false));
                break;
            case 1:
                recyclerView.setLayoutManager(new MyLinearLayoutManager(recyclerView.getContext()));
                break;
            default:
                recyclerView.setLayoutManager(new MyFlexboxLayoutManager(recyclerView.getContext()));
                break;
        }

    }


    @BindingAdapter("android:rv_strokeColor")
    public static void setStrokeColor(View textView, int color) {
        if (textView instanceof RoundViewDelegate.onRoundViewDelegateInter) {
            RoundViewDelegate.onRoundViewDelegateInter viewDelegateInter = (RoundViewDelegate.onRoundViewDelegateInter) textView;
            viewDelegateInter.getDelegate().setStrokeColor(color);
        }
    }

    @BindingAdapter("android:rv_strokeWidth")
    public static void setStrokeWidth(View textView, float wdp) {
        if (textView instanceof RoundViewDelegate.onRoundViewDelegateInter) {
            RoundViewDelegate.onRoundViewDelegateInter viewDelegateInter = (RoundViewDelegate.onRoundViewDelegateInter) textView;
            viewDelegateInter.getDelegate().setStrokeWidth(wdp);
        }
    }

    @BindingAdapter("android:rv_backgroundColor")
    public static void setBackgroundColor(View textView, int color) {
        if (textView instanceof RoundViewDelegate.onRoundViewDelegateInter) {
            RoundViewDelegate.onRoundViewDelegateInter viewDelegateInter = (RoundViewDelegate.onRoundViewDelegateInter) textView;
            viewDelegateInter.getDelegate().setBackgroundColor(color);
        }
    }

    @BindingAdapter("android:rv_background_startColor")
    public static void setBackgroundStartColor(View textView, int color) {
        if (textView instanceof RoundViewDelegate.onRoundViewDelegateInter) {
            RoundViewDelegate.onRoundViewDelegateInter viewDelegateInter = (RoundViewDelegate.onRoundViewDelegateInter) textView;
            viewDelegateInter.getDelegate().setStartColor(color);
        }
    }

    @BindingAdapter("android:rv_background_endColor")
    public static void setBackgroundEndColor(View textView, int color) {
        if (textView instanceof RoundViewDelegate.onRoundViewDelegateInter) {
            RoundViewDelegate.onRoundViewDelegateInter viewDelegateInter = (RoundViewDelegate.onRoundViewDelegateInter) textView;
            viewDelegateInter.getDelegate().setEndColor(color);
        }
    }


    @BindingAdapter("android:rv_cornerRadius_TL")
    public static void setCornerRadius_TL(View textView, float wdp) {
        if (textView instanceof RoundViewDelegate.onRoundViewDelegateInter) {
            RoundViewDelegate.onRoundViewDelegateInter viewDelegateInter = (RoundViewDelegate.onRoundViewDelegateInter) textView;
            viewDelegateInter.getDelegate().setCornerRadius_TL(DensityUtil.dp2px(wdp));
        }
    }


    @BindingAdapter("android:rv_cornerRadius_TR")
    public static void setCornerRadius_TR(View textView, float wdp) {
        if (textView instanceof RoundViewDelegate.onRoundViewDelegateInter) {
            RoundViewDelegate.onRoundViewDelegateInter viewDelegateInter = (RoundViewDelegate.onRoundViewDelegateInter) textView;
            viewDelegateInter.getDelegate().setCornerRadius_TR(DensityUtil.dp2px(wdp));
        }
    }


    @BindingAdapter("android:rv_cornerRadius_BL")
    public static void setCornerRadius_BL(View textView, float wdp) {
        if (textView instanceof RoundViewDelegate.onRoundViewDelegateInter) {
            RoundViewDelegate.onRoundViewDelegateInter viewDelegateInter = (RoundViewDelegate.onRoundViewDelegateInter) textView;
            viewDelegateInter.getDelegate().setCornerRadius_BL(DensityUtil.dp2px(wdp));
        }
    }


    @BindingAdapter("android:rv_cornerRadius_BR")
    public static void setCornerRadius_BR(View textView, float wdp) {
        if (textView instanceof RoundViewDelegate.onRoundViewDelegateInter) {
            RoundViewDelegate.onRoundViewDelegateInter viewDelegateInter = (RoundViewDelegate.onRoundViewDelegateInter) textView;
            viewDelegateInter.getDelegate().setCornerRadius_BR(DensityUtil.dp2px(wdp));
        }
    }


    /**
     * @param textView
     * @param type     0删除线  1 下划线
     */
    @BindingAdapter("android:deleteLine")
    public static void deleteLine(TextView textView, int type) {
        if (type == 0) {
            textView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);// 删除线
        } else {
            textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        }

    }


    @BindingConversion
    public static ColorDrawable convertColorToDrawable(int color) {
        return new ColorDrawable(color);
    }


}
