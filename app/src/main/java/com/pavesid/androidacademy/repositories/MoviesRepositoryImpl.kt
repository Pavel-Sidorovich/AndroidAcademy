package com.pavesid.androidacademy.repositories

import com.pavesid.androidacademy.data.actors.CreditsResponse
import com.pavesid.androidacademy.data.details.Details
import com.pavesid.androidacademy.data.details.DetailsResponse
import com.pavesid.androidacademy.data.genres.Genre
import com.pavesid.androidacademy.data.movies.JsonMovie
import com.pavesid.androidacademy.data.movies.Movie
import com.pavesid.androidacademy.db.MoviesDao
import com.pavesid.androidacademy.retrofit.MoviesApiImpl
import javax.inject.Inject
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class MoviesRepositoryImpl @Inject constructor(
    private val moviesDao: MoviesDao
) : MoviesRepository {

    private var genres = listOf<Genre>()

    override suspend fun getMovies(
        page: Int,
        language: String
    ): List<Movie> = getMoviesFromInternet(page, language)

    override suspend fun getDetails(id: Int): Details = getDetailsFromInternet(id)

    override suspend fun getActors(id: Int): CreditsResponse = MoviesApiImpl.getActors(id)

    private suspend fun getMoviesFromDB(): List<Movie> = moviesDao.getAllMovies()

    private suspend fun getMoviesFromInternet(
        page: Int,
        language: String
    ): List<Movie> {
        var movies = listOf<JsonMovie>()
        coroutineScope {
            launch {
                movies = MoviesApiImpl.getMovies(page, language).movies
            }
            if (genres.isEmpty()) {
                launch {
                    genres = MoviesApiImpl.getGenres().genres
                }
            }
        }
        return parseMovies(
            movies,
            genres
        )
    }

    private suspend fun getDetailsFromInternet(
        id: Int
    ): Details {
        lateinit var details: DetailsResponse
        lateinit var credits: CreditsResponse
        coroutineScope {
            launch {
                details = MoviesApiImpl.getDetails(id)
            }
            launch {
                credits = MoviesApiImpl.getActors(id)
            }
        }
        return Details(
            details,
            credits.cast,
            credits.crew
        )
    }

    override suspend fun updateMovie(movie: Movie) {
        moviesDao.updateMovie(movie)
    }

    override suspend fun insertMovie(movie: Movie) {
        moviesDao.insertMovie(movie)
    }

    override suspend fun insertMovies(movies: List<Movie>) {
        moviesDao.insertMovies(movies)
    }

    private fun parseMovies(
        jsonMovies: List<JsonMovie>,
        jsonGenres: List<Genre>
    ): List<Movie> {
        val genresMap = jsonGenres.associateBy { it.id }

        return jsonMovies.map { jsonMovie ->
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
                }
            )
        }
    }
}
