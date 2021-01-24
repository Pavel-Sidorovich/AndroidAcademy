package com.pavesid.androidacademy.repositories

import com.pavesid.androidacademy.data.actors.CreditsResponse
import com.pavesid.androidacademy.data.details.Details
import com.pavesid.androidacademy.data.details.DetailsResponse
import com.pavesid.androidacademy.data.entities.MovieLikeEntity
import com.pavesid.androidacademy.data.genres.Genre
import com.pavesid.androidacademy.data.movies.Movie

class MoviesRemoteRepositoryMock : MoviesRepository {

    private val movies: MutableList<Movie> = mutableListOf()

    private val genres: MutableList<Genre> = mutableListOf()

    override suspend fun getDetails(id: Long): Details = Details(
        DetailsResponse(1, "", "", "", "", emptyList(), 0f, 0, false, 0),
        emptyList(),
        emptyList()
    )

    override suspend fun getActorsFromAPI(id: Long): CreditsResponse = CreditsResponse(emptyList(), emptyList(), 1)

    override suspend fun getMoviesByGenreFromAPI(id: Long, page: Int): List<Movie> = movies

    override suspend fun getMoviesByGenreFromDB(id: Long): List<Movie> = movies

    override suspend fun getMoviesFromAPI(page: Int): List<Movie> = movies

    override suspend fun getMoviesFromDB(): List<Movie> = movies

    override suspend fun searchMoviesFromAPI(query: String, page: Int): List<Movie> = movies

    override suspend fun getGenresFromAPI(): List<Genre> = genres

    override suspend fun getGenresFromDB(): List<Genre> = genres

    override suspend fun updateMovieLike(movieLikeEntity: MovieLikeEntity) {
        movies.find { movie -> movie.id == movieLikeEntity.id }?.liked = movieLikeEntity.liked
    }
}
