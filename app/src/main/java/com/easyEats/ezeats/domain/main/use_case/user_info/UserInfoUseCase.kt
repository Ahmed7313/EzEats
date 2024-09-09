package com.easyEats.ezeats.domain.main.use_case.user_info

import com.easyEats.ezeats.domain.main.model.request.edit_user_info.UserInfoRequest
import com.easyEats.ezeats.domain.main.repository.MainRepository
import javax.inject.Inject

class UserInfoUseCase @Inject constructor(val mainRepository: MainRepository) {

    suspend fun getUserInfo() = mainRepository.getUserInfo()
    suspend fun editUserInfo(userInfoRequest: UserInfoRequest) =
        mainRepository.editUserProfile(userInfoRequest)


    suspend fun deleteAccount() = mainRepository.deleteAccount()
    fun getToken() = mainRepository.getToken()

}