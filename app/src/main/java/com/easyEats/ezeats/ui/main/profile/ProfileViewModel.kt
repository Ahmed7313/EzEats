package com.easyEats.ezeats.ui.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.domain.main.model.request.offer_request.OfferRequest
import com.easyEats.ezeats.domain.main.model.response.home_response.HotDeal
import com.easyEats.ezeats.domain.main.model.response.user_info.UserInfoResponse
import com.easyEats.ezeats.domain.main.use_case.MainUseCase
import com.easyEats.ezeats.ui.main.main.MainViewModel
import com.easyEats.ezeats.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject
/**
 * ViewModel for managing the profile-related data and actions such as fetching user info
 * and clearing the device token. It interacts with the `MainUseCase` to make API calls.
 * This ViewModel uses `LiveData` to communicate with the UI layer.
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(val mainUseCase: MainUseCase) :
    MainViewModel(mainUseCase) {

    // LiveData for holding the result of fetching user info
    private val _userResultMutableLiveData =
        MutableLiveData<Resource<BaseResponse<UserInfoResponse>>>()
    val userResultLiveData: LiveData<Resource<BaseResponse<UserInfoResponse>>> get() = _userResultMutableLiveData

    // LiveData for holding the result of clearing the device token
    private val _clearDeviceTokenMutableLiveData =
        MutableLiveData<Resource<BaseResponse<Any>>>()
    val clearDeviceTokenLiveData: LiveData<Resource<BaseResponse<Any>>> get() = _clearDeviceTokenMutableLiveData

    /**
     * Triggers the API call to fetch the user's information and update the `userResultLiveData`.
     */
    fun getUserInfoResult() {
        // Creates the request for fetching user info
        val flow = createUserInfoResultRequest()

        // Applies the flow and handles different states (loading, success, error)
        apply(flow = flow, maxRetries = 2, onError = { error ->
            // Sets the LiveData value to error in case of failure
            _userResultMutableLiveData.value =
                Resource.Error("No Internet Connection")
        }, onSuccess = { resource ->
            // Sets the LiveData value to success with the fetched data
            _userResultMutableLiveData.value = resource
        })
    }

    /**
     * Creates a flow that requests the user's information from the use case.
     *
     * @return A flow that emits the result of the request, including success, error, and unauthenticated states.
     */
    private fun createUserInfoResultRequest(): Flow<Resource<BaseResponse<UserInfoResponse>>> =
        channelFlow {
            send(Resource.Loading())  // Emits a loading state initially
            // Calls the use case to fetch the user info
            val result = mainUseCase.userInfoUseCase.getUserInfo()
            result.handleResponse(onSuccess = {
                send(Resource.Success(result))  // Emits a success state with the result
            }, onError = { message, error ->
                send(Resource.Error(message, error))  // Emits an error state with the error message
            }, onUnauthenticated = {
                send(Resource.UnAuthenticated())  // Emits an unauthenticated state
            })
        }

    /**
     * Triggers the API call to clear the device token and update the `clearDeviceTokenLiveData`.
     */
    fun clearDeviceTokenResult() {
        // Creates the request to clear the device token
        val flow = createClearDeviceTokenRequest()

        // Applies the flow and handles different states (loading, success, error)
        apply(flow = flow, maxRetries = 2, onError = { error ->
            // Sets the LiveData value to error in case of failure
            _clearDeviceTokenMutableLiveData.value =
                Resource.Error("No Internet Connection")
        }, onSuccess = { resource ->
            // Sets the LiveData value to success with the result
            _clearDeviceTokenMutableLiveData.value = resource
        })
    }

    /**
     * Creates a flow that requests clearing the device token from the use case.
     *
     * @return A flow that emits the result of the request, including success, error, and unauthenticated states.
     */
    private fun createClearDeviceTokenRequest(): Flow<Resource<BaseResponse<Any>>> =
        channelFlow {
            send(Resource.Loading())  // Emits a loading state initially
            // Calls the use case to clear the device token
            val result = mainUseCase.userInfoUseCase.clearDeviceToken()
            result.handleResponse(onSuccess = {
                send(Resource.Success(result))  // Emits a success state with the result
            }, onError = { message, error ->
                send(Resource.Error(message, error))  // Emits an error state with the error message
            }, onUnauthenticated = {
                send(Resource.UnAuthenticated())  // Emits an unauthenticated state
            })
        }
}
