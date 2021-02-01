package com.pavesid.androidacademy.data.details

import com.pavesid.androidacademy.data.actors.Cast
import com.pavesid.androidacademy.data.actors.Crew

data class DetailsWithCredits(
    val details: Details,
    val cast: List<Cast>,
    val crew: List<Crew>
)
