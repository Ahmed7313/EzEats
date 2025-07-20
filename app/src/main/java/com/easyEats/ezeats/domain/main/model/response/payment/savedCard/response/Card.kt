package com.easyEats.ezeats.domain.main.model.response.payment.savedCard.response


import com.google.gson.annotations.SerializedName

data class Card(
    @SerializedName("add")
    var add: Boolean? = false,
    @SerializedName("cardType")
    var cardType: String? = "",
    @SerializedName("last4Digits")
    var last4Digits: String? = "",
    @SerializedName("objectID")
    var objectID: String? = ""
)