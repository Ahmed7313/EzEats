package com.easyEats.ezeats.domain.main.model.response.address.add


import com.google.gson.annotations.SerializedName

data class SaveAddressRequest(
    @SerializedName("orderId")
    var orderId: String? = "",
    @SerializedName("userAddressId")
    var userAddressId: String? = "",
)