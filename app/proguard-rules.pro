-useuniqueclassmembernames
-keeppackagenames
-renamesourcefileattribute SourceFile
-adaptresourcefilenames **.properties
-adaptresourcefilecontents **.properties,META-INF/MANIFEST.MF
-verbose
-keepattributes SourceFile,LineNumberTable
-keeppackagenames [com.mgoutad.an]

#-keep class com.mgoutad.ooa.klpsdk {
#    public com.mgoutad.ooa.klpsdk getInstance();
#    public void startMiiService(android.app.Activity);
#}

-keep public class * extends android.app.Activity

-keep public class * extends android.app.Application

-keep public class * extends android.app.Service

-keep public class * extends android.content.BroadcastReceiver

-keep public class * extends android.content.ContentProvider

-keep public class * extends android.app.Dialog
#-keep public class com.wqy.maiadsdk.R$*{*;}


# Keep - Applications. Keep all application classes, along with their 'main'
# methods.
-keepclasseswithmembers public class * {
    public static void main(java.lang.String[]);
}

# Also keep - Enumerations. Keep the special static methods that are required in
# enumeration classes.
-keepclassmembers enum  * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Also keep - Database drivers. Keep all implementations of java.sql.Driver.
#-keep class * extends java.sql.Driver

# Also keep - Swing UI L&F. Keep all extensions of javax.swing.plaf.ComponentUI,
# along with the special 'createUI' method.
#-keep class * extends javax.swing.plaf.ComponentUI {
#   public static javax.swing.plaf.ComponentUI createUI(javax.swing.JComponent);
#}

# Keep names - Native method names. Keep all native class/method names.
#-keepclasseswithmembers,allowshrinking class * {
#    native <methods>;
#}


-dontwarn org.apache.http.**
-keep class org.apache.http.**{*;}
 #不混淆R类
-keep class **.R$* {*;}