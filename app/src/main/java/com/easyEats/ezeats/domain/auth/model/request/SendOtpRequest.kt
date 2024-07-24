package com.easyEats.ezeats.domain.auth.model.request

import com.google.gson.annotations.SerializedName

data class SendOtpRequest(
    @SerializedName("inputValue")
    val inputValue: String
)