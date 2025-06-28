package com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.result


import com.google.gson.annotations.SerializedName

data class RestaurantListResult(
    @SerializedName("pagination")
    var pagination: Pagination? = Pagination(),
    @SerializedName("restaurants")
    var restaurants: Restaurants? = Restaurants()
)