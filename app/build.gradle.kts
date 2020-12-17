plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("kotlin-android")
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
        getByName("debug") {
            isTestCoverageEnabled = true
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

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {

    // std lib
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // App libs
    implementation(AppDependencies.appLibraries)

    // Kapt
    kapt(AppDependencies.kaptLibraries)

    implementation("org.jacoco:org.jacoco.core:0.8.5")

    // Test libs
    testImplementation(AppDependencies.testLibraries)
    androidTestImplementation(AppDependencies.androidTestLibraries)
}

kapt {
    correctErrorTypes = true
}

fun Project.getKtlintConfiguration(): Configuration {
    return configurations.findByName("ktlint") ?: configurations.create("ktlint") {
        val dependency = project.dependencies.create(AppDependencies.ktlint)
        dependencies.add(dependency)
    }
}

val outputDirJacoco = "${project.buildDir}/reports/jacoco"
val outputDir = "${project.buildDir}/reports/ktlint/"
val inputFiles = project.fileTree(mapOf("dir" to "src", "include" to "**/*.kt"))

val ktlintDebugCheck by tasks.creating(JavaExec::class) {
    inputs.files(inputFiles)
    outputs.dir(outputDir)

    description = "Check Kotlin code style."
    classpath = getKtlintConfiguration()
    main = "com.pinterest.ktlint.Main"
    args = listOf("src/**/*.kt")
}

val ktlintFormat by tasks.creating(JavaExec::class) {
    inputs.files(inputFiles)
    outputs.dir(outputDir)

    description = "Fix Kotlin code style deviations."
    classpath = getKtlintConfiguration()
    main = "com.pinterest.ktlint.Main"
    args = listOf("-F", "src/**/*.kt")
}
