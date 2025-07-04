package com.easyEats.ezeats.domain.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0, // unique cart item ID
    val productId: String,
    val name: String,
    var qty: Int,
    val imageUrl: String?,
    val price: Double,
    val description: String?,
    val note: String? = null,
)