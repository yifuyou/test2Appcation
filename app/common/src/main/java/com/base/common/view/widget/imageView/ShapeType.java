package com.base.common.view.widget.imageView;


import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by yangkuo on 2018-07-12.
 */
@IntDef({ShapeType.RECTANGLE, ShapeType.CIRCLE, ShapeType.ROUNDRECT})
@Retention(RetentionPolicy.SOURCE)
public @interface ShapeType {
    int RECTANGLE = 0;//矩形
    int CIRCLE = 1;//圆形
    int ROUNDRECT = 2;
}
