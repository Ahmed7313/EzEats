package com.easyEats.ezeats.base

data class BaseResponse<T>(
//    val message: String?,
    val result: T?
)

//// Define the Result class with generics
//data class Result<T>(
//    val code: Int,
//    val data: T?,
//    val message: String,
//    val error: T?
//) {
//    // Check if the response is successful
//    fun isSuccess(): Boolean {
//        return code in 200..299 && data != null
//    }
//
//    // Check if there is an error
//    fun isError(): Boolean {
//        return error != null
//    }
//
//    // Check if the response indicates unauthenticated
//    fun isUnauthenticated(): Boolean {
//        return code == 401
//    }
//}
//
//// Define the BaseResponse class with a generic Result object
//data class BaseResponse<T>(
//    val result: Result<T>
//) {
//    // Process the result and handle success, error, or unauthenticated accordingly
//    fun handleResponse(
//        onSuccess: (T) -> Unit,
//        onError: (T?, String) -> Unit,
//        onUnauthenticated: () -> Unit
//    ) {
//        when {
//            result.isSuccess() -> result.data?.let { onSuccess(it) }
//            result.isUnauthenticated() -> onUnauthenticated()
//            else -> onError(result.error, result.message)
//        }
//    }
//}