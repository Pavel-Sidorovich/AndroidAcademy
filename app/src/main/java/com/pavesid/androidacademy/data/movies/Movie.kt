package com.pavesid.androidacademy.data.movies

import com.pavesid.androidacademy.data.genres.Genre
import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val id: Long,
    val title: String,
    val overview: String,
    val poster: String,
    val backdrop: String,
    val ratings: Float,
    val numberOfRatings: Int,
    val minimumAge: Int,
    val runtime: Int,
    val genres: List<Genre>,
    var liked: Boolean = false
)
