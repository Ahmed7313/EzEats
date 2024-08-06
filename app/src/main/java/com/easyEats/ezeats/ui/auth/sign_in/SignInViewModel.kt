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
import com.easyEats.ezeats.utils.events.SingleLiveEvent
import com.parse.ParseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(private val authUseCase: AuthUseCase) : AuthViewModel() {

    private val _loginResult = MutableLiveData<Resource<ParseUser>?>()
    val loginResult: MutableLiveData<Resource<ParseUser>?> get() = _loginResult

    private val _sendOtpMutableLiveData = SingleLiveEvent<Resource<BaseResponse<Int>>?>()
    val sendOtpLiveData: SingleLiveEvent<Resource<BaseResponse<Int>>?> get() = _sendOtpMutableLiveData

    val phoneNumber = MutableLiveData<String>()

    fun login() {
        val loginLiveData = authUseCase.signInUseCase.login(phoneNumber.value ?: "")
        loginLiveData.observeForever { resource ->
            _loginResult.value = resource
        }
    }

    fun saveToken(user: ParseUser) {
        viewModelScope.launch {
            Log.d(TAG, "saveToken: ${user.sessionToken}")
            authUseCase.signInUseCase.saveToken(user.sessionToken)
        }
    }

    fun saveMobileNumber() {
        viewModelScope.launch {
            authUseCase.signInUseCase.saveMobileNumber(phoneNumber.value ?: "")
        }
    }

    fun sendOtp() {
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

    private fun createSendOtpFlow(): Flow<Resource<BaseResponse<Int>>> = channelFlow {
        trySend(Resource.Loading())
        val sendOtp = authUseCase.signInUseCase.sendOTP(SendOtpRequest("+2${phoneNumber.value}"))
        if (sendOtp.isSuccessful) {
            trySend(Resource.Success(sendOtp.body()))
        } else {
            trySend(Resource.Error(sendOtp.message()))
        }
        close()
    }
}

