package com.easyEats.ezeats.domain.main.model.response.voucher


import com.google.gson.annotations.SerializedName

data class VoucherResponseItem(
    @SerializedName("isEzeatsCode")
    val isEzeatsCode: Boolean?,
    @SerializedName("orderCode")
    val orderCode: String?,
    @SerializedName("promoCode")
    val promoCode: String?,
    @SerializedName("promoCodeDescription")
    val promoCodeDescription: String?,
    @SerializedName("promoCodeExpiryDate")
    val promoCodeExpiryDate: PromoCodeExpiryDate?,
    @SerializedName("promoCodeTitle")
    val promoCodeTitle: String?,
    @SerializedName("promocodeId")
    val promocodeId: String?,
    @SerializedName("redeemed")
    val redeemed: Boolean?,
    @SerializedName("restaurantCover")
    val restaurantCover: String?,
    @SerializedName("restaurantName")
    val restaurantName: String?
)