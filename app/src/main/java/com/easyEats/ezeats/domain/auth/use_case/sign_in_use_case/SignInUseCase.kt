package com.easyEats.ezeats.domain.auth.use_case.sign_in_use_case

import com.easyEats.ezeats.domain.auth.model.request.FindUserRequest
import com.easyEats.ezeats.domain.auth.model.request.SendOtpRequest
import com.easyEats.ezeats.domain.auth.repository.AuthRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(private val repository: AuthRepository) {


    suspend fun saveToken(token: String) = repository.savToken(token)
    suspend fun saveMobileNumber(mobileNumber: String) = repository.saveMobileNumber(mobileNumber)
    suspend fun findUser(findUserRequest: FindUserRequest)=repository.findUser(findUserRequest)
    suspend fun sendOTP(request: SendOtpRequest) = repository.sendOTP(request)

}