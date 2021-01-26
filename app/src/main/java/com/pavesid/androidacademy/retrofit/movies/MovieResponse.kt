package com.pavesid.androidacademy.retrofit.movies

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieResponse(
    @SerialName("dates")
    val dates: Dates = Dates("", ""),
    @SerialName("page")
    val page: Int,
    @SerialName("results")
    val movies: List<JsonMovie>,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int
)
