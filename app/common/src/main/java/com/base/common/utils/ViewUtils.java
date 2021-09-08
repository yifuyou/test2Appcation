package com.base.common.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.base.common.utils.UIUtils.getContext;


public class ViewUtils {

    /**
     * 禁止EditText输入特殊字符 和空格
     *
     * @param editText
     */

    public static void setEditTextInhibitInputSpeChat(EditText editText) {
        InputFilter filter = new InputFilter() {
            @Override

            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(" ")) return "";

                String speChat = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*()——+|{}【】‘；：”“'。，、？]";

                Pattern pattern = Pattern.compile(speChat);

                Matcher matcher = pattern.matcher(source.toString());

                if (matcher.find()) return "";

                else return null;

            }

        };

        editText.setFilters(new InputFilter[]{filter});

    }



    /* 把自身从父View中移 */
    public static void removeSelfFromParent(View view) {
        // 先找到父类，再过父类移除子类
        if (view != null) {
            ViewParent parent = view.getParent();
            if (parent instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) parent;
                group.removeView(view);
            }
        }
    }

    /**
     * @param view             调整指定控件的大小，来达到调整控件位置的目的
     * @param defaultTopMargin -1则表示调上一个控件的大小，-2表示前两个，>=0表示调当前view的defaultTopMargin
     */
    public static void setViewBottom(@NonNull final View view, final int defaultTopMargin) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                Point point = DensityUtil.getScreenMetrics(view.getContext());
                Rect rect = DensityUtil.getViewRectOnScreen(view);
                int h = DensityUtil.getStatusBarHeight();
                int b = DensityUtil.getNavigationBarHeight();
                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                if (view.getParent() != null) {
                    //距离底部的距离
                    int spe = point.y - lp.bottomMargin - rect.bottom;

                    //调整指定控件的大小，来达到调整控件位置的目的
                    if (defaultTopMargin < 0) {
                        //获取当前控件的位index;
                        int index = ((ViewGroup) view.getParent()).indexOfChild(view);
                        //检查需要调整的控是否存在
                        int index_previous = index + defaultTopMargin;
                        //如果需要调整的控存在,index_previous就是它的index
                        if (index_previous >= 0) {
                            View previousView = ((ViewGroup) view.getParent()).getChildAt(index_previous);
                            if (previousView != null) {
                                //获取需要调整控件的大小
                                ViewGroup.LayoutParams gpParams = previousView.getLayoutParams();
                                //如果控件没有在屏幕的底部
                                if (spe > 0) {
                                    gpParams.height += spe;
                                    previousView.setLayoutParams(gpParams);
                                }
                                //如果控件超出屏幕的底部，检查是否需要复位
                                else if (spe < 0) {
                                    int minimumHeight = previousView.getMinimumHeight();
                                    if (gpParams.height > minimumHeight) {
                                        gpParams.height = minimumHeight;
                                        previousView.setLayoutParams(gpParams);
                                    }
                                }
                            }
                        }
                    }
                    //调整控件的topMargin来达到调整控件位置的目的
                    else {
                        //如果控件没有在屏幕的底部,将控件设置在底部
                        if (spe > 0) {
                            lp.topMargin += spe;
                            view.setLayoutParams(lp);
                        }
                        //如果控件超出屏幕的底部，检查是否需要复位
                        else if (spe < 0) {
                            if (lp.topMargin > defaultTopMargin) {
                                lp.topMargin = defaultTopMargin;
                                view.setLayoutParams(lp);
                            }
                        }
                    }
                }
            }
        });

    }

    /* 获取根视图 */
    public static View getRootView(Activity act) {
        return act.getWindow().getDecorView().findViewById(android.R.id.content);
    }

    //
//    private static View getRootView(Activity context) {
//        return ((ViewGroup) context.findViewById(android.R.id.content)).getChildAt(0);
//    }
//
    public static <T extends View> T getFragmentView(@NonNull ViewGroup parent, @LayoutRes int layoutResid) {
        return (T) LayoutInflater.from(parent.getContext()).inflate(layoutResid, parent, false);
    }

    public static <T extends ViewDataBinding> T getDataBing(@NonNull ViewGroup parent, @LayoutRes int layoutResid) {
        return (T) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), layoutResid, parent, false);
    }

    public static <T extends ViewDataBinding> T getDataBing(@NonNull Context context, @LayoutRes int layoutResid) {
        return (T) DataBindingUtil.inflate(LayoutInflater.from(context), layoutResid, null, false);
    }

    /**
     * 获取布局
     *
     * @ resId
     * @
     */

    public static <T extends View> T getLayoutView(Context context, @LayoutRes int res_layid) {
        return (T) LayoutInflater.from(context).inflate(res_layid, null);
    }


    public static View getLayoutViewMatch(Context context, @LayoutRes int layoutResId) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        View view = getLayoutView(context, layoutResId);
        view.setLayoutParams(lp);
        return view;
    }

    public static View getLayoutViewWidthMatch(Context context, @LayoutRes int layoutResId) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View view = getLayoutView(context, layoutResId);
        view.setLayoutParams(lp);
        return view;
    }

    public static int getMeasuredTextViewHeight(TextView textView, CharSequence text) {
        if (textView == null || text == null) return 0;

        textView.setText(text);
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(textView.getWidth(), View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(textView.getHeight(), View.MeasureSpec.UNSPECIFIED);
        /** 当然，measure完后，并不会实际改变View的尺寸，需要调用View.layout方法去进行布局。
         * 调用layout函数后，View的大小将会变成你想要设置成的大小。
         */
        textView.measure(measuredWidth, measuredHeight);
        textView.layout(0, 0, textView.getMeasuredWidth(), textView.getMeasuredHeight());

        return textView.getHeight();
    }


    /**
     * 对TextView的简易封装确线程安全，可以在非UI线程调用
     */
    public static void setTextView_Text(final TextView textView, final String text) {
        if (UIUtils.isRunInMainThread()) {
            textView.setText(text);
        } else {
            UIUtils.post(new Runnable() {
                @Override
                public void run() {
                    textView.setText(text);
                }
            });
        }
    }


    public static AlertDialog.Builder showDialog(Context mContext, String title, String message) {
        // 构造对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setMessage(message);
        return builder;
    }

    /**
     * 设轩控件的宽高比
     * 先调整view大小再调整view位置
     *
     * @param isRatioWidth      是宽度在parent中的比例？   false为高度在parent中的比例
     * @param ratioWidth        如果是宽，则宽占比
     * @param ratiowh           宽高比
     * @param inParent==0则不调整位置
     */
    public static void setAspectRatio(@NonNull final View view, final boolean isRatioWidth, final float ratioWidth, final float ratiowh, final float inParent) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                if (view.getParent() == null) return;
                ViewGroup viewGroup = (ViewGroup) view.getParent();
                //获取父控件在屏幕中的位置
                Rect parR = DensityUtil.getViewRectInWindow(viewGroup);
                if (isRatioWidth) {
                    float w = ratioWidth * (parR.right - parR.left);
                    float height = w / ratiowh;
                    setViewSize(view, (int) w, (int) height);
                } else {
                    float h = ratioWidth * (parR.bottom - parR.top);
                    float width = h * ratiowh;
                    setViewSize(view, (int) width, (int) h);
                }

                if (inParent > 0) {

                    //如果view不是viewGroup中第一个控件，则要移除前面的
                    int index = viewGroup.indexOfChild(view);
                    if (index > 0) {
                        for (int i = index - 1; i >= 0; i--) {
                            viewGroup.removeViewAt(i);
                        }
                    }

                    //获取view在屏幕中的位置
                    Rect parView = DensityUtil.getViewRectInWindow(view);

                    //检查view所在的位置是否符合比例inParent
                    int toPostion = (int) ((parR.bottom - parR.top) * inParent) + parR.top;
                    boolean isNeedChanged = toPostion != parView.top;
                    //调整位置
                    if (isNeedChanged) {
                        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                        if (lp != null) {
                            lp.topMargin = toPostion - parR.top;
                            view.setLayoutParams(lp);
                        }
                    }
                }

            }
        });

    }

    /**
     * 设轩控件的位置
     *
     * @param view
     * @param inParentV
     * @param inParentH
     */
    public static void setViewPosition(@NonNull final View view, final float inParentV, final float inParentH) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);


                if (view.getParent() == null) return;
                ViewGroup viewGroup = (ViewGroup) view.getParent();
                //如果view不是viewGroup中第一个控件，则要移除前面的
                int index = viewGroup.indexOfChild(view);
                if (index > 0) {
                    for (int i = index - 1; i >= 0; i--) {
                        viewGroup.removeViewAt(i);
                    }
                }

                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                int w = (int) (viewGroup.getWidth() * inParentH) - lp.leftMargin;
                int h = (int) (viewGroup.getHeight() * inParentV) - lp.topMargin;

                if (inParentH == 0) w = 0;
                if (inParentV == 0) h = 0;


                viewGroup.setPadding(w, h, 0, 0);


            }
        });
    }


    /**
     * 根据坐标查找view  找到就返回
     *
     * @param view
     * @param point 父布局的起始从标
     * @param x
     * @param y
     * @return
     */
    public static View getViewByPosition(@NonNull View view, @NonNull Point point, float x, float y) {

        int parentX = (int) view.getX();
        int parentY = (int) view.getY();

        int left = point.x + parentX;
        int top = point.y + parentY;
        int right = left + view.getWidth();
        int bottom = top + view.getHeight();

        if (view.getVisibility() != View.VISIBLE) {
            return null;
        }
        if (view instanceof ViewGroup) { //当前是ViewGroup容器
            int childCount = ((ViewGroup) view).getChildCount();
            //深度优先， 从最后一个子节点开始遍历，如果找到则返回。 先递归判断子View

            View nowView;
            if (childCount > 0) {
                for (int i = childCount - 1; i >= 0; i--) {
                    nowView = ((ViewGroup) view).getChildAt(i);

                    View topView = getViewByPosition(nowView, new Point(left, top), x, y);
                    if (topView != null) {
                        return topView;
                    }
                }
            }


            //子View都没找到匹配的， 再判断自己
            if (left < x && top < y && right > x && bottom > y) {
                return view;   //当前ViewGroup就是顶层View
            } else {
                return null; //没找到匹配的
            }


        } else { //当前是View

            if (left < x && top < y && right > x && bottom > y) {
                return view;   //当前ViewGroup就是顶层View
            } else {
                return null; //没找到匹配的
            }

        }


    }


    public static void initAutoHeightView(View view, int width) {
        if (view == null) return;

        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(view.getHeight(), View.MeasureSpec.UNSPECIFIED);
        /** 当然，measure完后，并不会实际改变View的尺寸，需要调用View.layout方法去进行布局。
         * 调用layout函数后，View的大小将会变成你想要设置成的大小。
         */
        view.measure(measuredWidth, measuredHeight);
        int left = view.getLeft();
        int top = view.getTop();
        int h = view.getMeasuredHeight();
        int w = view.getMeasuredWidth();
        view.layout(left, top, view.getMeasuredWidth(), view.getMeasuredHeight());

//        setViewSize(view, w, h);

    }

    public static void setViewSize(View view, int x, int y) {
        ViewGroup.LayoutParams laParams = view.getLayoutParams();
        if (laParams != null) {
            laParams.width = x;
            laParams.height = y;
        } else {
            laParams = new ViewGroup.LayoutParams(x, y);
        }
        view.setLayoutParams(laParams);
    }

    public static void setViewMargin(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams laParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            laParams.leftMargin = left;
            laParams.topMargin = top;
            laParams.rightMargin = right;
            laParams.bottomMargin = bottom;
            view.setLayoutParams(laParams);
        }
    }

    public static void setViewMarginTop(View view, int top, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams laParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            laParams.topMargin = top;
            laParams.bottomMargin = bottom;
            view.setLayoutParams(laParams);
        }
    }

    public static void setViewMarginBottom(View view, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams laParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            laParams.bottomMargin = bottom;
            view.setLayoutParams(laParams);
        }
    }

    public static void setViewMarginTop(View view, int top) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams laParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            laParams.topMargin = top;
            view.setLayoutParams(laParams);
        }
    }


    public static void setViewMarginStart(View view, int left, int right) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams laParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            laParams.leftMargin = left;
            laParams.rightMargin = right;
            view.setLayoutParams(laParams);
        }
    }


    public static void setViewWidth(View view, int width) {
        ViewGroup.LayoutParams laParams = view.getLayoutParams();
        if (laParams != null) {
            laParams.width = width;
            view.setLayoutParams(laParams);
        }
    }

    public static void setViewHeight(View view, int height) {
        ViewGroup.LayoutParams laParams = view.getLayoutParams();
        if (laParams != null) {
            laParams.height = height;
            view.setLayoutParams(laParams);
        }
    }

    /**
     * 插入图片
     *
     * @param textView
     * @param drawableId
     * @param str
     */
    public static void insertDrawable(TextView textView, int drawableId, String str) {
        if (textView == null) return;
        final SpannableString ss = new SpannableString(str);
        //得到drawable对象，即所要插入的图片
        Drawable d = ContextCompat.getDrawable(getContext(), drawableId);
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        //用这个drawable对象代替字符串easy
        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
        //包括0但是不包括str.length()即：4。[0,4)。值得注意的是当我们复制这个图片的时候，实际是复制了"easy"这个字符串。
        ss.setSpan(span, 0, str.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.append(ss);
    }


    public static void setMaxLength(TextView editText, int length) {
        if (editText == null) return;
        if (length > 0) {
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});
        }
    }



    /**
     * 获取 文字局中的开始位置
     *
     * @param paint
     * @param height TextView的高度
     */
    public static float getTextCentreHeight(Paint paint, int height) {
        return height / 2f + (paint.getTextSize() - paint.descent()) / 2;
    }


    public static float getTextHeight(Paint mTextPaint) {
//        return paint.getTextSize() - paint.descent();
        return mTextPaint.descent() - mTextPaint.ascent();
    }


}
