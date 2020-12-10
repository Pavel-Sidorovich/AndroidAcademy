package com.pavesid.androidacademy.data

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JsonMovie(
    val id: Int,
    val title: String,
    @SerialName("poster_path")
    @SerializedName("poster_path")
    val posterPicture: String,
    @SerialName("backdrop_path")
    @SerializedName("backdrop_path")
    val backdropPicture: String,
    val runtime: Int,
    @SerialName("genre_ids")
    @SerializedName("genre_ids")
    val genreIds: List<Int>,
    val actors: List<Int>,
    @SerialName("vote_average")
    @SerializedName("vote_average")
    val ratings: Float,
    @SerialName("vote_count")
    @SerializedName("vote_count")
    val votesCount: Int,
    val overview: String,
    val adult: Boolean
)
