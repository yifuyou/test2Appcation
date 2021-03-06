package com.base.common.view.widget.imageView.progress;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;

@GlideModule
public class ProgressAppGlideModule extends AppGlideModule {
//    @Override
//    public boolean isManifestParsingEnabled() {
//        return false;
//    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
//        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(ProgressManager.getOkHttpClient()));
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(UnsafeOkHttpClient.getUnsafeOkHttpClient()));
    }

//    @Override
//    public void applyOptions(Context context, GlideBuilder builder) {
//        super.applyOptions(context, builder);
//        builder.setDiskCache(new DiskLruCacheFactory(Constans.glideImageSaveDir, 1024 * 1024 * 300));
//    }
}