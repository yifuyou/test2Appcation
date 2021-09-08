package com.zhihu.matisse.utils;

import android.content.Context;

import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.IncapableCause;
import com.zhihu.matisse.internal.entity.Item;
import com.zhihu.matisse.internal.utils.PhotoMetadataUtils;

import java.util.Set;

public class VideoSizeFilter extends Filter {


    private int mMaxSize;

    VideoSizeFilter(int maxSizeInBytes) {
        mMaxSize = maxSizeInBytes;
    }

    @Override
    public Set<MimeType> constraintTypes() {
        return MimeType.ofVideo();
    }

    @Override
    public IncapableCause filter(Context context, Item item) {
        if (!needFiltering(context, item))
            return null;


        if (item.size > mMaxSize) {
            return new IncapableCause(IncapableCause.DIALOG, "大小不超过" + String.valueOf(PhotoMetadataUtils.getSizeInMB(mMaxSize)+"M"));
        }


        return null;
    }
}
