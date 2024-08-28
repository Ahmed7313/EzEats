package com.easyEats.ezeats.domain.main.model.response.restaurant_reponse


data class Restaurant(
    val distance: Any?,
    val id: String?,
    val locationName: String?,
    val logo: String?,
    val name: String?,
    val offers: List<Offer?>?,
    val type: String?,
    val category: List<String>? = listOf(),
    val rating: Double? = 0.0,
    val reviewsCount: Int? = 0

)