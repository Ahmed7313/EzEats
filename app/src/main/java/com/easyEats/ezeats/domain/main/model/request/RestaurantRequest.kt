package com.easyEats.ezeats.domain.main.model.request

/**
 * Request body for the new getRestaurants API.
 *
 * @param page optional page number for pagination
 * @param limit optional page size
 * @param userLat optional user latitude
 * @param cmpoundId optional compound id if available
 * @param userLon optional user longitude
 */
data class RestaurantRequest(
    val categoryId: String = "",
    val userLat: String = "",
    val userLon: String = "",
    val cmpoundId: String? = null,
)
