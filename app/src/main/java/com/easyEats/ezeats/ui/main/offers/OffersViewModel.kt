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

@HiltViewModel
class OffersViewModel @Inject constructor(val mainUseCase: MainUseCase) :
    MainViewModel(mainUseCase) {

    private val _restaurantResultStateFlow =
        MutableStateFlow<Resource<BaseResponse<List<RestaurantsResponseItem>>>>(Resource.Loading())
    val restaurantResultStateFlow: StateFlow<Resource<BaseResponse<List<RestaurantsResponseItem>>>> get() = _restaurantResultStateFlow


    private val _categoryResultMutableLiveData =
        MutableLiveData<Resource<BaseResponse<List<Category>>>>()
    val categoryResultLiveData: LiveData<Resource<BaseResponse<List<Category>>>> get() = _categoryResultMutableLiveData



    fun getRestaurantsResult(categoryId: String,userLat:String?,userLong:String?) {
        viewModelScope.launch {

                val flow = createRestaurantsResultRequest(RestaurantRequest(categoryId = categoryId, userLat = userLat ?: "0.0", userLon = userLong ?: "0.0"))
                apply(
                    flow = flow,
                    maxRetries = 2,
                    onError = {
                        _restaurantResultStateFlow.value =
                            Resource.Error("No Internet Connection")
                    },
                    onSuccess = { resource ->
                        _restaurantResultStateFlow.value = resource
                    }
                )
        }
    }


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
                }, onUnauthenticated = {
                    send(Resource.UnAuthenticated())
                }
            )
        }

    fun getCategoriesRestaurantResult() {
            val flow = createCategoriesResultRequest()
            apply(flow = flow, maxRetries = 2, onError = { error ->
                _categoryResultMutableLiveData.value =
                    Resource.Error("No Internet Connection")
            }, onSuccess = { resource ->
                _categoryResultMutableLiveData.value = resource
            })
    }


    private fun createCategoriesResultRequest(): Flow<Resource<BaseResponse<List<Category>>>> =
        channelFlow {
            send(Resource.Loading())
            val result = mainUseCase.offersUserCase.getCategory()
            result.handleResponse(onSuccess = {
                send(Resource.Success(result))
            }, onError = { message, error ->
                send(Resource.Error(message, error))
            }, onUnauthenticated = {
                send(Resource.UnAuthenticated())
            })
        }

    fun setRestaurantsData(
        context: Context,
        binding: FragmentPercentScreenBinding,
        restaurantAdapter: RestaurantAdapter,
        restaurants: List<RestaurantsResponseItem>
    ) {
        binding.rvRestaurant.adapter = restaurantAdapter
        if (restaurants.isEmpty()) {
            binding.rvRestaurant.visibility = GONE
            binding.tvAllRestaurants.visibility = GONE
            binding.llNoRestaurants.visibility = VISIBLE
        } else {

            binding.rvRestaurant.visibility = VISIBLE
            binding.tvAllRestaurants.visibility = VISIBLE
            binding.llNoRestaurants.visibility = GONE
            restaurantAdapter.submitList(restaurants)
        }
    }


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

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "onCleared: view model cleared")
    }
}