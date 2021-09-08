package com.base.common.view.adapter.ada;

import android.view.View;


import com.base.common.R;
import com.base.common.databinding.ItemAddedPicturesBinding;
import com.base.common.model.bean.ADInfo;
import com.base.common.view.adapter.connector.BaseItemMultiType;
import com.base.common.view.adapter.connector.BaseItemTypeInterface;
import com.base.common.view.widget.imageView.ImageLoaderUtils;
import com.base.common.view.widget.nineImageView.ImagesActivity;

public class ImageAdapter extends GeneralRecyclerAdapter {


    @Override
    public void initMultiItemType() {
        putMultiItemType(BaseItemTypeInterface.TYPE_CHILD, new BaseItemMultiType<ADInfo, ItemAddedPicturesBinding>() {
            @Override
            public int getLayoutId() {
                return R.layout.item_added_pictures;
            }

            @Override
            public void onBindViewHolder(ItemAddedPicturesBinding binding, int position, ADInfo bean) {
                super.onBindViewHolder(binding, position, bean);
                binding.givImageDelete.setVisibility(View.GONE);
                ImageLoaderUtils.loadImage(binding.givImage, bean.getImageUrl());
            }

            @Override
            public boolean onItemTypeClick(View view, BaseRVAdapter adapter, int onclickType, int position, ADInfo bean) {

                ImagesActivity.startActivity(recyclerView, R.id.givImage, "imageUrl", position);

                return super.onItemTypeClick(view, adapter, onclickType, position, bean);
            }

        });
    }


    public void addImage(String imageUrl) {
        ADInfo adInfo = new ADInfo();
        adInfo.setImageUrl(imageUrl);
        add(adInfo);
    }

}
