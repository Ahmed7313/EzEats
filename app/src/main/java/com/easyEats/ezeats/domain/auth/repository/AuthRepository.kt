package com.easyEats.ezeats.domain.auth.repository

import androidx.lifecycle.LiveData
import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.domain.auth.model.request.FindUserRequest
import com.easyEats.ezeats.domain.auth.model.request.SendOtpRequest
import com.easyEats.ezeats.domain.auth.model.request.SocialRequest
import com.easyEats.ezeats.domain.auth.model.request.VerifyOtpRequest
import com.easyEats.ezeats.domain.auth.model.response.FindUserResponse
import com.easyEats.ezeats.domain.main.model.UserLocationAddress
import com.easyEats.ezeats.utils.Resource
import com.parse.ParseUser
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.Body

interface AuthRepository {
    suspend fun userWatchOnBoardingScreen(userWatchOnBoardingScreen: Boolean)
    fun isUserWatchOnBoardingScreen(): Flow<Boolean>
    fun userHasToken(): Flow<String>
    fun isUserVerified(): Flow<Boolean>
    fun getToken(): Flow<String>
    fun register(phoneNumber: String, name: String): LiveData<Resource<ParseUser>>
    suspend fun savToken(toke: String)
    suspend fun setUserVerification(isUserVerified: Boolean)
    suspend fun saveAddress(userLocationAddress: UserLocationAddress)
    suspend fun saveMobileNumber(mobileNumber: String)
    suspend fun saveName(name: String)
    suspend fun sendOTP(request: SendOtpRequest): BaseResponse<Boolean>
    suspend fun verifyOtp(request: VerifyOtpRequest): BaseResponse<Boolean>
    suspend fun loginWithGoogle(request: SocialRequest): Response<FindUserResponse>

    suspend fun findUser( findUserRequest: FindUserRequest): Response<FindUserResponse>

}