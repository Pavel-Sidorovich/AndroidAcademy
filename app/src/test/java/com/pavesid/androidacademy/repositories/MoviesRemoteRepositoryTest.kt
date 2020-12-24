package com.pavesid.androidacademy.repositories

import com.pavesid.androidacademy.data.Movie

class MoviesRemoteRepositoryTest : MoviesRepository {

    private var shouldReturnNetworkError = false

    override suspend fun getMovies(): List<Movie> {
        return if (shouldReturnNetworkError) {
            throw RuntimeException("Error")
        } else {
            emptyList()
        }
    }

    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }
}
