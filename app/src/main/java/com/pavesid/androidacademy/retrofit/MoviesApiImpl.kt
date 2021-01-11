package com.pavesid.androidacademy.retrofit

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.pavesid.androidacademy.App
import com.pavesid.androidacademy.data.actors.CreditsResponse
import com.pavesid.androidacademy.data.details.DetailsResponse
import com.pavesid.androidacademy.data.genres.GenresResponse
import com.pavesid.androidacademy.data.movies.MovieResponse
import java.io.File
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@ExperimentalSerializationApi
object MoviesApiImpl {

    private val jsonFormat = Json { ignoreUnknownKeys = true }

    private val httpCacheDirectory = File(App.thisCacheDir, "responses")
    private const val cacheSize = 10L * 1024 * 1024
    private val cache = Cache(httpCacheDirectory, cacheSize)

    private val client = OkHttpClient().newBuilder()
        .addInterceptor(MoviesApiQueryInterceptor())
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .addInterceptor(CacheControlInterceptor())
        .cache(cache)
        .build()

    private val retrofit: Retrofit =
        Retrofit.Builder()
            .client(client)
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(jsonFormat.asConverterFactory("application/json".toMediaType()))
            .build()

    private val api: MoviesApi = retrofit.create(MoviesApi::class.java)

    suspend fun getMovies(
        page: Int = 1
    ): MovieResponse = api.getMovies(page)

    suspend fun getGenres(): GenresResponse = api.getGenres()

    suspend fun getDetails(movieId: Int): DetailsResponse = api.getDetails(movieId)

    suspend fun getActors(id: Int): CreditsResponse = api.getCredits(id)

    suspend fun getMoviesByGenre(
        id: Int,
        page: Int
    ): MovieResponse = api.getMoviesByGenre(genreId = id, page = page)
}
