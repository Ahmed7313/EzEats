package com.easyEats.ezeats.ui.main.active_order_details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.easyEats.ezeats.MyApplication.Companion.TAG
import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.domain.main.model.request.complaint_request.ComplaintRequest
import com.easyEats.ezeats.domain.main.model.response.active_order.ActiveOrderResponse
import com.easyEats.ezeats.domain.main.use_case.MainUseCase
import com.easyEats.ezeats.ui.main.main.MainViewModel
import com.easyEats.ezeats.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for handling the active order details and support email requests.
 * It communicates with the MainUseCase to fetch and manage data.
 *
 * @param mainUseCase Provides use case methods to perform API calls for active order tracking and sending support emails.
 */
@HiltViewModel
class ActiveOrderDetailsViewModel @Inject constructor(
    val mainUseCase: MainUseCase,
) : MainViewModel(mainUseCase) {


    // LiveData for tracking active order results
    private val _activeOrderResultMutableLiveData =
        MutableLiveData<Resource<BaseResponse<ActiveOrderResponse>>>()
    val trackActiveOrderResultLiveData: LiveData<Resource<BaseResponse<ActiveOrderResponse>>> get() = _activeOrderResultMutableLiveData

    // LiveData for tracking the result of sending a support email
    private val _sendSupportEmailResultMutableLiveData =
        MutableLiveData<Resource<BaseResponse<Any>>>()
    val sendSupportEmailResultLiveData: LiveData<Resource<BaseResponse<Any>>> get() = _sendSupportEmailResultMutableLiveData

    init {
        getActiveOrdersRestaurantResult() // Automatically fetch active orders on initialization
    }

    /**
     * Fetches the active order result from the repository.
     * Applies error handling and retries in case of network failure.
     */
    private fun getActiveOrdersRestaurantResult() {
        viewModelScope.launch {
            val flow = createActiveOrdersResultRequest()
            apply(flow = flow, maxRetries = 2, onError = { error ->
                Log.d(TAG, "getActiveOrdersRestaurantResult: Error ${error.localizedMessage}")
                _activeOrderResultMutableLiveData.value = Resource.Error("No Internet Connection")
            }, onSuccess = { resource ->
                Log.d(TAG, "getActiveOrdersRestaurantResult: Success")
                _activeOrderResultMutableLiveData.value = resource
            })
        }
    }

    /**
     * Creates a flow to request active order tracking data.
     *
     * @return Flow emitting the current resource state for active order data.
     */
    private fun createActiveOrdersResultRequest(): Flow<Resource<BaseResponse<ActiveOrderResponse>>> =
        channelFlow {
            send(Resource.Loading()) // Emit loading state
            val result = mainUseCase.trackActiveOrderUseCase.trackActiveOrder()
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
     * Sends a support email based on the provided complaint request.
     *
     * @param complaintRequest Data containing the complaint details to be sent.
     */
    fun sendSupportEmailResult(complaintRequest: ComplaintRequest) {
        viewModelScope.launch {
            val flow = createSupportEmailResultRequest(complaintRequest)
            apply(flow = flow, maxRetries = 2, onError = { error ->
                _sendSupportEmailResultMutableLiveData.value = Resource.Error("No Internet Connection")
            }, onSuccess = { resource ->
                _sendSupportEmailResultMutableLiveData.value = resource
            })
        }
    }

    /**
     * Creates a flow to send the support email.
     *
     * @param complaintRequest Complaint request data to be sent.
     * @return Flow emitting the current resource state for the support email request.
     */
    private fun createSupportEmailResultRequest(complaintRequest: ComplaintRequest): Flow<Resource<BaseResponse<Any>>> =
        channelFlow {
            send(Resource.Loading()) // Emit loading state
            val result = mainUseCase.trackActiveOrderUseCase.senSupportEmail(complaintRequest)
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
}
