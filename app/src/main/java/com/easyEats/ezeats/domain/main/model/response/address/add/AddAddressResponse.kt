package com.easyEats.ezeats.domain.main.model.response.address.add


import com.google.gson.annotations.SerializedName

data class AddAddressResponse(
    @SerializedName("result")
    var addAddressResult: AddAddressResult? = AddAddressResult()
)