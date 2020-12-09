import org.gradle.api.artifacts.dsl.DependencyHandler

object AppDependencies {

    //Stdlib
    private const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin_version}"

    //Android UI
    private const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat_version}"
    private const val coreKtx = "androidx.core:core-ktx:${Versions.core_ktx_version}"
    private const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintlayout_version}"
    private const val material = "com.google.android.material:material:${Versions.material_version}"

    //Coil
    private const val coil = "io.coil-kt:coil:${Versions.coil_version}"
    private const val coilTransformations =
        "com.github.Commit451.coil-transformations:transformations:${Versions.coil_version}"

    //RatingBar
    private const val ratingBar =
        "me.zhanghai.android.materialratingbar:library:${Versions.ratingbar_version}"

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

    //Fragment KTX
    private const val fragmentKtx =
        "androidx.fragment:fragment-ktx:${Versions.fragment_ktx_version}"

    // LeakCanary
    private const val leakcanary =
        "com.squareup.leakcanary:leakcanary-android:${Versions.leakcanary_version}"

    //Serialization
    private const val serialization =
        "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.serialization_version}"

    //Timber
    private const val timber = "com.jakewharton.timber:timber:${Versions.timber_version}"

    //Test libs
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
