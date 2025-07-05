package com.easyEats.ezeats.domain.main.model.request.cart.tip


import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("data")
    var `data`: Data? = Data(),
    @SerializedName("message")
    var message: String? = "",
    @SerializedName("success")
    var success: Boolean? = false
)