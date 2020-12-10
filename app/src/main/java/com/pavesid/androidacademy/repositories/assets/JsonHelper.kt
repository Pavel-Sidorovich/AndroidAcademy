package com.pavesid.androidacademy.repositories.assets

import android.content.Context
import com.pavesid.androidacademy.data.Actor
import com.pavesid.androidacademy.data.Genre
import com.pavesid.androidacademy.data.JsonActor
import com.pavesid.androidacademy.data.JsonGenre
import com.pavesid.androidacademy.data.JsonMovie
import com.pavesid.androidacademy.data.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

internal object JsonHelper {

    internal suspend fun loadMovies(context: Context): List<Movie> = withContext(Dispatchers.IO) {
        val genresMap = loadGenres(context)
        val actorsMap = loadActors(context)

        val data = readAssetFileToString(context, "data.json")
        parseMovies(data, genresMap, actorsMap)
    }

    private val jsonFormat = Json { ignoreUnknownKeys = true }

    private suspend fun loadGenres(context: Context): List<Genre> = withContext(Dispatchers.IO) {
        val data = readAssetFileToString(context, "genres.json")
        parseGenres(data)
    }

    private fun parseGenres(data: String): List<Genre> {
        val jsonGenres = jsonFormat.decodeFromString<List<JsonGenre>>(data)
        return jsonGenres.map { Genre(id = it.id, name = it.name) }
    }

    private fun readAssetFileToString(context: Context, fileName: String): String {
        val stream = context.assets.open(fileName)
        return stream.bufferedReader().readText()
    }

    private suspend fun loadActors(context: Context): List<Actor> = withContext(Dispatchers.IO) {
        val data = readAssetFileToString(context, "people.json")
        parseActors(data)
    }

    private fun parseActors(data: String): List<Actor> {
        val jsonActors = jsonFormat.decodeFromString<List<JsonActor>>(data)
        return jsonActors.map { Actor(id = it.id, name = it.name, picture = it.profilePicture) }
    }

    private fun parseMovies(
        data: String,
        genres: List<Genre>,
        actors: List<Actor>
    ): List<Movie> {
        val genresMap = genres.associateBy { it.id }
        val actorsMap = actors.associateBy { it.id }

        val jsonMovies = jsonFormat.decodeFromString<List<JsonMovie>>(data)

        return jsonMovies.map { jsonMovie ->
            Movie(
                id = jsonMovie.id,
                title = jsonMovie.title,
                overview = jsonMovie.overview,
                poster = jsonMovie.posterPicture,
                backdrop = jsonMovie.backdropPicture,
                ratings = jsonMovie.ratings,
                numberOfRatings = jsonMovie.votesCount,
                minimumAge = if (jsonMovie.adult) 16 else 13,
                runtime = jsonMovie.runtime,
                genres = jsonMovie.genreIds.map {
                    genresMap[it] ?: throw IllegalArgumentException("Genre not found")
                },
                actors = jsonMovie.actors.map {
                    actorsMap[it] ?: throw IllegalArgumentException("Actor not found")
                }
            )
        }
    }
}
