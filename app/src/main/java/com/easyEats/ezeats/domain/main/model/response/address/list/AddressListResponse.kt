package com.easyEats.ezeats.domain.main.model.response.address.list


import com.google.gson.annotations.SerializedName

data class AddressListResponse(
    @SerializedName("result")
    var addressListResult: AddressListResult? = AddressListResult()
)