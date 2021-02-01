package com.pavesid.androidacademy.services

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import java.util.concurrent.TimeUnit

object MoviesWorkerRepository {
    private val constraints = Constraints.Builder().setRequiresCharging(true).setRequiredNetworkType(NetworkType.CONNECTED).build()

    val request = PeriodicWorkRequestBuilder<MoviesWorker>(8, TimeUnit.HOURS)
        .setConstraints(constraints)
        .build()
}
