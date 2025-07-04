package com.easyEats.ezeats.domain.room

import androidx.room.Entity
import androidx.room.PrimaryKey

//var tableOrderId = ""
//var currentOrderId = ""
//var userId = ""
//var shopId = ""
//var tableId = ""

@Entity(tableName = "shop")
data class ShopEntity(
    @PrimaryKey
    val tableOrderId: String,
    val currentOrderId: String,
    val userId: String,
    val shopId: String,
    val tableId: String,
    val shopName: String,
    val logo: String,
    val coverLogo : String,
    val reviewsCount: Int,
    val distance: String,
    val rating: Double,
    val redirectUrl: String = "",
)