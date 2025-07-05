package com.easyEats.ezeats.domain.main.model.response.address.list


import com.easyEats.ezeats.domain.main.model.response.address.Address
import com.google.gson.annotations.SerializedName

data class AddressListResult(
    @SerializedName("addresses")
    var addresses: List<Address>? = arrayListOf<Address>(),
    @SerializedName("success")
    var success: Boolean? = false
)