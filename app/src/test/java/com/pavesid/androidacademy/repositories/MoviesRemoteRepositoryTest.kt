package com.pavesid.androidacademy.repositories

import com.pavesid.androidacademy.data.Movie

class MoviesRemoteRepositoryTest : MoviesRepository {

    private var shouldReturnNetworkError = false

    private val movies: MutableList<Movie> = mutableListOf()

    override suspend fun getMovies(): List<Movie> {
        return if (shouldReturnNetworkError) {
            throw RuntimeException("Error")
        } else {
            movies
        }
    }

    override suspend fun updateMovie(movie: Movie) {
        movies.removeIf {
            it.id == movie.id
        }
        movies.add(movie)
    }

    override suspend fun insertMovie(movie: Movie) {
        movies.add(movie)
    }

    override suspend fun insertMovies(movies: List<Movie>) {
        this.movies.addAll(movies)
    }

    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }
}
