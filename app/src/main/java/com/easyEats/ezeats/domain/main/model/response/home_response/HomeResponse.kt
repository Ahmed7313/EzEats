package com.easyEats.ezeats.domain.main.model.response.home_response



data class HomeResponse(
    val featuredOffers: List<FeaturedOffer>?,
    val hotDeals: List<HotDeal>?
)