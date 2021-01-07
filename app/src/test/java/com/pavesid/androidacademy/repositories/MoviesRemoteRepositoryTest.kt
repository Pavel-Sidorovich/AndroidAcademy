package com.pavesid.androidacademy.repositories

import com.pavesid.androidacademy.data.actors.ActorsResponse
import com.pavesid.androidacademy.data.details.Details
import com.pavesid.androidacademy.data.details.DetailsResponse
import com.pavesid.androidacademy.data.movies.Movie

class MoviesRemoteRepositoryTest : MoviesRepository {

    private var shouldReturnNetworkError = false

    private val movies: MutableList<Movie> = mutableListOf()

    override suspend fun getMovies(page: Int, language: String): List<Movie> {
        return if (shouldReturnNetworkError) {
            throw RuntimeException("Error")
        } else {
            movies
        }
    }

    override suspend fun getDetails(id: Int): Details {
        return Details(
            DetailsResponse(1, "", "", "", "", emptyList(), 0f, 0, false, 0),
            emptyList(),
            emptyList()
        )
    }

    override suspend fun getActors(id: Int): ActorsResponse {
        return ActorsResponse(emptyList(), emptyList(), 1)
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
