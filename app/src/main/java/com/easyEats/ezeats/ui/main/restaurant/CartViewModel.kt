package com.easyEats.ezeats.ui.main.restaurant

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.easyEats.ezeats.MyApplication.Companion.TAG
import com.easyEats.ezeats.domain.main.model.response.restaurant_menu.SubMenu
import com.easyEats.ezeats.domain.main.use_case.MainUseCase
import com.easyEats.ezeats.domain.room.ProductDao
import com.easyEats.ezeats.domain.room.ProductWithChoices
import com.easyEats.ezeats.ui.compose.resturant.ChoiceSelection
import com.easyEats.ezeats.ui.main.main.MainViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing the restaurant data, including fetching special offers for the restaurant.
 * It interacts with the use case layer to fetch the special offers and exposes them as LiveData.
 */
@HiltViewModel
class CartViewModel @Inject constructor(
    private val mainUseCase: MainUseCase,
    private val productDao: ProductDao,
    savedStateHandle: SavedStateHandle
) : MainViewModel(mainUseCase) {


    private val _cartProducts = MutableStateFlow<List<ProductWithChoices>>(emptyList())
    val cartProducts = _cartProducts.asStateFlow()

    private val _totalCartPrice = MutableStateFlow(0.0)
    val totalCartPrice = _totalCartPrice.asStateFlow()

    private val _addToCartResult = MutableStateFlow<ProductDao.Result?>(null)
    val addToCartResult = _addToCartResult.asStateFlow()

    var tableOrderId = ""
    var currentOrderId = ""
    var userId = ""
    var shopId = ""
    var tableId = ""

    fun initShop(tableOrderId: String, currentOrderId: String, userId: String, shopId: String, tableId: String) {
        this.tableOrderId = tableOrderId
        this.currentOrderId = currentOrderId
        this.userId = userId
        this.shopId = shopId
        this.tableId = tableId
    }

    init {
        // Observe changes in the cart products and calculate the total price.
        observeCartChanges()
    }

    private fun observeCartChanges() {
        viewModelScope.launch {
            productDao.getProductsWithChoices()
                .collect { products ->
                    _cartProducts.value = products
                    calculateTotalPrice(products)
                }
        }
    }

    private suspend fun calculateTotalPrice(products: List<ProductWithChoices>) {
        var total = 0.0
        products.forEach { productWithChoices ->
            total += productDao.getProductTotalPriceOrZero(productWithChoices.product.productId)
        }
        _totalCartPrice.value = total
    }



    fun addToCart(
        currentSubMenu: SubMenu,
        selected: HashMap<String, List<ChoiceSelection>>,
        count: Int,
        note: String
    ) {
        viewModelScope.launch {
            productDao.addShop(tableOrderId, currentOrderId, userId, shopId, tableId)
            val productId = productDao.addProduct(currentSubMenu,count,note)
            productDao.addProductVariation(productId,selected)
        }
        viewModelScope.launch {
            try {
//                val result = productDao.validateAndAddProduct(
//                    shopId = shopId,
//                    tableId = tableId,
//                    userId = userId,
//                    restaurantId = restaurantId,
//                    product = product,
//                    subOptionId = subOptionId,
//                    choiceId = choiceId,
//                    choicePrice = choicePrice
//                )
//                _addToCartResult.value = result
            } catch (e: Exception) {
                Log.e(TAG, "addToCart error: ${e.message}")
                _addToCartResult.value = ProductDao.Result.ERROR
            }
        }
    }



}
