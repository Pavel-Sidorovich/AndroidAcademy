package com.pavesid.androidacademy.data.details

import com.pavesid.androidacademy.data.actors.Cast

data class Details(
    val detailsResponse: DetailsResponse,
    val cast: List<Cast>
)
