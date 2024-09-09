package com.easyEats.ezeats.ui.auth.intro_screen

import androidx.lifecycle.viewModelScope
import com.easyEats.ezeats.domain.auth.use_case.AuthUseCase
import com.easyEats.ezeats.ui.auth.main.AuthViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroScreenViewModel @Inject constructor(private val authUseCase: AuthUseCase) :
    AuthViewModel() {

    fun saveUserVerification(isUserVerified: Boolean) {
        viewModelScope.launch {
            authUseCase.verifyUseCase.setUserVerification(isUserVerified)
        }
    }
}