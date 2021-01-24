package com.pavesid.androidacademy.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.pavesid.androidacademy.repositories.Converters

@TypeConverters(Converters::class)
@Entity(tableName = "movie_items")
data class MovieEntity(
    @PrimaryKey
    @ColumnInfo(name = "movie_id")
    val id: Long,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "overview")
    val overview: String,
    @ColumnInfo(name = "poster_path")
    val posterPicture: String?,
    @ColumnInfo(name = "backdrop_path")
    val backdropPicture: String?,
    @ColumnInfo(name = "genre_ids")
    val genreIds: List<Long>,
    @ColumnInfo(name = "vote_average")
    val ratings: Float,
    @ColumnInfo(name = "vote_count")
    val votesCount: Int,
    @ColumnInfo(name = "popularity")
    val popularity: Float,
    @ColumnInfo(name = "adult")
    val adult: Boolean
)
