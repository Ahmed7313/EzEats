package com.easyEats.ezeats.domain.main.model.response.address.list


import com.easyEats.ezeats.domain.main.model.response.address.add.AddressModel
import com.google.gson.annotations.SerializedName

data class AddressListResult(
    @SerializedName("addresses")
    var addresses: List<AddressModel?>? = listOf(),
    @SerializedName("success")
    var success: Boolean? = false
)