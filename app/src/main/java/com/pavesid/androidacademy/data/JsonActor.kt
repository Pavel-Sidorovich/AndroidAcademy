package com.pavesid.androidacademy.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JsonActor(
    val id: Int,
    val name: String,
    @SerialName("profile_path")
    val profilePicture: String
)
