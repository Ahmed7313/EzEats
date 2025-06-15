package com.easyEats.ezeats.domain.main.model.response.home_response


import com.google.gson.annotations.SerializedName

data class ActiveOrder(
    @SerializedName("estimatedTime")
    val estimatedTime: String?,
    @SerializedName("isPickup")
    val isPickup: Boolean?,
    @SerializedName("items")
    val items: List<Item?>?,
    @SerializedName("orderId")
    val orderId: String?,
    @SerializedName("orderNumber")
    val orderNumber: String?,
    @SerializedName("restaurantName")
    val restaurantName: String?,
    @SerializedName("state")
    val state: String?,
    @SerializedName("displayState")
    val displayState: String?,
    @SerializedName("totalPrice")
    val totalPrice: String?,
    @SerializedName("restaurantImage")
    val restaurantImage: String?
)