package com.pavesid.androidacademy.repositories

import com.pavesid.androidacademy.data.actors.Cast
import com.pavesid.androidacademy.data.actors.Crew
import com.pavesid.androidacademy.data.details.Details
import com.pavesid.androidacademy.data.details.DetailsWithCredits
import com.pavesid.androidacademy.data.genres.Genre
import com.pavesid.androidacademy.data.movies.Movie
import com.pavesid.androidacademy.db.genries.GenreDao
import com.pavesid.androidacademy.db.genries.GenreEntity
import com.pavesid.androidacademy.db.likes.MovieLikeEntity
import com.pavesid.androidacademy.db.likes.MoviesLikeDao
import com.pavesid.androidacademy.db.movies.MovieEntity
import com.pavesid.androidacademy.db.movies.MoviesDao
import com.pavesid.androidacademy.retrofit.MoviesApi
import com.pavesid.androidacademy.retrofit.movies.JsonMovie
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

    override suspend fun getDetails(id: Long): DetailsWithCredits = getDetailsFromAPI(id)

    override suspend fun getMoviesByGenreFromAPI(id: Long, page: Int): List<Movie> {
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

    override suspend fun getMoviesByGenreFromDB(id: Long): List<Movie> {
        var movies = emptyList<JsonMovie>()
        var genres = emptyList<Genre>()
        var entities = emptyList<MovieLikeEntity>()
        coroutineScope {
            launch {
                movies = moviesDao.getMoviesByGenre("%$id%").map {
                    JsonMovie(
                        id = it.id,
                        title = it.title,
                        overview = it.overview,
                        posterPicture = it.posterPicture,
                        backdropPicture = it.backdropPicture,
                        ratings = it.ratings,
                        votesCount = it.votesCount,
                        adult = it.adult,
                        popularity = it.popularity,
                        genreIds = it.genreIds
                    )
                }
            }
            launch {
                genres = genreDao.getAllGenreEntities().map { Genre(it.id, it.name) }
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

    override suspend fun getMoviesFromAPI(page: Int): List<Movie> {
        var movies = emptyList<JsonMovie>()
        var genres = emptyList<Genre>()
        var entities = emptyList<MovieLikeEntity>()
        coroutineScope {
            launch {
                val list = moviesApi.getMovies(page).movies
                movies = list
                if (page == 1) {
                    launch {
                        moviesDao.deleteAllMovieEntities()
                        moviesDao.insertAllMovieEntities(
                            list.map {
                                MovieEntity(
                                    id = it.id,
                                    title = it.title,
                                    overview = it.overview,
                                    posterPicture = it.posterPicture,
                                    backdropPicture = it.backdropPicture,
                                    ratings = it.ratings,
                                    votesCount = it.votesCount,
                                    adult = it.adult,
                                    popularity = it.popularity,
                                    genreIds = it.genreIds
                                )
                            }
                        )
                    }
                }
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

    override suspend fun getMoviesFromDB(): List<Movie> {
        var movies = emptyList<JsonMovie>()
        var genres = emptyList<Genre>()
        var entities = emptyList<MovieLikeEntity>()
        coroutineScope {
            launch {
                movies = moviesDao.getAllMovieEntities().map {
                    JsonMovie(
                        id = it.id,
                        title = it.title,
                        overview = it.overview,
                        posterPicture = it.posterPicture,
                        backdropPicture = it.backdropPicture,
                        ratings = it.ratings,
                        votesCount = it.votesCount,
                        adult = it.adult,
                        popularity = it.popularity,
                        genreIds = it.genreIds
                    )
                }
            }
            launch {
                genres = genreDao.getAllGenreEntities().map { Genre(it.id, it.name) }
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

    override suspend fun searchMoviesFromAPI(query: String, page: Int): List<Movie> {
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
    ): DetailsWithCredits {
        lateinit var details: Details
        lateinit var cast: List<Cast>
        lateinit var crew: List<Crew>
        coroutineScope {
            launch {
                details = moviesApi.getDetails(id).let {
                    Details(
                        id = it.id,
                        title = it.title,
                        overview = it.overview,
                        poster = it.posterPicture,
                        backdrop = it.backdropPicture,
                        genres = it.genres,
                        ratings = it.ratings,
                        numberOfRatings = it.votesCount,
                        adult = it.adult,
                        runtime = it.runtime

                    )
                }
            }
            launch {
                val credits = moviesApi.getCredits(id)
                cast = credits.cast
                crew = credits.crew
            }
        }
        return DetailsWithCredits(
            details,
            cast,
            crew
        )
    }

    override suspend fun updateMovieLike(movie: Movie) {
        if (movie.liked) {
            moviesLikeDao.insertMovieLikeEntity(MovieLikeEntity(movie.id, movie.liked))
        } else {
            moviesLikeDao.deleteMovieLikeEntity(movie.id)
        }
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
