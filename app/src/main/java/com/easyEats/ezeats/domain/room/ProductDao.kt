package com.easyEats.ezeats.domain.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductChoice(choice: ProductChoiceEntity)

    @Transaction
    @Query("SELECT * FROM products")
    fun getProductsWithChoices(): Flow<List<ProductWithChoices>>

    @Delete
    suspend fun deleteProduct(product: ProductEntity)
}