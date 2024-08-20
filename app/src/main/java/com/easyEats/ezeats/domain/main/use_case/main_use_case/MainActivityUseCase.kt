package com.easyEats.ezeats.domain.main.use_case.main_use_case

import com.easyEats.ezeats.domain.main.model.UserLocationAddress
import com.easyEats.ezeats.domain.main.repository.MainRepository
import javax.inject.Inject

class MainActivityUseCase @Inject constructor(private val mainRepository: MainRepository) {
    suspend fun saveAddress(userLocationAddress: UserLocationAddress) =
        mainRepository.saveAddress(userLocationAddress)

     fun getCurrentAddress() = mainRepository.fetchAddress()
}