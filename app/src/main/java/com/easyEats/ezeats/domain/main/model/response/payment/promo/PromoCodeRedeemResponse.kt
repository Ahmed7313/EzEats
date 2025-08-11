package com.easyEats.ezeats.domain.main.model.response.payment.promo

import com.google.gson.annotations.SerializedName

data class PromoCodeRedeemResponse(
    @SerializedName("result")
    var result: PromoCodeRedeemResult? = null,
)

data class PromoCodeRedeemResult(
    @SerializedName("success")
    var success: Boolean? = false,
    @SerializedName("message")
    var message: String? = ""
)