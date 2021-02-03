package com.pavesid.androidacademy.services

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.pavesid.androidacademy.di.IODispatcher
import com.pavesid.androidacademy.repositories.MoviesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class MoviesWorker @WorkerInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: MoviesRepository,
    @IODispatcher private val dispatcher: CoroutineDispatcher
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(dispatcher) {
        try {
            repository.getMoviesFromAPI(1)
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
