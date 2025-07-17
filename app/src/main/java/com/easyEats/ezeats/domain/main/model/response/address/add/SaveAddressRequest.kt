package com.easyEats.ezeats.domain.main.model.response.address.add


import com.google.gson.annotations.SerializedName

data class SaveAddressRequest(
    @SerializedName("compoundName")
    var compoundName: String? = "",
    @SerializedName("orderId")
    var orderId: String? = "",
    @SerializedName("userAddressId")
    var userAddressId: String? = "",
)