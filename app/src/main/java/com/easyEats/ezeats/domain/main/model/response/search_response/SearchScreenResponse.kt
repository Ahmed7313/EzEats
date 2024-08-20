package com.easyEats.ezeats.domain.main.model.response.search_response


data class SearchScreenResponse(
    val categories: List<Category?>?,
    val popularSearches: List<PopularSearch?>?,
    val recentSearches: List<String?>?
)