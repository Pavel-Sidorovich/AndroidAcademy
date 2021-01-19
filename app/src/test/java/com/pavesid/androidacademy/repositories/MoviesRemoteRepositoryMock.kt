package com.pavesid.androidacademy.repositories

import com.pavesid.androidacademy.data.actors.CreditsResponse
import com.pavesid.androidacademy.data.details.Details
import com.pavesid.androidacademy.data.details.DetailsResponse
import com.pavesid.androidacademy.data.entities.MovieEntity
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

    override suspend fun getActors(id: Long): CreditsResponse = CreditsResponse(emptyList(), emptyList(), 1)

    override suspend fun getMoviesByGenre(id: Long, page: Int): List<Movie> = movies

    override suspend fun searchMovies(query: String, page: Int): List<Movie> = movies

    override suspend fun getGenres(): List<Genre> = genres

    override suspend fun updateMovie(movieEntity: MovieEntity) {
        movies.find { movie -> movie.id == movieEntity.id }?.liked = movieEntity.liked
    }
}
