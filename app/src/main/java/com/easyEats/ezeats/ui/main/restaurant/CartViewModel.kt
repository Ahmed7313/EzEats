package com.easyEats.ezeats.ui.main.restaurant

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.easyEats.ezeats.MyApplication
import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.domain.main.model.request.cart.ItemCheckoutModel
import com.easyEats.ezeats.domain.main.model.request.cart.SuboptionsId
import com.easyEats.ezeats.domain.main.model.request.cart.TipRequest
import com.easyEats.ezeats.domain.main.model.request.cart.UpdateCartRequest
import com.easyEats.ezeats.domain.main.model.request.cart.response.UpdateCartResponse
import com.easyEats.ezeats.domain.main.model.request.cart.tip.TipResponse
import com.easyEats.ezeats.domain.main.model.request.order.SavePickupRequest
import com.easyEats.ezeats.domain.main.model.request.restaurant.CheckTableAvailabilityRequest
import com.easyEats.ezeats.domain.main.model.response.payment.PaymentRequest
import com.easyEats.ezeats.domain.main.model.response.restaurant_menu.SubMenu
import com.easyEats.ezeats.domain.main.model.response.restaurant_menu.check_table_availability.TableAvailabilityResponse
import com.easyEats.ezeats.domain.main.use_case.MainUseCase
import com.easyEats.ezeats.domain.room.ProductDao
import com.easyEats.ezeats.domain.room.ProductWithChoices
import com.easyEats.ezeats.domain.room.ShopEntity
import com.easyEats.ezeats.ui.compose.resturant.ChoiceSelection
import com.easyEats.ezeats.ui.main.main.MainViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
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
    val TAG = "CartViewModel"

    private val _shopDataState = MutableStateFlow<ShopEntity?>(null)
    val shopDataState: StateFlow<ShopEntity?> = _shopDataState.asStateFlow()

    private val _cartSummary = MutableStateFlow(CartSummary(0, 0.0))
    val cartSummary = _cartSummary.asStateFlow()


    private val _cartProducts = MutableStateFlow<List<ProductWithChoices>>(emptyList())
    val cartProducts = _cartProducts.asStateFlow()

    private val _updateCartResponse = MutableStateFlow<UpdateCartResponse>(UpdateCartResponse())
    val updateCartResponse = _updateCartResponse.asStateFlow()

    var _showLoading = MutableStateFlow<Boolean>(false)
    val showLoading = _showLoading.asStateFlow()

//    var _showLoadingBlur = MutableStateFlow<Boolean>(false)
//    val showLoadingBlur = _showLoadingBlur.asStateFlow()

    private val _enableTakeAction = MutableStateFlow<Boolean>(false)
    val enableTakeAction = _enableTakeAction.asStateFlow()


    private val _addToCartResult = MutableStateFlow<ProductDao.Result?>(null)
    val addToCartResult = _addToCartResult.asStateFlow()

    var tableOrderId = ""
    var currentOrderId = ""
    var userId = ""
    var shopId = ""
    var tableId = ""
    var shopName = ""
    var logo = ""
    var coverLogo = ""
    var reviewsCount = 0
    var distance = ""
    var rating = 0.0

    fun initShop(
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
        this.tableOrderId = tableOrderId
        this.currentOrderId = currentOrderId
        this.userId = userId
        this.shopId = shopId
        this.tableId = tableId
        this.shopName = shopName
        this.logo = logo
        this.coverLogo = coverLogo
        this.reviewsCount = reviewsCount
        this.distance = distance
        this.rating = rating
        observeCartChanges()
    }

    init {
        // Observe changes in the cart products and calculate the total price.
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun checkTableAvailability(): Flow<TableAvailabilityResponse?> {
        return productDao.getShop().flatMapLatest { shop ->
            flow {
                if (shop != null) {
                    try {
                        val result = mainUseCase.restaurantUseCase.checkTableAvailability(
                            CheckTableAvailabilityRequest(shop.shopId, shop.tableId)
                        )
                        emit(result)
                        _enableTakeAction.emit(true)
                    } catch (e: Exception) {
                        Log.d(MyApplication.TAG, "checkTableAvailability: ${e.message}")
                        _enableTakeAction.emit(false)
                        emit(null)
                    }
                } else {
                    emit(null)
                    _enableTakeAction.emit(false)
                }
            }
        }
    }

    fun getProductsCount() = flow<Int> {
        Log.d("getProductsCount", "getProductsCount: staRT")
        productDao.getTotalQuantity().collect {
            emit(it)
        }
    }


    private fun observeCartChanges() {
        viewModelScope.launch {
            productDao.getProductsChoices()
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
//        _totalCartPrice.value = total
        viewModelScope.launch {
            calculateSummary()
        }
    }

    fun updateProductQty(productId: String, qty: Int) {
        viewModelScope.launch {
            if (qty > 0)
                productDao.updateProductQuantity(productId, qty)
            else {
                productDao.deleteProduct(productId)
            }
            observeCartChanges()
        }
    }

    suspend fun addToCart(
        currentSubMenu: SubMenu,
        selected: HashMap<String, List<ChoiceSelection>>,
        count: Int,
        note: String
    ) {
        productDao.addShop(
            tableOrderId,
            currentOrderId,
            userId,
            shopId,
            tableId,
            shopName,
            logo,
            coverLogo,
            reviewsCount, distance, rating
        )
        val exist = productDao.isProductExist(currentSubMenu.id.toString(), note)
        Log.d(TAG, "addToCart: exist")
        if (exist) {
            Log.d(TAG, "addToCart: exist 2")
            val isProductChoicesInsertBefore =
                productDao.isProductChoicesInsertBefore(currentSubMenu.id.toString(), selected)
            Log.d(TAG, "addToCart: $isProductChoicesInsertBefore")
            if (isProductChoicesInsertBefore) {
                Log.d(TAG, "addToCart: before")
                productDao.updateProductQuantityPlus(currentSubMenu.id.toString())
            } else {
                Log.d(TAG, "addToCart: new")
                val productId = productDao.addProduct(currentSubMenu, count, note)
                Log.d(TAG, "addToCart: $productId")
                productDao.addProductVariation(productId, selected)
            }
        } else {
            Log.d(TAG, "addToCart: not exist")
            val productId = productDao.addProduct(currentSubMenu, count, note)
            Log.d(TAG, "addToCart: $productId")
            productDao.addProductVariation(productId, selected)
        }
        calculateSummary()
    }

    private suspend fun calculateSummary() {
        if (shopId.isNotEmpty())
            _cartSummary.value = productDao.count_with_total_price(shopId)
    }

    fun checkValidToAdd(): Flow<Boolean> {
        return productDao.getCurrentShopId().map { currentShopId ->
            currentShopId == null || currentShopId == shopId
        }
    }

    fun getCartData(shopId: String) {
        viewModelScope.launch {
            productDao.getCartProducts(shopId).collect { products ->
                _cartProducts.value = products
            }

            _cartSummary.value = productDao.count_with_total_price(shopId)
        }
    }


    fun getShop() {
        viewModelScope.launch {
            productDao.getShop().collect { products ->
                _shopDataState.value = products
            }
        }
    }

    fun clearShop() {
        viewModelScope.launch {
            productDao.clearShops()
            productDao.clearCart()
            productDao.clearProductChoices()
        }
    }

    /*
            Log.d(TAG, "checkout: here")
        val request = UpdateCartRequest()
        val items = mutableListOf<ItemCheckoutModel>()
        Log.d(TAG, "checkout: startg")
        val shop = productDao.getShop().firstOrNull()
        request.orderId = shop?.tableId
        Log.d(TAG, "checkout: ${request.orderId}")
        val products = productDao.getProductsList()
        products.collect { productsList ->
            Log.d(TAG, "checkout: ${productsList.size}")
            for (product in productsList) {
                Log.d(TAG, "checkout: productId: ${product.productId}")
                    val choices = productDao.getProductsChoices(product.id).first()
                Log.d(TAG, "checkout_choices: ${choices.size}")
                for (choice in choices){
                    Log.d(TAG, "checkout: subOptions size : ${choice.subOptionId}")

                }
            }
        }
     */

    suspend fun checkout() {
        val request = UpdateCartRequest()
        val items = mutableListOf<ItemCheckoutModel>()

        val shop = productDao.getShop().firstOrNull()
        request.orderId = shop?.currentOrderId

        val products = productDao.getProductsList().first()

        for (product in products) {
            val subOptions = productDao.getProductsChoices(product.id).first()
            val suboptionMap = subOptions.groupBy { it.subOptionId }

            val subOptionsList = suboptionMap.map { (suboptionId, choices) ->
                SuboptionsId(
                    suboptionId = suboptionId,
                    choicesIds = choices.map { it.choiceId }
                )
            }

            val itemModel = ItemCheckoutModel(
                itemId = product.productId,
                notes = product.note,
                quantity = product.qty,
                suboptionsIds = subOptionsList
            )

            items.add(itemModel)
        }

        request.items = items

        try {
            _showLoading.value = true
            mainUseCase.homeUseCase.updateCart(request).let {
                _showLoading.value = false
                Log.d(TAG, "checkout: result")
                _enableTakeAction.value = true
                _updateCartResponse.value = it
            }


        } catch (e: Exception) {
            e.printStackTrace()
            _showLoading.value = false
            _enableTakeAction.value = false
            Log.d(TAG, "checkout: ${e.message}")
        }
    }

    suspend fun updateCart(): Flow<UpdateCartResponse?> {
        _showLoading.value = true
        val request = UpdateCartRequest()
        val items = mutableListOf<ItemCheckoutModel>()

        val shop = productDao.getShop().firstOrNull()
        request.orderId = shop?.currentOrderId
        val products = productDao.getProductsList().first()

        for (product in products) {
            val subOptions = productDao.getProductsChoices(product.id).first()
            val suboptionMap = subOptions.groupBy { it.subOptionId }

            val subOptionsList = suboptionMap.map { (suboptionId, choices) ->
                SuboptionsId(
                    suboptionId = suboptionId,
                    choicesIds = choices.map { it.choiceId }
                )
            }

            val itemModel = ItemCheckoutModel(
                itemId = product.productId,
                notes = product.note,
                quantity = product.qty,
                suboptionsIds = subOptionsList
            )

            items.add(itemModel)
        }

        request.items = items

        return flow {
            try {
                val result = mainUseCase.homeUseCase.updateCart(request)
                _showLoading.value = false
                emit(result)
            } catch (e: Exception) {
                null
            }
        }

    }

    /*

    @OptIn(ExperimentalCoroutinesApi::class)
    fun checkTableAvailability(): Flow<TableAvailabilityResponse?> {
        return productDao.getShop().flatMapLatest { shop ->
            flow {
                if (shop != null) {
                    try {
                        val result = mainUseCase.restaurantUseCase.checkTableAvailability(
                            CheckTableAvailabilityRequest(shop.shopId, shop.tableId)
                        )
                        emit(result)
                    } catch (e: Exception) {
                        Log.d(MyApplication.TAG, "checkTableAvailability: ${e.message}")
                        emit(null)
                    }
                } else {
                    emit(null)
                }
            }
        }
    }
     */

    fun savePickUpStation(request: SavePickupRequest) : Flow<*> {
        _showLoading.value = true
        return flow {
            try{
                val result = mainUseCase.homeUseCase.savePickupStation(request)
                _showLoading.value = false
                emit(result)
            }catch (e: Exception){
                _showLoading.value = false
                emit(null)
            }
        }
    }

    suspend fun saveTip(
        amount: String?,
    ): Flow<TipResponse?> {
        _showLoading.value = true
        val shop = productDao.getShop().firstOrNull()
        val orderId = shop?.currentOrderId
        Log.d(TAG, "saveTip: $orderId")
        Log.d(TAG, "saveTip: $amount")
        return flow {
            if (amount != null && orderId != null && amount.isNotEmpty() && orderId.isNotEmpty()) {
                try {
                    Log.d(TAG, "saveTip: start")
                    _showLoading.value = false
                    val tipResponse =
                        mainUseCase.homeUseCase.saveTip(TipRequest(amount.toInt(), orderId))
                    Log.d(TAG, "saveTip: emit")

                    emit(tipResponse)
                } catch (Ex: Exception) {
                    _showLoading.value = false
                    emit(null)
                }
            } else {
                _showLoading.value = false
                emit(null)
            }
        }
    }

    fun payment(request: PaymentRequest) : Flow<BaseResponse<Any>?>  {
        _showLoading.value = true
        return flow {
            try{
                val result = mainUseCase.homeUseCase.payment(request)
                _showLoading.value = false
                emit(result)
            }catch (Ex: Exception){
                _showLoading.value = false
                emit(null)
            }
        }
    }


}
