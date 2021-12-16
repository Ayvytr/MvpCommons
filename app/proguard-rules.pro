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

-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod
-dontoptimize
-dontwarn InnerClasses

-dontskipnonpubliclibraryclassmembers
-dontwarn android.support.v4.**
-dontwarn android.webkit.WebView

-keep public class * extends android.support.v4.**
-keep class android.support.** { *;}
-keep interface android.support.** { *;}

-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.support.v4.**
-keep public class com.android.vending.licensing.ILicensingService
-keep class com.android.vending.licensing.ILicensingService
-keep class android.support.v4.** { *; }

-keep class android.support.v7.** { *;}

-keepclasseswithmembers class * {
    public <init>(android.content.Context);
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class * implements android.os.Parcelable {
    static ** CREATOR;
}

-dontwarn **.bean.**
-keep class **.bean.** { *;}
-dontwarn **.event.**
-keep class **.event.** { *;}

-dontwarn org.apache.**
-keep class org.apache.http.**{*;}
-keep class org.json.**{*;}

#基线包使用，生成mapping.txt
-printmapping mapping.txt
#生成的mapping.txt在app/build/outputs/mapping/release路径下，移动到/app路径下
#修复后的项目使用，保证混淆结果一致
#-applymapping mapping.txt
#hotfix
-keep class com.taobao.sophix.**{*;}
-keep class com.ta.utdid2.device.**{*;}
-dontwarn com.alibaba.sdk.android.utils.**
#防止inline
-dontoptimize


-keepclassmembers class com.weshare.wanxiang.MyApplication {
    public <init>();
}
# 如果不使用android.support.annotation.Keep则需加上此行
-keep class com.weshare.wanxiang.SophixStubApplication$RealApplicationStub

#友盟统计
-keep class com.umeng.** {*;}

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep public class **.R$*{
public static final int *;
}

#-------bugly--开始-------------
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
#-------bugly--结束-------------

#-------arouter开始-------------
-dontwarn com.alibaba.android.arouter.**
-keep public class com.alibaba.android.arouter.**{*;}
-keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}
-keep class * extends com.alibaba.android.arouter.facade.template.IProvider{*;}
-keep interface * implements com.alibaba.android.arouter.facade.template.IProvider
-keep class * implements com.alibaba.android.arouter.facade.template.IProvider
-keep class com.alibaba.android.arouter.facade.annotation.Autowired
-keep class * {
    @com.alibaba.android.arouter.facade.annotation.Autowired <fields>;
}
-keepclasseswithmembers class * {
    @com.alibaba.android.arouter.facade.annotation.Autowired <methods>;
}
#-------arouter结束-------------

#-------ShareSDK混淆--开始-----------
-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class **.R$* {*;}
-keep class **.R{*;}
-keep class com.mob.**{*;}
-keep class m.framework.**{*;}
-dontwarn cn.sharesdk.**
-dontwarn com.sina.**
-dontwarn com.mob.**
-dontwarn **.R$*
#-------ShareSDK混淆--结束-----------

#-------OkHttp--开始-----------
-keepattributes Signature
-keepattributes Annotation
-dontwarn okhttp3.**
-keep class okhttp3.** { *; }
-keep interface okhttp3.* { *; }
-dontwarn okio.**
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-keep class okio.** { *; }
-keep class sun.misc.Unsafe { *; }
-dontwarn javax.annotation.**
-dontwarn org.codehaus.mojo.animal_sniffer.*
-dontwarn javax.naming.**
#-------OkHttp--结束-----------

#-------ButterKnife--开始-----------
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}
#-------ButterKnife--结束-----------

#-------gson--开始-----------
-dontwarn com.google.gson.**
-keep class com.google.gson.** {*;}
-keepattributes Signature
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-------gson--结束-----------

#-------RxJava,RxAndroid--开始-----------
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field*{
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef{
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
#-------RxJava,RxAndroid--结束-----------

#-------retrofit--开始-----------
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn javax.annotation.**
-dontwarn kotlin.Unit
-dontwarn retrofit2.-KotlinExtensions
#-------retrofit--结束-----------

#-------EventBus--开始-----------
-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
#-------EventBus--结束-----------

#-------permission--开始-----------
-dontwarn com.yanzhenjie.permission.**
#-------permission--结束-----------

#-------glide--开始-----------
-keep class com.bumptech.glide.Glide { *; }
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}
-dontwarn com.bumptech.glide.**
#-------glide--结束-----------

#-------bugly开始-------------
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
#-------bugly结束-------------

#-------极光推送开始-------------
-dontoptimize
-dontpreverify

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-keep class * extends cn.jpush.android.helpers.JPushMessageReceiver {*;}
-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }

-dontwarn com.google.**
-keep class com.google.gson.**{*;}
-keep class com.google.protobuf.** {*;}
#-------极光推送结束-------------

#-------极验--开始-------------
-dontwarn com.geetest.sdk.**
-keep class com.geetest.sdk.**{*;}
#-------极验--结束-------------
