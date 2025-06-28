package com.easyEats.ezeats.domain.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.easyEats.ezeats.domain.main.model.response.restaurant_menu.SubMenu
import com.easyEats.ezeats.ui.compose.resturant.ChoiceSelection
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Query("DELETE FROM shop")
    suspend fun clearShops()

    @Query("SELECT shopId FROM shop LIMIT 1")
    suspend fun getCurrentShopId(): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShop(shop: ShopEntity)

    @Query("SELECT EXISTS (SELECT 1 FROM products WHERE productId = :productId)")
    suspend fun isProductExist(productId: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity): Long

    @Query("UPDATE products SET qty = qty + :additionalQty WHERE productId = :productId")
    suspend fun updateProductQuantity(productId: String, additionalQty: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductChoice(choice: ProductChoiceEntity)


    @Transaction
    suspend fun addShop(
        tableOrderId: String,
        currentOrderId: String,
        userId: String,
        shopId: String,
        tableId: String
    ) {
        clearShops()
        insertShop(
            ShopEntity(
                tableOrderId, currentOrderId, userId, shopId, tableId
            )
        )
    }

    @Transaction
    suspend fun addProduct(currentSubMenu: SubMenu, count: Int, note: String): String {
        val productEntity = ProductEntity(
            name = currentSubMenu.name.toString(),
            description = currentSubMenu.description,
            productId = currentSubMenu.id.toString(),
            qty = count,
            note = note,
            price = currentSubMenu.price!!,
            imageUrl = currentSubMenu.coverPhoto?.url.toString()
        )
        insertProduct(productEntity)
        return productEntity.productId
    }

    @Transaction
    suspend fun addProductVariation(
        productId: String,
        selected: HashMap<String, List<ChoiceSelection>>,
    ): Result {
        selected.forEach {
            it.value.forEach { choice ->
                insertProductChoice(
                    ProductChoiceEntity(
                        productId = productId,
                        subOptionId = it.key,
                        choiceId = choice.id,
                        choicePrice = choice.price
                    )
                )
            }

        }
        return Result.SUCCESS
    }

    private suspend fun addProductWithOptionalChoice(
        product: ProductEntity,
        subOptionId: String?,
        choiceId: String?,
        choicePrice: Double?
    ): Boolean {
        val productExists = isProductExist(product.productId)
        if (!productExists) {
            insertProduct(product)
        } else {
            updateProductQuantity(product.productId, product.qty)
        }

        // Add choice if provided
        if (subOptionId != null && choiceId != null && choicePrice != null) {
            val choiceExists = isChoiceExist(product.productId, subOptionId, choiceId)
            if (!choiceExists) {
                insertProductChoice(
                    ProductChoiceEntity(
                        productId = product.productId,
                        subOptionId = subOptionId,
                        choiceId = choiceId,
                        choicePrice = choicePrice
                    )
                )
            }
        }
        return true
    }


    @Transaction
    @Query("SELECT * FROM products")
    fun getProductsWithChoices(): Flow<List<ProductWithChoices>>

    @Query("SELECT EXISTS (SELECT 1 FROM product_choices WHERE productId = :productId AND subOptionId = :subOptionId AND choiceId = :choiceId)")
    suspend fun isChoiceExist(productId: String, subOptionId: String, choiceId: String): Boolean

    @Query(
        """
        SELECT COALESCE(
            (SELECT (p.price * p.qty) + COALESCE(SUM(pc.choicePrice), 0) 
            FROM products p 
            LEFT JOIN product_choices pc ON p.productId = pc.productId 
            WHERE p.productId = :productId 
            GROUP BY p.productId), 
            0.0
        )
    """
    )
    suspend fun getProductTotalPriceOrZero(productId: String): Double

    @Delete
    suspend fun deleteProduct(product: ProductEntity)

    @Query("DELETE FROM products")
    suspend fun clearCart()

    @Query("DELETE FROM product_choices")
    suspend fun clearProductChoices()

    @Transaction
    suspend fun clearAllCartData() {
        clearProductChoices()
        clearCart()
    }

    enum class Result {
        SUCCESS,
        CART_CLEARED_AND_ADDED,
        ERROR
    }
}