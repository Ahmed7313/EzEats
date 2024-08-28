package com.easyEats.ezeats.domain.main.use_case.offers_use_case

import com.easyEats.ezeats.domain.main.model.request.offers.OffersRequest
import com.easyEats.ezeats.domain.main.repository.MainRepository
import javax.inject.Inject

class OffersUserCase @Inject constructor(private val mainRepository: MainRepository) {

    suspend fun getRestaurantsByCategory(offersRequest: OffersRequest) =
        mainRepository.getRestaurantsByCategory(offersRequest)

    suspend fun getCategory() = mainRepository.getCategory()
}