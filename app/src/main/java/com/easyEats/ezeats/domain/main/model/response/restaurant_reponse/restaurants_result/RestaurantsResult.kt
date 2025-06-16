package com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.restaurants_result

import com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.new_restaurants_reponse.RestaurantsResponseItem
import com.google.gson.annotations.SerializedName

/**
 * Response wrapper for the new getRestaurants API.
 */
data class RestaurantsResult(
    @SerializedName("restaurants")
    val restaurants: RestaurantsList?,
    @SerializedName("pagination")
    val pagination: Pagination?
)

/** Lists of restaurants grouped by order type */
data class RestaurantsList(
    @SerializedName("pickupRestaurants")
    val pickupRestaurants: List<RestaurantsResponseItem>?,
    @SerializedName("deliveryRestaurants")
    val deliveryRestaurants: List<RestaurantsResponseItem>?,
    @SerializedName("dineinRestaurants")
    val dineinRestaurants: List<RestaurantsResponseItem>?
)

/** Pagination information returned from the API */
data class Pagination(
    @SerializedName("currentPage")
    val currentPage: Int?,
    @SerializedName("totalPages")
    val totalPages: Int?,
    @SerializedName("totalRecords")
    val totalRecords: Int?,
    @SerializedName("recordsPerPage")
    val recordsPerPage: Int?
)
