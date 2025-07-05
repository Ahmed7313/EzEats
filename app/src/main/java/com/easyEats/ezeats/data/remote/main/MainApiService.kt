package com.easyEats.ezeats.data.remote.main

import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.domain.main.model.request.cart.TipRequest
import com.easyEats.ezeats.domain.main.model.request.cart.UpdateCartRequest
import com.easyEats.ezeats.domain.main.model.request.cart.response.UpdateCartResponse
import com.easyEats.ezeats.domain.main.model.request.cart.tip.TipResponse
import com.easyEats.ezeats.domain.main.model.request.complaint_request.ComplaintRequest
import com.easyEats.ezeats.domain.main.model.request.edit_user_info.UserInfoRequest
import com.easyEats.ezeats.domain.main.model.request.firebase_token.UpdateTokeRequest
import com.easyEats.ezeats.domain.main.model.request.home.HomeRequest
import com.easyEats.ezeats.domain.main.model.request.offer_request.OfferRequest
import com.easyEats.ezeats.domain.main.model.request.offers.OffersRequest
import com.easyEats.ezeats.domain.main.model.request.order_history.OrderHistoryRequest
import com.easyEats.ezeats.domain.main.model.request.restaurant.CheckTableAvailabilityRequest
import com.easyEats.ezeats.domain.main.model.request.restaurant.RestaurantMenuRequest
import com.easyEats.ezeats.domain.main.model.request.restaurant.RestaurantRequest
import com.easyEats.ezeats.domain.main.model.request.search.SearchRequest
import com.easyEats.ezeats.domain.main.model.request.search.SearchSuggestionsRequest
import com.easyEats.ezeats.domain.main.model.response.active_order.ActiveOrderResponse
import com.easyEats.ezeats.domain.main.model.response.compound.CompoundResponse
import com.easyEats.ezeats.domain.main.model.response.home_response.HomeResponse
import com.easyEats.ezeats.domain.main.model.response.home_response.HotDeal
import com.easyEats.ezeats.domain.main.model.response.location_response.Location
import com.easyEats.ezeats.domain.main.model.response.loyality_center.LoyaltyCenterResponse
import com.easyEats.ezeats.domain.main.model.response.order_history.OrderHistoryResponse
import com.easyEats.ezeats.domain.main.model.response.partners.PartnersResponse
import com.easyEats.ezeats.domain.main.model.response.restaurant_menu.RestaurantMenuResponse
import com.easyEats.ezeats.domain.main.model.response.restaurant_menu.check_table_availability.TableAvailabilityResponse
import com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.Restaurant
import com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.RestaurantInfo
import com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.new_restaurants_reponse.RestaurantsResponseItem
import com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.result.RestaurantListResultResponse
import com.easyEats.ezeats.domain.main.model.response.search_response.Category
import com.easyEats.ezeats.domain.main.model.response.search_response.SearchScreenResponse
import com.easyEats.ezeats.domain.main.model.response.user_info.UserInfoResponse
import com.easyEats.ezeats.domain.main.model.response.voucher.VoucherResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body
import retrofit2.http.POST

interface MainApiService {

    @POST("homeScreen")
    suspend fun getHomeScreenDetails(@Body request: HomeRequest): BaseResponse<HomeResponse>

    @POST("getAllCompoundData")
    suspend fun getAllCompoundData(): CompoundResponse



    @POST("searchSuggestions")
    suspend fun searchSuggestions(@Body request: SearchSuggestionsRequest): BaseResponse<List<String>>

    @POST("searchScreen")
    suspend fun searchScreen(): BaseResponse<SearchScreenResponse>

    @POST("getMobileLocations")
    suspend fun getLocations(): BaseResponse<List<Location>>

    @POST("clearRecentSearches")
    suspend fun clearRecentSearches(): BaseResponse<Any>

    @POST("searchMobile")
    suspend fun searchRestaurant(@Body searchRequest: SearchRequest): BaseResponse<List<RestaurantsResponseItem>>

    @POST("searchMobile")
    suspend fun searchOffer(@Body searchRequest: SearchRequest): BaseResponse<List<HotDeal>>

    @POST("getRestaurantsAndOffersByCategory")
    suspend fun getRestaurantsByCategory(@Body offersRequest: OffersRequest): BaseResponse<List<Restaurant>>

    @POST("getCategory")
    suspend fun getCategory(): BaseResponse<List<Category>>

    @POST("getRestaurants")
    suspend fun getOffersByRestaurantId(@Body restaurantRequest: RestaurantRequest): BaseResponse<RestaurantInfo>

    @POST("getRestaurantMenu")
    suspend fun getRestaurantMenu(@Body restaurantMenuRequest: RestaurantMenuRequest): BaseResponse<RestaurantMenuResponse>


    @POST("checkTableAvailability")
    suspend fun checkTableAvailability(@Body restaurantMenuRequest: CheckTableAvailabilityRequest): TableAvailabilityResponse



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

    @POST("loyaltyCenter")
    suspend fun getUserPoints(): BaseResponse<LoyaltyCenterResponse>

    @POST("getUserRewardsMobile")
    suspend fun getUserRewardsMobile(): BaseResponse<VoucherResponse>

    @POST("trackActiveOrder")
    suspend fun trackActiveOrder(): BaseResponse<ActiveOrderResponse>

    @POST("sendSupportEmail")
    suspend fun sendSupportEmail(@Body complaintRequest: ComplaintRequest): BaseResponse<Any>

    @POST("getEntities")
    suspend fun getPartners(): BaseResponse<PartnersResponse>

    @POST("updateDeviceToken")
    suspend fun updateDeviceToken(@Body updateTokeRequest: UpdateTokeRequest): BaseResponse<String>

    @POST("clearDeviceToken")
    suspend fun clearDeviceToken(): BaseResponse<Any>

    @POST("getRestaurants")
    suspend fun getRestaurants(@Body restaurantRequest: com.easyEats.ezeats.domain.main.model.request.RestaurantRequest): RestaurantListResultResponse

    @POST("updateCart")
    suspend fun updateCart(@Body restaurantRequest: UpdateCartRequest): UpdateCartResponse


    @POST("saveTip")
    suspend fun saveTip(@Body restaurantRequest: TipRequest): TipResponse


}