package com.pavesid.androidacademy.retrofit

import com.pavesid.androidacademy.BuildConfig.API_KEY
import java.util.Locale
import okhttp3.Interceptor
import okhttp3.Response

class MoviesApiQueryInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalHttpUrl = originalRequest.url

        val newHttpUrl = originalHttpUrl.newBuilder()
            .addQueryParameter(API_KEY_PARAMETER, API_KEY)
            .addQueryParameter(LANGUAGE_PARAMETER, Locale.getDefault().toLanguageTag())
            .build()

        val request = originalRequest.newBuilder()
            .url(newHttpUrl)
            .build()

        return chain.proceed(request)
    }

    private companion object {
        private const val API_KEY_PARAMETER = "api_key"

        private const val LANGUAGE_PARAMETER = "language"
    }
}
