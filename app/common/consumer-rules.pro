# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
#如下举例三种不同的混淆配置的作用。
#
#-keep public class * 只保证类名不被混淆，构造方法、普通方法、属性都会被混淆、或者移除。构造方法、普通方法，都是方法，都有可能被移除的，移除构造方法的条件是，没有new过
#-keepclassmembers class * 只保证配置的方法或属性不被移除和混淆，类名会被混淆
#-keepclasseswithmembers class * extends 保证类名和配置项不会被移除和混淆
#配置项+类名，当前包下class
#-keep class com.pitaya.commanager.{;}
#
#配置项+类名，当前包下class+所有子包下的class
#-keep class com.pitaya.commanager.*{;} #类内部的
#
#类名不被混淆，所有的子类，不包含当前类
#-keep public class * extends com.pitaya.commanager.ComLifecycle
#
#方法，所有的子类，不包含当前类
#-keepclassmembers class $ extends com.meituan.sankuai.cep.component.recyclerviewadapter.BaseViewHolder {
#<init>(...);
#}
#
#方法+类名，所有的子类，不包含当前类
#-keepclasseswithmembers class * extends com.meituan.sankuai.cep.component.recyclerviewadapter.BaseViewHolder{
#<init>(...);
#}

-keep class com.squareup.wire.** { *; }
-keep class com.opensource.svgaplayer.proto.** { *; }

# wechat
-keep class com.tencent.mm.opensdk.** {*;}
-keep class com.tencent.wxop.** {*;}
-keep class com.tencent.mm.sdk.** {*;}

#bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

#图片剪切
-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }

-dontwarn com.jeremyliao.liveeventbus.**
-keep class com.jeremyliao.liveeventbus.** { *; }
-keep class androidx.lifecycle.** { *; }
-keep class androidx.arch.core.** { *; }



#指定压缩级别
-optimizationpasses 5

#不跳过非公共的库的类成员
-dontskipnonpubliclibraryclassmembers

#混淆时采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

#把混淆类中的方法名也混淆了
#-useuniqueclassmembernames

#优化时允许访问并修改有修饰符的类和类的成员
-allowaccessmodification

#将文件来源重命名为“SourceFile”字符串
-renamesourcefileattribute SourceFile
#保留行号
-keepattributes SourceFile,LineNumberTable
# 泛型与反射
-keepattributes Signature
-keepattributes EnclosingMethod

# 保留本地native方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

# 保护注解
-keepattributes *Annotation*


#保持所有实现 Serializable 接口的类成员
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#Fragment不需要在AndroidManifest.xml中注册，需要额外保护下
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.app.Fragment

# 保持测试相关的代码
-dontnote junit.framework.**
-dontnote junit.runner.**
-dontwarn android.test.**
-dontwarn android.support.test.**
-dontwarn org.junit.**



-keepclasseswithmembers class * {                                               # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);     # 保持自定义控件类不被混淆
}

-keepclassmembers class * extends android.app.Activity {                        # 保持自定义控件类不被混淆
   public void *(android.view.View);
}

-keepclassmembers enum * {                                                      # 保持枚举 enum 类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {                                # 保持 Parcelable 不被混淆
  public static final android.os.Parcelable$Creator *;
}

############################################
#
# Android开发中一些需要保留的公共部分
#
#############################################

# 保留我们使用的四大组件，自定义的Application等等这些类不被混淆
# 因为这些子类都有可能被外部调用
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Appliction
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService


# 保留support下的所有类及其内部类
-keep class android.support.** {*;}

# 保留继承的
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v7.**
-keep public class * extends android.support.annotation.**


# 保留在Activity中的方法参数是view的方法，
# 这样以来我们在layout中写的onClick就不会被影响
-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}

# 保留枚举类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 保留我们自定义控件（继承自View）不被混淆
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# 保留Parcelable序列化类不被混淆
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# 保留Serializable序列化的类不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# 对于带有回调函数的onXXEvent、**On*Listener的，不能被混淆
-keepclassmembers class * {
    void *(**On*Event);
    void *(**On*Listener);
}

# webView处理，项目中没有使用到webView忽略即可
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
    public *;
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.webView, jav.lang.String);
}

#//不混淆所有类名中包含了“model”的类及其成员
-keep public class **.*model*.** {*;}


-keep public class * extends android.app.Application {  *; }
#注意 AndroidManifest.xml中的applicaion
-keep class com.first.football.AndroidApp
-keep class  com.first.football.AndroidApp {
    <init>(...);
}
-keep class com.base.common.app.BaseApp

-keep class com.base.common.app.SampleApplicationLike {
    <init>(...);
}

-keep class androidx.core.app.CoreComponentFactory
-keep class com.base.data.** { *; }
-keep class com.orm.** { *; }
-keep class net.sqlcipher.** { *; }

#//不混淆某个接口的实现
-keep class * implements com.base.common.view.adapter.connector.BaseItemTypeInterface { *; }
-keep interface  com.base.common.view.adapter.connector.BaseItemTypeInterface
-keep public class * extends androidx.lifecycle.AndroidViewModel
-keep public class * extends androidx.recyclerview.widget.RecyclerView.ViewHolder { *; }
-keep class com.base.common.view.adapter.connector.BaseViewHolder{*;}


# 保留R下面的资源
-keep class **.R$* {*;}

-keep public class **.BR { public *; }
-keep public class **.BR$* { public *; }
-keepclassmembers class **.BR$* {
    public static <fields>;
}
-keep class androidx.databinding.** { *; }
-keep class * extends androidx.databinding.** { *; }


