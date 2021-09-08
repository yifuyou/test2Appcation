package com.base.common.view.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.text.Layout;
import android.text.NoCopySpan;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.AlignmentSpan;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.base.common.utils.DensityUtil;
import com.base.common.utils.LogUtil;

import java.lang.reflect.Field;

/**
 * 带展开/收起的TextView
 * 必须在{@link #setOriginalText(CharSequence, boolean...)} 　之前调用{@link #initWidth(int)}
 */
public class ExpandableTextView extends AppCompatTextView {
    private static final String TAG = ExpandableTextView.class.getSimpleName();

    public String ELLIPSIS_STRING = new String(new char[]{'\u2026'});
    private static final int DEFAULT_MAX_LINE = 3;
    public static final String DEFAULT_OPEN_SUFFIX = " 展开";
    public static final String DEFAULT_CLOSE_SUFFIX = " 收起";
    volatile boolean animating = false;
    boolean isClosed = false;
    private int mMaxLines = DEFAULT_MAX_LINE;
    /**
     * TextView可展示宽度，包含paddingLeft和paddingRight
     */
    private int initWidth = 0;
    private int height;
    /**
     * 原始文本
     */
    private CharSequence originalText;

    private SpannableStringBuilder mOpenSpannableStr, mCloseSpannableStr;

    private boolean hasAnimation = true;
    private Animation mOpenAnim, mCloseAnim;
    public int mOpenHeight, mCLoseHeight;
    private boolean mExpandable;
    private boolean mCloseInNewLine = true;
    private boolean isClickEnable = true;

    @Nullable
    private SpannableString mOpenSuffixSpan, mCloseSuffixSpan;
    private String mOpenSuffixStr = DEFAULT_OPEN_SUFFIX;
    private String mCloseSuffixStr = DEFAULT_CLOSE_SUFFIX;
    private int mOpenSuffixColor, mCloseSuffixColor;
    private CharSequenceToSpannableHandler mCharSequenceToSpannableHandler;

    public ExpandableTextView(Context context) {
        super(context);
        initialize();
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public ExpandableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        CustomLinkMovementMethod link = new CustomLinkMovementMethod();
        setMovementMethod(link);
        link.setOnTextClickListener(new CustomLinkMovementMethod.TextClickedListener() {
            @Override
            public void onTextClicked() {
                if (l != null) {
                    l.onClick(ExpandableTextView.this);
                }
            }
        });

    }


    /**
     * 初始化
     */
    private void initialize() {

        mOpenSuffixColor = mCloseSuffixColor = Color.parseColor("#333333");

        setMovementMethod(CustomLinkMovementMethod.getInstance());
//        setIncludeFontPadding(false);
        updateOpenSuffixSpan();
        updateCloseSuffixSpan();
    }

    public void setClickEnable(boolean enable) {
        isClickEnable = enable;
    }

    @Override
    public boolean hasOverlappingRendering() {
        return false;
    }

    public void setOriginalText(CharSequence originalText, boolean... isOpen) {
        if (originalText.toString().startsWith("\n")) {
            String str = originalText.toString();
            int start = 0;
            while (str.substring(start).startsWith("\n")) {
                start++;
            }
            int end = str.length();
            while (str.substring(end).endsWith("\n")) {
                end--;
            }
            originalText = originalText.subSequence(start, end);
        }

        this.originalText = originalText;
        mExpandable = false;
        mCloseSpannableStr = new SpannableStringBuilder();
        final int maxLines = mMaxLines;
        SpannableStringBuilder tempText = charSequenceToSpannable(originalText);
        mOpenSpannableStr = charSequenceToSpannable(originalText);

        if (maxLines != -1) {
            Layout layout = measuredTextViewHeight(tempText).getLayout();
            mExpandable = layout.getLineCount() > maxLines;

            if (mExpandable) {
                //拼接展开内容
                if (mCloseInNewLine) {
                    mOpenSpannableStr.append("\n");
                }
                if (mCloseSuffixSpan != null) {
                    mOpenSpannableStr.append(mCloseSuffixSpan);
                }
                //计算原文截取位置
                int endPos = layout.getLineEnd(maxLines - 1) - 4;
                if (this.originalText.length() <= endPos) {
                    mCloseSpannableStr = charSequenceToSpannable(this.originalText);
                } else {
                    mCloseSpannableStr = charSequenceToSpannable(this.originalText.subSequence(0, endPos));
                }
                SpannableStringBuilder tempText2 = charSequenceToSpannable(mCloseSpannableStr).append(ELLIPSIS_STRING);

                if (mOpenSuffixSpan != null) {
                    tempText2.append(mOpenSuffixSpan);
                }
                int suffixLength = mOpenSuffixSpan == null ? 0 : mOpenSuffixSpan.length();
                int lastSpace = mCloseSpannableStr.length() - suffixLength;
                if (lastSpace >= 0 && this.originalText.length() > lastSpace) {
                    CharSequence redundantChar = this.originalText.subSequence(lastSpace, lastSpace + suffixLength);
                    int offset = hasEnCharCount(redundantChar) - hasEnCharCount(mOpenSuffixSpan) + 1;
                    lastSpace = offset <= 0 ? lastSpace : lastSpace - offset;
                    if (lastSpace >= 0) {
                        mCloseSpannableStr = charSequenceToSpannable(this.originalText.subSequence(0, lastSpace));
                    }
                }

                //计算收起的文本高度
                mCloseSpannableStr.append(ELLIPSIS_STRING);

                if (mOpenSuffixSpan != null) {
                    mCloseSpannableStr.append(mOpenSuffixSpan);
                }
            }
        }

        mCLoseHeight = measuredTextViewHeight(mCloseSpannableStr).getHeight();
        mOpenHeight = measuredTextViewHeight(mOpenSpannableStr).getHeight();

        isClosed = mExpandable;
        if (mExpandable) {
            if (isOpen.length > 0 && isOpen[0]) {
                isClosed = false;
                ExpandableTextView.super.setMaxLines(Integer.MAX_VALUE);
                getLayoutParams().height = mOpenHeight;
                setText(mOpenSpannableStr);
            } else {
                getLayoutParams().height = mCLoseHeight;
                setText(mCloseSpannableStr);
            }

            getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    if (getParent() instanceof ViewGroup) {
                        ViewGroup viewGroup = (ViewGroup) getParent();
                        viewGroup.requestLayout();
                    }
                }
            });

        } else {
            setText(mOpenSpannableStr);
        }

    }


    /**
     * 展开
     */
    private void open() {
        if (hasAnimation) {
//            mCLoseHeight = measuredTextViewHeight(mCloseSpannableStr).getHeight();
//            mOpenHeight = measuredTextViewHeight(mOpenSpannableStr).getHeight();
            executeOpenAnim();
        } else {
            ExpandableTextView.super.setMaxLines(Integer.MAX_VALUE);
            setText(mOpenSpannableStr);
            if (mOpenCloseCallback != null) {
                mOpenCloseCallback.onOpen();
            }
        }
    }

    private int hasEnCharCount(CharSequence str) {
        int count = 0;
        if (!TextUtils.isEmpty(str)) {
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                if (c >= ' ' && c <= '~') {
                    count++;
                }
            }
        }
        return count;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void switchOpenClose() {
        if (isClickEnable) {
            if (mExpandable) {
                isClosed = !isClosed;
                if (isClosed) {
                    close();
                } else {
                    open();
                }
            }
        } else {
            if (this.mOpenCloseCallback != null) {
                this.mOpenCloseCallback.onOpen();
            }
        }
    }

    /**
     * 设置是否有动画
     *
     * @param hasAnimation
     */
    public void setHasAnimation(boolean hasAnimation) {
        this.hasAnimation = hasAnimation;
    }


    /**
     * 收起
     */
    private void close() {
        if (hasAnimation) {
            executeCloseAnim();
        } else {
            ExpandableTextView.super.setMaxLines(mMaxLines);
            getLayoutParams().height = mCLoseHeight;
            setText(mCloseSpannableStr);

            getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    int he = getHeight();
                    LogUtil.d("ExpandableTextView", "textView  " + he);
                    if (getParent() instanceof ViewGroup) {
                        ViewGroup viewGroup = (ViewGroup) getParent();
                        int h = viewGroup.getHeight();
                        LogUtil.d("ExpandableTextView", "viewGroup  " + h);
                        viewGroup.requestLayout();
                    }
                }
            });

            if (mOpenCloseCallback != null) {
                mOpenCloseCallback.onClose();
            }
        }
    }

    private RecyclerView getRecyclerView(View view) {
        if (view == null) return null;
        else if (view instanceof RecyclerView) {
            return (RecyclerView) view;
        } else {
            return getRecyclerView((View) view.getParent());
        }
    }

    public ExpandableTextView measuredTextViewHeight(CharSequence text) {

        ExpandableTextView cloneTextView = new ExpandableTextView(getContext());
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.width = initWidth;
        layoutParams.height = -2;
        setLayoutParams(layoutParams);
        cloneTextView.setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
        cloneTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getTextSize());
        cloneTextView.setTypeface(getTypeface());
        cloneTextView.setIncludeFontPadding(getIncludeFontPadding());
        cloneTextView.setTextAlignment(getTextAlignment());
        cloneTextView.setLineSpacing(getLineSpacingExtra(), getLineSpacingMultiplier());

        cloneTextView.setText(text);
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(initWidth, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(cloneTextView.getHeight(), View.MeasureSpec.UNSPECIFIED);
        /** 当然，measure完后，并不会实际改变View的尺寸，需要调用View.layout方法去进行布局。
         * 调用layout函数后，View的大小将会变成你想要设置成的大小。
         */
        cloneTextView.measure(measuredWidth, measuredHeight);
        cloneTextView.layout(0, 0, cloneTextView.getMeasuredWidth(), cloneTextView.getMeasuredHeight());

        return cloneTextView;
    }


    /**
     * 执行展开动画
     */
    private void executeOpenAnim() {
        //创建展开动画
//        if (mOpenAnim == null) {
        mOpenAnim = new ExpandCollapseAnimation(this, mCLoseHeight, mOpenHeight);
        mOpenAnim.setFillAfter(true);
        mOpenAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                ExpandableTextView.super.setMaxLines(Integer.MAX_VALUE);
                setText(mOpenSpannableStr);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //  动画结束后textview设置展开的状态
                animating = false;
                getLayoutParams().height = -2;
                if (mOpenCloseCallback != null) {
                    mOpenCloseCallback.onOpen();
                }
//                requestLayout();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
//        }

        if (animating) {
            return;
        }
        animating = true;
        clearAnimation();
        //  执行动画
        startAnimation(mOpenAnim);
    }

    /**
     * 执行收起动画
     */
    private void executeCloseAnim() {
        //创建收起动画
//        if (mCloseAnim == null) {
        mCloseAnim = new ExpandCollapseAnimation(this, mOpenHeight, mCLoseHeight);
        mCloseAnim.setFillAfter(true);
        if (animating) {
            return;
        }
        animating = true;
        clearAnimation();

        postDelayed(new Runnable() {
            @Override
            public void run() {
                animating = false;
                ExpandableTextView.super.setMaxLines(mMaxLines);
                getLayoutParams().height = -2;
                setText(mCloseSpannableStr);
            }
        }, mCloseAnim.getDuration());
        //  执行动画
        startAnimation(mCloseAnim);
    }


    /**
     * @param spannable
     * @return
     */
    private Layout createStaticLayout(SpannableStringBuilder spannable) {
        int contentWidth = initWidth - getPaddingLeft() - getPaddingRight();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            StaticLayout.Builder builder = StaticLayout.Builder.obtain(spannable, 0, spannable.length(), getPaint(), contentWidth);
            builder.setAlignment(Layout.Alignment.ALIGN_NORMAL);
            builder.setIncludePad(getIncludeFontPadding());
            builder.setLineSpacing(getLineSpacingExtra(), getLineSpacingMultiplier());
            return builder.build();

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return new StaticLayout(spannable, getPaint(), contentWidth, Layout.Alignment.ALIGN_NORMAL,
                    getLineSpacingMultiplier(), getLineSpacingExtra(), getIncludeFontPadding());
        } else {
            return new StaticLayout(spannable, getPaint(), contentWidth, Layout.Alignment.ALIGN_NORMAL,
                    getFloatField("mSpacingMult", 1f), getFloatField("mSpacingAdd", 0f), getIncludeFontPadding());
        }
    }

    private float getFloatField(String fieldName, float defaultValue) {
        float value = defaultValue;
        if (TextUtils.isEmpty(fieldName)) {
            return value;
        }
        try {
            // 获取该类的所有属性值域
            Field[] fields = this.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (TextUtils.equals(fieldName, field.getName())) {
                    value = field.getFloat(this);
                    break;
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return value;
    }


    /**
     * @param charSequence
     * @return
     */
    private SpannableStringBuilder charSequenceToSpannable(@NonNull CharSequence charSequence) {
        SpannableStringBuilder spannableStringBuilder = null;
        if (mCharSequenceToSpannableHandler != null) {
            spannableStringBuilder = mCharSequenceToSpannableHandler.charSequenceToSpannable(charSequence);
        }
        if (spannableStringBuilder == null) {
            spannableStringBuilder = new SpannableStringBuilder(charSequence);
        }
        return spannableStringBuilder;
    }

    /**
     * 初始化TextView的可展示宽度
     *
     * @param width
     */
    public void initWidth(int width) {
        initWidth = width;
    }

    public int getInitWidth() {
        return initWidth;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public void setMaxLines(int maxLines) {
        this.mMaxLines = maxLines;
        super.setMaxLines(maxLines);
    }

    /**
     * 设置展开后缀text
     *
     * @param openSuffix
     */
    public void setOpenSuffix(String openSuffix) {
        mOpenSuffixStr = openSuffix;
        updateOpenSuffixSpan();
    }

    /**
     * 设置展开后缀文本颜色
     *
     * @param openSuffixColor
     */
    public void setOpenSuffixColor(@ColorInt int openSuffixColor) {
        mOpenSuffixColor = openSuffixColor;
        updateOpenSuffixSpan();
    }

    /**
     * 设置收起后缀text
     *
     * @param closeSuffix
     */
    public void setCloseSuffix(String closeSuffix) {
        mCloseSuffixStr = closeSuffix;
        updateCloseSuffixSpan();
    }

    /**
     * 设置收起后缀文本颜色
     *
     * @param closeSuffixColor
     */
    public void setCloseSuffixColor(@ColorInt int closeSuffixColor) {
        mCloseSuffixColor = closeSuffixColor;
        updateCloseSuffixSpan();
    }


    public void setEllipsisString(String str) {
        ELLIPSIS_STRING = str;
    }

    /**
     * 收起后缀是否另起一行
     *
     * @param closeInNewLine
     */
    public void setCloseInNewLine(boolean closeInNewLine) {
        mCloseInNewLine = closeInNewLine;
        updateCloseSuffixSpan();
    }

    /**
     * 更新展开后缀Spannable
     */
    private void updateOpenSuffixSpan() {
        if (TextUtils.isEmpty(mOpenSuffixStr)) {
            mOpenSuffixSpan = null;
            return;
        }
        mOpenSuffixSpan = new SpannableString(mOpenSuffixStr);
        mOpenSuffixSpan.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, mOpenSuffixStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mOpenSuffixSpan.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                switchOpenClose();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(mOpenSuffixColor);
                ds.setUnderlineText(false);
            }
        }, 0, mOpenSuffixStr.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
    }

    /**
     * 更新收起后缀Spannable
     */
    private void updateCloseSuffixSpan() {
        if (TextUtils.isEmpty(mCloseSuffixStr)) {
            mCloseSuffixSpan = null;
            return;
        }
        mCloseSuffixSpan = new SpannableString(mCloseSuffixStr);
        mCloseSuffixSpan.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, mCloseSuffixStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (mCloseInNewLine) {
            AlignmentSpan alignmentSpan = new AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE);
            mCloseSuffixSpan.setSpan(alignmentSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        mCloseSuffixSpan.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                switchOpenClose();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(mCloseSuffixColor);
                ds.setUnderlineText(false);
            }
        }, 0, mCloseSuffixStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }


    public OpenAndCloseCallback mOpenCloseCallback;

    public void setOpenAndCloseCallback(OpenAndCloseCallback callback) {
        this.mOpenCloseCallback = callback;
    }

    public interface OpenAndCloseCallback {
        void onOpen();

        void onClose();
    }

    /**
     * 设置文本内容处理
     *
     * @param handler
     */
    public void setCharSequenceToSpannableHandler(CharSequenceToSpannableHandler handler) {
        mCharSequenceToSpannableHandler = handler;
    }

    public interface CharSequenceToSpannableHandler {
        @NonNull
        SpannableStringBuilder charSequenceToSpannable(CharSequence charSequence);
    }

    class ExpandCollapseAnimation extends Animation {
        private final View mTargetView;//动画执行view
        private final int mStartHeight;//动画执行的开始高度
        private final int mEndHeight;//动画结束后的高度

        ExpandCollapseAnimation(View target, int startHeight, int endHeight) {
            mTargetView = target;
            mStartHeight = startHeight;
            mEndHeight = endHeight;
            setDuration(400);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            mTargetView.setScrollY(0);
            //计算出每次应该显示的高度,改变执行view的高度，实现动画
            ViewGroup.LayoutParams layoutParams = mTargetView.getLayoutParams();
            layoutParams.height = (int) ((mEndHeight - mStartHeight) * interpolatedTime + mStartHeight);
            mTargetView.setLayoutParams(layoutParams);

//            Rect rect = DensityUtil.getViewRectInWindow(mTargetView);
//            int top = rect.top;
//            LogUtil.d("mTargetView    startHeight  " + mStartHeight + "  top   " + top);
//            LogUtil.d("mTargetView    " + layoutParams.height);
        }
    }

    public static class MyLinkedMovementMethod extends LinkMovementMethod {
        private static MyLinkedMovementMethod sInstance;

        public static MyLinkedMovementMethod getInstance() {
            if (sInstance == null)
                sInstance = new MyLinkedMovementMethod();
            return sInstance;
        }

        @Override
        public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
            // 因为TextView没有点击事件，所以点击TextView的非富文本时，super.onTouchEvent()返回false；
            // 此时可以让TextView的父容器执行点击事件；
            boolean isConsume = super.onTouchEvent(widget, buffer, event);
            if (!isConsume && event.getAction() == MotionEvent.ACTION_UP) {
                ViewParent parent = widget.getParent();
                if (parent instanceof ViewGroup) {
                    // 获取被点击控件的父容器，让父容器执行点击；
                    ((ViewGroup) parent).performClick();
                }
            }
            return isConsume;
        }
    }

    public static class OverLinkMovementMethod extends LinkMovementMethod {

        public static boolean canScroll = false;

        @Override
        public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
            int action = event.getAction();

            if (action == MotionEvent.ACTION_MOVE) {
                if (!canScroll) {
                    return true;
                }
            }

            return super.onTouchEvent(widget, buffer, event);
        }

        public static MovementMethod getInstance() {
            if (sInstance == null)
                sInstance = new OverLinkMovementMethod();

            return sInstance;
        }

        private static OverLinkMovementMethod sInstance;
        private static Object FROM_BELOW = new NoCopySpan.Concrete();
    }


    public static class CustomLinkMovementMethod extends ScrollingMovementMethod {
        private static final int CLICK = 1;
        private static final int UP = 2;
        private static final int DOWN = 3;

        public interface TextClickedListener {
            void onTextClicked();
        }

        TextClickedListener listener = null;

        public void setOnTextClickListener(TextClickedListener listen) {
            listener = listen;
        }

        @Override
        public boolean onKeyDown(TextView widget, Spannable buffer,
                                 int keyCode, KeyEvent event) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_CENTER:
                case KeyEvent.KEYCODE_ENTER:
                    if (event.getRepeatCount() == 0) {
                        if (action(CLICK, widget, buffer)) {
                            return true;
                        }
                    }
            }

            return super.onKeyDown(widget, buffer, keyCode, event);
        }

        @Override
        protected boolean up(TextView widget, Spannable buffer) {
            if (action(UP, widget, buffer)) {
                return true;
            }

            return super.up(widget, buffer);
        }

        @Override
        protected boolean down(TextView widget, Spannable buffer) {
            if (action(DOWN, widget, buffer)) {
                return true;
            }

            return super.down(widget, buffer);
        }

        @Override
        protected boolean left(TextView widget, Spannable buffer) {
            if (action(UP, widget, buffer)) {
                return true;
            }

            return super.left(widget, buffer);
        }

        @Override
        protected boolean right(TextView widget, Spannable buffer) {
            if (action(DOWN, widget, buffer)) {
                return true;
            }

            return super.right(widget, buffer);
        }

        private boolean action(int what, TextView widget, Spannable buffer) {
            boolean handled = false;

            Layout layout = widget.getLayout();

            int padding = widget.getTotalPaddingTop() +
                    widget.getTotalPaddingBottom();
            int areatop = widget.getScrollY();
            int areabot = areatop + widget.getHeight() - padding;

            int linetop = layout.getLineForVertical(areatop);
            int linebot = layout.getLineForVertical(areabot);

            int first = layout.getLineStart(linetop);
            int last = layout.getLineEnd(linebot);

            ClickableSpan[] candidates = buffer.getSpans(first, last, ClickableSpan.class);

            int a = Selection.getSelectionStart(buffer);
            int b = Selection.getSelectionEnd(buffer);

            int selStart = Math.min(a, b);
            int selEnd = Math.max(a, b);

            if (selStart < 0) {
                if (buffer.getSpanStart(FROM_BELOW) >= 0) {
                    selStart = selEnd = buffer.length();
                }
            }

            if (selStart > last)
                selStart = selEnd = Integer.MAX_VALUE;
            if (selEnd < first)
                selStart = selEnd = -1;

            switch (what) {
                case CLICK:
                    if (selStart == selEnd) {
                        return false;
                    }

                    ClickableSpan[] link = buffer.getSpans(selStart, selEnd, ClickableSpan.class);

                    if (link.length != 1)
                        return false;

                    link[0].onClick(widget);
                    break;

                case UP:
                    int beststart, bestend;

                    beststart = -1;
                    bestend = -1;

                    for (int i = 0; i < candidates.length; i++) {
                        int end = buffer.getSpanEnd(candidates[i]);

                        if (end < selEnd || selStart == selEnd) {
                            if (end > bestend) {
                                beststart = buffer.getSpanStart(candidates[i]);
                                bestend = end;
                            }
                        }
                    }

                    if (beststart >= 0) {
                        Selection.setSelection(buffer, bestend, beststart);
                        return true;
                    }

                    break;

                case DOWN:
                    beststart = Integer.MAX_VALUE;
                    bestend = Integer.MAX_VALUE;

                    for (int i = 0; i < candidates.length; i++) {
                        int start = buffer.getSpanStart(candidates[i]);

                        if (start > selStart || selStart == selEnd) {
                            if (start < beststart) {
                                beststart = start;
                                bestend = buffer.getSpanEnd(candidates[i]);
                            }
                        }
                    }

                    if (bestend < Integer.MAX_VALUE) {
                        Selection.setSelection(buffer, beststart, bestend);
                        return true;
                    }

                    break;
            }

            return false;
        }

        public boolean onKeyUp(TextView widget, Spannable buffer,
                               int keyCode, KeyEvent event) {
            return false;
        }

        @Override
        public boolean onTouchEvent(TextView widget, Spannable buffer,
                                    MotionEvent event) {
            int action = event.getAction();

            if (action == MotionEvent.ACTION_UP ||
                    action == MotionEvent.ACTION_DOWN) {
                int x = (int) event.getX();
                int y = (int) event.getY();

                x -= widget.getTotalPaddingLeft();
                y -= widget.getTotalPaddingTop();

                x += widget.getScrollX();
                y += widget.getScrollY();

                Layout layout = widget.getLayout();
                int line = layout.getLineForVertical(y);
                int off = layout.getOffsetForHorizontal(line, x);

                ClickableSpan[] link = buffer.getSpans(off, off, ClickableSpan.class);

                if (link.length != 0) {
                    if (action == MotionEvent.ACTION_UP) {
                        link[0].onClick(widget);
                    } else if (action == MotionEvent.ACTION_DOWN) {
                        Selection.setSelection(buffer,
                                buffer.getSpanStart(link[0]),
                                buffer.getSpanEnd(link[0]));
                    }

                    return true;
                } else {
                    Selection.removeSelection(buffer);

                    if (action == MotionEvent.ACTION_UP) {
                        if (listener != null)
                            listener.onTextClicked();
                    }
                }
            }

            return super.onTouchEvent(widget, buffer, event);
        }


        public void initialize(TextView widget, Spannable text) {
            Selection.removeSelection(text);
            text.removeSpan(FROM_BELOW);
        }

        public void onTakeFocus(TextView view, Spannable text, int dir) {
            Selection.removeSelection(text);

            if ((dir & View.FOCUS_BACKWARD) != 0) {
                text.setSpan(FROM_BELOW, 0, 0, Spannable.SPAN_POINT_POINT);
            } else {
                text.removeSpan(FROM_BELOW);
            }
        }

        public static MovementMethod getInstance() {
            if (sInstance == null)
                sInstance = new CustomLinkMovementMethod();

            return sInstance;
        }

        private static CustomLinkMovementMethod sInstance;
        private static Object FROM_BELOW = new NoCopySpan.Concrete();
    }

}
