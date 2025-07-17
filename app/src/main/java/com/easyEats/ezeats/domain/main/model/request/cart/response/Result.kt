package com.easyEats.ezeats.domain.main.model.request.cart.response


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
@Parcelize
data class Result(
    @SerializedName("deliveryFees")
    var deliveryFees: Double? = 0.0,
    @SerializedName("order")
    var order: Order? = Order(),
    @SerializedName("promoCodeDiscount")
    var promoCodeDiscount: Double? = 0.0,
    @SerializedName("promoCodeDiscountValue")
    var promoCodeDiscountValue: Double? = 0.0,
    @SerializedName("serviceFees")
    var serviceFees: Double? = 0.0,
    @SerializedName("serviceFeesPercentage")
    var serviceFeesPercentage: Double? = 0.0,
    @SerializedName("sharedAmount")
    var sharedAmount: Double? = 0.0,
    @SerializedName("subtotal")
    var subtotal: Double? = 0.0,
    @SerializedName("success")
    var success: Boolean? = false,
    @SerializedName("table")
    var table: Table? = Table(),
    @SerializedName("tax")
    var tax: Double? = 0.0,
    @SerializedName("tip")
    var tip: Double? = 0.0,
    @SerializedName("total")
    var total: Double? = 0.0,
    @SerializedName("compoundName")
    var compoundName: String? = "",

) :Parcelable