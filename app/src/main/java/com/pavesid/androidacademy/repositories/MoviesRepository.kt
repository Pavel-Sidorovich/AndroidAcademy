package com.pavesid.androidacademy.repositories

import com.pavesid.androidacademy.data.actors.ActorsResponse
import com.pavesid.androidacademy.data.details.Details
import com.pavesid.androidacademy.data.movies.Movie

interface MoviesRepository {

    suspend fun getMovies(
        page: Int = 1,
        language: String = "en-US"
    ): List<Movie>

    suspend fun getDetails(
        id: Int
    ): Details?

    suspend fun getActors(
        id: Int
    ): ActorsResponse

    suspend fun updateMovie(movie: Movie)

    suspend fun insertMovie(movie: Movie)

    suspend fun insertMovies(movies: List<Movie>)
}
