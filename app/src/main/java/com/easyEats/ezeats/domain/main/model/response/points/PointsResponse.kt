package com.easyEats.ezeats.domain.main.model.response.points

import com.google.gson.annotations.SerializedName

data class PointsResponse(
    @SerializedName("EZPoints")
    val eZPoints: Int?,
    @SerializedName("LoyaltyPoints")
    val loyaltyPoints: List<RestaurantPoint>
)
