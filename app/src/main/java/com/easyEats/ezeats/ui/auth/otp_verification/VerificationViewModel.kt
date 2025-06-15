package com.easyEats.ezeats.ui.auth.otp_verification

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.easyEats.ezeats.MyApplication.Companion.TAG
import com.easyEats.ezeats.MyApplication.Companion.baseApplication
import com.easyEats.ezeats.R
import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.domain.auth.model.request.SendOtpRequest
import com.easyEats.ezeats.domain.auth.model.request.VerifyOtpRequest
import com.easyEats.ezeats.domain.auth.use_case.AuthUseCase
import com.easyEats.ezeats.ui.auth.main.AuthViewModel
import com.easyEats.ezeats.utils.Resource
import com.easyEats.ezeats.utils.Resource.Loading
import com.easyEats.ezeats.utils.Resource.UnAuthenticated
import com.easyEats.ezeats.utils.Resource.Success
import com.easyEats.ezeats.utils.Resource.Error
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * VerificationViewModel is a ViewModel responsible for handling OTP verification and sending OTP requests.
 * It extends the AuthViewModel and uses the AuthUseCase for authentication-related operations.
 *
 * @property authUseCase The use case for authentication operations.
 */
@HiltViewModel
class VerificationViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    savedStateHandle: SavedStateHandle
) :
    AuthViewModel() {

    // LiveData to expose OTP verification result
    private val _verifyOtpMutableLiveData = MutableLiveData<Resource<Boolean>>()
    val verifyOtpLiveData: LiveData<Resource<Boolean>> get() = _verifyOtpMutableLiveData

    // LiveData to expose OTP sending result
    private val _sendOtpMutableLiveData = MutableLiveData<
            Resource<BaseResponse<Boolean>>>()
    val sendOtpLiveData: LiveData<
            Resource<BaseResponse<Boolean>>>
        get() = _sendOtpMutableLiveData

    val phoneNumber: String? = savedStateHandle["phoneNumber"]

    val otp = MutableLiveData<String>()

    private val verifyExceptionHandler = CoroutineExceptionHandler { _, exception ->
        _verifyOtpMutableLiveData.value =
            Error(baseApplication.getString(R.string.no_internet_connection))
    }

    private val sendOtpExceptionHandler = CoroutineExceptionHandler { _, exception ->
        _sendOtpMutableLiveData.value =
            Error(baseApplication.getString(R.string.no_internet_connection))
    }


    fun verifyOtp() {
        val flow = createVerifyOtp()
        apply(
            flow = flow,
            maxRetries = 2,
            onError = { error ->
                _verifyOtpMutableLiveData.value =
                    Error("No Internet Connection")
            },
            onSuccess = { resource ->
                _verifyOtpMutableLiveData.value = resource
            }
        )
    }

    private fun createVerifyOtp(): Flow<Resource<Boolean>> = flow {
        emit(Loading())
        val verifyOtp = authUseCase.verifyUseCase.verifyOtp(
            VerifyOtpRequest(
                "+2$phoneNumber",
                otp.value.toString()
            )
        )
        verifyOtp.handleResponse(
            onSuccess = {
                saveUserVerification(true)
                emit(Success(it))
            },
            onError = { message, error ->
                emit(Error(message, error))
            }, onUnauthenticated = {
                emit(UnAuthenticated())
            }
        )
    }

    fun sendOtp() {
        val flow = createSendOtpFlow()
        apply(
            flow = flow,
            maxRetries = 2,
            onError = { error ->
                _sendOtpMutableLiveData.value =
                    Error("No Internet Connection")
            },
            onSuccess = { resource ->
                _sendOtpMutableLiveData.value = resource
            }
        )
    }

    private fun createSendOtpFlow(): Flow<Resource<BaseResponse<Boolean>>> = channelFlow {
        send(Loading())
        val sendOtp = authUseCase.signInUseCase.sendOTP(SendOtpRequest("+2${phoneNumber}"))
        sendOtp.handleResponse(onSuccess = {
            send(Success(sendOtp))
        },
            onError = { message, error ->
                send(Error(message, error))
            }, onUnauthenticated = {
                send(UnAuthenticated())
            }
        )
    }

    private fun saveUserVerification(isUserVerified: Boolean) {
        viewModelScope.launch {
            authUseCase.verifyUseCase.setUserVerification(isUserVerified)
        }
    }

    fun saveToken(token: String) {
        viewModelScope.launch {
            authUseCase.signInUseCase.saveToken(token)
        }
    }
}