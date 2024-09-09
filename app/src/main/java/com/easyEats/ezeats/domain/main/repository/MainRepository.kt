package com.easyEats.ezeats.domain.main.repository

import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.domain.main.model.UserLocationAddress
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
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body

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

    suspend fun getOffersByRestaurantId(restaurantRequest: RestaurantRequest): BaseResponse<RestaurantInfo>

    suspend fun getOfferById(offerRequest: OfferRequest): BaseResponse<HotDeal>

    suspend fun getUserInfo(): BaseResponse<UserInfoResponse>

    suspend fun editUserProfile(userInfoRequest: UserInfoRequest): BaseResponse<Any>

    fun isUserVerified(): Flow<Boolean>

    suspend fun saveToken(token: String)

    suspend fun setUserVerification(isUserVerified: Boolean)

    suspend fun deleteAccount(): BaseResponse<Any>

    suspend fun getUserOrderHistory(orderHistoryRequest: OrderHistoryRequest): BaseResponse<OrderHistoryResponse>

    suspend fun getUserPoints(pointsRequest: PointsRequest): BaseResponse<PointsResponse>

    fun getToken(): Flow<String>

}