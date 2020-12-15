package com.pavesid.androidacademy

import android.app.Application
import android.os.StrictMode
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        if (BuildConfig.DEBUG) {
            StrictMode.enableDefaults()
            Timber.plant(Timber.DebugTree())
        }
        super.onCreate()
    }

    companion object {
        const val INIT = "initialized"
        const val POS_X = "x_position"
        const val POS_Y = "y_position"
        const val RADIUS = "radius"
        const val SCREEN = "screen_image"
        const val FINISH = "animation_finish"
    }
}
