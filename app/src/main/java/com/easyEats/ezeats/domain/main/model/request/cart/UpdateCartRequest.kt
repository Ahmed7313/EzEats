package com.easyEats.ezeats.domain.main.model.request.cart


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class UpdateCartRequest(
    @SerializedName("items")
    var items: List<ItemCheckoutModel?>? = listOf(),
    @SerializedName("orderId")
    var orderId: String? = ""
)