package com.base.common.view.widget.imageView;


public enum ImageUpLoadState {

    UNLoad(0), LoadIng(1), LoadERR(-1), LoadSuccess(2);

    public int state;

    ImageUpLoadState(int state) {
        this.state = state;
    }

}
