package com.easyEats.ezeats.ui.auth.sign_in

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.easyEats.ezeats.MyApplication.Companion.TAG
import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.domain.auth.model.request.SendOtpRequest
import com.easyEats.ezeats.domain.auth.use_case.AuthUseCase
import com.easyEats.ezeats.ui.auth.main.AuthViewModel
import com.easyEats.ezeats.utils.Resource
import com.parse.ParseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(private val authUseCase: AuthUseCase) : AuthViewModel() {

    private val _loginResult = MutableLiveData<Resource<ParseUser>?>()
    val loginResult: MutableLiveData<Resource<ParseUser>?> get() = _loginResult

    private val _sendOtpMutableLiveData = MutableLiveData<Resource<BaseResponse<Boolean>>?>()
    val sendOtpLiveData: MutableLiveData<Resource<BaseResponse<Boolean>>?> get() = _sendOtpMutableLiveData

    val phoneNumber = MutableLiveData<String>()

    fun login() {
        val phone=("+2" + phoneNumber.value) ?: ""
        Log.d(TAG, "login: $phone")
        val loginLiveData = authUseCase.signInUseCase.login(("+2" + phoneNumber.value) ?: "")
        loginLiveData.observeForever { resource ->
            _loginResult.value = resource
        }
    }

    private fun saveToken(user: ParseUser) {
        viewModelScope.launch {
            authUseCase.signInUseCase.saveToken(user.sessionToken)
        }
    }

    private fun saveMobileNumber() {
        viewModelScope.launch {
            authUseCase.signInUseCase.saveMobileNumber(phoneNumber.value ?: "")
        }
    }

    private fun sendOtp() {
        val flow = createSendOtpFlow()
        apply(
            flow = flow,
            maxRetries = 2,
            onError = { error ->
                _sendOtpMutableLiveData.value =
                    Resource.Error(error.localizedMessage ?: "Unknown error")
            },
            onSuccess = { resource ->
                _sendOtpMutableLiveData.value = resource
            }
        )
    }

    private fun createSendOtpFlow(): Flow<Resource<BaseResponse<Boolean>>> = channelFlow {
        send(Resource.Loading())
        val sendOtp =
            authUseCase.signInUseCase.sendOTP(SendOtpRequest("+2${phoneNumber.value.toString()}"))
        sendOtp.handleResponse(
            onSuccess = {
                saveMobileNumber()
                send(Resource.Success(sendOtp))
            },
            onError = { message, error ->
                send(Resource.Error(message, error))
            }, onUnauthenticated = {
                send(Resource.UnAuthenticated())
            }
        )
    }

    fun sendOtpAndSaveToke(resource: Resource.Success<ParseUser>) {
        if (!isOtpSent) {
            isOtpSent=true
            val user = resource.data
            sendOtp()
            saveToken(user ?: return)
        }
    }


}

