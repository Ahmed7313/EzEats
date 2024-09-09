package com.easyEats.ezeats.domain.main.model.request.order_history

data class OrderHistoryRequest(
    val page: Int,
    val itemsPerPage: Int=10,
    val userLat: String = "",
    val userLon: String = ""
)