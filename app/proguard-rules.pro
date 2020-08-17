-ignorewarnings
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable
-keepattributes Signature
#保护注解
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keepclassmembers class * implements android.os.Parcelable {
    static ** CREATOR;
}
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}



############Glide################
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

#support.v4/v7包不混淆
#-keep class android.support.** { *; }
#-keep class android.support.v4.** { *; }
#-keep public class * extends android.support.v4.**
#-keep interface android.support.v4.app.** { *; }
#-keep class android.support.v7.** { *; }
#-keep public class * extends android.support.v7.**
#-keep interface android.support.v7.app.** { *; }
#-dontwarn android.support.**    # 忽略警告

#androidX
-keep class androidx.** {*;}
-keep public class * extends androidx.**
-keep interface androidx.** { *; }
-dontwarn androidx.**

 #butterknife的混淆代码
 -keep class butterknife.** { *; }
 -dontwarn butterknife.internal.**
 -keep class **$$ViewBinder { *; }
 -keep public class * implements butterknife.Unbinder { public <init>(**, android.view.View); }
 -keepclasseswithmembernames class * {
     @butterknife.* <fields>;
 }

 -keepclasseswithmembernames class * {
     @butterknife.* <methods>;
 }

#gson
-dontwarn com.google.**
-keep class com.google.gson.** {*;}

 #避免混淆eventbus
 -keepattributes *Annotation*
 -keepclassmembers class ** {
     @org.greenrobot.eventbus.Subscribe <methods>;
 }
 -keep enum org.greenrobot.eventbus.ThreadMode { *; }


#libs
 -dontwarn com.mediatek.**
 -keep class com.mediatek.** {*;}

 -keep class com.google.android.exoplayer.** {*;}
 -keep class com.hisense.hianidraw.**{*;}

 #aqpq jar
 -keep class com.hisense.avmiddleware.**{*;}
 -keep class vendor.hisense.mw.**{*;}
 -keep class android.**{*;}