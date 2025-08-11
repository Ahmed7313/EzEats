package com.easyEats.ezeats.ui.main.offers

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.databinding.FragmentPercentScreenBinding
import com.easyEats.ezeats.domain.main.model.request.RestaurantRequest
import com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.result.RestaurantListResultResponse
import com.easyEats.ezeats.domain.main.model.response.search_response.Category
import com.easyEats.ezeats.domain.main.use_case.MainUseCase
import com.easyEats.ezeats.ui.main.main.MainViewModel
import com.easyEats.ezeats.ui.main.offers.adapter.OffersCategoryAdapter
import com.easyEats.ezeats.utils.Constants
import com.easyEats.ezeats.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for handling the fetching and displaying of restaurant and category data.
 * Uses `MainUseCase` for interacting with business logic and making network requests.
 */
@HiltViewModel
class OffersViewModel @Inject constructor(val mainUseCase: MainUseCase) :
    MainViewModel(mainUseCase) {

    private val _categorySelectState = MutableStateFlow<String>(Constants.CATEGORY_ID_ALL)
    val categorySelectState = _categorySelectState.asStateFlow()


    fun resetSelectedCategoryName() {
        _categorySelectState.value = Constants.CATEGORY_ID_ALL
    }

    fun setSelectedCategoryName(name: String) {
        _categorySelectState.value = name
    }

    val TAG = "OffersViewModel"

    /**
     * Mutable state flow for holding restaurant results.
     * This is exposed as a StateFlow to the UI layer.
     */
    private val _restaurantResultStateFlow =
        MutableStateFlow<Resource<RestaurantListResultResponse>>(Resource.Loading())
    val restaurantResultStateFlow: StateFlow<Resource<RestaurantListResultResponse>> get() = _restaurantResultStateFlow

    /**
     * Mutable live data for holding category results.
     * This is exposed as LiveData to be observed in the UI layer.
     */
    private val _categoryResultMutableLiveData =
        MutableLiveData<Resource<BaseResponse<List<Category>>>>()
    val categoryResultLiveData: LiveData<Resource<BaseResponse<List<Category>>>> get() = _categoryResultMutableLiveData

    /**
     * Fetches restaurant data for a given category.
     * It constructs a request with category ID and optional user latitude and longitude.
     */
    fun getRestaurantsResult(
        categoryId: String,
        userLat: String?,
        userLong: String?,
        compound: String
    ) {
        Log.d(TAG, "getRestaurantsResult: start")
        _restaurantResultStateFlow.value = Resource.Loading()
        try {
            viewModelScope.launch {
                try {
//                     val request = createRestaurantsResultRequest()
                    val response = mainUseCase.homeUseCase.getRestaurants(
                        RestaurantRequest(
                            categoryId = categoryId,
                            userLat = if (compound.isEmpty()) userLat ?: "" else "",
                            userLon = if (compound.isEmpty()) userLong ?: "" else "",
                            compoundId = if (compound.isEmpty()) null else compound
                        )
                    )
                    _restaurantResultStateFlow.value = Resource.Success(response)
//                    Log.d(TAG, "createRestaurantsResultRequest: ${restaurants.restaurantListResult?.restaurants?.pickupRestaurants?.size}")
//                    Log.d(TAG, "createRestaurantsResultRequest: ${restaurants.restaurantListResult?.restaurants?.deliveryRestaurants?.size}")

//                    apply(
//                        flow = flow,
//                        maxRetries = 2,
//                        onError = {
//                            _restaurantResultStateFlow.value = Resource.Error("No Internet Connection")
//                        },
//                        onSuccess = { resource ->
//                            _restaurantResultStateFlow.value = resource
//                        }
//                    )
                } catch (e: Exception) {
                    _restaurantResultStateFlow.value = Resource.Error(e.message.toString(), null)
                    Log.d(TAG, "getRestaurantsResult: ${e.message}")
                }
            }
        } catch (e: Exception) {
            _restaurantResultStateFlow.value = Resource.Error(e.message.toString(), null)
            Log.d(TAG, "getRestaurantsResult: ${e.message}")
        }

    }

    /**
     * Creates a flow that makes a request to get restaurant data.
     * It uses the `homeUseCase.getRestaurants()` method and handles different response states.
     */
    private fun createRestaurantsResultRequest(request: RestaurantRequest) {

//        viewModelScope.launch {
//            val restaurants = mainUseCase.homeUseCase.getRestaurants(request)
//            Log.d(TAG, "createRestaurantsResultRequest: ${restaurants.restaurantListResult?.restaurants?.pickupRestaurants?.size}")
//            Log.d(TAG, "createRestaurantsResultRequest: ${restaurants.restaurantListResult?.restaurants?.deliveryRestaurants?.size}")
//        }
    }
//        channelFlow {
//            try{
//                Log.d(TAG, "createRestaurantsResultRequest: starting")
//                send(Resource.Loading())
//
////                restaurants.handleResponse(onSuccess = {
////                    Log.d(TAG, "createRestaurantsResultRequest: ${it?.restaurants?.pickupRestaurants?.size}")
////                    Log.d(TAG, "createRestaurantsResultRequest: ${it?.restaurants?.deliveryRestaurants?.size}")
////                }, onError = { message , error ->
////
////                    Log.d(TAG, "createRestaurantsResultRequest: $message , $error")
////                }, onUnauthenticated = {
////
////                })
//            }catch (e: Exception){
//                Log.d(TAG, "createRestaurantsResultRequest: ${e.message}")
//            }
//        }

    /**
     * Fetches category data for restaurants.
     * The results are handled and updated in the `_categoryResultMutableLiveData`.
     */
    fun getCategoriesRestaurantResult() {
        val flow = createCategoriesResultRequest()
        apply(
            flow = flow,
            maxRetries = 2,
            onError = { error ->
                _categoryResultMutableLiveData.value = Resource.Error("No Internet Connection")
            },
            onSuccess = { resource ->
                _categoryResultMutableLiveData.value = resource
            }
        )
    }

    /**
     * Creates a flow that makes a request to get category data.
     * Uses `offersUserCase.getCategory()` method to fetch the categories.
     */
    private fun createCategoriesResultRequest(): Flow<Resource<BaseResponse<List<Category>>>> =
        channelFlow {
            send(Resource.Loading())
            val result = mainUseCase.offersUserCase.getCategory()
            result.handleResponse(
                onSuccess = {
                    send(Resource.Success(result))
                },
                onError = { message, error ->
                    send(Resource.Error(message, error))
                },
                onUnauthenticated = {
                    send(Resource.UnAuthenticated())
                }
            )
        }

    /**
     * Sets the restaurant data in the UI.
     * It updates the visibility and content of the restaurant RecyclerView.
     */
//    fun setRestaurantsData(
//        context: Context,
//        binding: FragmentPercentScreenBinding,
//        restaurantAdapter: RestaurantAdapter,
//        restaurants: List<RestaurantsResponseItem>
//    ) {
//        binding.rvRestaurant.adapter = restaurantAdapter
//        if (restaurants.isEmpty()) {
//            binding.rvRestaurant.visibility = GONE
//            binding.tvAllRestaurants.visibility = GONE
//            binding.llNoRestaurants.visibility = VISIBLE
//        } else {
//            binding.rvRestaurant.visibility = VISIBLE
//            binding.tvAllRestaurants.visibility = VISIBLE
//            binding.llNoRestaurants.visibility = GONE
//            restaurantAdapter.submitList(restaurants)
//        }
//    }

    /**
     * Sets the category data in the UI.
     * Adds a custom "All" category at the top of the list and removes a specific category.
     */
    fun setDataOfCategories(
        binding: FragmentPercentScreenBinding,
        categoryAdapter: OffersCategoryAdapter,
        resource: Resource.Success<BaseResponse<List<Category>>>
    ) {
        binding.rvCategories.adapter = categoryAdapter
        val categories = resource.data?.result?.data as MutableList<Category>
        categories.removeIf { it.categoryId == "eqvWcHcHaD" }
        categories.sortBy { it.categoryName?.lowercase() }

        val itemAll = Category(
            "",
            "https://jtxlimrcv63uszqdkzh2a9d1r7pd0u.files-sashido.cloud/feedbf971af02fcc87b5c2f80f76e5fa_Artboard%201%20copy%2019.png",
            "All",
            null
        )
        categories.add(0, itemAll)
        categoryAdapter.submitList(categories)
    }

    /**
     * Called when the ViewModel is cleared.
     * Used to log or perform any necessary cleanup.
     */
    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "onCleared: view model cleared")
    }
}
