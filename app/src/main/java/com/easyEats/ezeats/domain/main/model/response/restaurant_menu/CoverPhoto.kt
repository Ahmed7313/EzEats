package com.easyEats.ezeats.domain.main.model.response.restaurant_menu


import com.google.gson.annotations.SerializedName

data class CoverPhoto(
    @SerializedName("name")
    val name: String?,
    @SerializedName("__type")
    val type: String?,
    @SerializedName("url")
    val url: String?
)