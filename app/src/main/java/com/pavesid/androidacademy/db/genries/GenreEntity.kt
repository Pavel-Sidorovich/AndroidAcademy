package com.pavesid.androidacademy.db.genries

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName

@Entity(tableName = "genre_items")
data class GenreEntity(
    @PrimaryKey
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String
)
