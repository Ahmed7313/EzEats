package com.easyEats.ezeats.domain.main.model.response.payment.promo.ezeats


import com.google.gson.annotations.SerializedName

data class PromoCodeExpiryDate(
    @SerializedName("iso")
    var iso: String? = "",
    @SerializedName("__type")
    var type: String? = ""
)