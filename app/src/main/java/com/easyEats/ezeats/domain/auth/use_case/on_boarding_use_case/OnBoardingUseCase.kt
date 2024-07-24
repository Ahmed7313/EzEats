package com.easyEats.ezeats.domain.auth.use_case.on_boarding_use_case

import com.easyEats.ezeats.domain.auth.repository.AuthRepository
import javax.inject.Inject

class OnBoardingUseCase @Inject constructor(private val repository: AuthRepository) {

    suspend fun userWatchOnBoardingScreen(userWatchOnBoardingScreen: Boolean) {
        repository.userWatchOnBoardingScreen(userWatchOnBoardingScreen)
    }
}