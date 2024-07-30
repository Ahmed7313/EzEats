package com.easyEats.ezeats.ui.auth.sign_up

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
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


/**
 * SignUpViewModel handles the sign-up logic and form validation for the sign-up screen.
 *
 * This ViewModel uses Hilt for dependency injection and extends the AuthViewModel for shared authentication logic.
 */
@HiltViewModel
class SignUpViewModel @Inject constructor(private val authUseCase: AuthUseCase) : AuthViewModel() {

    // LiveData to expose name validity
    private val _isNameValid = MutableLiveData(false)
    val isNameValid: LiveData<Boolean> get() = _isNameValid

    // LiveData to expose registration result
    private val _registrationResult = MutableLiveData<Resource<ParseUser>>()
    val registrationResult: LiveData<Resource<ParseUser>> get() = _registrationResult

    // Combined LiveData for overall form validity
    val isFormValid = MediatorLiveData<Boolean>().apply {
        addSource(isPhoneNumberValid) { value = isFormValid() }
        addSource(_isNameValid) { value = isFormValid() }
    }

    private val _sendOtpMutableLiveData = MutableLiveData<Resource<BaseResponse<Int>>>()
    val sendOtpLiveData: LiveData<Resource<BaseResponse<Int>>> get() = _sendOtpMutableLiveData


    /**
     * Registers a new user with the given phone number and name.
     *
     * @param phoneNumber The phone number of the user
     * @param name The name of the user
     */
    fun register(phoneNumber: String, name: String) {
        // Call the register function from the UseCase
        val registrationLiveData = authUseCase.signUpUseCase.register(phoneNumber, name)

        // Observe LiveData changes and update the ViewModel's LiveData
        registrationLiveData.observeForever { resource ->
            _registrationResult.value = resource
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

    /**
     * Saves the session token of the user.
     *
     * @param user The user whose session token is to be saved
     */
    fun saveToken(user: ParseUser?) {
        viewModelScope.launch {
            authUseCase.signUpUseCase.saveToken(user?.sessionToken.toString())
        }
    }

    fun saveMobileNumber(mobileNumber: String) {
        viewModelScope.launch {
            authUseCase.signUpUseCase.saveMobileNumber(mobileNumber)
        }
    }

    fun saveName(name: String) {
        viewModelScope.launch {
            authUseCase.signUpUseCase.saveName(name)
        }
    }

    /**
     * Validates the name field to ensure it's not empty.
     *
     * @param name The name to validate
     */
    fun validateName(name: String) {
        _isNameValid.value = name.isNotBlank()
    }

    /**
     * Checks if the form is valid by ensuring both the phone number and name are valid.
     *
     * @return Boolean indicating the overall form validity
     */
    private fun isFormValid() = isPhoneNumberValid.value == true && _isNameValid.value == true
}