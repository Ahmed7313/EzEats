package com.easyEats.ezeats.utils

// Resource.kt
sealed class Resource<T> {

    // Represents a successful response
    data class Success<T>(val data: T?) : Resource<T>()

    // Represents an error response
    data class Error<T>(val message: String, val data: T? = null) : Resource<T>()

    // Represents a loading state
    class Loading<T> : Resource<T>()
}
