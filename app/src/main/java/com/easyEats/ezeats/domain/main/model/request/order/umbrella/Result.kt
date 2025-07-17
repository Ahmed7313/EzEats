package com.easyEats.ezeats.domain.main.model.request.order.umbrella


import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("deliveryFees")
    var deliveryFees: Double? = 0.0,
    @SerializedName("message")
    var message: String? = "",
    @SerializedName("orderId")
    var orderId: String? = "",
    @SerializedName("pickupStation")
    var pickupStation: String? = "",
    @SerializedName("success")
    var success: Boolean? = false
)