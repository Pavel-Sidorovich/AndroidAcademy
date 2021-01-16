package com.pavesid.androidacademy.repositories

import com.pavesid.androidacademy.data.actors.CreditsResponse
import com.pavesid.androidacademy.data.details.Details
import com.pavesid.androidacademy.data.genres.Genre
import com.pavesid.androidacademy.data.movies.Movie

interface MoviesRepository {

    suspend fun getDetails(
        id: Long
    ): Details

    suspend fun getActors(
        id: Long
    ): CreditsResponse

    suspend fun getMoviesByGenre(
        id: Long = -1,
        page: Int = 1
    ): List<Movie>

    suspend fun searchMovies(
        query: String = "",
        page: Int = 1
    ): List<Movie>

    suspend fun getGenres(): List<Genre>

    suspend fun updateMovie(movie: Movie)

    suspend fun insertMovie(movie: Movie)

    suspend fun insertMovies(movies: List<Movie>)
}
