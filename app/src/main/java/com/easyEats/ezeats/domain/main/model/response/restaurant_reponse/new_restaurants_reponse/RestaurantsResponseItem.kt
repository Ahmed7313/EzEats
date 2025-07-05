package com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.new_restaurants_reponse


import com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.result.ResLocation
import com.google.gson.annotations.SerializedName

data class RestaurantsResponseItem(
    @SerializedName("available")
    var available: Boolean? = false,
    @SerializedName("categories")
    var categories: List<String>? = listOf(),
    @SerializedName("createdAt")
    var createdAt: String? = "",
    @SerializedName("discountOnMenu")
    var discountOnMenu: Int? = 0,
    @SerializedName("distance")
    var distance: String? = "",
    @SerializedName("estimateDelivery")
    var estimateDelivery: String? = "",
    @SerializedName("hasDiscount")
    var hasDiscount: Boolean? = false,
    @SerializedName("hasFreeDelivery")
    var hasFreeDelivery: Boolean? = false,
    @SerializedName("hasOffers")
    var hasOffers: Boolean? = false,
    @SerializedName("isBusy")
    var isBusy: Boolean? = false,
    @SerializedName("isClose")
    var isClose: String? = "",
    @SerializedName("isDelivery")
    var isDelivery: Boolean? = false,
    @SerializedName("isFoodtruck")
    var isFoodtruck: Boolean? = false,
    @SerializedName("isKashier")
    var isKashier: Boolean? = false,
    @SerializedName("isPaymob")
    var isPaymob: Boolean? = false,
    @SerializedName("isPickUp")
    var isPickUp: Boolean? = false,
    @SerializedName("kashierAPIkey")
    var kashierAPIkey: String? = "",
    @SerializedName("kashierMID")
    var kashierMID: String? = "",
    @SerializedName("kashierSecretKey")
    var kashierSecretKey: String? = "",
    @SerializedName("locationName")
    var locationName: String? = "",
    @SerializedName("logo")
    var logo: String? = "",
    @SerializedName("name")
    var name: String? = "",
    @SerializedName("objectId")
    var objectId: String? = "",
    @SerializedName("PApiKey")
    var pApiKey: String? = "",
    @SerializedName("PIntegrationID")
    var pIntegrationID: String? = "",
    @SerializedName("paymobApiKey")
    var paymobApiKey: String? = "",
    @SerializedName("paymentSystem")
    var paymentSystem: String? = "",
    @SerializedName("paymobIntegrationID")
    var paymobIntegrationID: String? = "",
    @SerializedName("rate")
    var rate: Double? = 0.0,
    @SerializedName("rating")
    var rating: Double? = 0.0,
    @SerializedName("redirectionTable")
    var redirectionTable: String? = "",
    @SerializedName("resLocation")
    var resLocation: ResLocation? = ResLocation(),
    @SerializedName("reviewsCount")
    var reviewsCount: Int? = 0,
    @SerializedName("updatedAt")
    var updatedAt: String? = ""
)