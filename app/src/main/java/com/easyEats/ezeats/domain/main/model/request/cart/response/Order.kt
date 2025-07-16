package com.easyEats.ezeats.domain.main.model.request.cart.response


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
@Parcelize
data class Order(
    @SerializedName("restaurant")
    var restaurant: Restaurant? = Restaurant(),
    @SerializedName("tableOrder")
    var tableOrder: TableOrder? = TableOrder(),
    @SerializedName("restaurantId")
    var restaurantId: String? = "",
    @SerializedName("userId")
    var userId: String? = "",
    @SerializedName("objectId")
    var objectId: String? = ""
) : Parcelable