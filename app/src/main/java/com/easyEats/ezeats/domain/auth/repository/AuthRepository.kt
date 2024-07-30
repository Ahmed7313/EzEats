package com.easyEats.ezeats.domain.auth.repository

import android.location.Address
import androidx.lifecycle.LiveData
import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.domain.auth.model.request.SendOtpRequest
import com.easyEats.ezeats.domain.auth.model.request.VerifyOtpRequest
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

    fun register(phoneNumber: String, name: String): LiveData<Resource<ParseUser>>

    fun login(phoneNumber: String): LiveData<Resource<ParseUser>>

    suspend fun savToken(toke: String)
    suspend fun setUserVerification(isUserVerified: Boolean)
    suspend fun saveAddress(address: String)
    suspend fun saveMobileNumber(mobileNumber: String)
    suspend fun saveName(name: String)

    suspend fun sendOTP(request: SendOtpRequest): Response<BaseResponse<Int>>

    suspend fun verifyOtp(request: VerifyOtpRequest): Response<BaseResponse<Boolean>>


}