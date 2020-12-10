package com.pavesid.androidacademy.repositories.assets

import android.content.Context
import com.pavesid.androidacademy.data.Movie
import com.pavesid.androidacademy.repositories.MoviesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

// Later would be transfer to test directory
class FakeAssetsRepository @Inject constructor(
    @ApplicationContext val context: Context
) : MoviesRepository {

    override suspend fun getMovies(): List<Movie> = JsonHelper.loadMovies(context)
}
