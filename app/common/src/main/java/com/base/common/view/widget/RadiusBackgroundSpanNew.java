package com.base.common.view.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.text.style.ReplacementSpan;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RadiusBackgroundSpanNew extends ReplacementSpan {
    private int fontSize = -1;
    private boolean isSp = false;
    private int margin;
    private int padding;
    private int radius;
    private int textColor;
    private int bgColor;
    private int height;

    public RadiusBackgroundSpanNew(int height, int fontSize, int padding, int margin, int radius, @ColorInt int textColor, @ColorInt int bgColor) {
        this.height = height;
        this.fontSize = fontSize;
        this.margin = margin;
        this.radius = radius;
        this.padding = padding;
        this.textColor = textColor;
        this.bgColor = bgColor;
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
        Paint newPaint = getCustomTextPaint(paint);
        return (int) newPaint.measureText(text, start, end) + margin * 2 + padding * 2;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int
            bottom, @NonNull Paint paint) {
        Paint newPaint = getCustomTextPaint(paint);

        int textWidth = (int) newPaint.measureText(text, start, end);


        Paint.FontMetrics fontMetrics = newPaint.getFontMetrics();
        //新字体的高度
        int fontHeight = (int) (fontMetrics.descent - fontMetrics.ascent);
        RectF rect = new RectF();

        rect.left = (int) (x + margin);
        rect.right = rect.left + textWidth + padding * 2;

        rect.top = top + margin + padding;
        rect.bottom = rect.top + height - margin;


//        rect.top = top + margin;
//        rect.bottom = bottom - margin;
//        rect.left = (int) (x + margin);
//        rect.right = rect.left + textWidth + margin;


        paint.setColor(bgColor);
        canvas.drawRoundRect(rect, radius, radius, paint);

        newPaint.setColor(textColor);

//        Paint.FontMetrics fontMetrics = newPaint.getFontMetrics();
        int offsetX = (int) ((rect.right - rect.left - textWidth) / 2) + margin;
        int offsetY = (int) ((y + fontMetrics.ascent + y + fontMetrics.descent) / 2 - (top + bottom) / 2);

        canvas.drawText(text, start, end, x + offsetX, y - offsetY + padding, newPaint);

    }

    private TextPaint getCustomTextPaint(Paint srcPaint) {
        TextPaint textPaint = new TextPaint(srcPaint);
        if (fontSize != -1) {
            textPaint.setTextSize(isSp ? fontSize * textPaint.density : fontSize);
        }
        return textPaint;
    }
}
