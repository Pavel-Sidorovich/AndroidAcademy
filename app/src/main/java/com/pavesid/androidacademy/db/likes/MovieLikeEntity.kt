package com.pavesid.androidacademy.db.likes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_like_items")
data class MovieLikeEntity(
    @PrimaryKey
    @ColumnInfo(name = "movie_id")
    val id: Long,
    @ColumnInfo(name = "movie_liked")
    var liked: Boolean = false
)
