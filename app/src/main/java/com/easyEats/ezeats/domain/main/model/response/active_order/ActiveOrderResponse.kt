package com.easyEats.ezeats.domain.main.model.response.active_order


import com.easyEats.ezeats.domain.main.model.response.order_history.SubOrder
import com.google.gson.annotations.SerializedName

data class ActiveOrderResponse(
    @SerializedName("hoursAgo")
    val hoursAgo: String?,
    @SerializedName("isDelivery")
    val isDelivery: Boolean?,
    @SerializedName("isPickup")
    val isPickup: Boolean?,
    @SerializedName("logo")
    val logo: String?,
    @SerializedName("orderDate")
    val orderDate: String?,
    @SerializedName("orderId")
    val orderId: String?,
    @SerializedName("orderNumber")
    val orderNumber: String?,
    @SerializedName("paymentMode")
    val paymentMode: String?,
    @SerializedName("phoneNumber")
    val phoneNumber: String?,
    @SerializedName("restaurantId")
    val restaurantId: String?,
    @SerializedName("restaurantName")
    val restaurantName: String?,
    @SerializedName("state")
    val state: String?,
    @SerializedName("displayState")
    val displayState: String?,
    @SerializedName("totalPrice")
    val totalPrice: Double?,
    @SerializedName("items")
    val subOrders: List<SubOrder?>?,
)