package com.pavesid.androidacademy.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.pavesid.androidacademy.db.Converters
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "movie_items")
@TypeConverters(Converters::class)
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
    val actors: List<Actor>
) : Parcelable
