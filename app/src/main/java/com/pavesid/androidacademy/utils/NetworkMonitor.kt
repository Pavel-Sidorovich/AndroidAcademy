package com.pavesid.androidacademy.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import javax.inject.Inject
import kotlin.properties.Delegates
import timber.log.Timber

class NetworkMonitor @Inject constructor(val context: Context) {

    fun startNetworkCallback() {
        val cm: ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val builder: NetworkRequest.Builder = NetworkRequest.Builder()

        cm.registerNetworkCallback(
            builder.build(),
            object : ConnectivityManager.NetworkCallback() {

                override fun onAvailable(network: Network) {
                    isNetworkConnected = true
                }

                override fun onLost(network: Network) {
                    isNetworkConnected = false
                }
            }
        )
    }

    companion object {
        var isNetworkConnected: Boolean by Delegates.observable(false) { _, _, newValue ->
            Timber.d(newValue.toString())
        }
    }
}
