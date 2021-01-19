package com.pavesid.androidacademy.data.details

import com.pavesid.androidacademy.data.actors.Cast
import com.pavesid.androidacademy.data.actors.Crew

data class Details(
    val detailsResponse: DetailsResponse,
    val cast: List<Cast>,
    val crew: List<Crew>
)
