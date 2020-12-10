package com.pavesid.androidacademy.data

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JsonActor(
    val id: Int,
    val name: String,
    @SerialName("profile_path")
    @SerializedName("profile_path")
    val profilePicture: String
)
