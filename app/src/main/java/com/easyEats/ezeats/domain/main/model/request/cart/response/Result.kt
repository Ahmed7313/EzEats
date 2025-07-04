package com.easyEats.ezeats.domain.main.model.request.cart.response


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class Result(
    @SerializedName("deliveryFees")
    var deliveryFees: Int? = 0,
    @SerializedName("order")
    var order: Order? = Order(),
    @SerializedName("promoCodeDiscount")
    var promoCodeDiscount: Int? = 0,
    @SerializedName("promoCodeDiscountValue")
    var promoCodeDiscountValue: Int? = 0,
    @SerializedName("restaurant")
    var restaurant: Restaurant? = Restaurant(),
    @SerializedName("serviceFees")
    var serviceFees: Int? = 0,
    @SerializedName("serviceFeesPercentage")
    var serviceFeesPercentage: Int? = 0,
    @SerializedName("sharedAmount")
    var sharedAmount: Int? = 0,
    @SerializedName("subtotal")
    var subtotal: Int? = 0,
    @SerializedName("success")
    var success: Boolean? = false,
    @SerializedName("table")
    var table: Table? = Table(),
    @SerializedName("tax")
    var tax: Int? = 0,
    @SerializedName("tip")
    var tip: Int? = 0,
    @SerializedName("total")
    var total: Int? = 0
)