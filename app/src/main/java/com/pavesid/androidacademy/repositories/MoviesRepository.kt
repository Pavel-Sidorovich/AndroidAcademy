package com.pavesid.androidacademy.repositories

import com.pavesid.androidacademy.data.Movie

interface MoviesRepository {

    suspend fun getMovies(): List<Movie>

    suspend fun getMoviesFromDB(): List<Movie>

    suspend fun updateMovie(movie: Movie)

    suspend fun insertMovie(movie: Movie)

    suspend fun insertMovies(movies: List<Movie>)
}
