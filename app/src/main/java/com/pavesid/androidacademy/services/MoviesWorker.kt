package com.pavesid.androidacademy.services

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.pavesid.androidacademy.repositories.MoviesRepository
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MoviesWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    @Inject
    lateinit var repository: MoviesRepository

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            repository.getMoviesFromAPI(1)
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
