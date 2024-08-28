package com.easyEats.ezeats.domain.main.repository

import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.domain.main.model.UserLocationAddress
import com.easyEats.ezeats.domain.main.model.request.home.HomeRequest
import com.easyEats.ezeats.domain.main.model.request.offers.OffersRequest
import com.easyEats.ezeats.domain.main.model.request.search.SearchRequest
import com.easyEats.ezeats.domain.main.model.request.search.SearchSuggestionsRequest
import com.easyEats.ezeats.domain.main.model.response.home_response.HomeResponse
import com.easyEats.ezeats.domain.main.model.response.home_response.HotDeal
import com.easyEats.ezeats.domain.main.model.response.location_response.Location
import com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.Restaurant
import com.easyEats.ezeats.domain.main.model.response.search_response.Category
import com.easyEats.ezeats.domain.main.model.response.search_response.SearchScreenResponse
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    suspend fun getHomeDetails(request: HomeRequest): BaseResponse<HomeResponse>
    suspend fun searchSuggestions(request: SearchSuggestionsRequest): BaseResponse<List<String>>
    suspend fun searchScreen(): BaseResponse<SearchScreenResponse>

    suspend fun getLocations(): BaseResponse<List<Location>>

    suspend fun saveAddress(userLocationAddress: UserLocationAddress)

    fun fetchAddress(): Flow<UserLocationAddress?>

    suspend fun clearRecentSearches(): BaseResponse<Any>

    suspend fun searchRestaurant(searchRequest: SearchRequest): BaseResponse<List<Restaurant>>
    suspend fun getRestaurantsByCategory(offersRequest: OffersRequest): BaseResponse<List<Restaurant>>
    suspend fun getCategory(): BaseResponse<List<Category>>
    suspend fun searchOffer(searchRequest: SearchRequest): BaseResponse<List<HotDeal>>

}