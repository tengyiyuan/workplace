
# To enable ProGuard in your project, edit project.properties
# to define the proguard.config property as described in that file.
#
# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in ${sdk.dir}/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the ProGuard
# include property in project.properties.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-optimizationpasses 5
-dontusemixedcaseclassnames
#是否混淆第三方jar
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-verbose
-ignorewarnings
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

# webview +js
#keep 使用webview的类

-keepclassmembers class com.archermind.sopaylife.WebBrowser {
	public *;
}
#保持哪些类不被混淆
-keep class android.** {*; }
-keep public class * extends android.view
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.pm
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
#ACRA specifics
#we need line numbers in our stack traces otherwise they are pretty useless

-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable

#ACRA needs "annotations" so and this...
-keepattributes *Annotation*

#保持 native 方法不被混淆
-keepclasseswithmembernames class * {
	native <methods>;
}

#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
	public <init>(android.content.Context,android.util.AttributeSet);
}
-keepclasseswithmembers class * {
	public <init>(android.content.Context, android.util.AttributeSet,init);
}
-keepclasseswithmembers class * {
    void onClick*(...);
}
-keepclasseswithmembers class * {
    *** *Callback(...);
}
# 保持自定义控件类不被混淆
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
# 保持枚举 enum 类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}
#微信
-keep class cn.sharesdk.demo.wxapi.WXEntryActivity {*;}
-keep class cn.sharesdk.onekeyshare.** {*;}
-keep class com.baidu.**{*;}
-keep class vi.com.gdi.bgl.**{*;}
#keep所有的javabean
-keep class com.archermind.entity.**{*;}
#keep 泛型
-keepattributes Signature
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# 使用Gson解析Bean的时候，所有javaBean都不能混淆，不然会报空指针错误 2016-5-11 9:49解决，费了我一天的时间，麻蛋
-keep class com.toplion.cplusschool.Bean.** { *; }

# Explicitly preserve all serialization members. The Serializable interface
# is only a marker interface, so it wouldn't save them.
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}


-keep public class * implements java.io.Serializable {
	public *;
}


