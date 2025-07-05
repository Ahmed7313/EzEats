package com.easyEats.ezeats.domain.room

import androidx.room.Embedded
import androidx.room.Relation
import com.easyEats.ezeats.domain.main.model.response.restaurant_menu.Choice
import com.easyEats.ezeats.domain.main.model.response.restaurant_menu.SubMenu

data class ProductWithChoices(
    @Embedded val product: ProductEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "productId",
        entity = ProductChoiceEntity::class
    )
    val choices: List<ProductChoiceEntity>
)

fun SubMenu.toProductEntity() = ProductEntity(
    productId = id ?: throw IllegalArgumentException("Product ID cannot be null"),
    name = name ?: "",
    imageUrl = coverPhoto?.url,
    price = price ?: 0.0,
    qty = 1,
    description = description
)
