package com.pavesid.androidacademy.data

import kotlinx.serialization.Serializable

@Serializable
data class JsonGenre(
    val id: Int,
    val name: String
)
