package com.easyEats.ezeats.domain.main.model.request.cart.tip


import com.google.gson.annotations.SerializedName

data class TipResponse(
    @SerializedName("result")
    var result: Result? = Result()
)