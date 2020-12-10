package com.pavesid.androidacademy.repositories.remote

import com.pavesid.androidacademy.data.Actor
import com.pavesid.androidacademy.data.Genre
import com.pavesid.androidacademy.data.JsonActor
import com.pavesid.androidacademy.data.JsonGenre
import com.pavesid.androidacademy.data.JsonMovie
import com.pavesid.androidacademy.data.Movie
import com.pavesid.androidacademy.repositories.MoviesRepository
import javax.inject.Inject

class MoviesRemoteRepository @Inject constructor() : MoviesRepository {

    override suspend fun getMovies(): List<Movie> = parseMovies(
        MoviesApiImpl.getMovies(),
        MoviesApiImpl.getGenres(),
        MoviesApiImpl.getActors()
    )

    private fun parseMovies(
        jsonMovies: List<JsonMovie>,
        jsonGenres: List<JsonGenre>,
        jsonActors: List<JsonActor>
    ): List<Movie> {
        val actorsMap =
            jsonActors.map { Actor(id = it.id, name = it.name, picture = it.profilePicture) }
                .associateBy { it.id }
        val genresMap = jsonGenres.map { Genre(id = it.id, name = it.name) }.associateBy { it.id }

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
