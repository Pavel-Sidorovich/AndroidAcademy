package com.pavesid.androidacademy.repositories

import com.pavesid.androidacademy.data.Actor
import com.pavesid.androidacademy.data.Genre
import com.pavesid.androidacademy.data.JsonMovie
import retrofit2.http.GET

interface MoviesApi {
    @GET("a7415444bfb8e73b14820d5953256c70/raw/224fd1918610bd6555d5f566962748a36be93401/data.json")
    suspend fun getMovies(): List<JsonMovie>

    @GET("cabd1ace6db4d8fa2dbddafd1f7657c9/raw/5552b38be572213f1061842deab6e06e7e7bb2ab/genres.json")
    suspend fun getGenres(): List<Genre>

    @GET("26133aa4e31a24e4ee4b19e60774c827/raw/9031d722b5ef6459bd81ceb938924122c2e18114/people.json")
    suspend fun getActors(): List<Actor>
}
