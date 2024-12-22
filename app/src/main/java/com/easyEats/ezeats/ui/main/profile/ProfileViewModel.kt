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

@HiltViewModel
class ProfileViewModel @Inject constructor(val mainUseCase: MainUseCase) :
    MainViewModel(mainUseCase) {

    private val _userResultMutableLiveData =
        MutableLiveData<Resource<BaseResponse<UserInfoResponse>>>()
    val userResultLiveData: LiveData<Resource<BaseResponse<UserInfoResponse>>> get() = _userResultMutableLiveData



    private val _clearDeviceTokenMutableLiveData =
        MutableLiveData<Resource<BaseResponse<Any>>>()
    val clearDeviceTokenLiveData: LiveData<Resource<BaseResponse<Any>>> get() = _clearDeviceTokenMutableLiveData


    fun getUserInfoResult() {
        val flow = createUserInfoResultRequest()
        apply(flow = flow, maxRetries = 2, onError = { error ->
            _userResultMutableLiveData.value =
                Resource.Error("No Internet Connection")
        }, onSuccess = { resource ->
            _userResultMutableLiveData.value = resource
        })
    }


    private fun createUserInfoResultRequest(): Flow<Resource<BaseResponse<UserInfoResponse>>> =
        channelFlow {
            send(Resource.Loading())
            val result = mainUseCase.userInfoUseCase.getUserInfo()
            result.handleResponse(onSuccess = {
                send(Resource.Success(result))
            }, onError = { message, error ->
                send(Resource.Error(message, error))
            }, onUnauthenticated = {
                send(Resource.UnAuthenticated())
            })
        }






    fun clearDeviceTokenResult() {
        val flow = createClearDeviceTokenRequest()
        apply(flow = flow, maxRetries = 2, onError = { error ->
            _clearDeviceTokenMutableLiveData.value =
                Resource.Error("No Internet Connection")
        }, onSuccess = { resource ->
            _clearDeviceTokenMutableLiveData.value = resource
        })
    }


    private fun createClearDeviceTokenRequest(): Flow<Resource<BaseResponse<Any>>> =
        channelFlow {
            send(Resource.Loading())
            val result = mainUseCase.userInfoUseCase.clearDeviceToken()
            result.handleResponse(onSuccess = {
                send(Resource.Success(result))
            }, onError = { message, error ->
                send(Resource.Error(message, error))
            }, onUnauthenticated = {
                send(Resource.UnAuthenticated())
            })
        }

}