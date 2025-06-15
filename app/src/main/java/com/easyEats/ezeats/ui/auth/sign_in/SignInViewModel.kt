package com.easyEats.ezeats.ui.auth.sign_in

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.easyEats.ezeats.MyApplication.Companion.TAG
import com.easyEats.ezeats.MyApplication.Companion.baseApplication
import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.domain.auth.model.request.FindUserRequest
import com.easyEats.ezeats.domain.auth.model.request.SendOtpRequest
import com.easyEats.ezeats.domain.auth.model.response.FindUserResponse
import com.easyEats.ezeats.domain.auth.use_case.AuthUseCase
import com.easyEats.ezeats.ui.auth.main.AuthViewModel
import com.easyEats.ezeats.utils.Resource
import com.parse.ParseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(private val authUseCase: AuthUseCase) : AuthViewModel() {

    private val _sendOtpMutableLiveData = MutableLiveData<Resource<BaseResponse<Boolean>>?>()
    val sendOtpLiveData: MutableLiveData<Resource<BaseResponse<Boolean>>?> get() = _sendOtpMutableLiveData

    val phoneNumber = MutableLiveData<String>()

    private val exceptionHandler = CoroutineExceptionHandler { _, ex ->
        Toasty.error(baseApplication, "No Internet Connection", Toast.LENGTH_LONG).show()
    }
    var userSessionToken: String? = ""


    fun findUser() {
        viewModelScope.launch(exceptionHandler) {
            val findUser =
                authUseCase.signInUseCase.findUser(FindUserRequest("+2${phoneNumber.value.toString()}"))
            if (findUser.isSuccessful) {
                if (findUser.body()?.result != null) {
                    userSessionToken = findUser.body()?.result
                    sendOtp()
                } else {
                    Toasty.error(baseApplication, "User Not Found", Toast.LENGTH_LONG).show()
                }
            } else {
                Toasty.error(
                    baseApplication,
                    "unExpected Error occurred, Please Try Again.",
                    Toast.LENGTH_LONG
                ).show()
            }
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
                    Resource.Error("No Internet Connection")
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
            isOtpSent = true
            sendOtp()
        }
    }


}

