package com.pavesid.androidacademy

import android.app.Application
import android.content.SharedPreferences
import android.os.StrictMode
import androidx.appcompat.app.AppCompatDelegate
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.pavesid.androidacademy.utils.NetworkMonitor
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import timber.log.Timber

@HiltAndroidApp
class App : Application(), Configuration.Provider {

    @Inject
    lateinit var prefs: SharedPreferences

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var request: PeriodicWorkRequest

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .build()

    override fun onCreate() {
        if (BuildConfig.DEBUG) {
            StrictMode.enableDefaults()
            Timber.plant(Timber.DebugTree())
        }
        super.onCreate()
        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
        networkMonitor.startNetworkCallback()
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
        private const val WORK_NAME = "${BuildConfig.APPLICATION_ID}.RefreshMoviesWorker"
    }
}
