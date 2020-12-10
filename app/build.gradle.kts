plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("org.jlleitschuh.gradle.ktlint").version("9.4.1")
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    compileSdkVersion(AppConfig.compileSdk)
    buildToolsVersion(AppConfig.buildToolsVersion)

    defaultConfig {
        applicationId = "com.pavesid.androidacademy"
        minSdkVersion(AppConfig.minSdk)
        targetSdkVersion(AppConfig.targetSdk)
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName

        testInstrumentationRunner = AppConfig.androidTestInstrumentation
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                AppConfig.proguardConsumerRules
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            this.jvmTarget = "1.8"
        }
    }

    viewBinding {
        android.buildFeatures.viewBinding = true
    }
}

dependencies {

    // std lib
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // App libs
    implementation(AppDependencies.appLibraries)

    // Test libs
    testImplementation(AppDependencies.testLibraries)
    androidTestImplementation(AppDependencies.androidTestLibraries)
}
