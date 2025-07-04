package com.easyEats.ezeats.domain.main.model.request.cart


import com.google.gson.annotations.SerializedName

data class UpdateCartResponse(
    @SerializedName("result")
    var result: String? = ""
)