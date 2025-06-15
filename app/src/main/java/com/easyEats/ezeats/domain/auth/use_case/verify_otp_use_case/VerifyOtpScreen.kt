package com.easyEats.ezeats.domain.auth.use_case.verify_otp_use_case

import com.easyEats.ezeats.domain.auth.model.request.SendOtpRequest
import com.easyEats.ezeats.domain.auth.model.request.VerifyOtpRequest
import com.easyEats.ezeats.domain.auth.repository.AuthRepository
import javax.inject.Inject

class VerifyOtpScreen @Inject constructor(private val repository: AuthRepository) {

    suspend fun verifyOtp(request: VerifyOtpRequest) =
        repository.verifyOtp(request)

    suspend fun setUserVerification(isUserVerified: Boolean) {
        repository.setUserVerification(isUserVerified)
    }

}