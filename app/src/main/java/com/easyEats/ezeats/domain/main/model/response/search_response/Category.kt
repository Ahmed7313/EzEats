package com.easyEats.ezeats.domain.main.model.response.search_response


import com.google.gson.annotations.SerializedName

data class Category(
    val categoryId: String?,
    val categoryLogo: String?,
    val categoryName: String?
)