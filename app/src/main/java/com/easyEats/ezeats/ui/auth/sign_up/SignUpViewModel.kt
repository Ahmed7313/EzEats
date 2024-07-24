package com.easyEats.ezeats.ui.auth.sign_up

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.easyEats.ezeats.domain.auth.use_case.AuthUseCase
import com.easyEats.ezeats.ui.auth.main.AuthViewModel
import com.easyEats.ezeats.utils.Resource
import com.parse.ParseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val authUseCase: AuthUseCase) : AuthViewModel() {

    // LiveData to expose name validity
    private val _isNameValid = MutableLiveData(false)
    val isNameValid: LiveData<Boolean> get() = _isNameValid

    private val _registrationResult = MutableLiveData<Resource<ParseUser>>()
    val registrationResult: LiveData<Resource<ParseUser>> get() = _registrationResult

    // Combined LiveData for overall form validity
    val isFormValid = MediatorLiveData<Boolean>().apply {
        addSource(isPhoneNumberValid) { value = isFormValid() }
        addSource(_isNameValid) { value = isFormValid() }
    }


    fun register(phoneNumber: String, name: String) {
        // Call the register function from the UseCase
        val registrationLiveData = authUseCase.signUpUseCase.register(phoneNumber, name)

        // Observe LiveData changes and update the ViewModel's LiveData
        registrationLiveData.observeForever { resource ->
            _registrationResult.value = resource
        }
    }

    fun saveToken(user: ParseUser) {
        viewModelScope.launch {
            authUseCase.signUpUseCase.saveToken(user.sessionToken)
            Log.d("TAG", "Ahmed Saad Kenawy: ${user.sessionToken} ")
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

    private fun isFormValid() = isPhoneNumberValid.value == true && _isNameValid.value == true
}
