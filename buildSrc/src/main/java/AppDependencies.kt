import org.gradle.api.artifacts.dsl.DependencyHandler

object AppDependencies {

    // Stdlib
    private const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin_version}"
    private const val kotlinStdLibJdk = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin_version}"

    // Android UI
    private const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat_version}"
    private const val coreKtx = "androidx.core:core-ktx:${Versions.core_ktx_version}"
    private const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraint_layout_version}"
    private const val material = "com.google.android.material:material:${Versions.material_version}"
    private const val swipeRefreshLayout = "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swipe_refresh_layout}"

    // Coil
    private const val coil = "io.coil-kt:coil:${Versions.coil_version}"
    private const val coilTransformations =
        "com.github.Commit451.coil-transformations:transformations:${Versions.coil_version}"

    // RatingBar
    private const val ratingBar =
        "me.zhanghai.android.materialratingbar:library:${Versions.rating_bar_version}"

    // Coroutines
    private const val coroutinesCore =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines_version}"
    private const val coroutinesAndroid =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines_version}"

    // Coroutine Lifecycle Scopes
    private const val livedata =
        "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle_version}"
    private const val viewmodel =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle_version}"
    private const val runtime =
        "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle_version}"

    // Fragment KTX
    private const val fragmentKtx =
        "androidx.fragment:fragment-ktx:${Versions.fragment_ktx_version}"

    // Preference KTX
    private const val preferenceKtx =
        "androidx.preference:preference-ktx:${Versions.preference_ktx_version}"

    // LeakCanary
    private const val leakcanary =
        "com.squareup.leakcanary:leakcanary-android:${Versions.leak_canary_version}"

    // Serialization
    private const val serialization =
        "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.serialization_version}"

    // Timber
    private const val timber = "com.jakewharton.timber:timber:${Versions.timber_version}"

    // Dagger - Hilt
    private const val hiltAndroid = "com.google.dagger:hilt-android:${Versions.hilt_version}"
    private const val hiltAndroidCompiler =
        "com.google.dagger:hilt-android-compiler:${Versions.hilt_version}"

    private const val hiltLifecycleViewModel =
        "androidx.hilt:hilt-lifecycle-viewmodel:${Versions.hilt_compiler_version}"
    private const val hiltCompiler =
        "androidx.hilt:hilt-compiler:${Versions.hilt_compiler_version}"
    private const val hiltWorker = "androidx.hilt:hilt-work:${Versions.hilt_compiler_version}"

    // Retrofit
    private const val logging = "com.squareup.okhttp3:logging-interceptor:${Versions.logging_version}"
    private const val retrofit2 = "com.squareup.retrofit2:retrofit:${Versions.retrofit_version}"
    private const val converterSerialization =
        "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:${Versions.serialization_converter_version}"

    // Firebase
    private const val firebaseCrashlytics = "com.google.firebase:firebase-crashlytics-ktx"
    private const val firebaseAnalytics = "com.google.firebase:firebase-analytics-ktx"

    // KTLint
    const val ktlint = "com.pinterest:ktlint:${Versions.kt_lint_version}"

    // Lottie
    private const val lottie = "com.airbnb.android:lottie:${Versions.lottie_version}"

    // Room
    private const val roomRuntime = "androidx.room:room-runtime:${Versions.room_version}"
    private const val roomCompiler = "androidx.room:room-compiler:${Versions.room_version}"

    // Kotlin Extensions and Coroutines support for Room
    private const val roomKtx = "androidx.room:room-ktx:${Versions.room_version}"

    // SmallBang
    private const val bang = "pub.hanks:smallbang:${Versions.bang_version}"

    // Kotlin + coroutines
    private const val workManager = "androidx.work:work-runtime-ktx:${Versions.work_manager_version}"

    // Test libs
    private const val junit = "junit:junit:${Versions.junit_version}"
    private const val extJUnit = "androidx.test.ext:junit:${Versions.extJunit_version}"
    private const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espresso_version}"
    private const val truth = "com.google.truth:truth:${Versions.truth_version}"
    private const val mockk = "io.mockk:mockk:${Versions.mockk_version}"


    private const val coroutines_test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines_test_version}"
    private const val core_testing = "androidx.arch.core:core-testing:${Versions.core_testing_version}"
    private const val mockito_core = "org.mockito:mockito-core:${Versions.mockito_core_version}"

    val appLibraries = arrayListOf<String>().apply {
        add(kotlinStdLib)
        add(kotlinStdLibJdk)
        add(coreKtx)
        add(appcompat)
        add(constraintLayout)
        add(material)
        add(swipeRefreshLayout)
        add(coil)
        add(coilTransformations)
        add(ratingBar)
        add(coroutinesCore)
        add(coroutinesAndroid)
        add(livedata)
        add(viewmodel)
        add(runtime)
        add(fragmentKtx)
        add(leakcanary)
        add(serialization)
        add(timber)
        add(hiltAndroid)
        add(hiltLifecycleViewModel)
        add(hiltWorker)
        add(logging)
        add(retrofit2)
        add(converterSerialization)
        add(lottie)
        add(roomRuntime)
        add(roomKtx)
        add(bang)
        add(workManager)
        add(preferenceKtx)
        add(firebaseCrashlytics)
        add(firebaseAnalytics)
    }

    val kaptLibraries = arrayListOf<String>().apply {
        add(hiltAndroidCompiler)
        add(hiltCompiler)
        add(roomCompiler)
    }

    val androidTestLibraries = arrayListOf<String>().apply {
        add(extJUnit)
        add(espressoCore)
    }

    val testLibraries = arrayListOf<String>().apply {
        add(junit)
        add(coroutines_test)
        add(core_testing)
        add(mockito_core)
        add(truth)
        add(mockk)
    }
}

// Util functions for adding the different type dependencies from build.gradle file
fun DependencyHandler.kapt(list: List<String>) {
    list.forEach { dependency ->
        add("kapt", dependency)
    }
}

fun DependencyHandler.implementation(list: List<String>) {
    list.forEach { dependency ->
        add("implementation", dependency)
    }
}

fun DependencyHandler.androidTestImplementation(list: List<String>) {
    list.forEach { dependency ->
        add("androidTestImplementation", dependency)
    }
}

fun DependencyHandler.testImplementation(list: List<String>) {
    list.forEach { dependency ->
        add("testImplementation", dependency)
    }
}
