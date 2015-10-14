name := "EasyTaipei"

organization := "moe.brianhsu.easytaipei"

scalaVersion := "2.11.7"

proguardOptions in Android ++= Seq(
  "-dontwarn com.thoughtworks.paranamer.**"
)

libraryDependencies ++= Seq(
  "com.android.support" % "design" % "23.0.1",
  "com.google.android.gms" % "play-services-maps" % "8.1.0",
  "org.json4s" %% "json4s-native" % "3.3.0",
  "com.google.maps.android" % "android-maps-utils" % "0.4+"
)
