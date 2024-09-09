package com.easyEats.ezeats.domain.main.model.response.points

import com.google.gson.annotations.SerializedName

data class RestaurantPoint(
    @SerializedName("resturantId")
    val restaurantId:String?,
    val points:Int,
    val restaurantLogo:String,
    val restaurantName:String?,
    val restaurantDescription:String?
)
