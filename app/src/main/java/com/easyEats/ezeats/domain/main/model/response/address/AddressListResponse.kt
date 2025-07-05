package com.easyEats.ezeats.domain.main.model.response.address


import com.google.gson.annotations.SerializedName

data class AddressListResponse(
    @SerializedName("result")
    var result: Result? = Result()
)