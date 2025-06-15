package com.easyEats.ezeats.domain.main.use_case.order_history_use_case

import com.easyEats.ezeats.domain.main.model.request.order_history.OrderHistoryRequest
import com.easyEats.ezeats.domain.main.repository.MainRepository
import javax.inject.Inject

class OrderHistoryUseCase @Inject constructor(val mainRepository: MainRepository) {

    suspend fun getUserOrderHistory(orderHistoryRequest: OrderHistoryRequest) =
        mainRepository.getUserOrderHistory(orderHistoryRequest)
}