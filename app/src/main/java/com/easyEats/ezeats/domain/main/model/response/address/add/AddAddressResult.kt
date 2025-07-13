package com.easyEats.ezeats.domain.main.model.response.address.add


import com.google.gson.annotations.SerializedName

data class AddAddressResult(
    @SerializedName("address")
    var addressModel: AddressModel? = AddressModel(),
    @SerializedName("success")
    var success: Boolean? = false
)