# ####################################################IM
-keepattributes Exceptions,InnerClasses
-keepattributes Signature

# VoIP
-keep class io.agora.rtc.** {*;}
# Location
-keep class com.amap.api.**{*;}
-keep class com.amap.api.services.**{*;}
# 红包
-keep class com.uuhelper.Application.** {*;}
-keep class net.sourceforge.zbar.** { *; }
-keep class com.google.** { *; }
-keep class com.alipay.** {*;}
-keep class com.jrmf360.** {*;}
#-ignorewarnings

# 华为
-dontwarn com.huawei.hms.**
-dontwarn com.google.**
-dontwarn com.meizu.**
-dontwarn  com.xiaomi.**
#-ignorewarnings
#-keepattributes *Annotation*
#-keepattributes Exceptions
#-keepattributes InnerClasses
#-keepattributes Signature
#-keepattributes SourceFile,LineNumberTable
#-keep class com.hianalytics.android.**{*;}
#-keep class com.huawei.updatesdk.**{*;}
#-keep class com.huawei.hms.**{*;}
#-keep class com.huawei.gamebox.plugin.gameservice.**{*;}
#-keep public class com.huawei.android.hms.agent.** extends android.app.Activity { public *; protected *; }
#-keep interface com.huawei.android.hms.agent.common.INoProguard {*;}
#-keep class * extends com.huawei.android.hms.agent.common.INoProguard {*;}

# ####################################################EventBus
-keepclassmembers class ** {
      public void onEvent*(**);
  }
  -keepclassmembers class * extends de.greenrobot.event.util.ThrowableFailureEvent {
      <init>(java.lang.Throwable);
  }

# ####################################################gson
-keep class sun.misc.Unsafe { *; }
#项目bean keep
-keep class com.aides.brother.brotheraides.bean.** { *; }

# ####################################################zxing
-dontwarn com.google.zxing.**


# ####################################################umeng
-dontwarn com.markupartist.**
-keep public class com.umeng.example.R$*{
    public static final int *;
}
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}


# ####################################################okhttp
-keep class com.squareup.okhttp.** { *;}
-dontwarn com.squareup.okhttp.**


# ####################################################universal-image-loader
-dontwarn com.nostra13.universalimageloader.**
-keep class com.nostra13.universalimageloader. {*;}

# ####################################################分享
-dontshrink
-dontoptimize
-dontwarn com.google.android.maps.**
-dontwarn android.webkit.WebView
-dontwarn com.umeng.**
-dontwarn com.tencent.**
-dontwarn com.facebook.**
-keep public class javax.**
-keep public class android.webkit.**
-dontwarn android.support.v4.**
-keep enum com.facebook.**
-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

-keep class com.facebook.** { *; }
-keep public class com.tencent.** {*;}
-keep class com.umeng.**
-keep class UMMoreHandler{*;}
-keep class com.tencent.** {*;}
-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
-keep class im.yixin.sdk.api.YXMessage {*;}
-keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}

-dontwarn twitter4j.**
-keep class twitter4j.** { *; }

-keep class com.tencent.** {*;}
-dontwarn com.tencent.**
-keep class com.kakao.** {*;}
-dontwarn com.kakao.**
-keep public class com.umeng.com.umeng.soexample.R$*{
    public static final int *;
}
-keep public class com.linkedin.android.mobilesdk.R$*{
    public static final int *;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class com.tencent.open.TDialog$*
-keep class com.tencent.open.TDialog$* {*;}
-keep class com.tencent.open.PKDialog
-keep class com.tencent.open.PKDialog {*;}
-keep class com.tencent.open.PKDialog$*
-keep class com.tencent.open.PKDialog$* {*;}
-keep class com.umeng.socialize.impl.ImageImpl {*;}
-keep class com.sina.** {*;}
-dontwarn com.sina.**
-keep class  com.alipay.share.sdk.** {
   *;
}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

-keep class com.linkedin.** { *; }
-keep class com.android.dingtalk.share.ddsharemodule.** { *; }
-keepattributes Signature

-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.H5PayCallback {
    <fields>;
    <methods>;
}
-keep class org.json.alipay.** { *; }
-keep class com.ta.utdid2.** { *;}
-keep class com.ut.device.** { *;}


# 云信
-dontwarn com.netease.**
-keep class com.netease.** {*;}
#如果你使用全文检索插件，需要加入
-dontwarn org.apache.lucene.**
-keep class org.apache.lucene.** {*;}

#阿里oos
-keep class com.alibaba.sdk.android.oss.** { *; }
-dontwarn okio.**
-dontwarn org.apache.commons.codec.binary.**

#二维码
-keep class com.wd.daquan.common.scancode.QRCodeUtils.** { *; }
#小视频
-keep class tv.danmaku.ijk.media.** {*;}

#imagePicker
-keep class com.aides.brother.brotheraides.imagepicker.entity.** { *; }
-keep class com.aides.brother.brotheraides.imagepicker.rxbus2.** { *; }

#微信
-keep class com.switfpass.pay.**{*;}
-keep class com.tencent.mm.**{*;}
#okhttp
-keep class okhttp3.**{*;}
-keep class okio.**{*;}

#glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

#-----------qingchat
#登录模块
-keep public class com.wd.daquan.login.bean.** {*;}
-keep public class com.wd.daquan.chat.bean.** {*;}
#公共模块
-keep public class com.wd.daquan.common.bean.** {*;}
#联系人
-keep public class com.wd.daquan.contacts.bean.** {*;}
-keep public class com.wd.daquan.chat.group.bean.** {*;}
-keep public class com.wd.daquan.third.** {*;}
-keep public class com.wd.daquan.mine.bean.** {*;}

-keep public class com.wd.daquan.**.bean.** {*;}
-keep public class com.da.library** {*;}

# JSON解析模块
-keep class org.json.** {*;}
## Gson
-keepattributes EnclosingMethod

#
#retrofit2
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

#RxJava/RxAndroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

# banner 的混淆代码
-keep class com.youth.banner.** {
    *;
}

#greanDao
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties {*;}
-dontwarn net.sqlcipher.database.**
-dontwarn rx.**

#bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}