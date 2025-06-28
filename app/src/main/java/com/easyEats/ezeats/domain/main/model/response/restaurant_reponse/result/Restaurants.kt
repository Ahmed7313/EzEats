package com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.result


import com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.new_restaurants_reponse.RestaurantsResponseItem
import com.google.gson.annotations.SerializedName

data class Restaurants(
    @SerializedName("deliveryRestaurants")
    var deliveryRestaurants: List<RestaurantsResponseItem>? = listOf(),
    @SerializedName("dineinRestaurants")
    var dineinRestaurants: List<RestaurantsResponseItem>? = listOf(),
    @SerializedName("pickupRestaurants")
    var pickupRestaurants: List<RestaurantsResponseItem>? = listOf()
)