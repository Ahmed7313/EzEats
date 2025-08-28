package com.easyEats.ezeats.data.repository.auth.repository

import androidx.lifecycle.LiveData
import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.data.local.data_store.DataStorePreference
import com.easyEats.ezeats.data.remote.auth.AuthApiService
import com.easyEats.ezeats.data.remote.auth.AuthWithParsing
import com.easyEats.ezeats.domain.auth.model.request.FindUserRequest
import com.easyEats.ezeats.domain.auth.model.request.SendOtpRequest
import com.easyEats.ezeats.domain.auth.model.request.SocialRequest
import com.easyEats.ezeats.domain.auth.model.request.VerifyOtpRequest
import com.easyEats.ezeats.domain.auth.model.response.FindUserResponse
import com.easyEats.ezeats.domain.auth.repository.AuthRepository
import com.easyEats.ezeats.domain.main.model.UserLocationAddress
import com.easyEats.ezeats.utils.Resource
import com.parse.ParseUser
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
    private val dataStorePreference: DataStorePreference,
    private val authWithParsing: AuthWithParsing,
    private val authApiService: AuthApiService
) : AuthRepository {

    override suspend fun userWatchOnBoardingScreen(userWatchOnBoardingScreen: Boolean) {
        dataStorePreference.userWatchOnBoardingScreen(userWatchOnBoardingScreen)
    }

    override fun isUserWatchOnBoardingScreen() = dataStorePreference.isUserWatchOnBoardingScreen()
    override fun userHasToken() = dataStorePreference.fetchToken()
    override fun isUserVerified(): Flow<Boolean> {
        return dataStorePreference.isUserVerified()
    }

    override fun getToken() = dataStorePreference.fetchToken()

    override fun register(phoneNumber: String, name: String): LiveData<Resource<ParseUser>> =
        authWithParsing.register(phoneNumber, name)



    override suspend fun savToken(token: String) {
        dataStorePreference.saveToken(token)
    }

    override suspend fun setUserVerification(isUserVerified: Boolean) {
        dataStorePreference.setUserVerification(isUserVerified)
    }

    override suspend fun saveAddress(userLocationAddress: UserLocationAddress) {
        dataStorePreference.saveAddress(userLocationAddress)
    }

    override suspend fun saveMobileNumber(mobileNumber: String) {
        dataStorePreference.saveMobileNumber(mobileNumber)
    }

    override suspend fun saveName(name: String) {
        dataStorePreference.saveName(name)
    }

    override suspend fun sendOTP(request: SendOtpRequest) =
        authApiService.sendOTP(request)

    override suspend fun verifyOtp(request: VerifyOtpRequest): BaseResponse<Boolean> =
        authApiService.verifyOtp(request)


    override suspend fun loginWithGoogle(request: SocialRequest): Response<FindUserResponse> {
        return authApiService.loginWithGoogle(request)
    }



    override suspend fun findUser(findUserRequest: FindUserRequest): Response<FindUserResponse> {
        return authApiService.findUser(findUserRequest)
    }
}