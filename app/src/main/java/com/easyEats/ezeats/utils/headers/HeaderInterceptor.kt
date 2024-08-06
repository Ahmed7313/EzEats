package com.easyEats.ezeats.utils.headers

import com.easyEats.ezeats.BuildConfig
import com.easyEats.ezeats.utils.Constants.APPLICATION_ID
import com.easyEats.ezeats.utils.Constants.PARSE_KEY
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject


/**
 * HeaderInterceptor is an OkHttp interceptor for adding Parse-specific headers to HTTP requests.
 * This interceptor adds the application ID and REST API key to each request.
 */
class HeaderInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader(APPLICATION_ID, BuildConfig.PARSE_APPLICATION_ID)
            .addHeader(PARSE_KEY, BuildConfig.PARSE_REST_API_KEY)
            .build()
        return chain.proceed(request)
    }
}
