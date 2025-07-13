package com.easyEats.ezeats.domain.main.model.request.cart.tip


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("finalTotal")
    var finalTotal: Double? = 0.0,
    @SerializedName("price")
    var price: Double? = 0.0,
    @SerializedName("tipAmount")
    var tipAmount: Double? = 0.0
)