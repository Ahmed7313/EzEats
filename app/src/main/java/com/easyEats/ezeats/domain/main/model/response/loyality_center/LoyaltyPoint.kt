package com.easyEats.ezeats.domain.main.model.response.loyality_center


import com.google.gson.annotations.SerializedName

data class LoyaltyPoint(
    @SerializedName("isDistrict5")
    val isDistrict5: Boolean?,
    @SerializedName("points")
    val points: Int?,
    @SerializedName("restaurantLogo")
    val restaurantLogo: String?,
    @SerializedName("restaurantName")
    val restaurantName: String?,
    @SerializedName("resturantId")
    val resturantId: String?
)