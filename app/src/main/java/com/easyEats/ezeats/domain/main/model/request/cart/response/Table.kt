package com.easyEats.ezeats.domain.main.model.request.cart.response


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class Table(
    @SerializedName("objectId")
    var objectId: String? = "",
    @SerializedName("restaurant")
    var restaurant: RestaurantX? = RestaurantX()
)