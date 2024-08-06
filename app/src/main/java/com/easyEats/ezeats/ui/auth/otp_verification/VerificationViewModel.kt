package com.easyEats.ezeats.ui.auth.otp_verification

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
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
    private val _verifyOtpMutableLiveData = MutableLiveData<Resource<BaseResponse<Boolean>>>()
    val verifyOtpLiveData: LiveData<Resource<BaseResponse<Boolean>>> get() = _verifyOtpMutableLiveData

    // LiveData to expose OTP sending result
    private val _sendOtpMutableLiveData = MutableLiveData<Resource<BaseResponse<Int>>>()
    val sendOtpLiveData: LiveData<Resource<BaseResponse<Int>>> get() = _sendOtpMutableLiveData

    val phoneNumber: String? = savedStateHandle["phoneNumber"]

    val otp = MutableLiveData<String>()

    private val verifyExceptionHandler = CoroutineExceptionHandler { _, exception ->
        _verifyOtpMutableLiveData.value =
            Resource.Error(baseApplication.getString(R.string.no_internet_connection))
    }

    private val sendOtpExceptionHandler = CoroutineExceptionHandler { _, exception ->
        _sendOtpMutableLiveData.value =
            Resource.Error(baseApplication.getString(R.string.no_internet_connection))
    }


    fun verifyOtp() {
        val flow = createVerifyOtp()
        apply(
            flow = flow,
            maxRetries = 2,
            onError = { error ->
                _verifyOtpMutableLiveData.value =
                    Resource.Error(error.localizedMessage ?: "Unknown error")
            },
            onSuccess = { resource ->
                _verifyOtpMutableLiveData.value = resource
            }
        )
    }

    private fun createVerifyOtp(): Flow<Resource<BaseResponse<Boolean>>> = channelFlow {
        send(Resource.Loading())
        val sendOtp = authUseCase.verifyUseCase.verifyOtp(
            VerifyOtpRequest(
                "+2$phoneNumber",
                otp.value.toString()
            )
        )
        if (sendOtp.isSuccessful) {
            if (sendOtp.body()?.result == true) {
                saveUserVerification(true)
                send(Resource.Success(sendOtp.body()))
            } else
                send(Resource.Error("Invalid Otp"))

        } else {
            send(Resource.Error(sendOtp.message()))
        }
        close()
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

    private fun createSendOtpFlow(): Flow<Resource<BaseResponse<Int>>> = flow {
        viewModelScope.launch(sendOtpExceptionHandler) {
            emit(Resource.Loading())
            val sendOtp = authUseCase.signInUseCase.sendOTP(SendOtpRequest("+2${phoneNumber}"))
            if (sendOtp.isSuccessful) {
                emit(Resource.Success(sendOtp.body()))
            } else {
                emit(Resource.Error(sendOtp.message()))
            }
        }
    }

    private fun saveUserVerification(isUserVerified: Boolean) {
        viewModelScope.launch {
            authUseCase.verifyUseCase.setUserVerification(isUserVerified)
            Log.d(TAG, "saveUserVerification: $isUserVerified")
        }
    }
}