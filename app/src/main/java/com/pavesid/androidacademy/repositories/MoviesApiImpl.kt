package com.pavesid.androidacademy.repositories

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.pavesid.androidacademy.data.Actor
import com.pavesid.androidacademy.data.Genre
import com.pavesid.androidacademy.data.JsonMovie
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit

@ExperimentalSerializationApi
object MoviesApiImpl : MoviesApi {
    private val contentType = MediaType.parse("application/json; charset=utf-8")
    private val jsonFormat = Json { ignoreUnknownKeys = true }
    private val retrofit: Retrofit =
        Retrofit.Builder().baseUrl("https://gist.githubusercontent.com/Pavel-Sidorovich/")
            .addConverterFactory(jsonFormat.asConverterFactory(contentType!!))
            .build()

    private val api: MoviesApi = retrofit.create(MoviesApi::class.java)

    override suspend fun getMovies(): List<JsonMovie> = api.getMovies()

    override suspend fun getGenres(): List<Genre> = api.getGenres()

    override suspend fun getActors(): List<Actor> = api.getActors()
}
