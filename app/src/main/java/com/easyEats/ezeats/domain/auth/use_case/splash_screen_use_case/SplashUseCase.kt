package com.easyEats.ezeats.domain.auth.use_case.splash_screen_use_case

import com.easyEats.ezeats.domain.auth.repository.AuthRepository
import javax.inject.Inject

class SplashUseCase @Inject constructor(private val repository: AuthRepository) {

    fun isUserWatchOnBoardingScreen() = repository.isUserWatchOnBoardingScreen()

    fun isUserVerified() = repository.isUserVerified()

}