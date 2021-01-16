package com.pavesid.androidacademy.data.movies

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JsonMovie(
    @SerialName("id")
    val id: Long,
    @SerialName("title")
    val title: String,
    @SerialName("overview")
    val overview: String,
    @SerialName("poster_path")
    val posterPicture: String?,
    @SerialName("backdrop_path")
    val backdropPicture: String?,
    @SerialName("genre_ids")
    val genreIds: List<Long>,
    @SerialName("vote_average")
    val ratings: Float,
    @SerialName("vote_count")
    val votesCount: Int,
    @SerialName("adult")
    val adult: Boolean
)
