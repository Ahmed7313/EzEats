package com.easyEats.ezeats.domain.main.use_case.restaurant_use_case

import com.easyEats.ezeats.domain.main.model.request.restaurant.RestaurantMenuRequest
import com.easyEats.ezeats.domain.main.model.request.restaurant.RestaurantRequest
import com.easyEats.ezeats.domain.main.repository.MainRepository
import javax.inject.Inject

class RestaurantUseCase @Inject constructor(private val mainRepository: MainRepository) {
    suspend fun getOffersByRestaurantId(restaurantRequest: RestaurantRequest) =
        mainRepository.getOffersByRestaurantId(restaurantRequest)

    suspend fun getRestaurantMenu(restaurantMenuRequest: RestaurantMenuRequest) =
        mainRepository.getRestaurantMenu(restaurantMenuRequest)
}