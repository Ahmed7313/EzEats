package com.easyEats.ezeats.domain.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "product_choices",
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],           // <- important: link to unique cart row
            childColumns = ["productId"],     // <- actually points to product *row* ID now
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ProductChoiceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val productId: String,
    val subOptionId: String,
    val choiceId: String,
    val choiceName: String,
    val choicePrice: Double
)