package com.pavesid.androidacademy.retrofit

import com.pavesid.androidacademy.utils.Utils
import okhttp3.Interceptor
import okhttp3.Response

class CacheControlInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())
        return if (Utils.isNetworkConnected) {
            val maxAge = 120 // read from cache for 2 minutes
            originalResponse.newBuilder()
                .header("Cache-Control", "public, max-age=$maxAge")
                .build()
        } else {
            val maxStale = 60 * 60 * 24 * 28 // tolerate 4-weeks stale
            originalResponse.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                .build()
        }
    }
}
