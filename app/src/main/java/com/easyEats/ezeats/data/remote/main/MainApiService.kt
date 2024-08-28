package com.easyEats.ezeats.data.remote.main

import com.easyEats.ezeats.base.BaseResponse
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
import retrofit2.http.Body
import retrofit2.http.POST

interface MainApiService {

    @POST("homeScreen")
    suspend fun getHomeScreenDetails(@Body request: HomeRequest): BaseResponse<HomeResponse>

    @POST("searchSuggestions")
    suspend fun searchSuggestions(@Body request: SearchSuggestionsRequest): BaseResponse<List<String>>

    @POST("searchScreen")
    suspend fun searchScreen(): BaseResponse<SearchScreenResponse>

    @POST("getMobileLocations")
    suspend fun getLocations(): BaseResponse<List<Location>>

    @POST("clearRecentSearches")
    suspend fun clearRecentSearches(): BaseResponse<Any>

    @POST("searchMobile")
    suspend fun searchRestaurant(@Body searchRequest: SearchRequest): BaseResponse<List<Restaurant>>

    @POST("searchMobile")
    suspend fun searchOffer(@Body searchRequest: SearchRequest): BaseResponse<List<HotDeal>>

    @POST("getRestaurantsAndOffersByCategory")
    suspend fun getRestaurantsByCategory(@Body offersRequest: OffersRequest): BaseResponse<List<Restaurant>>

    @POST("getCategory")
    suspend fun getCategory(): BaseResponse<List<Category>>


}