package com.easyEats.ezeats.ui.main.offers

import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.databinding.FragmentPercentScreenBinding
import com.easyEats.ezeats.domain.main.model.request.offers.OffersRequest
import com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.Restaurant
import com.easyEats.ezeats.domain.main.model.response.search_response.Category
import com.easyEats.ezeats.domain.main.use_case.MainUseCase
import com.easyEats.ezeats.ui.main.main.MainViewModel
import com.easyEats.ezeats.ui.main.offers.adapter.OffersCategoryAdapter
import com.easyEats.ezeats.ui.main.search_result.adapter.RestaurantAdapter
import com.easyEats.ezeats.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject

@HiltViewModel
class OffersViewModel @Inject constructor(val mainUseCase: MainUseCase) :
    MainViewModel(mainUseCase) {

    // LiveData that holds the search results for Restaurants.
    private val _restaurantResultMutableLiveData =
        MutableLiveData<Resource<BaseResponse<List<Restaurant>>>>()
    val restaurantResultLiveData: LiveData<Resource<BaseResponse<List<Restaurant>>>> get() = _restaurantResultMutableLiveData


    private val _categoryResultMutableLiveData =
        MutableLiveData<Resource<BaseResponse<List<Category>>>>()
    val categoryResultLiveData: LiveData<Resource<BaseResponse<List<Category>>>> get() = _categoryResultMutableLiveData

    init {
        getRestaurantsRestaurantResult("")
        getCategoriesRestaurantResult()
    }

    fun getRestaurantsRestaurantResult(categoryId: String) {
        val flow = createRestaurantsResultRequest(
            OffersRequest(
                categoryId = categoryId,
                userLat = userLocationAddressMutableLiveData.value?.lat?:"",
                userLon = userLocationAddressMutableLiveData.value?.lng?:""
            )
        )
        apply(flow = flow, maxRetries = 2, onError = { error ->
            _restaurantResultMutableLiveData.value =
                Resource.Error(error.localizedMessage ?: "Unknown error")
        }, onSuccess = { resource ->
            _restaurantResultMutableLiveData.value = resource
        })
    }


    private fun createRestaurantsResultRequest(offersRequest: OffersRequest): Flow<Resource<BaseResponse<List<Restaurant>>>> =
        channelFlow {
            send(Resource.Loading())
            val result = mainUseCase.offersUserCase.getRestaurantsByCategory(offersRequest)
            result.handleResponse(onSuccess = {
                send(Resource.Success(result))
            }, onError = { message, error ->
                send(Resource.Error(message, error))
            }, onUnauthenticated = {
                send(Resource.UnAuthenticated())
            })
        }

    fun getCategoriesRestaurantResult() {
        val flow = createCategoriesResultRequest()
        apply(flow = flow, maxRetries = 2, onError = { error ->
            _categoryResultMutableLiveData.value =
                Resource.Error(error.localizedMessage ?: "Unknown error")
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
        binding: FragmentPercentScreenBinding,
        restaurantAdapter: RestaurantAdapter,
        resource: Resource.Success<BaseResponse<List<Restaurant>>>
    ) {
        binding.rvRestaurant.adapter = restaurantAdapter
        restaurantAdapter.submitList(resource.data?.result?.data)
        if (resource.data?.result?.data?.isEmpty() == true) {
            binding.rvRestaurant.visibility = GONE
            binding.tvAllRestaurants.visibility = GONE
            binding.llNoRestaurants.visibility = VISIBLE
        } else {
            binding.rvRestaurant.visibility = VISIBLE
            binding.tvAllRestaurants.visibility = VISIBLE
            binding.llNoRestaurants.visibility = GONE
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
        val itemAll = Category("eqvWcHcHaD", "All", "All", null)
        categories.add(0, itemAll)
        categoryAdapter.submitList(categories)
    }
}