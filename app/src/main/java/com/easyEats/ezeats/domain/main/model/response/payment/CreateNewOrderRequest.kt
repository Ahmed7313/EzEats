package com.easyEats.ezeats.domain.main.model.response.payment


import com.google.gson.annotations.SerializedName

data class CreateNewOrderRequest(
    @SerializedName("tableOrderId")
    var tableOrderID: String? = "",
)