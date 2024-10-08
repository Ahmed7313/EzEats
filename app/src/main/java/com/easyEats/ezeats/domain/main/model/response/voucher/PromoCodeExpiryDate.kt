package com.easyEats.ezeats.domain.main.model.response.voucher


import com.google.gson.annotations.SerializedName

data class PromoCodeExpiryDate(
    @SerializedName("iso")
    val iso: String?,
    @SerializedName("__type")
    val type: String?
)