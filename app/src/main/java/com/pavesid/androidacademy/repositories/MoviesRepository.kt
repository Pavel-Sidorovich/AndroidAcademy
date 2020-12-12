package com.pavesid.androidacademy.repositories

import com.pavesid.androidacademy.data.Movie

interface MoviesRepository {
    suspend fun getMovies(): List<Movie>
}
