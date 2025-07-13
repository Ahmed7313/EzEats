package com.easyEats.ezeats.domain.main.repository

import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.domain.main.model.UserLocationAddress
import com.easyEats.ezeats.domain.main.model.request.address.AddressListRequest
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
import com.easyEats.ezeats.domain.main.model.request.order.SavePickupRequest
import com.easyEats.ezeats.domain.main.model.request.order_history.OrderHistoryRequest
import com.easyEats.ezeats.domain.main.model.request.restaurant.CheckTableAvailabilityRequest
import com.easyEats.ezeats.domain.main.model.request.restaurant.RestaurantMenuRequest
import com.easyEats.ezeats.domain.main.model.request.restaurant.RestaurantRequest
import com.easyEats.ezeats.domain.main.model.request.search.SearchRequest
import com.easyEats.ezeats.domain.main.model.request.search.SearchSuggestionsRequest
import com.easyEats.ezeats.domain.main.model.response.active_order.ActiveOrderResponse
import com.easyEats.ezeats.domain.main.model.response.address.add.AddAddressRequest
import com.easyEats.ezeats.domain.main.model.response.address.add.AddAddressResponse
import com.easyEats.ezeats.domain.main.model.response.address.add.SaveAddressRequest
import com.easyEats.ezeats.domain.main.model.response.address.list.AddressListResponse
import com.easyEats.ezeats.domain.main.model.response.compound.CompoundResponse
import com.easyEats.ezeats.domain.main.model.response.compound.CompoundResult
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

interface MainRepository {

    suspend fun getHomeDetails(request: HomeRequest): BaseResponse<HomeResponse>


    suspend fun searchSuggestions(request: SearchSuggestionsRequest): BaseResponse<List<String>>
    suspend fun searchScreen(): BaseResponse<SearchScreenResponse>

    suspend fun getLocations(): BaseResponse<List<Location>>

    suspend fun saveAddress(userLocationAddress: UserLocationAddress)

    fun fetchAddress(): Flow<UserLocationAddress?>

    fun getCompound() : Flow<List<CompoundResult>?>

    suspend fun clearRecentSearches(): BaseResponse<Any>

    suspend fun searchRestaurant(searchRequest: SearchRequest): BaseResponse<List<RestaurantsResponseItem>>
    suspend fun getAllCompoundData():CompoundResponse

    suspend fun getRestaurantsByCategory(offersRequest: OffersRequest): BaseResponse<List<Restaurant>>
    suspend fun getCategory(): BaseResponse<List<Category>>
    suspend fun searchOffer(searchRequest: SearchRequest): BaseResponse<List<HotDeal>>

    suspend fun getOffersByRestaurantId(restaurantRequest: RestaurantRequest): BaseResponse<RestaurantInfo>
    suspend fun getRestaurantMenu(restaurantMenuRequest: RestaurantMenuRequest): BaseResponse<RestaurantMenuResponse>
    suspend fun checkTableAvailability(restaurantMenuRequest: CheckTableAvailabilityRequest): TableAvailabilityResponse



    suspend fun getOfferById(offerRequest: OfferRequest): BaseResponse<HotDeal>

    suspend fun getUserInfo(): BaseResponse<UserInfoResponse>

    suspend fun editUserProfile(userInfoRequest: UserInfoRequest): BaseResponse<Any>

    fun isUserVerified(): Flow<Boolean>

    suspend fun saveToken(token: String)

    suspend fun setUserVerification(isUserVerified: Boolean)
    suspend fun saveCompound(isUserVerified: List<CompoundResult>)



    suspend fun deleteAccount(): BaseResponse<Any>

    suspend fun getUserOrderHistory(orderHistoryRequest: OrderHistoryRequest): BaseResponse<OrderHistoryResponse>

    suspend fun getUserPoints(): BaseResponse<LoyaltyCenterResponse>

    fun getToken(): Flow<String>

    suspend fun getUserRewardsMobile(): BaseResponse<VoucherResponse>

    suspend fun trackActiveOrder(): BaseResponse<ActiveOrderResponse>

    suspend fun sendSupportEmail(complaintRequest: ComplaintRequest): BaseResponse<Any>

    suspend fun getPartners(): BaseResponse<PartnersResponse>

    suspend fun updateDeviceToken(updateTokeRequest: UpdateTokeRequest): BaseResponse<String>

    suspend fun clearDeviceToken(): BaseResponse<Any>

    suspend fun saveUserRating(isUserViewedRatingDialog: Boolean)

    fun checkIfUserViewedRatingDialog(): Flow<Boolean>

    suspend fun getRestaurants(restaurantRequest: com.easyEats.ezeats.domain.main.model.request.RestaurantRequest): RestaurantListResultResponse

    suspend fun updateCart(restaurantRequest: UpdateCartRequest): UpdateCartResponse
    suspend fun saveTip(restaurantRequest: TipRequest): TipResponse
    suspend fun addressList(request: AddressListRequest): AddressListResponse
    suspend fun addAddress(request: AddAddressRequest): AddAddressResponse
    suspend fun saveAddress(request: SaveAddressRequest): BaseResponse<Any>
    suspend fun savePickupStation(request: SavePickupRequest): BaseResponse<Any>



}