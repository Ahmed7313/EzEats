package com.easyEats.ezeats.data.remote

import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.domain.auth.model.request.SendOtpRequest
import com.easyEats.ezeats.domain.auth.model.request.VerifyOtpRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("SendOTP")
    suspend fun sendOTP(@Body request: SendOtpRequest): Response<BaseResponse<Int>>

    @POST("VerifyOTP")
    suspend fun verifyOtp(@Body request: VerifyOtpRequest): Response<BaseResponse<Boolean>>

}