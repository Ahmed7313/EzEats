package com.easyEats.ezeats.domain.main.model.response.order_history


import com.google.gson.annotations.SerializedName

data class SubOrder(
    @SerializedName("description")
    val description: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("picture")
    val picture: String?,
    @SerializedName("price")
    val price: Double?,
    @SerializedName("quantity")
    val quantity: Int?,
    @SerializedName("subItems")
    val extras:List<Choices?>?

)