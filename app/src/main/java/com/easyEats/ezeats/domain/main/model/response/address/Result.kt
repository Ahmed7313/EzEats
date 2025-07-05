package com.easyEats.ezeats.domain.main.model.response.address


import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("address")
    var address: Address? = Address(),
    @SerializedName("success")
    var success: Boolean? = false
)