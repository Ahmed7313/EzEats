package com.easyEats.ezeats.domain.auth.use_case

import com.easyEats.ezeats.domain.auth.model.request.VerifyOtpRequest
import com.easyEats.ezeats.domain.auth.repository.AuthRepository
import com.easyEats.ezeats.domain.auth.use_case.on_boarding_use_case.OnBoardingUseCase
import com.easyEats.ezeats.domain.auth.use_case.sign_in_use_case.SignInUseCase
import com.easyEats.ezeats.domain.auth.use_case.sign_up_use_case.SignUpUseCase
import com.easyEats.ezeats.domain.auth.use_case.splash_screen_use_case.SplashUseCase
import com.easyEats.ezeats.domain.auth.use_case.verify_otp_use_case.VerifyOtpScreen
import javax.inject.Inject

data class AuthUseCase(
    val splashUseCase: SplashUseCase,
    val onBoardingUseCase: OnBoardingUseCase,
    val signUpUseCase: SignUpUseCase,
    val signInUseCase: SignInUseCase,
    val verifyUseCase: VerifyOtpScreen
)