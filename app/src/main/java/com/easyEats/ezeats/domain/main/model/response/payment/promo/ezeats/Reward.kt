package com.easyEats.ezeats.domain.main.model.response.payment.promo.ezeats


import com.google.gson.annotations.SerializedName

data class Reward(
    @SerializedName("isApplied")
    var isApplied: Boolean? = false,
    @SerializedName("isCampaignCode")
    var isCampaignCode: Boolean? = false,
    @SerializedName("isExpired")
    var isExpired: Boolean? = false,
    @SerializedName("isEzeatsCode")
    var isEzeatsCode: Boolean? = false,
    @SerializedName("isPercentage")
    var isPercentage: Boolean? = false,
    @SerializedName("isQuantityExceeded")
    var isQuantityExceeded: Boolean? = false,
    @SerializedName("isValidForRestaurant")
    var isValidForRestaurant: Boolean? = false,
    @SerializedName("orderCode")
    var orderCode: String? = "",
    @SerializedName("orderId")
    var orderId: String? = "",
    @SerializedName("promoCode")
    var promoCode: String? = "",
    @SerializedName("promoCodeDescription")
    var promoCodeDescription: String? = "",
    @SerializedName("promoCodeExpiryDate")
    var promoCodeExpiryDate: PromoCodeExpiryDate? = PromoCodeExpiryDate(),
    @SerializedName("promoCodeTitle")
    var promoCodeTitle: String? = "",
    @SerializedName("promoCodeValue")
    var promoCodeValue: Int? = 0,
    @SerializedName("promocodeId")
    var promocodeId: String? = "",
    @SerializedName("restaurantCover")
    var restaurantCover: String? = "",
    @SerializedName("restaurantName")
    var restaurantName: String? = ""
)