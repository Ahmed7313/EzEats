package com.easyEats.ezeats.domain.main.model.response.payment


import com.google.gson.annotations.SerializedName

data class PaymentRequest(
    @SerializedName("TableOrderID")
    var TableOrderID: String? = "",
    @SerializedName("UserID")
    var UserID: String? = "",
    @SerializedName("paymentMethod")
    var paymentMethod: String? = "",
)