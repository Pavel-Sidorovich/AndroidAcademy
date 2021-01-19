package com.pavesid.androidacademy.repositories

import com.pavesid.androidacademy.data.actors.CreditsResponse
import com.pavesid.androidacademy.data.details.Details
import com.pavesid.androidacademy.data.details.DetailsResponse
import com.pavesid.androidacademy.data.entities.MovieEntity
import com.pavesid.androidacademy.data.genres.Genre
import com.pavesid.androidacademy.data.movies.JsonMovie
import com.pavesid.androidacademy.data.movies.Movie
import com.pavesid.androidacademy.db.MoviesDao
import com.pavesid.androidacademy.retrofit.MoviesApi
import javax.inject.Inject
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class MoviesRepositoryImpl @Inject constructor(
    private val moviesDao: MoviesDao,
    private val moviesApi: MoviesApi
) : MoviesRepository {

    override suspend fun getDetails(id: Long): Details = getDetailsFromInternet(id)

    override suspend fun getActors(id: Long): CreditsResponse = moviesApi.getCredits(id)

    override suspend fun getMoviesByGenre(id: Long, page: Int): List<Movie> {
        var movies = emptyList<JsonMovie>()
        var genres = emptyList<Genre>()
        var entities = emptyList<MovieEntity>()
        coroutineScope {
            launch {
                movies = moviesApi.getMoviesByGenre(id, page).movies
            }
            launch {
                genres = moviesApi.getGenres().genres
            }
            launch {
                entities = moviesDao.getAllMoviesEntity()
            }
        }
        return parseMovies(
            movies,
            genres,
            entities
        )
    }

    override suspend fun getMovies(page: Int): List<Movie> {
        var movies = emptyList<JsonMovie>()
        var genres = emptyList<Genre>()
        var entities = emptyList<MovieEntity>()
        coroutineScope {
            launch {
                movies = moviesApi.getMovies(page).movies
            }
            launch {
                genres = moviesApi.getGenres().genres
            }
            launch {
                entities = moviesDao.getAllMoviesEntity()
            }
        }
        return parseMovies(
            movies,
            genres,
            entities
        )
    }

    override suspend fun searchMovies(query: String, page: Int): List<Movie> {
        var movies = emptyList<JsonMovie>()
        var genres = emptyList<Genre>()
        var entities = emptyList<MovieEntity>()
        coroutineScope {
            launch {
                movies = moviesApi.searchMovie(query, page).movies
            }
            launch {
                genres = moviesApi.getGenres().genres
            }
            launch {
                entities = moviesDao.getAllMoviesEntity()
            }
        }
        return parseMovies(
            movies,
            genres,
            entities
        )
    }

    override suspend fun getGenres(): List<Genre> = moviesApi.getGenres().genres

    private suspend fun getDetailsFromInternet(
        id: Long
    ): Details {
        lateinit var details: DetailsResponse
        lateinit var credits: CreditsResponse
        coroutineScope {
            launch {
                details = moviesApi.getDetails(id)
            }
            launch {
                credits = moviesApi.getCredits(id)
            }
        }
        return Details(
            details,
            credits.cast,
            credits.crew
        )
    }

    override suspend fun updateMovie(movieEntity: MovieEntity) = if (movieEntity.liked) {
        moviesDao.insertMovie(movieEntity)
    } else {
        moviesDao.deleteMovie(movieEntity.id)
    }

    private fun parseMovies(
        jsonMovies: List<JsonMovie>,
        jsonGenres: List<Genre>,
        moviesEntity: List<MovieEntity>
    ): List<Movie> {
        val genresMap = jsonGenres.associateBy { it.id }

        return jsonMovies.map { jsonMovie ->
            val entity = moviesEntity.find { it.id == jsonMovie.id }
            Movie(
                id = jsonMovie.id,
                title = jsonMovie.title,
                overview = jsonMovie.overview,
                poster = jsonMovie.posterPicture.orEmpty(),
                backdrop = jsonMovie.backdropPicture.orEmpty(),
                ratings = jsonMovie.ratings,
                numberOfRatings = jsonMovie.votesCount,
                minimumAge = if (jsonMovie.adult) 16 else 13,
                runtime = 0,
                genres = jsonMovie.genreIds.map {
                    genresMap[it] ?: throw IllegalArgumentException("Genre not found")
                },
                liked = entity?.liked ?: false
            )
        }
    }
}
