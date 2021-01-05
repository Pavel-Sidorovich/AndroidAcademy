package com.pavesid.androidacademy.utils

import kotlin.properties.Delegates
import timber.log.Timber

object Utils {
    var isNetworkConnected: Boolean by Delegates.observable(false) { _, _, newValue ->
        Timber.d(newValue.toString())
    }
}
