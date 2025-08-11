package com.easyEats.ezeats.domain.main.model.response.payment.promo.redeem

import com.google.gson.annotations.SerializedName

data class CancelPromoCodeRedeemRequest(
    @SerializedName("userId")
    var userId: String? = "",
    @SerializedName("orderId")
    var orderId: String? = "",
)