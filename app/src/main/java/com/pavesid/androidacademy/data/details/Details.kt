package com.pavesid.androidacademy.data.details

import com.pavesid.androidacademy.data.genres.Genre

data class Details(
    val id: Long,
    val title: String,
    val overview: String,
    val poster: String?,
    val backdrop: String?,
    val genres: List<Genre>,
    val ratings: Float,
    val numberOfRatings: Int,
    val adult: Boolean,
    val runtime: Int
)
