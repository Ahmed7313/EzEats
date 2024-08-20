package com.easyEats.ezeats.domain.main.model.request.search

data class SearchRequest(
    val searchString: String,
    val viewType: String = "restaurant",
    val userLat: String = "",
    val userLon: String = ""
)