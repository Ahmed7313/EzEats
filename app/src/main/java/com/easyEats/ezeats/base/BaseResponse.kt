package com.easyEats.ezeats.base

// Define the Result class with generics
data class Result<T>(
    val code: Int,
    val data: T?,
    val message: String?,
    val error: Error?
) {
    // Check if the response is successful
    fun isSuccess(): Boolean {
        return code in 200..299
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
    // Process the result and handle success, error, or unauthenticated accordingly
    suspend fun handleResponse(
        onSuccess: suspend (T?) -> Unit,
        onError: suspend (String, Error?) -> Unit,
        onUnauthenticated: suspend () -> Unit = {}
    ) {
        when {
            result.isSuccess() -> {
                if (result.data!=null){
                    result.data.let { onSuccess(it) }
                }else{
                    onSuccess(null)
                }

            }
            result.isUnauthenticated() -> onUnauthenticated()
            else -> onError(result.message.toString(), result.error)
        }
    }
}
