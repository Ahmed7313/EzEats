package com.easyEats.ezeats.domain.main.model.response.restaurant_reponse

import com.easyEats.ezeats.domain.main.model.response.home_response.HotDeal
import kotlinx.parcelize.RawValue

data class RestaurantInfo(
    val restaurantName: String?,
    val restaurantLogo: String?,
    val restaurantCover: String?,
    val distance: String?,
    val reviewsCount: Int?,
    val rating: Double?,
    val offers: List<HotDeal?>?,
)