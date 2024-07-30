package com.easyEats.ezeats.domain.auth.use_case.sign_up_use_case

import com.easyEats.ezeats.domain.auth.model.request.SendOtpRequest
import com.easyEats.ezeats.domain.auth.repository.AuthRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(private val repository: AuthRepository) {

    fun register(phoneNumber: String, name: String) =
        repository.register(phoneNumber, name)

    suspend fun sendOTP(request: SendOtpRequest) = repository.sendOTP(request)


    suspend fun saveToken(token: String) = repository.savToken(token)
    suspend fun saveMobileNumber(mobileNumber: String) = repository.saveMobileNumber(mobileNumber)
    suspend fun saveName(name: String) = repository.saveName(name)


}