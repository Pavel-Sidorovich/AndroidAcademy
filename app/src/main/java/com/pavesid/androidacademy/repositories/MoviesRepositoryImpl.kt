package com.pavesid.androidacademy.repositories

import com.pavesid.androidacademy.data.actors.CreditsResponse
import com.pavesid.androidacademy.data.details.Details
import com.pavesid.androidacademy.data.details.DetailsResponse
import com.pavesid.androidacademy.data.entities.GenreEntity
import com.pavesid.androidacademy.data.entities.MovieLikeEntity
import com.pavesid.androidacademy.data.genres.Genre
import com.pavesid.androidacademy.data.movies.JsonMovie
import com.pavesid.androidacademy.data.movies.Movie
import com.pavesid.androidacademy.db.GenreDao
import com.pavesid.androidacademy.db.MoviesDao
import com.pavesid.androidacademy.db.MoviesLikeDao
import com.pavesid.androidacademy.retrofit.MoviesApi
import javax.inject.Inject
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class MoviesRepositoryImpl @Inject constructor(
    private val moviesLikeDao: MoviesLikeDao,
    private val moviesDao: MoviesDao,
    private val genreDao: GenreDao,
    private val moviesApi: MoviesApi
) : MoviesRepository {

    override suspend fun getDetails(id: Long): Details = getDetailsFromAPI(id)

    override suspend fun getActors(id: Long): CreditsResponse = moviesApi.getCredits(id)

    override suspend fun getMoviesByGenre(id: Long, page: Int): List<Movie> {
        var movies = emptyList<JsonMovie>()
        var genres = emptyList<Genre>()
        var entities = emptyList<MovieLikeEntity>()
        coroutineScope {
            launch {
                movies = moviesApi.getMoviesByGenre(id, page).movies
            }
            launch {
                genres = moviesApi.getGenres().genres
            }
            launch {
                entities = moviesLikeDao.getAllMovieLikeEntities()
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
        var entities = emptyList<MovieLikeEntity>()
        coroutineScope {
            launch {
                movies = moviesApi.getMovies(page).movies
            }
            launch {
                genres = moviesApi.getGenres().genres
            }
            launch {
                entities = moviesLikeDao.getAllMovieLikeEntities()
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
        var entities = emptyList<MovieLikeEntity>()
        coroutineScope {
            launch {
                movies = moviesApi.searchMovie(query, page).movies
            }
            launch {
                genres = moviesApi.getGenres().genres
            }
            launch {
                entities = moviesLikeDao.getAllMovieLikeEntities()
            }
        }
        return parseMovies(
            movies,
            genres,
            entities
        )
    }

    override suspend fun getGenresFromAPI(): List<Genre> {
        val genres = moviesApi.getGenres().genres
        if (genres.isNotEmpty()) {
            genreDao.apply {
                deleteAllGenreEntities()
                insertAllGenreEntities(
                    genres.map { genre ->
                        GenreEntity(genre.id, genre.name)
                    }
                )
            }
        }
        return genres
    }

    override suspend fun getGenresFromDB(): List<Genre> =
        genreDao.getAllGenreEntities().map { genreEntity ->
            Genre(genreEntity.id, genreEntity.name)
        }

    private suspend fun getDetailsFromAPI(
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

    override suspend fun updateMovie(movieLikeEntity: MovieLikeEntity) =
        if (movieLikeEntity.liked) {
            moviesLikeDao.insertMovieLikeEntity(movieLikeEntity)
        } else {
            moviesLikeDao.deleteMovieLikeEntity(movieLikeEntity.id)
        }

    private fun parseMovies(
        jsonMovies: List<JsonMovie>,
        jsonGenres: List<Genre>,
        moviesLikeEntity: List<MovieLikeEntity>
    ): List<Movie> {
        val genresMap = jsonGenres.associateBy { it.id }

        return jsonMovies.map { jsonMovie ->
            val entity = moviesLikeEntity.find { it.id == jsonMovie.id }
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
