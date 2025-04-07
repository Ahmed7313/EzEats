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
@HiltViewModel // Annotation for Hilt to inject dependencies into this ViewModel
class PointsViewModel @Inject constructor(
    val mainUseCase: MainUseCase // Dependency injected using Hilt
) : MainViewModel(mainUseCase) { // Inherits from MainViewModel

    // Backing property for MutableLiveData to manage the points result state
    private val _pointsResultMutableLiveData =
        MutableLiveData<Resource<BaseResponse<LoyaltyCenterResponse>>>()

    // Public LiveData to expose the points result to observers
    val pointsResultLiveData: LiveData<Resource<BaseResponse<LoyaltyCenterResponse>>>
        get() = _pointsResultMutableLiveData

    // Fetches points for the restaurant and handles API response
    fun getPointsRestaurantResult() {
        viewModelScope.launch {
            // Creates a Flow for the API request
            val flow = createPointsResultRequest()

            // Applies retry logic and handles success and error states
            apply(
                flow = flow,
                maxRetries = 2, // Maximum number of retries for the request
                onError = { error ->
                    _pointsResultMutableLiveData.value =
                        Resource.Error(error.message.toString()) // Updates LiveData with the error message
                },
                onSuccess = { resource ->
                    _pointsResultMutableLiveData.value = resource // Updates LiveData with the success result
                }
            )
        }
    }

    // Retrieves the user token as a Flow
    fun getUserToken(): Flow<String> = mainUseCase.pointsUseCase.getToken()

    // Creates a Flow to manage the API request and handle its response
    private fun createPointsResultRequest(): Flow<Resource<BaseResponse<LoyaltyCenterResponse>>> =
        channelFlow {
            // Emit a loading state before starting the request
            send(Resource.Loading())

            // Make the API call to fetch user points
            val result = mainUseCase.pointsUseCase.getUserPoints()

            // Handle the API response with the appropriate Resource state
            result.handleResponse(
                onSuccess = {
                    send(Resource.Success(result)) // Emit a success state with the result
                },
                onError = { message, error ->
                    send(Resource.Error(message, error)) // Emit an error state with details
                },
                onUnauthenticated = {
                    send(Resource.UnAuthenticated()) // Emit an unauthenticated state
                }
            )
        }
}
