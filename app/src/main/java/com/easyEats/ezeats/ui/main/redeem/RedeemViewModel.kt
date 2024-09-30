package com.easyEats.ezeats.ui.main.redeem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.domain.main.model.request.offer_request.OfferRequest
import com.easyEats.ezeats.domain.main.model.response.home_response.HotDeal
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
class RedeemViewModel @Inject constructor(
    val mainUseCase: MainUseCase,
    savedStateHandle: SavedStateHandle
) :
    MainViewModel(mainUseCase) {

    private val offerId: String? = savedStateHandle["offerId"]

    private val _offerResultMutableLiveData =
        MutableLiveData<Resource<BaseResponse<HotDeal>>>()
    val offerResultLiveData: LiveData<Resource<BaseResponse<HotDeal>>> get() = _offerResultMutableLiveData

    init {
        getOfferRestaurantResult()
    }

    private fun getOfferRestaurantResult() {
        val flow = createOfferResultRequest(OfferRequest(offerId.toString()))
        apply(flow = flow, maxRetries = 2, onError = { error ->
            _offerResultMutableLiveData.value =
                Resource.Error("No Internet Connection")
        }, onSuccess = { resource ->
            _offerResultMutableLiveData.value = resource
        })
    }


    private fun createOfferResultRequest(offerRequest: OfferRequest): Flow<Resource<BaseResponse<HotDeal>>> =
        channelFlow {
            send(Resource.Loading())
            val result = mainUseCase.redeemOfferUseCase.getOfferById(offerRequest)
            result.handleResponse(onSuccess = {
                send(Resource.Success(result))
            }, onError = { message, error ->
                send(Resource.Error(message, error))
            }, onUnauthenticated = {
                send(Resource.UnAuthenticated())
            })
        }
}