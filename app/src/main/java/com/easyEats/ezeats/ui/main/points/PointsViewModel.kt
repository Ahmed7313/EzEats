package com.easyEats.ezeats.ui.main.points

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.domain.main.model.request.offer_request.OfferRequest
import com.easyEats.ezeats.domain.main.model.response.home_response.HotDeal
import com.easyEats.ezeats.domain.main.model.response.loyality_center.LoyaltyCenterResponse
import com.easyEats.ezeats.domain.main.model.response.points.PointsResponse
import com.easyEats.ezeats.domain.main.use_case.MainUseCase
import com.easyEats.ezeats.ui.main.main.MainViewModel
import com.easyEats.ezeats.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PointsViewModel @Inject constructor(val mainUseCase: MainUseCase) :
    MainViewModel(mainUseCase) {
    private val _pointsResultMutableLiveData =
        MutableLiveData<Resource<BaseResponse<LoyaltyCenterResponse>>>()
    val pointsResultLiveData: LiveData<Resource<BaseResponse<LoyaltyCenterResponse>>> get() = _pointsResultMutableLiveData

    init {
        getPointsRestaurantResult()
    }

    private fun getPointsRestaurantResult() {
        viewModelScope.launch {
            val flow = createPointsResultRequest()
            apply(flow = flow, maxRetries = 2, onError = { error ->
                _pointsResultMutableLiveData.value =
                    Resource.Error(error.message.toString())
            }, onSuccess = { resource ->
                _pointsResultMutableLiveData.value = resource
            })

        }
    }

    fun getUserToken(): Flow<String> = mainUseCase.pointsUseCase.getToken()

    private fun createPointsResultRequest(): Flow<Resource<BaseResponse<LoyaltyCenterResponse>>> =
        channelFlow {
            send(Resource.Loading())
            val result = mainUseCase.pointsUseCase.getUserPoints()
            result.handleResponse(onSuccess = {
                send(Resource.Success(result))
            }, onError = { message, error ->
                send(Resource.Error(message, error))
            }, onUnauthenticated = {
                send(Resource.UnAuthenticated())
            })
        }


}