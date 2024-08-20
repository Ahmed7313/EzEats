package com.easyEats.ezeats.domain.main.use_case.search_use_case

import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.domain.main.model.request.search.SearchRequest
import com.easyEats.ezeats.domain.main.model.response.home_response.HotDeal
import com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.Restaurant
import com.easyEats.ezeats.domain.main.repository.MainRepository
import javax.inject.Inject

class SearchUseCase @Inject constructor(private val mainRepository: MainRepository) {
    suspend fun searchRestaurant(searchRequest: SearchRequest): BaseResponse<List<Restaurant>> {
        return mainRepository.searchRestaurant(searchRequest)
    }

    suspend fun searchOffer(searchRequest: SearchRequest): BaseResponse<List<HotDeal>> {
        return mainRepository.searchOffer(searchRequest)
    }
}