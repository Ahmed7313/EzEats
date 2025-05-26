package com.easyEats.ezeats.ui.main.offers

import android.content.Context
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.animation.AnimationUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DefaultItemAnimator
import com.easyEats.ezeats.MyApplication.Companion.TAG
import com.easyEats.ezeats.R
import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.databinding.FragmentPercentScreenBinding
import com.easyEats.ezeats.domain.main.model.request.RestaurantRequest
import com.easyEats.ezeats.domain.main.model.request.offers.OffersRequest
import com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.Restaurant
import com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.new_restaurants_reponse.RestaurantsResponseItem
import com.easyEats.ezeats.domain.main.model.response.search_response.Category
import com.easyEats.ezeats.domain.main.use_case.MainUseCase
import com.easyEats.ezeats.ui.main.main.MainViewModel
import com.easyEats.ezeats.ui.main.offers.adapter.OffersCategoryAdapter
import com.easyEats.ezeats.ui.main.search_result.adapter.RestaurantAdapter
import com.easyEats.ezeats.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
/**
 * ViewModel for handling the fetching and displaying of restaurant and category data.
 * Uses `MainUseCase` for interacting with business logic and making network requests.
 */
@HiltViewModel
class OffersViewModel @Inject constructor(val mainUseCase: MainUseCase) :
    MainViewModel(mainUseCase) {

    /**
     * Mutable state flow for holding restaurant results.
     * This is exposed as a StateFlow to the UI layer.
     */
    private val _restaurantResultStateFlow =
        MutableStateFlow<Resource<BaseResponse<List<RestaurantsResponseItem>>>>(Resource.Loading())
    val restaurantResultStateFlow: StateFlow<Resource<BaseResponse<List<RestaurantsResponseItem>>>> get() = _restaurantResultStateFlow

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
    fun getRestaurantsResult(categoryId: String, userLat: String?, userLong: String?) {
        viewModelScope.launch {
            val flow = createRestaurantsResultRequest(RestaurantRequest(categoryId = categoryId, userLat = userLat ?: "0.0", userLon = userLong ?: "0.0"))
            apply(
                flow = flow,
                maxRetries = 2,
                onError = {
                    _restaurantResultStateFlow.value = Resource.Error("No Internet Connection")
                },
                onSuccess = { resource ->
                    _restaurantResultStateFlow.value = resource
                }
            )
        }
    }

    /**
     * Creates a flow that makes a request to get restaurant data.
     * It uses the `homeUseCase.getRestaurants()` method and handles different response states.
     */
    private fun createRestaurantsResultRequest(request: RestaurantRequest): Flow<Resource<BaseResponse<List<RestaurantsResponseItem>>>> =
        channelFlow {
            send(Resource.Loading())
            val restaurants = mainUseCase.homeUseCase.getRestaurants(request)
            restaurants.handleResponse(
                onSuccess = {
                    send(Resource.Success(restaurants))
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
        val itemAll = Category(
            "eqvWcHcHaD",
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
