package com.easyEats.ezeats.data.repository.main.repository

import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.data.local.data_store.DataStorePreference
import com.easyEats.ezeats.data.remote.main.MainApiService
import com.easyEats.ezeats.domain.main.model.UserLocationAddress
import com.easyEats.ezeats.domain.main.model.request.home.HomeRequest
import com.easyEats.ezeats.domain.main.model.request.search.SearchRequest
import com.easyEats.ezeats.domain.main.model.request.search.SearchSuggestionsRequest
import com.easyEats.ezeats.domain.main.model.response.home_response.HotDeal
import com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.Restaurant
import com.easyEats.ezeats.domain.main.repository.MainRepository
import javax.inject.Inject

class MainResponseImpl @Inject constructor(
    private val mainApiService: MainApiService,
    private val dataStorePreference: DataStorePreference,
) : MainRepository {
    override suspend fun getHomeDetails(request: HomeRequest) =
        mainApiService.getHomeScreenDetails(request)

    override suspend fun searchSuggestions(request: SearchSuggestionsRequest) =
        mainApiService.searchSuggestions(request)

    override suspend fun searchScreen() = mainApiService.searchScreen()
    override suspend fun getLocations() = mainApiService.getLocations()

    override suspend fun saveAddress(userLocationAddress: UserLocationAddress) {
        dataStorePreference.saveAddress(userLocationAddress)
    }

    override fun fetchAddress() = dataStorePreference.userLocationAddressFlow
    override suspend fun clearRecentSearches(): BaseResponse<Any> {
        return mainApiService.clearRecentSearches()
    }

    override suspend fun searchRestaurant(searchRequest: SearchRequest): BaseResponse<List<Restaurant>> {
        return mainApiService.searchRestaurant(searchRequest)
    }

    override suspend fun searchOffer(searchRequest: SearchRequest): BaseResponse<List<HotDeal>> {
        return mainApiService.searchOffer(searchRequest)
    }
}