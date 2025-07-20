package com.easyEats.ezeats.domain.main.model.response.payment.savedCard.response


import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("cards")
    var cards: List<Card?>? = listOf(),
    @SerializedName("loading")
    var loading: Boolean? = false
)