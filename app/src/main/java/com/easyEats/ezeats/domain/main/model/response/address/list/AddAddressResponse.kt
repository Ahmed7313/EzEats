package com.easyEats.ezeats.domain.main.model.response.address.list

import com.easyEats.ezeats.domain.main.model.response.address.Result
import com.google.gson.annotations.SerializedName

data class AddAddressResponse(
    @SerializedName("result")
    var result: AddressListResult? = AddressListResult()
)