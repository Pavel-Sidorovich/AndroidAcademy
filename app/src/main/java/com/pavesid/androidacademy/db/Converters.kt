package com.pavesid.androidacademy.db

import androidx.room.TypeConverter
import com.pavesid.androidacademy.data.actors.Cast
import com.pavesid.androidacademy.data.genres.Genre
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {

    @TypeConverter
    fun genresToString(genres: List<Genre>): String = genres.joinToString("|") {
        Json.encodeToString(it)
    }

    @TypeConverter
    fun stringToGenre(genres: String): List<Genre> = genres.split("|").map {
        Json.decodeFromString(it)
    }

    @TypeConverter
    fun actorsToString(actors: List<Cast>): String = actors.joinToString("|") {
        Json.encodeToString(it)
    }

    @TypeConverter
    fun stringToActors(actors: String): List<Cast> = if (actors.isEmpty()) {
        emptyList()
    } else {
        actors.split("|").map {
            Json.decodeFromString(it)
        }
    }
}
