package com.pavesid.androidacademy.services

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import com.pavesid.androidacademy.BuildConfig
import java.util.concurrent.TimeUnit

object MoviesWorkerRepository {
    private val constraints = Constraints.Builder().setRequiresCharging(true)
        .setRequiredNetworkType(NetworkType.CONNECTED).build()

    val request = PeriodicWorkRequestBuilder<MoviesWorker>(15, TimeUnit.MINUTES)
        .setConstraints(constraints)
        .build()

    const val WORK_NAME = "${BuildConfig.APPLICATION_ID}.RefreshMoviesWorker"
}
