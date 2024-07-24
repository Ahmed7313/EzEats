package com.easyEats.ezeats.ui.auth.splash_screen

import androidx.lifecycle.ViewModel
import com.easyEats.ezeats.domain.auth.use_case.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SplashViewModel @Inject constructor(authUseCase: AuthUseCase) : ViewModel() {
    val isUserWatchOnBoardingScreen = authUseCase.splashUseCase.isUserWatchOnBoardingScreen()
}