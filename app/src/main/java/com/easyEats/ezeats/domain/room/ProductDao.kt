package com.easyEats.ezeats.domain.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.easyEats.ezeats.domain.main.model.response.restaurant_menu.SubMenu
import com.easyEats.ezeats.ui.compose.resturant.ChoiceSelection
import com.easyEats.ezeats.ui.main.restaurant.CartSummary
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Query("DELETE FROM shop")
    suspend fun clearShops()

    @Query("SELECT shopId FROM shop LIMIT 1")
    fun getCurrentShopId(): Flow<String?>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShop(shop: ShopEntity)

    @Query("SELECT EXISTS (SELECT 1 FROM products WHERE productId = :productId AND note = :note)")
    suspend fun isProductExist(productId: String, note: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity): Long


    @Query("UPDATE products SET qty = :additionalQty WHERE productId = :productId")
    suspend fun updateProductQuantity(productId: String, additionalQty: Int)

    @Query(
        "UPDATE products SET    qty = qty + 1 WHERE  productId = :productId"
    )
    suspend fun updateProductQuantityPlus(productId: String)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductChoice(choice: ProductChoiceEntity)


    @Transaction
    suspend fun addShop(
        tableOrderId: String,
        currentOrderId: String,
        userId: String,
        shopId: String,
        tableId: String,
        shopName: String,
        logo: String,
        coverLogo: String,
        reviewsCount: Int,
        distance: String,
        rating: Double
    ) {
        clearShops()
        insertShop(
            ShopEntity(
                tableOrderId,
                currentOrderId,
                userId,
                shopId,
                tableId,
                shopName,
                logo,
                coverLogo,
                reviewsCount,
                distance,
                rating
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
        val product = insertProduct(productEntity)
        return product.toString()
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
                        choicePrice = choice.price,
                        choiceName = choice.name
                    )
                )
            }

        }
        return Result.SUCCESS
    }


    @Transaction
//    @Query("SELECT products.* , product_choices.choiceName , product_choices.choicePrice , ((products.qty * products.price) + product_choices.choicePrice) as product_total  FROM products inner join product_choices on products.id = product_choices.productId group by product_choices.productId")
    @Query("SELECT * FROM products")
    fun getProductsChoices(): Flow<List<ProductWithChoices>>

    @Transaction
    @Query("SELECT * FROM product_choices where productId = :productId")
    fun getProductsChoices(productId: Long): Flow<List<ProductChoiceEntity>>

    @Transaction
    @Query("SELECT * FROM products")
    fun getProductsList(): Flow<List<ProductEntity>>

    @Transaction
    @Query("SELECT COALESCE(SUM(qty), 0) FROM products")
    fun getTotalQuantity(): Flow<Int>


    @Query("DELETE FROM products WHERE productId = :productId")
    suspend fun deleteProduct(productId: String)

    @Query("SELECT EXISTS (SELECT 1 FROM product_choices WHERE productId = :productId AND subOptionId = :subOptionId AND choiceId = :choiceId)")
    suspend fun isProductChoiceExist(
        productId: String,
        subOptionId: String,
        choiceId: String
    ): Boolean

    @Transaction
    suspend fun isProductChoicesInsertBefore(
        productId: String,
        selected: HashMap<String, List<ChoiceSelection>>,
    ): Boolean {
        selected.forEach {
            it.value.forEach { choice ->
                val result = isProductChoiceExist(
                    productId = productId,
                    subOptionId = it.key,
                    choiceId = choice.id
                )
                if (!result) return false
            }

        }
        return true
    }

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

    @Query(
        """
    SELECT (
        SELECT SUM(p.qty) 
        FROM products p
        WHERE EXISTS (
            SELECT 1 FROM shop s 
            WHERE s.shopId = :shopId
        )
    ) as count,
    (
        SELECT COALESCE(SUM(totalPrice), 0.0) FROM (
            SELECT (p.price * p.qty) + COALESCE(SUM(pc.choicePrice), 0.0) as totalPrice
            FROM products p
            LEFT JOIN product_choices pc ON p.productId = pc.productId
            WHERE EXISTS (
                SELECT 1 FROM shop s 
                WHERE s.shopId = :shopId
            )
            GROUP BY p.productId
        )
    ) as total
"""
    )
    suspend fun count_with_total_price(
        shopId: kotlin.String,
    ): CartSummary

    @Query(
        """
    SELECT p.*, s.shopName, 
    (
        SELECT (p2.price * p2.qty) + COALESCE(SUM(pc.choicePrice), 0.0)
        FROM products p2
        LEFT JOIN product_choices pc ON p2.productId = pc.productId
        WHERE p2.productId = p.productId
        GROUP BY p2.productId
    ) as totalPrice
    FROM products p
    INNER JOIN shop s
    WHERE EXISTS (
        SELECT 1 FROM shop s2
        WHERE s2.shopId = :shopId
    )
"""
    )
    fun getCartProducts(shopId: String): Flow<List<ProductWithChoices>>


    @Query("SELECT * FROM shop LIMIT 1")
    fun getShop(): Flow<ShopEntity?>


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
        SAME_PRODUCT_CHOICE,
        NOT_SAME_PRODUCT_CHOICE
    }
}