package com.easyEats.ezeats.domain.main.model.response.payment.promo.redeem

import com.google.gson.annotations.SerializedName

data class PromoCodeRedeemRequest(
    @SerializedName("userId")
    var userId: String? = "",
    @SerializedName("promocode")
    var promocode: String? = "",
    @SerializedName("orderId")
    var orderId: String? = "",
)