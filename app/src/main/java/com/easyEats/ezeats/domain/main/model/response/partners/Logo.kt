package com.easyEats.ezeats.domain.main.model.response.partners


import com.google.gson.annotations.SerializedName

data class Logo(
    @SerializedName("name")
    val name: String?,
    @SerializedName("__type")
    val type: String?,
    @SerializedName("url")
    val url: String?
)