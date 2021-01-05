package com.pavesid.androidacademy.retrofit

import com.pavesid.androidacademy.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class MoviesApiQueryInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalHttpUrl = originalRequest.url

        val newHttpUrl = originalHttpUrl.newBuilder()
            .addQueryParameter(API_KEY_PARAMETER, BuildConfig.API_KEY_MOVIE_DB)
            .build()

        val request = originalRequest.newBuilder()
            .url(newHttpUrl)
            .build()

        return chain.proceed(request)
    }

    private companion object {
        private const val API_KEY_PARAMETER = "api_key"
    }
}
