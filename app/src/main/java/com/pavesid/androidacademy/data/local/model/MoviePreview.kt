package com.pavesid.androidacademy.data.local.model

data class MoviePreview(
    val id: Int,
    val image: String,
    val name: String,
    val rating: Int,
    val pg: Int,
    val reviews: Int,
    val duration: Int,
    val tags: List<String>
)
