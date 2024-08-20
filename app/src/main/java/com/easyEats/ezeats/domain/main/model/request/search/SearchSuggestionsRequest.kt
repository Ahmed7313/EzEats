package com.easyEats.ezeats.domain.main.model.request.search

data class SearchSuggestionsRequest(
    val searchString: String,
    val viewType: String = "restaurant"
)
