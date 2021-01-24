package com.pavesid.androidacademy.repositories

import com.pavesid.androidacademy.data.actors.CreditsResponse
import com.pavesid.androidacademy.data.details.Details
import com.pavesid.androidacademy.data.entities.MovieLikeEntity
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
        id: Long = Long.MIN_VALUE,
        page: Int = 1
    ): List<Movie>

    suspend fun getMovies(page: Int): List<Movie>

    suspend fun searchMovies(
        query: String = "",
        page: Int = 1
    ): List<Movie>

    suspend fun getGenresFromAPI(): List<Genre>

    suspend fun getGenresFromDB(): List<Genre>

    suspend fun updateMovie(movieLikeEntity: MovieLikeEntity)
}
