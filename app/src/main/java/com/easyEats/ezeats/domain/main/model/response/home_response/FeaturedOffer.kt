package com.easyEats.ezeats.domain.main.model.response.home_response

data class FeaturedOffer(
    val available: Boolean?,
    val colorGradient: ColorGradient?,
    val description: String?,
    val distance: String?,
    val dateForm: DateForm?,
    val id: String?,
    val isFeature: Boolean?,
    val isFeaturedLogo: String?,
    val isHot: Any?,
    val isNew: Boolean?,
    val name: String?,
    val offerLocation: Any?,
    val offerLogo: String?,
    val points: Any?,
    val pointsType: String?,
    val restaurantLogo: String?,
    val restaurantName: String?,
    val timeZone: String?,
    val to: DateForm?
)