package com.easyEats.ezeats.utils

import com.easyEats.ezeats.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response


/**
 * HeaderInterceptor is an OkHttp interceptor for adding Parse-specific headers to HTTP requests.
 * This interceptor adds the application ID and REST API key to each request.
 */
class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("X-Parse-Application-Id", BuildConfig.PARSE_APPLICATION_ID)
            .addHeader("X-Parse-REST-API-Key", BuildConfig.PARSE_REST_API_KEY)
            .build()
        return chain.proceed(request)
    }
}
