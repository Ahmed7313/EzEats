package com.easyEats.ezeats.domain.main.model.request.cart.response


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class RestaurantX(
    @SerializedName("objectId")
    var objectId: String? = ""
)