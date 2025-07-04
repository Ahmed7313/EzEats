package com.easyEats.ezeats.domain.main.model.request.cart.response


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class Restaurant(
    @SerializedName("country")
    var country: String? = "",
    @SerializedName("kashierAPIkey")
    var kashierAPIkey: String? = "",
    @SerializedName("kashierMID")
    var kashierMID: String? = "",
    @SerializedName("kashierSecretKey")
    var kashierSecretKey: String? = "",
    @SerializedName("objectId")
    var objectId: String? = "",
    @SerializedName("paymentSystem")
    var paymentSystem: String? = ""
)