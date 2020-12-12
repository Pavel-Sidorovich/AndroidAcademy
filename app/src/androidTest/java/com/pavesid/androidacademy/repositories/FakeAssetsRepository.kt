package com.pavesid.androidacademy.repositories

import android.content.Context
import com.pavesid.androidacademy.data.Movie
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class FakeAssetsRepository @Inject constructor(
    @ApplicationContext val context: Context
) : MoviesRepository {

    override suspend fun getMovies(): List<Movie> = MoviesSource.loadMovies(context)
}
