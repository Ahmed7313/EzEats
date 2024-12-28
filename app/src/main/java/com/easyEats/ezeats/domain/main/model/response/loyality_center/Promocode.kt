package com.easyEats.ezeats.domain.main.model.response.loyality_center


import com.easyEats.ezeats.domain.main.model.response.voucher.PromoCodeExpiryDate
import com.google.gson.annotations.SerializedName

data class Promocode(
    @SerializedName("code")
    val code: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("entityData")
    val entityData: EntityData?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("isRedeemed")
    val isRedeemed: Boolean?,
    @SerializedName("orderId")
    val orderId: String?,
    @SerializedName("restaurantId")
    val restaurantId: String?,
    @SerializedName("value")
    val value: String?,
    @SerializedName("promoCodeExpiryDate")
    val promoCodeExpiryDate: PromoCodeExpiryDate?,
)