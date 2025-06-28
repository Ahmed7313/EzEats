package com.easyEats.ezeats.ui.main.restaurant

import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.easyEats.ezeats.MyApplication.Companion.TAG
import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.databinding.FragmentRestaurantBinding
import com.easyEats.ezeats.domain.main.model.request.restaurant.CheckTableAvailabilityRequest
import com.easyEats.ezeats.domain.main.model.request.restaurant.RestaurantMenuRequest
import com.easyEats.ezeats.domain.main.model.request.restaurant.RestaurantRequest
import com.easyEats.ezeats.domain.main.model.response.home_response.HotDeal
import com.easyEats.ezeats.domain.main.model.response.restaurant_menu.RestaurantMenuResponse
import com.easyEats.ezeats.domain.main.model.response.restaurant_menu.SubMenu
import com.easyEats.ezeats.domain.main.model.response.restaurant_menu.check_table_availability.TableAvailabilityResponse
import com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.RestaurantInfo
import com.easyEats.ezeats.domain.main.use_case.MainUseCase
import com.easyEats.ezeats.domain.room.ProductDao
import com.easyEats.ezeats.domain.room.ProductWithChoices
import com.easyEats.ezeats.ui.compose.resturant.ChoiceSelection
import com.easyEats.ezeats.ui.main.main.MainViewModel
import com.easyEats.ezeats.ui.main.restaurant.adapter.SpecialOfferAdapter
import com.easyEats.ezeats.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing the restaurant data, including fetching special offers for the restaurant.
 * It interacts with the use case layer to fetch the special offers and exposes them as LiveData.
 */
@HiltViewModel
class RestaurantViewModel @Inject constructor(
    private val mainUseCase: MainUseCase,
    private val productDao: ProductDao,
    savedStateHandle: SavedStateHandle
) : MainViewModel(mainUseCase) {

    // The restaurant ID, obtained from the SavedStateHandle.
    val restaurantId: String? = savedStateHandle["restaurantId"]


    private val _isBottomSheetVisible = MutableStateFlow(false)
    val isBottomSheetVisible = _isBottomSheetVisible.asStateFlow()

    private val _selectedSubMenu = MutableStateFlow<SubMenu?>(null)
    val selectedSubMenu = _selectedSubMenu.asStateFlow()

    var availableRestaurantId = ""
    var availableUserId = ""
    var availableTableId = ""


    fun showBottomSheet(subMenu: SubMenu) {
        Log.d(TAG, "showBottomSheet: ")
        viewModelScope.launch {
            _selectedSubMenu.value = subMenu
            Log.d(TAG, "showBottomSheet: ${isBottomSheetVisible.value}")
            _isBottomSheetVisible.value = true
            Log.d(TAG, "showBottomSheet: showing bottom sheet for ${subMenu.name}")
        }
    }
    fun dismissBottomSheet() {
        viewModelScope.launch {
            _isBottomSheetVisible.value = false
            _selectedSubMenu.value = null
            Log.d(TAG, "dismissBottomSheet: hiding bottom sheet")
        }
    }
    fun cleanupBottomSheet() {
        viewModelScope.launch {
            dismissBottomSheet()
        }
    }
    // LiveData that holds the resource of special offers.
    private val _specialOffers = MutableLiveData<Resource<BaseResponse<RestaurantInfo>>>()
    val specialOffers: LiveData<Resource<BaseResponse<RestaurantInfo>>> = _specialOffers

    private val _restaurantMenu = MutableLiveData<Resource<BaseResponse<RestaurantMenuResponse>>>()
    val restaurantMenu: LiveData<Resource<BaseResponse<RestaurantMenuResponse>>> = _restaurantMenu

    private val _cartProducts = MutableStateFlow<List<ProductWithChoices>>(emptyList())
    val cartProducts = _cartProducts.asStateFlow()

    private val _totalCartPrice = MutableStateFlow(0.0)
    val totalCartPrice = _totalCartPrice.asStateFlow()


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



    /**
     * Fetches special offers for the restaurant by retrieving the current user's location.
     * If the location is available, it creates a request and fetches the special offers for the restaurant.
     */
    fun getSpecialOffers(restaurantId: String?) {
        viewModelScope.launch {
            fetchCurrentAddress().collect {
                if (it?.lat?.isNullOrEmpty()?.not() == true) {
                    // Create a request to get special offers for the restaurant.
                    val flow = createSpecialOffersRequest(
                        //osama
                        RestaurantRequest(
                            restaurantId!!,
                            userLat = "30.0145125310423", //it.lat ?: "" ,
                            userLon = "30.975965400415998" //it.lng ?: ""
                        )
                    )
                    // Apply the request with retry logic.
                    apply(
                        flow = flow,
                        maxRetries = 2,
                        onError = { error ->
                            // Update the LiveData with an error state if there's an issue.
                            _specialOffers.value = Resource.Error("No Internet Connection")
                        },
                        onSuccess = { resource ->
                            // Update the LiveData with the fetched special offers.
                            _specialOffers.value = resource
                        }
                    )
                }
            }
        }
    }

    fun getRestaurantMenu(restaurantId: String?) {
        viewModelScope.launch {
            // Create a request to get special offers for the restaurant.
            val flow = createRestaurantMenuRequest(
                RestaurantMenuRequest(
                    restaurantId!!
                )
            )
            // Apply the request with retry logic.
            apply(
                flow = flow,
                maxRetries = 2,
                onError = { error ->
                    // Update the LiveData with an error state if there's an issue.
                    _restaurantMenu.value = Resource.Error("No Internet Connection")
                },
                onSuccess = { resource ->
                    // Update the LiveData with the fetched special offers.
                    _restaurantMenu.value = resource
                }
            )

        }
    }

    fun checkTableAvailability(restaurantId: String, tableNumber: String) : Flow<TableAvailabilityResponse?>  = flow{

        availableRestaurantId = ""
        availableUserId = ""
        availableTableId = ""
        try {
            val result = mainUseCase.restaurantUseCase.checkTableAvailability(CheckTableAvailabilityRequest(restaurantId,tableNumber))
            emit(result)
        }catch (e: Exception){
            emit(null)
            Log.d(TAG, "checkTableAvailability: ")
        }
    }

    /**
     * Creates a Flow that makes the request for special offers by restaurant ID.
     * Sends the appropriate response (loading, success, error, or unauthenticated).
     *
     * @param restaurantRequest The request object containing the restaurant ID and user's location.
     */
    private fun createSpecialOffersRequest(restaurantRequest: RestaurantRequest): Flow<Resource<BaseResponse<RestaurantInfo>>> =
        channelFlow {
            send(Resource.Loading()) // Emit loading state.
            val result = mainUseCase.restaurantUseCase.getOffersByRestaurantId(restaurantRequest)
            // Handle the response and send the result back to the Flow.
            result.handleResponse(
                onSuccess = {
                    send(Resource.Success(result)) // Emit success state with the result.
                },
                onError = { message, error ->
                    send(
                        Resource.Error(
                            message,
                            error
                        )
                    ) // Emit error state with the message and error.
                },
                onUnauthenticated = {
                    send(Resource.UnAuthenticated()) // Emit unauthenticated state if the user is not authenticated.
                }
            )
        }

    private fun createRestaurantMenuRequest(restaurantMenuRequest: RestaurantMenuRequest): Flow<Resource<BaseResponse<RestaurantMenuResponse>>> =
        channelFlow {
            try {
                send(Resource.Loading())
                val result = mainUseCase.restaurantUseCase.getRestaurantMenu(restaurantMenuRequest)

                result.handleResponse(
                    onSuccess = { data ->
                        if (data != null) {
                            send(Resource.Success(result))
                        } else {
                            send(Resource.Error("Invalid data format"))
                        }
                    },
                    onError = { message, error ->
                        Log.e(TAG, "Error: $message, Details: $error")
                        send(Resource.Error(message))
                    },
                    onUnauthenticated = {
                        send(Resource.UnAuthenticated())
                    }
                )
            } catch (e: Exception) {
                Log.e(TAG, "Decoding error: ${e.message}", e)
                send(Resource.Error("Failed to process response: ${e.message}"))
            }
        }

    /**
     * Sets the restaurant data into the UI, including the special offers count, special offers list, and visibility
     * of various UI elements depending on whether the restaurant has offers or not.
     *
     * @param binding The binding for the fragment's layout.
     * @param specialOfferAdapter The adapter used to display the special offers in the RecyclerView.
     * @param offersCount The count of available offers.
     * @param offers The list of special offers to display.
     */
    fun setRestaurantData(
        binding: FragmentRestaurantBinding,
        specialOfferAdapter: SpecialOfferAdapter,
        offersCount: Int,
        offers: List<HotDeal?>?
    ) {
        // Set the offers count text.
        binding.tvOffersCount.text = offersCount.toString() + " Offers"

        // Set the adapter for the RecyclerView.
//        binding.rvSpecialOffers.adapter = specialOfferAdapter

        // Submit the list of special offers to the adapter.
        specialOfferAdapter.submitList(offers)

        // Show or hide views based on whether there are offers.
        if (offers.isNullOrEmpty()) {
            binding.tvOffersCount.visibility = GONE
//            binding.rvSpecialOffers.visibility = GONE
//            binding.llNoOffers.visibility = VISIBLE
            binding.tvSpecialOffers.visibility = GONE
            binding.tvDescription.visibility = GONE
        } else {
//            binding.rvSpecialOffers.visibility = VISIBLE
            binding.llNoOffers.visibility = GONE
            binding.tvSpecialOffers.visibility = VISIBLE
            binding.tvDescription.visibility = VISIBLE
        }
    }

}
