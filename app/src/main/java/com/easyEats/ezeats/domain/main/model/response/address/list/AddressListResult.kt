package com.easyEats.ezeats.domain.main.model.response.address.list


import com.easyEats.ezeats.domain.main.model.response.address.add.AddressModel
import com.google.gson.annotations.SerializedName

data class AddressListResult(
    @SerializedName("addresses")
    var addresses: List<AddressModel?>? = listOf(),
    @SerializedName("user")
    var user: AddressUser? = AddressUser(),
    @SerializedName("success")
    var success: Boolean? = false
)

data class AddressUser(@SerializedName("objectId") val objectId: String = "")