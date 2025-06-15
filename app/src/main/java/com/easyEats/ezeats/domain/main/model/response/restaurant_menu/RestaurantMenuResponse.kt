package com.easyEats.ezeats.domain.main.model.response.restaurant_menu


import com.google.gson.annotations.SerializedName

data class RestaurantMenuResponse(
    @SerializedName("menu")
    val menu: List<Menu>?,
    @SerializedName("phoneNumber")
    val phoneNumber: String?
)