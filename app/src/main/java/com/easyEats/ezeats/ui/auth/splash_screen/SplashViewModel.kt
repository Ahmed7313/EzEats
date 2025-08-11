package com.easyEats.ezeats.ui.auth.splash_screen

import androidx.lifecycle.ViewModel
import com.easyEats.ezeats.domain.auth.use_case.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


/**
 * SplashViewModel is responsible for handling the logic related to the splash screen.
 *
 * This ViewModel uses Hilt for dependency injection to interact with the use cases.
 *
 * @property authUseCase The use case class that handles authentication and onboarding logic.
 */
@HiltViewModel
class SplashViewModel @Inject constructor(val authUseCase: AuthUseCase) : ViewModel() {

    // LiveData to indicate if the user has watched the onboarding screen
    val isUserWatchOnBoardingScreen = authUseCase.splashUseCase.isUserWatchOnBoardingScreen()
    val isUserVerified = authUseCase.splashUseCase.isUserVerified()
}