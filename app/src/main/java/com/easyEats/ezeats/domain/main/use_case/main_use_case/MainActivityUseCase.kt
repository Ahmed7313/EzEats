package com.easyEats.ezeats.domain.main.use_case.main_use_case

import com.easyEats.ezeats.domain.main.model.UserLocationAddress
import com.easyEats.ezeats.domain.main.model.response.compound.CompoundResult
import com.easyEats.ezeats.domain.main.repository.MainRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainActivityUseCase @Inject constructor(private val mainRepository: MainRepository) {
    suspend fun saveAddress(userLocationAddress: UserLocationAddress) =
        mainRepository.saveAddress(userLocationAddress)

    fun getCurrentAddress() = mainRepository.fetchAddress()
    suspend fun saveToken(token: String) =
        mainRepository.saveToken(token)

    suspend fun setUserVerification(isUserVerified: Boolean) {
        mainRepository.setUserVerification(isUserVerified)
    }

    suspend fun saveCompound(isUserVerified: List<CompoundResult>) {
        mainRepository.saveCompound(isUserVerified)
    }

    fun getCompound() = mainRepository.getCompound()



    suspend fun isUserVerified(): Flow<Boolean> {
        return mainRepository.isUserVerified()
    }

}