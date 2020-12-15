package com.pavesid.androidacademy.repositories

import com.pavesid.androidacademy.data.Actor
import com.pavesid.androidacademy.data.Genre
import com.pavesid.androidacademy.data.JsonMovie
import com.pavesid.androidacademy.data.Movie
import javax.inject.Inject
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class MoviesRemoteRepository @Inject constructor() : MoviesRepository {

    override suspend fun getMovies(): List<Movie> {
        var movies = listOf<JsonMovie>()
        var genres = listOf<Genre>()
        var actors = listOf<Actor>()
        coroutineScope {
            launch {
                movies = MoviesApiImpl.getMovies()
            }
            launch {
                genres = MoviesApiImpl.getGenres()
            }
            launch {
                actors = MoviesApiImpl.getActors()
            }
        }
        return parseMovies(
            movies,
            genres,
            actors
        )
    }

    private fun parseMovies(
        jsonMovies: List<JsonMovie>,
        jsonGenres: List<Genre>,
        jsonActors: List<Actor>
    ): List<Movie> {
        val actorsMap = jsonActors.associateBy { it.id }
        val genresMap = jsonGenres.associateBy { it.id }

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
