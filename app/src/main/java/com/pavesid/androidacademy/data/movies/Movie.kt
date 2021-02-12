package com.pavesid.androidacademy.data.movies

import com.pavesid.androidacademy.data.genres.Genre
import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val id: Long,
    val title: String = "",
    val overview: String = "",
    val poster: String = "",
    val backdrop: String = "",
    val ratings: Float = 0f,
    val numberOfRatings: Int = 0,
    val minimumAge: Int = 0,
    var runtime: Int = 0,
    val genres: List<Genre> = emptyList(),
    var liked: Boolean = false
)
