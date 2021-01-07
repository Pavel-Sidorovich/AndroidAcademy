package com.pavesid.androidacademy.data.movies

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.pavesid.androidacademy.data.genres.Genre
import com.pavesid.androidacademy.db.Converters
import kotlinx.serialization.Serializable

@Entity(tableName = "movie_items")
@TypeConverters(Converters::class)
@Serializable
data class Movie(
    @PrimaryKey
    val id: Int,
    val title: String,
    val overview: String,
    val poster: String,
    val backdrop: String,
    val ratings: Float,
    val numberOfRatings: Int,
    val minimumAge: Int,
    val runtime: Int,
    val genres: List<Genre>,
    var liked: Boolean = false
)
