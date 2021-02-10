// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    
    repositories {
        google()
        jcenter()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:${Versions.gradle_version}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin_version}")
        classpath("org.jetbrains.kotlin:kotlin-serialization:${Versions.kotlin_version}")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt_version}")
        classpath("com.google.gms:google-services:${Versions.google_services_version}")
        classpath("com.google.firebase:firebase-crashlytics-gradle:${Versions.crashlytics_versions}")
        classpath("com.github.ben-manes:gradle-versions-plugin:${Versions.gradle_versions_plugin}")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven {
            url = uri("https://jitpack.io")
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
