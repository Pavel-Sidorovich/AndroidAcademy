package com.pavesid.androidacademy.retrofit

import okhttp3.Interceptor
import okhttp3.Response

class MoviesApiQueryInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalHttpUrl = originalRequest.url

        val newHttpUrl = originalHttpUrl.newBuilder()
            .addQueryParameter(API_KEY_PARAMETER, API_KEY)
            .build()

        val request = originalRequest.newBuilder()
            .url(newHttpUrl)
            .build()

        return chain.proceed(request)
    }

    private companion object {
        private const val API_KEY_PARAMETER = "api_key"
        private const val API_KEY = "d65cb4c2a725df61b850f9653bc32c5b"
    }
}
