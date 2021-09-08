package com.base.common.utils;

import android.content.Context;

import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;

import java.util.ArrayList;

public class SvgaUtils {

    private Context context;
    private ArrayList<String> stringList;
    private SVGAImageView svgaImage;
    private SVGAParser parser;

    private boolean isStop = false;//执行完后是否移除

    public SvgaUtils(Context context, SVGAImageView svgaImage) {
        this.context = context;
        this.svgaImage = svgaImage;
    }

    /**
     * 初始化数据
     */
    public void initAnimator(boolean isStop) {
        this.isStop = isStop;
        parser = new SVGAParser(context);
        parser.init(context);

        stringList = new ArrayList<>();

        //监听大动画的控件周期
        svgaImage.setCallback(new SVGACallback() {

            @Override
            public void onPause() {
            }

            @Override
            public void onFinished() {
                //当动画结束，如果数组容器大于0，则移除容器第一位的数据，轮询播放动画。
                if (stringList != null && stringList.size() > 0) {
                    stringList.remove(0);
                    //如果移除之后的容器大于0，则开始展示新一个的大动画
                    if (stringList != null && stringList.size() > 0) {
                        try {
                            parseSVGA();//解析加载动画
                        } catch (Exception e) {

                        }
                    } else {
                        stopSVGA();
                    }
                } else {
                    stopSVGA();
                }
            }

            @Override
            public void onRepeat() {
                stopSVGA();
            }

            @Override
            public void onStep(int i, double v) {
            }
        });


    }

    /**
     * 显示动画
     */
    public void startAnimator(String svgaName) {
        stringList.add(stringList.size(), svgaName + ".svga");
        //如果礼物容器列表的数量是1，则解析动画，如果数量不是1，则此处不解析动画，在上一个礼物解析完成之后加载再动画
        if (stringList.size() == 1) {
            parseSVGA();
        }
    }

    /**
     * 停止动画
     */
    private void stopSVGA() {
        if (svgaImage.isAnimating() && stringList.size() == 0 && isStop) {
            svgaImage.stopAnimation();
        }
    }

    /**
     * 解析加载动画
     */
    private void parseSVGA() {
        if (stringList.size() > 0) {
            try {
                parser.decodeFromAssets(stringList.get(0), new SVGAParser.ParseCompletion() {
                    @Override
                    public void onComplete(SVGAVideoEntity svgaVideoEntity) {
                        //解析动画成功，到这里才真正的显示动画
                        SVGADrawable drawable = new SVGADrawable(svgaVideoEntity);
                        svgaImage.setImageDrawable(drawable);
                        svgaImage.startAnimation();
                    }

                    @Override
                    public void onError() {
                        //如果动画数组列表大于0,移除第一位的动画,继续循环解析
                        if (stringList.size() > 0) {
                            stringList.remove(0);
                            parseSVGA();
                        } else {
                            stopSVGA();
                        }
                    }
                });
            } catch (Exception e) {
            }
        } else {
            stopSVGA();
        }
    }

}
