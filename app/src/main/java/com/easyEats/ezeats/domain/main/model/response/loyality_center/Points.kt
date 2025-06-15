package com.easyEats.ezeats.domain.main.model.response.loyality_center


import com.google.gson.annotations.SerializedName

data class Points(
    @SerializedName("LoyaltyPoints")
    val loyaltyPoints: List<LoyaltyPoint?>?,
    @SerializedName("marakezPoints")
    val marakezPoints: List<Any?>?
)