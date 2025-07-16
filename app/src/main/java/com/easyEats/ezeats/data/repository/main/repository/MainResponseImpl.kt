package com.easyEats.ezeats.data.repository.main.repository

import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.data.local.data_store.DataStorePreference
import com.easyEats.ezeats.data.remote.main.MainApiService
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
import com.easyEats.ezeats.domain.main.model.response.compound.CompoundResult
import com.easyEats.ezeats.domain.main.model.response.home_response.HotDeal
import com.easyEats.ezeats.domain.main.model.response.loyality_center.LoyaltyCenterResponse
import com.easyEats.ezeats.domain.main.model.response.order_history.OrderHistoryResponse
import com.easyEats.ezeats.domain.main.model.response.partners.PartnersResponse
import com.easyEats.ezeats.domain.main.model.response.payment.PaymentRequest
import com.easyEats.ezeats.domain.main.model.response.restaurant_menu.RestaurantMenuResponse
import com.easyEats.ezeats.domain.main.model.response.restaurant_menu.check_table_availability.TableAvailabilityResponse
import com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.Restaurant
import com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.RestaurantInfo
import com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.new_restaurants_reponse.RestaurantsResponseItem
import com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.result.RestaurantListResultResponse
import com.easyEats.ezeats.domain.main.model.response.search_response.Category
import com.easyEats.ezeats.domain.main.model.response.user_info.UserInfoResponse
import com.easyEats.ezeats.domain.main.model.response.voucher.VoucherResponse
import com.easyEats.ezeats.domain.main.repository.MainRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainResponseImpl @Inject constructor(
    private val mainApiService: MainApiService,
    private val dataStorePreference: DataStorePreference,
) : MainRepository {
    override suspend fun getHomeDetails(request: HomeRequest) =
        mainApiService.getHomeScreenDetails(request)

    override suspend fun getAllCompoundData() =
        mainApiService.getAllCompoundData()

    override suspend fun searchSuggestions(request: SearchSuggestionsRequest) =
        mainApiService.searchSuggestions(request)

    override suspend fun searchScreen() = mainApiService.searchScreen()
    override suspend fun getLocations() = mainApiService.getLocations()

    override suspend fun saveAddress(userLocationAddress: UserLocationAddress) {
        dataStorePreference.saveAddress(userLocationAddress)
    }

    override fun fetchAddress() = dataStorePreference.userLocationAddressFlow
    override fun getCompound() = dataStorePreference.getCompound
    override suspend fun clearRecentSearches(): BaseResponse<Any> {
        return mainApiService.clearRecentSearches()
    }

    override suspend fun searchRestaurant(searchRequest: SearchRequest): BaseResponse<List<RestaurantsResponseItem>> {
        return mainApiService.searchRestaurant(searchRequest)
    }

    override suspend fun getRestaurantsByCategory(offersRequest: OffersRequest): BaseResponse<List<Restaurant>> {
        return mainApiService.getRestaurantsByCategory(offersRequest)
    }

    override suspend fun getCategory(): BaseResponse<List<Category>> {
        return mainApiService.getCategory()
    }

    override suspend fun searchOffer(searchRequest: SearchRequest): BaseResponse<List<HotDeal>> {
        return mainApiService.searchOffer(searchRequest)
    }

    override suspend fun getOffersByRestaurantId(restaurantRequest: RestaurantRequest): BaseResponse<RestaurantInfo> {
        return mainApiService.getOffersByRestaurantId(restaurantRequest)
    }

    override suspend fun getRestaurantMenu(restaurantMenuRequest: RestaurantMenuRequest):BaseResponse<RestaurantMenuResponse> {
        return mainApiService.getRestaurantMenu(restaurantMenuRequest)
    }

    override suspend fun checkTableAvailability(restaurantMenuRequest: CheckTableAvailabilityRequest): TableAvailabilityResponse {
        return mainApiService.checkTableAvailability(restaurantMenuRequest)
    }

    override suspend fun getOfferById(offerRequest: OfferRequest): BaseResponse<HotDeal> {
        return mainApiService.getOfferById(offerRequest)
    }

    override suspend fun getUserInfo(): BaseResponse<UserInfoResponse> {
        return mainApiService.getUserInfo()
    }

    override suspend fun editUserProfile(userInfoRequest: UserInfoRequest): BaseResponse<Any> {
        return mainApiService.editUserProfile(userInfoRequest)
    }

    override fun isUserVerified(): Flow<Boolean> {
        return dataStorePreference.isUserVerified()
    }

    override suspend fun saveToken(token: String) {
        return dataStorePreference.saveToken(token)
    }

    override suspend fun setUserVerification(isUserVerified: Boolean) {
        return dataStorePreference.setUserVerification(isUserVerified)
    }

    override suspend fun saveCompound(isUserVerified: List<CompoundResult>) {
        return dataStorePreference.saveCompound(isUserVerified)
    }


    override suspend fun deleteAccount(): BaseResponse<Any> {
        return mainApiService.deleteAccount()
    }

    override suspend fun getUserOrderHistory(orderHistoryRequest: OrderHistoryRequest): BaseResponse<OrderHistoryResponse> {
        return mainApiService.getUserOrderHistory(orderHistoryRequest)
    }

    override suspend fun getUserPoints(): BaseResponse<LoyaltyCenterResponse> {
        return mainApiService.getUserPoints()
    }

    override fun getToken() = dataStorePreference.fetchToken()
    override suspend fun getUserRewardsMobile(): BaseResponse<VoucherResponse> {
        return mainApiService.getUserRewardsMobile()
    }

    override suspend fun trackActiveOrder(): BaseResponse<ActiveOrderResponse> {
        return mainApiService.trackActiveOrder()
    }

    override suspend fun sendSupportEmail(complaintRequest: ComplaintRequest): BaseResponse<Any> {
        return mainApiService.sendSupportEmail(complaintRequest)
    }

    override suspend fun getPartners(): BaseResponse<PartnersResponse> {
        return mainApiService.getPartners()
    }

    override suspend fun updateDeviceToken(updateTokeRequest: UpdateTokeRequest): BaseResponse<String> {
        return mainApiService.updateDeviceToken(updateTokeRequest)
    }

    override suspend fun clearDeviceToken(): BaseResponse<Any> {
        return mainApiService.clearDeviceToken()
    }

    override suspend fun saveUserRating(isUserViewedRatingDialog: Boolean) {
        dataStorePreference.saveUserRating(isUserViewedRatingDialog)
    }

    override fun checkIfUserViewedRatingDialog(): Flow<Boolean> =
        dataStorePreference.checkIfUserViewedRatingDialog()

    override suspend fun getRestaurants(restaurantRequest: com.easyEats.ezeats.domain.main.model.request.RestaurantRequest): RestaurantListResultResponse {
        return mainApiService.getRestaurants(restaurantRequest)
    }

    override suspend fun updateCart(restaurantRequest: UpdateCartRequest): UpdateCartResponse =
        mainApiService.updateCart(restaurantRequest)


    override suspend fun saveTip(restaurantRequest: TipRequest): TipResponse =
        mainApiService.saveTip(restaurantRequest)
    override suspend fun addressList(request: AddressListRequest): AddressListResponse =
        mainApiService.addressList(request)

    override suspend fun addAddress(request: AddAddressRequest): AddAddressResponse =
        mainApiService.addAddress(request)

    override suspend fun saveAddress(request: SaveAddressRequest): BaseResponse<Any> =
        mainApiService.saveAddress(request)


    override suspend fun savePickupStation(request: SavePickupRequest): BaseResponse<Any> =
        mainApiService.savePickUpStation(request)


    override suspend fun payment(request: PaymentRequest): BaseResponse<Any> =
        mainApiService.kashierPayment(request)


}