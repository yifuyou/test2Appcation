package com.zhihu.matisse.utils;

import android.content.Context;
import android.graphics.Point;

import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.R;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.IncapableCause;
import com.zhihu.matisse.internal.entity.Item;
import com.zhihu.matisse.internal.utils.PhotoMetadataUtils;

import java.util.HashSet;
import java.util.Set;


public class GifSizeFilter extends Filter {

    private int mMinWidth;
    private int mMinHeight;
    private int mMaxSize;

    GifSizeFilter(int minWidth, int minHeight, int maxSizeInBytes) {
        mMinWidth = minWidth;
        mMinHeight = minHeight;
        mMaxSize = maxSizeInBytes;
    }

    @Override
    public Set<MimeType> constraintTypes() {
        return new HashSet<MimeType>() {{
            add(MimeType.GIF);
        }};
    }

    @Override
    public IncapableCause filter(Context context, Item item) {
        if (!needFiltering(context, item))
            return null;

        Point size = PhotoMetadataUtils.getBitmapBound(context.getContentResolver(), item.getContentUri());

        if (mMinWidth != 0 && mMinHeight == 0) {
            if (size.x < mMinWidth || size.y < mMinHeight || item.size > mMaxSize) {
                return new IncapableCause(IncapableCause.DIALOG, context.getString(R.string.error_gif, mMinWidth, String.valueOf(PhotoMetadataUtils.getSizeInMB(mMaxSize))));
            }
        } else {
            if (item.size > mMaxSize) {
                return new IncapableCause(IncapableCause.DIALOG, "大小不超过" + String.valueOf(PhotoMetadataUtils.getSizeInMB(mMaxSize) + "M"));
            }
        }


        return null;
    }
}
