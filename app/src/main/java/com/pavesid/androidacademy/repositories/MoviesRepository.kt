package com.pavesid.androidacademy.repositories

import com.pavesid.androidacademy.data.details.DetailsWithCredits
import com.pavesid.androidacademy.data.genres.Genre
import com.pavesid.androidacademy.data.movies.Movie

interface MoviesRepository {

    suspend fun getDetails(
        id: Long
    ): DetailsWithCredits

    suspend fun getMoviesByGenreFromAPI(
        id: Long = Long.MIN_VALUE,
        page: Int = 1
    ): List<Movie>

    suspend fun getMoviesByGenreFromDB(
        id: Long = Long.MIN_VALUE
    ): List<Movie>

    suspend fun getMoviesFromAPI(page: Int): List<Movie>

    suspend fun getMoviesFromDB(): List<Movie>

    suspend fun searchMoviesFromAPI(
        query: String = "",
        page: Int = 1
    ): List<Movie>

    suspend fun getGenresFromAPI(): List<Genre>

    suspend fun getGenresFromDB(): List<Genre>

    suspend fun updateMovieLike(movie: Movie)
}
