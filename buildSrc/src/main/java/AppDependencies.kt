import org.gradle.api.artifacts.dsl.DependencyHandler

object AppDependencies {

    // Stdlib
    private const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin_version}"

    // Android UI
    private const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat_version}"
    private const val coreKtx = "androidx.core:core-ktx:${Versions.core_ktx_version}"
    private const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraint_layout_version}"
    private const val material = "com.google.android.material:material:${Versions.material_version}"

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

    // Retrofit
    private const val retrofit2 = "com.squareup.retrofit2:retrofit:${Versions.retrofit_version}"
    private const val converterSerialization =
        "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:${Versions.serialization_converter_version}"

    // LocalBroadcastManager
    private const val localBroadcastManager =
        "androidx.localbroadcastmanager:localbroadcastmanager:${Versions.local_broadcast_manager_version}"

    //KTLint
    const val ktlint = "com.pinterest:ktlint:${Versions.kt_lint_version}"

    // Test libs
    private const val junit = "junit:junit:${Versions.junit}"
    private const val extJUnit = "androidx.test.ext:junit:${Versions.extJunit}"
    private const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espresso}"

    val appLibraries = arrayListOf<String>().apply {
        add(kotlinStdLib)
        add(coreKtx)
        add(appcompat)
        add(constraintLayout)
        add(material)
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
        add(retrofit2)
        add(converterSerialization)
        add(localBroadcastManager)
        add(preferenceKtx)
    }

    val kaptLibraries = arrayListOf<String>().apply {
        add(hiltAndroidCompiler)
        add(hiltCompiler)
    }

    val androidTestLibraries = arrayListOf<String>().apply {
        add(extJUnit)
        add(espressoCore)
    }

    val testLibraries = arrayListOf<String>().apply {
        add(junit)
    }
}

//util functions for adding the different type dependencies from build.gradle file
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
