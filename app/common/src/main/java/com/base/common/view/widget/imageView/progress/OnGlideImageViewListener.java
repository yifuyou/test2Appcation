package com.base.common.view.widget.imageView.progress;


import com.bumptech.glide.load.engine.GlideException;

public interface OnGlideImageViewListener {

    void onProgress(int percent, boolean isDone, GlideException exception);
}
