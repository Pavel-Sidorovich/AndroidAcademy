package com.pavesid.androidacademy.repositories

import com.pavesid.androidacademy.data.details.Details
import com.pavesid.androidacademy.data.details.DetailsWithCredits
import com.pavesid.androidacademy.data.genres.Genre
import com.pavesid.androidacademy.data.movies.Movie

class MoviesRemoteRepositoryMock : MoviesRepository {

    private val movies: MutableList<Movie> = mutableListOf()

    private val genres: MutableList<Genre> = mutableListOf()

    override suspend fun getDetails(id: Long): DetailsWithCredits = DetailsWithCredits(
        Details(1, "", "", "", "", emptyList(), 0f, 0, false, 0),
        emptyList(),
        emptyList()
    )

    override suspend fun getMoviesByGenreFromAPI(id: Long, page: Int): List<Movie> = movies

    override suspend fun getMoviesByGenreFromDB(id: Long): List<Movie> = movies

    override suspend fun getMoviesFromAPI(page: Int): List<Movie> = movies

    override suspend fun getMoviesFromDB(): List<Movie> = movies

    override suspend fun searchMoviesFromAPI(query: String, page: Int): List<Movie> = movies

    override suspend fun getGenresFromAPI(): List<Genre> = genres

    override suspend fun getGenresFromDB(): List<Genre> = genres
    override suspend fun updateMovieLike(movie: Movie) {
        movies.find { movie.id == it.id }?.liked = movie.liked
    }
}
