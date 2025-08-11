package com.easyEats.ezeats.data.remote.auth

import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.domain.auth.model.request.FindUserRequest
import com.easyEats.ezeats.domain.auth.model.request.SendOtpRequest
import com.easyEats.ezeats.domain.auth.model.request.SocialRequest
import com.easyEats.ezeats.domain.auth.model.request.VerifyOtpRequest
import com.easyEats.ezeats.domain.auth.model.response.FindUserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("SendMobileOTP")
    suspend fun sendOTP(@Body request: SendOtpRequest): BaseResponse<Boolean>


    @POST("loginWithGoogle")
    suspend fun loginWithGoogle(@Body request: SocialRequest): BaseResponse<Boolean>


    @POST("VerifyMobileOTP")
    suspend fun verifyOtp(@Body request: VerifyOtpRequest): BaseResponse<Boolean>


    @POST("findUser")
    suspend fun findUser(@Body findUserRequest: FindUserRequest):
            Response<FindUserResponse>

}