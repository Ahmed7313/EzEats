package com.easyEats.ezeats.base

import android.util.Log

// Define the Result class with generics
data class Result<T>(
    val code: Int,
    val success: Boolean,
    val data: T?,
    val message: String?,
    val error: Error?
) {
    // Check if the response is successful
    fun isSuccess(): Boolean {
        return code in 200..299 || success
    }

    // Check if there is an error
    fun isError(): Boolean {
        return error != null || code >= 300
    }

    // Check if the response indicates unauthenticated
    fun isUnauthenticated(): Boolean {
        return code == 401
    }
}

data class Error(
    val code: Int,
    val message: String?
)

// Define the BaseResponse class with a generic Result object
data class BaseResponse<T>(
    val result: Result<T>
) {

    val TAG = "BaseResponse"
    // Process the result and handle success, error, or unauthenticated accordingly
    suspend fun handleResponse(
        onSuccess: suspend (T?) -> Unit,
        onError: suspend (String, Error?) -> Unit,
        onUnauthenticated: suspend () -> Unit = {}
    ) {
        try {
            when {
                result.isSuccess() -> {
                    try {
                        onSuccess(result.data)
                    } catch (e: Exception) {
                        Log.d(TAG, "handleResponse: ${e.message}")
                        onError("Failed to process data: ${e.message}", Error(-1, e.message))
                    }
                }
                result.isUnauthenticated() -> onUnauthenticated()
                else -> onError(
                    result.message ?: "Unknown error",
                    result.error ?: Error(-1, "Unknown error")
                )
            }
        } catch (e: Exception) {
            Log.d(TAG, "handleResponse: ${e.message}")
            onError("Unexpected error: ${e.message}", Error(-1, e.message))
        }
    }
}
