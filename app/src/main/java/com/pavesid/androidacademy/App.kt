package com.pavesid.androidacademy

import android.app.Application
import android.content.SharedPreferences
import android.os.StrictMode
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import timber.log.Timber

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var prefs: SharedPreferences

    override fun onCreate() {
        if (BuildConfig.DEBUG) {
            StrictMode.enableDefaults()
            Timber.plant(Timber.DebugTree())
        }
        super.onCreate()
        changeTheme()
    }

    private fun changeTheme() {
        if (prefs.getBoolean(
                THEME,
                false
            )
        ) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    companion object {
        const val THEME = "switchTheme"
    }
}
