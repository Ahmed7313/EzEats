package com.easyEats.ezeats.domain.main.model.request.cart


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class TipRequest(
    @SerializedName("tipAmount")
    var items: Int? = 0,
    @SerializedName("orderId")
    var orderId: String? = ""
)