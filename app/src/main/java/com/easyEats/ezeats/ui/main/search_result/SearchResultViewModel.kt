package com.easyEats.ezeats.ui.main.search_result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.domain.main.model.request.search.SearchRequest
import com.easyEats.ezeats.domain.main.model.response.home_response.HotDeal
import com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.Restaurant
import com.easyEats.ezeats.domain.main.use_case.MainUseCase
import com.easyEats.ezeats.ui.main.main.MainViewModel
import com.easyEats.ezeats.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject


@HiltViewModel
class SearchResultViewModel @Inject constructor(
    val mainUseCase: MainUseCase, savedStateHandle: SavedStateHandle
) : MainViewModel(mainUseCase.mainUseCase) {

    val searchKey: String? = savedStateHandle["searchKey"]
    val viewType = MutableLiveData(ViewType.Restaurant)
    private val _searchResultMutableLiveData =
        MutableLiveData<Resource<BaseResponse<List<Restaurant>>>>()
    val searchResultLiveData: LiveData<Resource<BaseResponse<List<Restaurant>>>> get() = _searchResultMutableLiveData

    private val _searchOfferResultMutableLiveData =
        MutableLiveData<Resource<BaseResponse<List<HotDeal>>>>()
    val searchOfferResultLiveData: LiveData<Resource<BaseResponse<List<HotDeal>>>> get() = _searchOfferResultMutableLiveData

    init {
        getSearchRestaurantResult()
    }

    fun setRestaurantViewType() {
        viewType.value = ViewType.Restaurant
    }

    fun setOfferViewType() {
        viewType.value = ViewType.Offer
    }

    fun getSearchRestaurantResult(viewType: String = "restaurant") {
        val flow = createSearchResultRequest(SearchRequest(searchKey ?: "", viewType))
        apply(flow = flow, maxRetries = 2, onError = { error ->
            _searchResultMutableLiveData.value =
                Resource.Error(error.localizedMessage ?: "Unknown error")
        }, onSuccess = { resource ->
            _searchResultMutableLiveData.value = resource
        })
    }

    private fun createSearchResultRequest(searchRequest: SearchRequest): Flow<Resource<BaseResponse<List<Restaurant>>>> =
        channelFlow {
            send(Resource.Loading())
            val homeResult = mainUseCase.searchUseCase.searchRestaurant(searchRequest)
            homeResult.handleResponse(onSuccess = {
                send(Resource.Success(homeResult))
            }, onError = { message, error ->
                send(Resource.Error(message, error))
            }, onUnauthenticated = {
                send(Resource.UnAuthenticated())
            })
        }


    fun getSearchOfferResult(viewType: String = "offers") {
        val flow = createSearchOfferResultRequest(SearchRequest(searchKey ?: "", viewType))
        apply(flow = flow, maxRetries = 2, onError = { error ->
            _searchOfferResultMutableLiveData.value =
                Resource.Error(error.localizedMessage ?: "Unknown error")
        }, onSuccess = { resource ->
            _searchOfferResultMutableLiveData.value = resource
        })
    }

    private fun createSearchOfferResultRequest(searchRequest: SearchRequest): Flow<Resource<BaseResponse<List<HotDeal>>>> =
        channelFlow {
            send(Resource.Loading())
            val homeResult = mainUseCase.searchUseCase.searchOffer(searchRequest)
            homeResult.handleResponse(onSuccess = {
                send(Resource.Success(homeResult))
            }, onError = { message, error ->
                send(Resource.Error(message, error))
            }, onUnauthenticated = {
                send(Resource.UnAuthenticated())
            })
        }
}