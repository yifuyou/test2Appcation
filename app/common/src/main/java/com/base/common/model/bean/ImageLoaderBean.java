package com.base.common.model.bean;

public class ImageLoaderBean {
    private String imageName;//图片名称
    private String imageContent;//图片说明文字或描述
    private Object imageObject;//图片的路经

    private int miniHeight;//最小高度
    private int maxHeight;//最大高度

    private int miniWidth;//最小宽度
    private int maxWidth;// 最大宽度


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

    public Object getImageObject() {
        return imageObject;
    }

    public void setImageObject(Object imageObject) {
        this.imageObject = imageObject;
    }

    public int getMiniHeight() {
        return miniHeight;
    }

    public void setMiniHeight(int miniHeight) {
        this.miniHeight = miniHeight;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public int getMiniWidth() {
        return miniWidth;
    }

    public void setMiniWidth(int miniWidth) {
        this.miniWidth = miniWidth;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }
}
