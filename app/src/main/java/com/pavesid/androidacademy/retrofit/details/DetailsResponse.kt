package com.pavesid.androidacademy.retrofit.details

import com.pavesid.androidacademy.data.genres.Genre
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DetailsResponse(
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
    @SerialName("genres")
    val genres: List<Genre>,
    @SerialName("vote_average")
    val ratings: Float,
    @SerialName("vote_count")
    val votesCount: Int,
    @SerialName("adult")
    val adult: Boolean,
    @SerialName("runtime")
    val runtime: Int
)
