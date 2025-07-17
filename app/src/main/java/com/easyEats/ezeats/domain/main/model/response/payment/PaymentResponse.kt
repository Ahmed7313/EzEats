package com.easyEats.ezeats.domain.main.model.response.payment


import com.google.gson.annotations.SerializedName

data class PaymentResponse(
    @SerializedName("result")
    var result: String? = "",
)