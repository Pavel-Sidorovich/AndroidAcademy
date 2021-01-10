package com.pavesid.androidacademy.data.actors

import kotlinx.serialization.Serializable

@Serializable
data class CreditsResponse(
    val cast: List<Cast>,
    val crew: List<Crew>,
    val id: Int
)
