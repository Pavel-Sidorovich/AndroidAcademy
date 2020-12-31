package com.pavesid.androidacademy.db

import androidx.room.TypeConverter
import com.pavesid.androidacademy.data.Actor
import com.pavesid.androidacademy.data.Genre
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {

    @TypeConverter
    fun genresToString(genres: List<Genre>): String = Json.encodeToString(genres)

    @TypeConverter
    fun stringToGenre(genres: String): List<Genre> = Json.decodeFromString(genres)

    @TypeConverter
    fun actorsToString(actors: List<Actor>): String = Json.encodeToString(actors)

    @TypeConverter
    fun stringToActors(actors: String): List<Actor> = if (actors.isEmpty()) {
        emptyList()
    } else {
        Json.decodeFromString(actors)
    }
}
