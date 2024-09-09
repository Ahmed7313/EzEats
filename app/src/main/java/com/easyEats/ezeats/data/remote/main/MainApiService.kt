package com.easyEats.ezeats.data.remote.main

import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.domain.main.model.request.edit_user_info.UserInfoRequest
import com.easyEats.ezeats.domain.main.model.request.home.HomeRequest
import com.easyEats.ezeats.domain.main.model.request.offer_request.OfferRequest
import com.easyEats.ezeats.domain.main.model.request.offers.OffersRequest
import com.easyEats.ezeats.domain.main.model.request.order_history.OrderHistoryRequest
import com.easyEats.ezeats.domain.main.model.request.points.PointsRequest
import com.easyEats.ezeats.domain.main.model.request.restaurant.RestaurantRequest
import com.easyEats.ezeats.domain.main.model.request.search.SearchRequest
import com.easyEats.ezeats.domain.main.model.request.search.SearchSuggestionsRequest
import com.easyEats.ezeats.domain.main.model.response.home_response.HomeResponse
import com.easyEats.ezeats.domain.main.model.response.home_response.HotDeal
import com.easyEats.ezeats.domain.main.model.response.location_response.Location
import com.easyEats.ezeats.domain.main.model.response.order_history.OrderHistoryResponse
import com.easyEats.ezeats.domain.main.model.response.points.PointsResponse
import com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.Offer
import com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.Restaurant
import com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.RestaurantInfo
import com.easyEats.ezeats.domain.main.model.response.search_response.Category
import com.easyEats.ezeats.domain.main.model.response.search_response.SearchScreenResponse
import com.easyEats.ezeats.domain.main.model.response.user_info.UserInfoResponse
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

    @POST("getOffersByRestaurantId")
    suspend fun getOffersByRestaurantId(@Body restaurantRequest: RestaurantRequest): BaseResponse<RestaurantInfo>

    @POST("getOfferById")
    suspend fun getOfferById(@Body offerRequest: OfferRequest): BaseResponse<HotDeal>

    @POST("getUserProfile")
    suspend fun getUserInfo(): BaseResponse<UserInfoResponse>

    @POST("editUserProfile")
    suspend fun editUserProfile(@Body userInfoRequest: UserInfoRequest): BaseResponse<Any>

    @POST("softDeleteUser")
    suspend fun deleteAccount(): BaseResponse<Any>

    @POST("getUserOrderHistory")
    suspend fun getUserOrderHistory(@Body orderHistoryRequest: OrderHistoryRequest): BaseResponse<OrderHistoryResponse>


    @POST("GetUserPoints")
    suspend fun getUserPoints(@Body pointsRequest: PointsRequest): BaseResponse<PointsResponse>
}