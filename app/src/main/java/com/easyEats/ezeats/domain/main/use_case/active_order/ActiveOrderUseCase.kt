package com.easyEats.ezeats.domain.main.use_case.active_order


import com.easyEats.ezeats.domain.main.model.request.complaint_request.ComplaintRequest
import com.easyEats.ezeats.domain.main.repository.MainRepository
import javax.inject.Inject

class ActiveOrderUseCase @Inject constructor(private val mainRepository: MainRepository) {

    suspend fun trackActiveOrder() =
        mainRepository.trackActiveOrder()

    suspend fun senSupportEmail(complaintRequest: ComplaintRequest) =
        mainRepository.sendSupportEmail(complaintRequest)
}