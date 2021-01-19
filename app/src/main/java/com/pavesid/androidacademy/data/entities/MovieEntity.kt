package com.pavesid.androidacademy.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_items")
data class MovieEntity(
    @PrimaryKey
    @ColumnInfo(name = "movie_id")
    val id: Long,
    @ColumnInfo(name = "movie_runtime")
    var runtime: Int = 0,
    @ColumnInfo(name = "movie_liked")
    var liked: Boolean = false
)
