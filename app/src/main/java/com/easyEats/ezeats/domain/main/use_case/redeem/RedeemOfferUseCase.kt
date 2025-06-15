package com.easyEats.ezeats.domain.main.use_case.redeem

import com.easyEats.ezeats.domain.main.model.request.offer_request.OfferRequest
import com.easyEats.ezeats.domain.main.repository.MainRepository
import javax.inject.Inject

class RedeemOfferUseCase @Inject constructor(val mainRepository: MainRepository) {

    suspend fun getOfferById(offerRequest: OfferRequest) =
        mainRepository.getOfferById(offerRequest)

}