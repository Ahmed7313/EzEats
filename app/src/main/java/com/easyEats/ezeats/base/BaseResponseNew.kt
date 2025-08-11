package com.easyEats.ezeats.base

import android.util.Log
import com.easyEats.ezeats.domain.main.model.response.payment.promo.PromoCodeRedeemResult
import com.google.gson.annotations.SerializedName

data class BaseResponseNew(
    @SerializedName("result")
    var result: Any? = null,
)
