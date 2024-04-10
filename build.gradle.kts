
buildscript {
    dependencies {
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.44.1")
        classpath ("com.google.gms:google-services:4.3.13")
        classpath( "org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.10")

        classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:2.5.0")
    }
}

plugins {
    id ("com.android.application") version "7.3.1" apply false
    id ("com.android.library") version "7.3.1" apply false
    id ("org.jetbrains.kotlin.android") version "1.8.10" apply false
}

