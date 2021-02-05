package com.pavesid.androidacademy.services

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import com.pavesid.androidacademy.BuildConfig
import java.util.concurrent.TimeUnit

object MoviesWorkerRepository {
    private val constraints = Constraints.Builder().setRequiresCharging(true)
        .setRequiredNetworkType(NetworkType.UNMETERED).build()

    val request = PeriodicWorkRequestBuilder<MoviesWorker>(8, TimeUnit.HOURS)
        .setConstraints(constraints)
        .build()

    const val WORK_NAME = "${BuildConfig.APPLICATION_ID}.RefreshMoviesWorker"
}
