package com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.new_restaurants_reponse

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class RestaurantsResponseItem(
    @SerializedName("available")
    val available: Boolean?,
    @SerializedName("categories")
    val categories: List<String?>?,
    @SerializedName("discountOnMenu")
    val discountOnMenu: Int?,
    @SerializedName("distance")
    val distance: String?,
    @SerializedName("distanceValue")
    val distanceValue: Int?,
    @SerializedName("estimateDelivery")
    val estimateDelivery: String?,
    @SerializedName("hasDiscount")
    val hasDiscount: Boolean?,
    @SerializedName("hasFreeDelivery")
    val hasFreeDelivery: Boolean?,
    @SerializedName("hasOffers")
    val hasOffers: Boolean?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("isBusy")
    val isBusy: Boolean?,
    @SerializedName("isCommingSoon")
    val isCommingSoon: Boolean?,
    @SerializedName("isDelivery")
    val isDelivery: Boolean?,
    @SerializedName("isPickUp")
    val isPickUp: Boolean?,
    @SerializedName("locationName")
    val locationName: String?,
    @SerializedName("logo")
    val logo: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("offers")
    val offers: @RawValue List<Any?>?,
    @SerializedName("rating")
    val rating: Double?,
    @SerializedName("reviewsCount")
    val reviewsCount: Int?
) : Parcelable
