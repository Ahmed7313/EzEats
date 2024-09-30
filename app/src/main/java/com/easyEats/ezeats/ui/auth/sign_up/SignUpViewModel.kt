package com.easyEats.ezeats.ui.auth.sign_up

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.easyEats.ezeats.MyApplication
import com.easyEats.ezeats.R
import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.domain.auth.model.request.SendOtpRequest
import com.easyEats.ezeats.domain.auth.use_case.AuthUseCase
import com.easyEats.ezeats.ui.auth.main.AuthViewModel
import com.easyEats.ezeats.utils.Resource
import com.easyEats.ezeats.utils.events.SingleLiveEvent
import com.parse.ParseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
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
    private val isNameValid = MutableLiveData(false)

    // LiveData to expose registration result
    private val _registrationResult = MutableLiveData<Resource<ParseUser>>()
    val registrationResult: MutableLiveData<Resource<ParseUser>> get() = _registrationResult

    private val _sendOtpMutableLiveData = SingleLiveEvent<Resource<BaseResponse<Boolean>>?>()
    val sendOtpLiveData: SingleLiveEvent<Resource<BaseResponse<Boolean>>?> get() = _sendOtpMutableLiveData

    val phoneNumber = MutableLiveData<String>()

    val name = MutableLiveData<String>()

    private val sendOtpExceptionHandler = CoroutineExceptionHandler { _, exception ->
        _sendOtpMutableLiveData.value =
            Resource.Error(MyApplication.baseApplication.getString(R.string.no_internet_connection))
    }


    // Combined LiveData for overall form validity
    val isFormValid = MediatorLiveData<Boolean>().apply {
        addSource(isPhoneNumberValid) { value = isFormValid() }
        addSource(isNameValid) { value = isFormValid() }
    }


    fun register() {
        // Call the register function from the UseCase
        val registrationLiveData =
            authUseCase.signUpUseCase.register(phoneNumber.value.toString(), name.value.toString())

        // Observe LiveData changes and update the ViewModel's LiveData
        registrationLiveData.observeForever { resource ->
            _registrationResult.value = resource
        }
    }

    fun sendOtp() {
        val flow = createSendOtpFlow()
        apply(
            flow = flow,
            maxRetries = 1,
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

    fun saveMobileNumber() {
        viewModelScope.launch {
            authUseCase.signUpUseCase.saveMobileNumber(phoneNumber.value.toString())
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
        isNameValid.value = name.isNotBlank()
    }

    fun saveNecessaryData(resource: Resource.Success<ParseUser>) {
        if (!isOtpSent) {
            isOtpSent=true
            saveToken(resource.data)
            saveName(name.value.toString())
            sendOtp()
        }
    }

    /**
     * Checks if the form is valid by ensuring both the phone number and name are valid.
     *
     * @return Boolean indicating the overall form validity
     */
    private fun isFormValid() = isPhoneNumberValid.value == true && isNameValid.value == true
}