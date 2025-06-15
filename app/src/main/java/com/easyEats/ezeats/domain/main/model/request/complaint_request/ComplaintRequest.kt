package com.easyEats.ezeats.domain.main.model.request.complaint_request

data class ComplaintRequest(
    val name: String,
    val email: String,
    val message: String,
    val restaurantId:String,
    val orderId:String
)