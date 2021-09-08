package com.base.common.model.bean;

import com.base.common.view.adapter.bean.ChildBaseBean;

import java.util.List;

public class ADInfoGroup extends ChildBaseBean {
    private String imageName;//图片名称
    private String imageContent;//图片说明文字或描述
    private int imageType;//图片类型
    private List<ADInfo> list;


    public int getImageType() {
        return imageType;
    }

    public void setImageType(int imageType) {
        this.imageType = imageType;
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

    public List<ADInfo> getList() {
        return list;
    }

    public void setList(List<ADInfo> list) {
        this.list = list;
    }
}
