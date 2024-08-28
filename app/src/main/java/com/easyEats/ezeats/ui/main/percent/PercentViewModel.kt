package com.easyEats.ezeats.ui.main.percent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.domain.main.model.request.offers.OffersRequest
import com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.Restaurant
import com.easyEats.ezeats.domain.main.model.response.search_response.Category
import com.easyEats.ezeats.domain.main.use_case.MainUseCase
import com.easyEats.ezeats.ui.main.main.MainViewModel
import com.easyEats.ezeats.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject

@HiltViewModel
class PercentViewModel @Inject constructor(val mainUseCase: MainUseCase) :
    MainViewModel(mainUseCase.mainUseCase) {

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
        val flow = createRestaurantsResultRequest(OffersRequest(categoryId))
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

    private fun getCategoriesRestaurantResult() {
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
}