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


    private val _deleteUserResultMutableLiveData =
        MutableLiveData<Resource<BaseResponse<Any>>>()
    val deleteUserResultLiveData: LiveData<Resource<BaseResponse<Any>>> get() = _deleteUserResultMutableLiveData


    fun getUserInfoResult() {
        val flow = createUserInfoResultRequest()
        apply(flow = flow, maxRetries = 2, onError = { error ->
            _userResultMutableLiveData.value =
                Resource.Error(error.localizedMessage ?: "Unknown error")
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



    fun deleteUserInfoResult() {
        val flow = createDeleteUserInfoResultRequest()
        apply(flow = flow, maxRetries = 2, onError = { error ->
            _deleteUserResultMutableLiveData.value =
                Resource.Error(error.localizedMessage ?: "Unknown error")
        }, onSuccess = { resource ->
            _deleteUserResultMutableLiveData.value = resource
        })
    }


    private fun createDeleteUserInfoResultRequest(): Flow<Resource<BaseResponse<Any>>> =
        channelFlow {
            send(Resource.Loading())
            val result = mainUseCase.userInfoUseCase.deleteAccount()
            result.handleResponse(onSuccess = {
                send(Resource.Success(result))
            }, onError = { message, error ->
                send(Resource.Error(message, error))
            }, onUnauthenticated = {
                send(Resource.UnAuthenticated())
            })
        }

}