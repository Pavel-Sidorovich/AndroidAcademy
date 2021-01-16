package com.pavesid.androidacademy.repositories

import com.pavesid.androidacademy.data.actors.CreditsResponse
import com.pavesid.androidacademy.data.details.Details
import com.pavesid.androidacademy.data.details.DetailsResponse
import com.pavesid.androidacademy.data.genres.Genre
import com.pavesid.androidacademy.data.movies.Movie

class MoviesRemoteRepositoryMock : MoviesRepository {

    private var shouldReturnNetworkError = false

    private val movies: MutableList<Movie> = mutableListOf()

    private val genres: MutableList<Genre> = mutableListOf()

    override suspend fun getDetails(id: Long): Details {
        return Details(
            DetailsResponse(1, "", "", "", "", emptyList(), 0f, 0, false, 0),
            emptyList(),
            emptyList()
        )
    }

    override suspend fun getActors(id: Long): CreditsResponse {
        return CreditsResponse(emptyList(), emptyList(), 1)
    }

    override suspend fun getMoviesByGenre(id: Long, page: Int): List<Movie> {
        return if (shouldReturnNetworkError) {
            throw RuntimeException("Error")
        } else {
            movies
        }
    }

    override suspend fun searchMovies(query: String, page: Int): List<Movie> {
        return if (shouldReturnNetworkError) {
            throw RuntimeException("Error")
        } else {
            movies
        }
    }

    override suspend fun getGenres(): List<Genre> {
        return if (shouldReturnNetworkError) {
            throw RuntimeException("Error")
        } else {
            genres
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
