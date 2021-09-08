package com.youth.banner.adapter;



import com.base.common.R;
import com.base.common.databinding.ItemBannerImagesBinding;
import com.base.common.view.adapter.connector.BaseItemMultiType;
import com.base.common.view.adapter.connector.BaseViewHolder;

/**
 * 默认实现的图片适配器，图片加载需要自己实现
 */
public class BannerImageAdapter<T> extends BannerAdapter<T> {


    @Override
    public void initMultiItemType() {
        putMultiItemType(BaseItemMultiType.TYPE_CHILD, new BaseItemMultiType<T, ItemBannerImagesBinding>() {
            @Override
            public int getLayoutId() {
                return R.layout.item_banner_images;
            }

            @Override
            public void onBindViewHolder(ItemBannerImagesBinding binding, int position, BaseViewHolder viewHolder, T bean) {
                super.onBindViewHolder(binding, position, viewHolder, bean);

            }
        });
    }


}
