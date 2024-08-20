package com.easyEats.ezeats.domain.main.use_case.home_use_case

import com.easyEats.ezeats.domain.main.model.UserLocationAddress
import com.easyEats.ezeats.domain.main.model.request.home.HomeRequest
import com.easyEats.ezeats.domain.main.repository.MainRepository
import javax.inject.Inject

class HomeUseCase @Inject constructor(private val mainRepository: MainRepository) {
    suspend fun getHomeDetails(request: HomeRequest) =
        mainRepository.getHomeDetails(request)

    suspend fun saveAddress(userLocationAddress: UserLocationAddress) =
        mainRepository.saveAddress(userLocationAddress)
}