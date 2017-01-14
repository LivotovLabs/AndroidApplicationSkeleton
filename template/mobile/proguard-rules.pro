# ================================================================================================
# Keep data package where we store any orm entities, dto's and other pojos
-keep class appskeleton.data.** { *; }
-keepclassmembers appskeleton.data.** { *; }
-keep interface appskeleton.data.** { *; }
-keepclassmembers interface appskeleton.data.** { *; }
-keeppackagenames appskeleton.data.**
#
# ================================================================================================


# ================================================================================================
# Commonly used rules goes below, they are not sensitive to your project package name.
# ================================================================================================
#
-optimizationpasses 1
#
# When not preverifing in a case-insensitive filing system, such as Windows. Because this tool unpacks your processed jars, you should then use:
-dontusemixedcaseclassnames
#
# Specifies not to ignore non-public library classes. As of version 4.5, this is the default setting
-dontskipnonpubliclibraryclasses
#
# Preverification is irrelevant for the dex compiler and the Dalvik VM, so we can switch it off with the -dontpreverify option.
-dontpreverify
#
# Specifies to write out some more information during processing.
# If the program terminates with an exception, this option will print out the entire stack trace, instead of just the exception message.
-verbose
#
# Extended logging for re-tracing
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable
#
# The -optimizations option disables some arithmetic simplifications that Dalvik 1.0 and 1.5 can't handle. Note that the Dalvik VM also can't handle aggressive overloading (of static fields).
# To understand or change this check http://proguard.sourceforge.net/index.html#/manual/optimizations.html
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#
# If you have any problems with "SIMULATION ERROR" from dex - use -dontoptimize to disable optimization at all
-dontoptimize
#
# To repackage classes on a single package
-repackageclasses ''
#
-dontwarn *.**
#
-libraryjars libs
#
# Uncomment if using annotations to keep them.
-keepattributes *Annotation*
#
# Uncomment if you do use Google Gson library
-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
#
# Keep classes that are referenced on the AndroidManifest
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class com.android.vending.licensing.ILicensingService
#
# Keep android support libs as is
-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }
#
# Keep crashlytics stuff
-keep class com.crashlytics.** { *; }
#
# Remove debug logs:
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
}
#
# To avoid changing names of methods invoked on layout's onClick.
# Uncomment and add specific method names if using onClick on layouts
-keepclassmembers class * {
 public void onClickButton(android.view.View);
}
#
# Maintain java native methods
-keepclasseswithmembernames class * {
    native <methods>;
}
#
# To maintain custom components names that are used on layouts XML:
-keep public class * extends android.view.View { *; }
-keep public class * extends android.view.ViewGroup { *; }
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
#
# Maintain enums
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
    public static ** toString();
}
#
# To keep parcelable classes (to serialize - deserialize objects to sent through Intents)
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
#
# Keep the R
-keepclassmembers class **.R$* {
    public static <fields>;
}
#
# Support Serializable classes
-keepclassmembers class * implements java.io.Serializable {
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
#
# Protect card.io library from obfuscation as recommended in the manual
-keep class io.card.**
-keepclassmembers class io.card.** {
    *;
}
#
# Protect GMS
-keep public class com.google.android.gms.* { public *; }
-dontwarn com.google.android.gms.**
-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}
-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @ccom.google.android.gms.common.annotation.KeepName *;
}
#
# Protect Google Analytics
-keep class com.google.android.apps.analytics.**{ *; }
#
# Protect Flurry Analytics
-keep class com.flurry.** { *; }
-dontwarn com.flurry.**
-keepattributes *Annotation*,EnclosingMethod
-keepclasseswithmembers class * { public <init>(android.content.Context, android.util.AttributeSet, int); }
-keep class * extends java.util.ListResourceBundle { protected Object[][] getContents(); }
-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable { public static final *** NULL; }
-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * { @com.google.android.gms.common.annotation.KeepName *; }
-keepnames class * implements android.os.Parcelable { public static final ** CREATOR; }
#
# Protect ZXing stuff in your project
-keepclassmembers class com.google.zxing.client.android.InactivityTimer { *; }
-keep class com.google.zxing.client.android.InactivityTimer
-keep class com.google.zxing.client.android.camera.open.IOpenCameraInterface
-keep class * extends com.google.zxing.client.android.camera.open.IOpenCameraInterface
-keep class com.google.zxing.client.android.common.**
-keep class com.google.zxing.client.android.camera.open.**
#
# Protect ZBar
-keep class net.sourceforge.zbar.** { *; }
#
# Moxy MVP
-keep class **$$PresentersBinder
-keep class **$$State
-keep class **$$ParamsHolder
-keep class **$$ViewStateClassNameProvider
-keepnames class * extends com.arellomobile.mvp.*
