package com.pavesid.androidacademy.repositories.remote

import com.pavesid.androidacademy.data.JsonActor
import com.pavesid.androidacademy.data.JsonGenre
import com.pavesid.androidacademy.data.JsonMovie
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MoviesApiImpl : MoviesApi {
    private val retrofit: Retrofit =
        Retrofit.Builder().baseUrl("https://gist.githubusercontent.com/Pavel-Sidorovich/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private val api: MoviesApi = retrofit.create(MoviesApi::class.java)

    override suspend fun getMovies(): List<JsonMovie> = api.getMovies()

    override suspend fun getGenres(): List<JsonGenre> = api.getGenres()

    override suspend fun getActors(): List<JsonActor> = api.getActors()
}
