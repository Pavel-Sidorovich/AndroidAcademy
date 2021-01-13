package com.pavesid.androidacademy.retrofit

import com.pavesid.androidacademy.data.actors.CreditsResponse
import com.pavesid.androidacademy.data.details.DetailsResponse
import com.pavesid.androidacademy.data.genres.GenresResponse
import com.pavesid.androidacademy.data.movies.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApi {
    @GET("discover/movie")
    suspend fun getMovies(
        @Query("page") page: Int,
        @Query("sort_by") sort: String = "popularity.desc"
    ): MovieResponse

    @GET("movie/{movie_id}")
    suspend fun getDetails(
        @Path("movie_id") movieId: Int
    ): DetailsResponse

    @GET("genre/movie/list")
    suspend fun getGenres(): GenresResponse

    @GET("movie/{movie_id}/credits")
    suspend fun getCredits(
        @Path("movie_id") movieId: Int
    ): CreditsResponse

    @GET("discover/movie")
    suspend fun getMoviesByGenre(
        @Query("with_genres") genreId: Int,
        @Query("page") page: Int,
        @Query("sort_by") sort: String = "popularity.desc"
    ): MovieResponse

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("query") query: String,
        @Query("page") page: Int
    ): MovieResponse
}
