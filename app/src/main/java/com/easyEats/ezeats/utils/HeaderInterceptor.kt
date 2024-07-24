package com.easyEats.ezeats.utils

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("X-Parse-Application-Id", "7ei5pxppEe8ErCMB6Ehm6HTIJRcdJJd7SRHUpN8n")
            .addHeader("X-Parse-REST-API-Key", "rVEiA55uav4M5PbI3dYbWbJcIR67pf8JFIHRQNQn")
            .build()
        return chain.proceed(request)
    }
}
