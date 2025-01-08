package com.easyEats.ezeats.domain.main.model.request

data class RestaurantRequest(
    val categoryId: String = "",
    val userLat: String = "",
    val userLon: String = ""
)
