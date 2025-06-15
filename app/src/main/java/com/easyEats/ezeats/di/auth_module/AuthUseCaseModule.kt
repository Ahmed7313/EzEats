package com.easyEats.ezeats.di.auth_module

import com.easyEats.ezeats.domain.auth.use_case.AuthUseCase
import com.easyEats.ezeats.domain.auth.use_case.on_boarding_use_case.OnBoardingUseCase
import com.easyEats.ezeats.domain.auth.use_case.sign_in_use_case.SignInUseCase
import com.easyEats.ezeats.domain.auth.use_case.sign_up_use_case.SignUpUseCase
import com.easyEats.ezeats.domain.auth.use_case.splash_screen_use_case.SplashUseCase
import com.easyEats.ezeats.domain.auth.use_case.verify_otp_use_case.VerifyOtpScreen
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object UseCaseModule {

    @Provides
    @Singleton
    fun provideAuthUseCase(
        splashUseCase: SplashUseCase,
        onBoardingUseCase: OnBoardingUseCase,
        signUpUseCase: SignUpUseCase,
        signInUseCase: SignInUseCase,
        verifyOtpScreen: VerifyOtpScreen
    ): AuthUseCase {
        return AuthUseCase(
            splashUseCase,
            onBoardingUseCase,
            signUpUseCase,
            signInUseCase,
            verifyOtpScreen
        )
    }
}