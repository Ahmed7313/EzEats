package com.easyEats.ezeats.domain.main.model.response.home_response


import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("description")
    val description: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("picture")
    val picture: Any?,
    @SerializedName("price")
    val price: Double?,
    @SerializedName("quantity")
    val quantity: Int?,
    @SerializedName("subItems")
    val subItems: List<Any?>?
)