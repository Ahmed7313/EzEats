package com.easyEats.ezeats.data.local.auth.repository

import androidx.lifecycle.LiveData
import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.data.local.data_store.DataStorePreference
import com.easyEats.ezeats.data.remote.ApiService
import com.easyEats.ezeats.data.remote.AuthWithParsing
import com.easyEats.ezeats.domain.auth.model.request.SendOtpRequest
import com.easyEats.ezeats.domain.auth.model.request.VerifyOtpRequest
import com.easyEats.ezeats.domain.auth.repository.AuthRepository
import com.easyEats.ezeats.utils.Resource
import com.parse.ParseUser
import retrofit2.Response
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
    private val dataStorePreference: DataStorePreference,
    private val authWithParsing: AuthWithParsing,
    private val apiService: ApiService
) : AuthRepository {

    override suspend fun userWatchOnBoardingScreen(userWatchOnBoardingScreen: Boolean) {
        dataStorePreference.userWatchOnBoardingScreen(userWatchOnBoardingScreen)
    }

    override fun isUserWatchOnBoardingScreen() = dataStorePreference.isUserWatchOnBoardingScreen()

    override fun register(phoneNumber: String, name: String): LiveData<Resource<ParseUser>> =
        authWithParsing.register(phoneNumber, name)

    override fun login(phoneNumber: String): LiveData<Resource<ParseUser>> =
        authWithParsing.login(phoneNumber)


    override suspend fun savToken(token: String) {
        dataStorePreference.saveToken(token)
    }

    override suspend fun sendOTP(request: SendOtpRequest) =
        apiService.sendOTP(request)

    override suspend fun verifyOtp(request: VerifyOtpRequest): Response<BaseResponse<Boolean>> =
        apiService.verifyOtp(request)
}