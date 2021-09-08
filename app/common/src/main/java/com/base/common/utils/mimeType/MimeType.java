package com.base.common.utils.mimeType;

import android.webkit.MimeTypeMap;

import androidx.collection.ArraySet;

import com.base.common.utils.IOUtils;
import com.base.common.utils.UIUtils;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

/**
 * <type extension=".png" mimetype="image/png" />
 * <type extension=".gif" mimetype="image/gif" />
 * <type extension=".jpg" mimetype="image/jpeg" />
 * <type extension=".jpeg" mimetype="image/jpeg" />
 * <type extension=".bmp" mimetype="image/bmp" />
 *
 * <type extension=".mp2" mimetype="audio/x-mpeg" />
 * <type extension=".mp3" mimetype="audio/mp3" />
 * <type extension=".wav" mimetype="audio/wav" />
 * <type extension=".ogg" mimetype="audio/x-ogg" />
 * <type extension=".mid" mimetype="audio/mid" />
 * <type extension=".midi" mimetype="audio/midi" />
 * <type extension=".m3u" mimetype="audio/x-mpegurl" />
 * <type extension=".m4a" mimetype="audio/mp4a-latm" />
 * <type extension=".m4b" mimetype="audio/mp4a-latm" />
 * <type extension=".m4p" mimetype="audio/mp4a-latm" />
 * <type extension=".mpga" mimetype="audio/mpeg" />
 * <type extension=".wma" mimetype="audio/x-ms-wma" />
 *
 *
 * <type extension=".mpe" mimetype="video/mpeg" />
 * <type extension=".mpg" mimetype="video/mpeg" />
 * <type extension=".mpeg" mimetype="video/mpeg" />
 * <type extension=".3gp" mimetype="video/3gpp" />
 * <type extension=".asf" mimetype="video/x-ms-asf" />
 * <type extension=".avi" mimetype="video/x-msvideo" />
 * <type extension=".m4u" mimetype="video/vnd.mpegurl" />
 * <type extension=".m4v" mimetype="video/x-m4v" />
 * <type extension=".mov" mimetype="video/quicktime" />
 * <type extension=".mp4" mimetype="video/mp4" />
 * <type extension=".rmvb" mimetype="video/*" />
 * <type extension=".wmv" mimetype="video/*" />
 * <type extension=".vob" mimetype="video/*" />
 * <type extension=".mkv" mimetype="video/*" />
 */
public enum MimeType {
    // ============== images ==============
    JPEG("image/jpeg", arraySetOf(
            "jpg",
            "jpeg"
    )),
    PNG("image/png", arraySetOf(
            "png"
    )),
    GIF("image/gif", arraySetOf(
            "gif"
    )),
    BMP("image/x-ms-bmp", arraySetOf(
            "bmp"
    )),
    WEBP("image/webp", arraySetOf(
            "webp"
    )),

    // ============== videos ==============
    MPEG("video/mpeg", arraySetOf(
            "mpeg",
            "mpg"
    )),
    MP4("video/mp4", arraySetOf(
            "mp4",
            "m4v"
    )),
    QUICKTIME("video/quicktime", arraySetOf(
            "mov"
    )),
    THREEGPP("video/3gpp", arraySetOf(
            "3gp",
            "3gpp"
    )),
    THREEGPP2("video/3gpp2", arraySetOf(
            "3g2",
            "3gpp2"
    )),
    MKV("video/x-matroska", arraySetOf(
            "mkv"
    )),
    WEBM("video/webm", arraySetOf(
            "webm"
    )),
    TS("video/mp2ts", arraySetOf(
            "ts"
    )),
    AVI("video/avi", arraySetOf("avi")),
    // ============== text ==============
    PLAIN("text/plain", arraySetOf("txt", "conf", "c", ".cpp", "h", "java", "log", "prop", "rc", "sh")),
    html("text/html", arraySetOf("html", "htm")),
    php("text/php", arraySetOf("php")),
    csv("text/csv", arraySetOf("csv")),
    xml("text/xml", arraySetOf("xml")),

    // ============== zip ==============
    jar("application/java-archive", arraySetOf("jar")),
    zip("application/zip", arraySetOf("zip")),
    rar("application/x-rar-compressed", arraySetOf("rar")),
    gz("application/gzip", arraySetOf("gz")),
    gtar("application/x-gtar", arraySetOf("gtar")),
    tar("application/x-tar", arraySetOf("tar")),
    tgz("application/jx-compressed", arraySetOf("tgz", "z")),

    // ============== application ==============

    apk("application/vnd.android.package-archive", arraySetOf("apk")),
    stream("application/octet-stream", arraySetOf("bin", "class", "exe")),
    msword("application/msword", arraySetOf("docx", "doc")),
    js("application/x-javascript", arraySetOf("js")),
    pdf("application/pdf", arraySetOf("pdf")),

    mpc("application/vnd.mpohun.certificate", arraySetOf("mpc")),
    msg("application/vnd.ms-outlook", arraySetOf("msg")),
    ppt("application/vnd.ms-powerpoint", arraySetOf("ppt", "pptx", "pps")),

    excel("application/vnd.ms-excel", arraySetOf("xlsx", "xls")),
    wps("application/vnd.ms-works", arraySetOf("wps")),
    rtf("application/rtf", arraySetOf("rtf"));


    private final String mMimeTypeName;
    private final Set<String> mExtensions;

    MimeType(String mimeTypeName, Set<String> extensions) {
        mMimeTypeName = mimeTypeName;
        mExtensions = extensions;
    }

    public static Set<MimeType> ofAll() {
        return EnumSet.allOf(MimeType.class);
    }

    public static Set<MimeType> of(MimeType type, MimeType... rest) {
        return EnumSet.of(type, rest);
    }

    public static Set<MimeType> ofImage() {
        return EnumSet.of(JPEG, PNG, GIF, BMP, WEBP);
    }

    public static Set<MimeType> ofImage(boolean onlyGif) {
        return EnumSet.of(GIF);
    }

    public static Set<MimeType> ofGif() {
        return ofImage(true);
    }

    public static Set<MimeType> ofVideo() {
        return EnumSet.of(MPEG, MP4, QUICKTIME, THREEGPP, THREEGPP2, MKV, WEBM, TS, AVI);
    }


    public static boolean isImage(String mimeType) {
        if (mimeType == null) return false;
        return mimeType.startsWith("image");
    }

    public static boolean isVideo(String mimeType) {
        if (mimeType == null) return false;
        return mimeType.startsWith("video");
    }

    public static boolean isVideoType(String url) {
        String suffix = IOUtils.getSuffix(url);
        return isVideo(getMimeType(suffix));
    }

    public static boolean isImageType(String url) {
        String suffix = IOUtils.getSuffix(url);
        return isImage(getMimeType(suffix));
    }

    public static boolean isGif(String mimeType) {
        if (mimeType == null) return false;
        return mimeType.equals(MimeType.GIF.toString());
    }

    public static boolean isGifType(String url) {
        String suffix = IOUtils.getSuffix(url);
        return isGif(getMimeType(suffix));
    }
//    public static Set<MimeType> onlyImage() {
//        return EnumSet.of(JPEG, PNG,   BMP, WEBP);
//    }
    /**
     * 获取后缀类型
     *
     * @param suffix //后缀
     * @return
     */
//    public static String getMimeType(String suffix) {
//        if (suffix == null) return "";
//        if (suffix.startsWith(".")) {
//            suffix = suffix.substring(1);
//        }
//        Set<MimeType> set = ofAll();
//        Iterator<MimeType> iterator = set.iterator();
//        while (iterator.hasNext()) {
//            MimeType mimeType = iterator.next();
//            if (mimeType.mExtensions.contains(suffix)) {
//                return mimeType.toString();
//            }
//        }
//        return "";
//    }

    /**
     * 获取文件的MIME类型
     */
    public static String getMimeType(String pathOrUrl) {
        String mimeType = "*/*";
        String ext = IOUtils.getSuffix(pathOrUrl);
        if (UIUtils.isEmpty(ext)) return mimeType;
        MimeTypeMap map = MimeTypeMap.getSingleton();
        ext = ext.substring(1);
        if (map.hasExtension(ext)) {
            mimeType = map.getMimeTypeFromExtension(ext);
        }
        return mimeType;
    }


    private static Set<String> arraySetOf(String... suffixes) {
        return new ArraySet<>(Arrays.asList(suffixes));
    }

    @Override
    public String toString() {
        return mMimeTypeName;
    }


}
