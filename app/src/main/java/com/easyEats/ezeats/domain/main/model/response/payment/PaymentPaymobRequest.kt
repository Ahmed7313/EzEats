package com.easyEats.ezeats.domain.main.model.response.payment


import com.google.gson.annotations.SerializedName

data class PaymentPaymobRequest(
    @SerializedName("cardId")
    var cardId: String? = "",
    @SerializedName("email")
    var email: String? = "",
    @SerializedName("firstname")
    var firstname: String? = "",
    @SerializedName("lastname")
    var lastname: String? = "",
    @SerializedName("phone")
    var phone: String? = "",
    @SerializedName("TableOrderID")
    var tableOrderID: String? = "",
    @SerializedName("UserID")
    var userID: String? = ""
)