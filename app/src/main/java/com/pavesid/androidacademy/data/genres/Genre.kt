package com.pavesid.androidacademy.data.genres

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Genre(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
    var isChecked: Boolean = false
)
