package com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.result


import com.google.gson.annotations.SerializedName

data class RestaurantListResultResponse(
    @SerializedName("result")
    var restaurantListResult: RestaurantListResult? = RestaurantListResult()
)