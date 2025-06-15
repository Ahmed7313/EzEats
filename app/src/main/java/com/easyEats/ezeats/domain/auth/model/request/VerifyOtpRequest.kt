package com.easyEats.ezeats.domain.auth.model.request

import com.google.gson.annotations.SerializedName

data class VerifyOtpRequest(
    @SerializedName("inputValue")
    val phoneNumber: String,
    @SerializedName("inputOTP")
    val otpCode: String
)