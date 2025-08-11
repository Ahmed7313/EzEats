package com.easyEats.ezeats.domain.main.model.response.payment.promo.ezeats


import com.google.gson.annotations.SerializedName

data class PromoCodeEzeatsResponse(
    @SerializedName("result")
    var result: Result? = Result()
)