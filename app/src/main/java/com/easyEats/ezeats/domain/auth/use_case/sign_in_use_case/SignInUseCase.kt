package com.easyEats.ezeats.domain.auth.use_case.sign_in_use_case

import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.domain.auth.model.request.SendOtpRequest
import com.easyEats.ezeats.domain.auth.repository.AuthRepository
import com.easyEats.ezeats.utils.Resource
import retrofit2.http.Body
import javax.inject.Inject

class SignInUseCase @Inject constructor(private val repository: AuthRepository) {

    fun login(phoneNumber: String) =
        repository.login(phoneNumber)


    suspend fun saveToken(token: String) = repository.savToken(token)

    suspend fun sendOTP(request: SendOtpRequest) =repository.sendOTP(request)

}