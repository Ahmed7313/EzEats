package com.easyEats.ezeats.ui.auth.sign_in

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.domain.auth.model.request.SendOtpRequest
import com.easyEats.ezeats.domain.auth.use_case.AuthUseCase
import com.easyEats.ezeats.ui.auth.main.AuthViewModel
import com.easyEats.ezeats.utils.Resource
import com.parse.ParseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SignInViewModel @Inject constructor(private val authUseCase: AuthUseCase) : AuthViewModel() {

    private val _loginResult = MutableLiveData<Resource<ParseUser>>()
    val loginResult: LiveData<Resource<ParseUser>> get() = _loginResult

    private val _sendOtpMutableLiveData = MutableLiveData<Resource<BaseResponse<Int>>>()
    val sendOtpLiveData: LiveData<Resource<BaseResponse<Int>>> get() = _sendOtpMutableLiveData


    fun login(phoneNumber: String) {
        // Call the register function from the UseCase
        val loginLiveData = authUseCase.signInUseCase.login(phoneNumber)

        // Observe LiveData changes and update the ViewModel's LiveData
        loginLiveData.observeForever { resource ->
            sendOtp(phoneNumber)
            _loginResult.value = resource
        }
    }

    fun saveToken(user: ParseUser) {
        viewModelScope.launch {
            authUseCase.signInUseCase.saveToken(user.sessionToken)
        }
    }

    private fun sendOtp(phoneNumber: String) {
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
