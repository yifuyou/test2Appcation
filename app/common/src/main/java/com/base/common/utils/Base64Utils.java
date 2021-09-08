package com.base.common.utils;

import com.base.common.utils.mimeType.MimeType;

/**
 * URL安全的Base64位编码工具类
 */
public class Base64Utils {

    // 图片水印编码字符串
    private static final String IMAGE_WATERMARK_ENCODE_STRING = "YXBwLzIwMjAwNjEwLzk3MTJlYmRmY2UzOTQ3N2FiZGZhYmM2YmNhMWUzNWU4LnBuZw==";
    // 文章图片宽度
    private static final int ARTICLES_IMAGE_WIDTH = 343;
    // 文章图片高度
    private static final int ARTICLES_IMAGE_HEIGHT = 191;
    // 动态图片宽度
    private static final int DYNAMIC_IMAGE_WIDTH = 343;
    // 动态图片高度
    private static final int DYNAMIC_IMAGE_HEIGHT = 290;
    // 图片+文字水印URL
    private static final String IMAGE_WITH_TEXT_WATERMARK_URL = "%s?x-oss-process=image/resize,s_800,limit_0/auto-orient,1/quality,q_90/watermark,image_%s,s_10,t_90,g_se,x_10,y_10,type_d3F5LXplbmhlaQ,size_20,text_%s,color_FFFFFF,shadow_50,order_0,align_1,interval_10,t_100,g_se,x_11,y_11";
    // 文字水印URL
    private static final String TEXT_WATERMARK_URL = "%s?x-oss-process=image/resize,w_%d,h_%d/watermark,type_d3F5LXplbmhlaQ,size_10,text_%s,color_FFFFFF,shadow_50,order_0,align_1,interval_2,t_100,g_se,x_11,y_11";
    // 图片水印URL
    private static final String IMAGE_WATERMARK_URL = "%s?x-oss-process=image/resize,w_%d,h_%d/watermark,image_%s,t_100,g_se,x_11,y_11";

    /**
     * 文章添加水印
     *
     * @param pic      拼接的图片路径
     * @param userName 用户名
     */
    public static String articlesAddWatermark(String pic, String userName) {
        if (UIUtils.isEmpty(pic)) return null;
        StringBuilder sb = new StringBuilder();
        for (String imageUrl : pic.split("@@")) {
            sb.append(imageWithTextWatermark(imageUrl, "@" + userName)).append("@@");
        }
        return sb.toString();
    }

    /**
     * 图片+文字水印
     *
     * @param imageUrl 图片路径
     * @param text     文本
     */
    public static String imageWithTextWatermark(String imageUrl, String text) {
        if (UIUtils.isEmpty(imageUrl)) return "";
        if (text == null) text = "";
        if (!imageUrl.startsWith("https://qiuzy.oss-cn-shenzhen.aliyuncs.com") || MimeType.isGifType(imageUrl))
            return imageUrl;

        String basicText = filter(Base64.getEncoder().encodeToString(text.getBytes()));
        return String.format(IMAGE_WITH_TEXT_WATERMARK_URL, imageUrl, IMAGE_WATERMARK_ENCODE_STRING, basicText);
    }

    /**
     * 编码字符串
     */
    public static String encode(String arg) {
        if (UIUtils.isEmpty(arg)) return "";
        return filter(Base64.getEncoder().encodeToString(arg.getBytes()));
    }

    /**
     * 过滤水印字符串
     */
    private static String filter(String arg) {
        return arg.replace("+", "-").replace("/", "_");
    }

    private Base64Utils() {
    }
}
