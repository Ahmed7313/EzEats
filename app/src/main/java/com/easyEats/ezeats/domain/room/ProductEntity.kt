package com.easyEats.ezeats.domain.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    val productId: String,
    val name: String,
    val imageUrl: String?,
    val price: Double,
    val description: String?
)