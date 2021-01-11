package com.pavesid.androidacademy.data.details

import com.pavesid.androidacademy.data.actors.Cast
import com.pavesid.androidacademy.data.actors.Crew
import kotlinx.serialization.SerialName

data class Details(
    @SerialName("detailsResponse")
    val detailsResponse: DetailsResponse,
    @SerialName("cast")
    val cast: List<Cast>,
    @SerialName("crew")
    val crew: List<Crew>
)
