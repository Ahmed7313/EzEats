package com.easyEats.ezeats.domain.main.model.request.restaurant

data class CheckTableAvailabilityRequest(
    val restaurantId: String,
    val tableNumber:String,
)