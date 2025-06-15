package com.easyEats.ezeats.utils

import com.easyEats.ezeats.base.Error

sealed class Resource<T> {
    // Represents a successful response
    data class Success<T>(val data: T?) : Resource<T>()

    // Represents an error response
    data class Error<T>(val message: String, val data: com.easyEats.ezeats.base.Error? = null) :
        Resource<T>()

    // Represents a loading state
    class Loading<T> : Resource<T>()

    // Represents an unauthenticated state
    class UnAuthenticated<T> : Resource<T>()
}
