package com.base.common.model.bean;


import androidx.databinding.Bindable;

import com.base.common.BR;
import com.base.common.view.adapter.bean.ChildBaseBean;
import com.base.common.view.widget.imageView.ImageUpLoadState;

import java.math.BigDecimal;

public class ADInfo extends ChildBaseBean {

    private ImageUpLoadState upLoadState = ImageUpLoadState.UNLoad;//图片上传状态
    private int id;//图片id
    private String imageId;//图片id
    private int imageType;//图片类型
    private String imageUrl;//图片网络地址
    private String imagePatch;//图片本地地址
    private String imageLinkHttpUrl;//点击图片链接地址
    private String imageName;//图片名称
    private String imageContent;//图片说明文字或描述
    private Object imageObject;
    private String imageTitle;
    private int count;

    private BigDecimal price;

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getImageType() {
        return imageType;
    }

    public void setImageType(int imageType) {
        this.imageType = imageType;
    }

    public String getImageTitle() {
        return imageTitle;
    }

    public void setImageTitle(String imageTitle) {
        this.imageTitle = imageTitle;
    }

    public ImageUpLoadState getUpLoadState() {
        return upLoadState;
    }

    public void setUpLoadState(ImageUpLoadState upLoadState) {
        this.upLoadState = upLoadState;
    }

    @Bindable
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
        notifyPropertyChanged(BR.count);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImagePatch() {
        return imagePatch;
    }

    public void setImagePatch(String imagePatch) {
        this.imagePatch = imagePatch;
    }

    public Object getImageObject() {
        return imageObject;
    }

    public void setImageObject(Object imageObject) {
        this.imageObject = imageObject;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageid) {
        this.imageId = imageid;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageContent() {
        return imageContent;
    }

    public void setImageContent(String imageContent) {
        this.imageContent = imageContent;
    }


    public String getImageLinkHttpUrl() {
        return imageLinkHttpUrl;
    }

    public void setImageLinkHttpUrl(String HttpUrl) {
        imageLinkHttpUrl = HttpUrl;
    }


}
