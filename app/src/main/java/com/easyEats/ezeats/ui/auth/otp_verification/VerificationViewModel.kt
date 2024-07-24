package com.easyEats.ezeats.ui.auth.otp_verification

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.domain.auth.model.request.SendOtpRequest
import com.easyEats.ezeats.domain.auth.model.request.VerifyOtpRequest
import com.easyEats.ezeats.domain.auth.use_case.AuthUseCase
import com.easyEats.ezeats.ui.auth.main.AuthViewModel
import com.easyEats.ezeats.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerificationViewModel @Inject constructor(private val authUseCase: AuthUseCase) :
    AuthViewModel() {

    private val _verifyOtpMutableLiveData = MutableLiveData<Resource<BaseResponse<Boolean>>>()
    val verifyOtpLiveData: LiveData<Resource<BaseResponse<Boolean>>> get() = _verifyOtpMutableLiveData

    private val _sendOtpMutableLiveData = MutableLiveData<Resource<BaseResponse<Int>>>()
    val sendOtpLiveData: LiveData<Resource<BaseResponse<Int>>> get() = _sendOtpMutableLiveData


    fun verifyOtp(phoneNumber: String, otp: String) {
        viewModelScope.launch {
            _verifyOtpMutableLiveData.value = Resource.Loading()
            val sendOtp =
                authUseCase.verifyUseCase.verifyOtp(VerifyOtpRequest("+2$phoneNumber", otp))
            if (sendOtp.isSuccessful) {
                _verifyOtpMutableLiveData.value = Resource.Success(sendOtp.body())
            } else {
                _verifyOtpMutableLiveData.value = Resource.Error(sendOtp.message())
            }
        }
    }

    fun sendOtp(phoneNumber: String) {
        viewModelScope.launch {
            _sendOtpMutableLiveData.value = Resource.Loading()
            val sendOtp = authUseCase.signInUseCase.sendOTP(SendOtpRequest("+2$phoneNumber"))
            if (sendOtp.isSuccessful) {
                _sendOtpMutableLiveData.value = Resource.Success(sendOtp.body())
            } else {
                _sendOtpMutableLiveData.value = Resource.Error(sendOtp.message())
            }
        }
    }

}