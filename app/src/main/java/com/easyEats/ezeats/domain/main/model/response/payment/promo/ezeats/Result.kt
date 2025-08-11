package com.easyEats.ezeats.domain.main.model.response.payment.promo.ezeats


import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("rewards")
    var rewards: List<Reward?>? = listOf(),
    @SerializedName("success")
    var success: Boolean? = false
)