package com.pavesid.androidacademy.retrofit.genres

import com.pavesid.androidacademy.data.genres.Genre
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenresResponse(
    @SerialName("genres")
    val genres: List<Genre>
)
