package com.easyEats.ezeats.domain.main.model.response.order_history


import com.google.gson.annotations.SerializedName

data class OrderHistoryResponse(
    @SerializedName("currentPage")
    val currentPage: Int?,
    @SerializedName("orders")
    val orders: List<Order?>?,
    @SerializedName("totalOrders")
    val totalOrders: Int?,
    @SerializedName("totalPages")
    val totalPages: Int?
)