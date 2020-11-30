package com.pavesid.androidacademy.data.local

import com.pavesid.androidacademy.data.local.model.Movie
import com.pavesid.androidacademy.data.local.model.MoviePreview

interface MoviesRepository {
    fun getMovieById(id: Int): Movie
    fun getAllPreviews(): List<MoviePreview>
}
