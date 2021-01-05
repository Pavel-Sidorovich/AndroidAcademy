package com.pavesid.androidacademy

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import androidx.appcompat.app.AppCompatDelegate
import com.pavesid.androidacademy.utils.Utils
import dagger.hilt.android.HiltAndroidApp
import java.io.File
import javax.inject.Inject
import timber.log.Timber

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var prefs: SharedPreferences

    init {
        instance = this
    }

    override fun onCreate() {
        if (BuildConfig.DEBUG) {
//            StrictMode.enableDefaults()
            Timber.plant(Timber.DebugTree())
        }
        super.onCreate()
        startNetworkCallback()
        changeTheme()
    }

    override fun onTerminate() {
        super.onTerminate()
        stopNetworkCallback()
    }

    private fun changeTheme() {
        if (prefs.getBoolean(
                THEME,
                false
            )
        ) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun startNetworkCallback() {
        val cm: ConnectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val builder: NetworkRequest.Builder = NetworkRequest.Builder()

        cm.registerNetworkCallback(
            builder.build(),
            object : ConnectivityManager.NetworkCallback() {

                override fun onAvailable(network: Network) {
                    Utils.isNetworkConnected = true
                }

                override fun onLost(network: Network) {
                    Utils.isNetworkConnected = false
                }
            }
        )
    }

    private fun stopNetworkCallback() {
        val cm: ConnectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        cm.unregisterNetworkCallback(ConnectivityManager.NetworkCallback())
    }

    companion object {
        const val THEME = "switchTheme"
        private lateinit var instance: Application
        val thisCacheDir: File
            get() = instance.cacheDir
    }
}
