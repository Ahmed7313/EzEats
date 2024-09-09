package com.easyEats.ezeats.domain.main.model.response.order_history


import com.google.gson.annotations.SerializedName

data class Order(
    @SerializedName("hoursAgo")
    val hoursAgo: String?,
    @SerializedName("isPaid")
    val isPaid: Boolean?,
    @SerializedName("items")
    val subOrders: List<SubOrder?>?,
    @SerializedName("orderDate")
    val orderDate: String?,
    @SerializedName("orderId")
    val orderId: String?,
    @SerializedName("orderNumber")
    val orderNumber: String?,
    @SerializedName("paymentMode")
    val paymentMode: String?,
    @SerializedName("restaurantName")
    val restaurantName: String?,
    @SerializedName("totalPrice")
    val totalPrice: Double?,
    val isPickup: Boolean?
)