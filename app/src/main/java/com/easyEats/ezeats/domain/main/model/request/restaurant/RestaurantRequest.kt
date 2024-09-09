package com.easyEats.ezeats.domain.main.model.request.restaurant

data class RestaurantRequest(
    val restaurantId: String,
    val userLat:String,
    val userLon:String
)