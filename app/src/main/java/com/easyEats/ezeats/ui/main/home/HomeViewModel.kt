package com.easyEats.ezeats.ui.main.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.easyEats.ezeats.MyApplication.Companion.baseApplication
import com.easyEats.ezeats.R
import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.domain.main.model.UserLocationAddress
import com.easyEats.ezeats.domain.main.model.request.RestaurantRequest
import com.easyEats.ezeats.domain.main.model.request.firebase_token.UpdateTokeRequest
import com.easyEats.ezeats.domain.main.model.request.home.HomeRequest
import com.easyEats.ezeats.domain.main.model.response.home_response.ActiveOrder
import com.easyEats.ezeats.domain.main.model.response.home_response.FeaturedOffer
import com.easyEats.ezeats.domain.main.model.response.home_response.HomeResponse
import com.easyEats.ezeats.domain.main.model.response.home_response.HotDeal
import com.easyEats.ezeats.domain.main.model.response.location_response.Location
import com.easyEats.ezeats.domain.main.model.response.partners.PartnersResponse
import com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.new_restaurants_reponse.RestaurantsResponseItem
import com.easyEats.ezeats.domain.main.use_case.MainUseCase
import com.easyEats.ezeats.ui.main.main.MainViewModel
import com.easyEats.ezeats.utils.Resource
import com.easyEats.ezeats.utils.location.LocationUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * ViewModel for managing home screen data and location-related operations.
 * It retrieves home screen content, including featured offers, hot deals, active orders, and location data.
 * It also handles location permissions and geolocation operations using the `LocationUtil`.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(private val mainUseCase: MainUseCase) :
    MainViewModel(mainUseCase) {

    // LiveData for holding the response of the home screen data request
    private val _homeResultMutableLiveData = MutableLiveData<Resource<BaseResponse<HomeResponse>>>()
    val homeResultLiveData: LiveData<Resource<BaseResponse<HomeResponse>>> get() = _homeResultMutableLiveData

    // LiveData for holding the list of featured offers
    private val _featuredOffers = MutableLiveData<List<FeaturedOffer>?>()
    val featuredOffers: LiveData<List<FeaturedOffer>?> = _featuredOffers


    // LiveData for holding the list of hot deals offers
    private val _hotDealsOffers = MutableLiveData<List<HotDeal>?>()
    val hotDealsOffers: LiveData<List<HotDeal>?> = _hotDealsOffers

    // LiveData for holding the list of hot deals offers
    private val _partners = MutableLiveData<Resource<BaseResponse<PartnersResponse>>>()
    val partners: LiveData<Resource<BaseResponse<PartnersResponse>>> = _partners

    // LiveData for holding the list of hot deals offers
    private val _updateDeviceToken = MutableLiveData<Resource<BaseResponse<String>>>()
    val updateDeviceToken: LiveData<Resource<BaseResponse<String>>> = _updateDeviceToken

    // LiveData for holding the active order details
    private val _activeOrder = MutableLiveData<ActiveOrder?>()
    val activeOrder: LiveData<ActiveOrder?> = _activeOrder

    // LiveData for controlling the shimmer UI during loading
    private val _showShimmer = MutableLiveData(true)
    val showShimmer: LiveData<Boolean> = _showShimmer

    // LiveData for holding the user's selected location address
    private val _addressMutableLiveData =
        MutableLiveData(baseApplication.getString(R.string.select_location))
    val addressLiveData: LiveData<String> = _addressMutableLiveData

    // Location utility instance for handling location-related tasks
    private var locationUtil: LocationUtil?=null

    /**
     * Sets the LocationUtil instance used for location operations.
     *
     * @param myLocationUtil An instance of LocationUtil.
     */
    fun setLocationUtil(myLocationUtil: LocationUtil) {
        locationUtil = myLocationUtil
    }


    /**
     * Fetches home screen data by making a request to the home API, based on the provided [HomeRequest].
     * Updates the LiveData with the result, including featured offers, hot deals, and active orders.
     */
    private fun getHomeResult(homeRequest: HomeRequest) {
        val flow = createHomeResultRequest(homeRequest)
        apply(flow = flow, maxRetries = 2, onError = { error ->
            Log.d("TAG", "getHomeResult: ${error.localizedMessage}")
            _homeResultMutableLiveData.value =
                Resource.Error("No Internet Connection")
        }, onSuccess = { resource ->
            _homeResultMutableLiveData.value = resource
        })
    }

    /**
     * Creates a flow to request the home screen data, handles the response and updates LiveData.
     *
     * @param homeRequest The request object containing user's location.
     * @return A Flow that represents the request and response.
     */
    private fun createHomeResultRequest(homeRequest: HomeRequest): Flow<Resource<BaseResponse<HomeResponse>>> =
        channelFlow {
            send(Resource.Loading())
            val homeResult = mainUseCase.homeUseCase.getHomeDetails(homeRequest)
            homeResult.handleResponse(onSuccess = {
                send(Resource.Success(homeResult))
                // Update featured offers, hot deals, and active order data
                _featuredOffers.value = homeResult.result.data?.featuredOffers
                _hotDealsOffers.value = homeResult.result.data?.hotDeals
                _activeOrder.value = homeResult.result.data?.activeOrder
                _showShimmer.value = false
            }, onError = { message, error ->
                send(Resource.Error(message, error))
            }, onUnauthenticated = {
                send(Resource.UnAuthenticated())
            })
        }

    /**
     * Checks whether location permission is granted.
     * If granted, fetches the user's location and address.
     * If not granted, requests the user for permission and proceeds accordingly.
     */
    fun checkLocationPermission() {
        viewModelScope.launch {
            fetchCurrentAddress().collect {
                if (it?.lat?.isEmpty()?.not() == true) {
                    // Fetch home result if location is available
                    getHomeResult(
                        HomeRequest(
                            userLat = it.lat,
                            userLon = it.lng
                        )
                    )
                    isLocationGranted.value = true
                } else {
                    // Request location permission if not granted
                    locationUtil?.askUserForLocationPermission(onPermissionDenied = {
                        isLocationGranted.value = false
                        getHomeResult(HomeRequest("", "")) // Proceed with empty location data
                    }, onPermissionGranted = {
                        isLocationGranted.value = true
                        getLocationAndAddress() // Fetch location and address
                    })
                }
            }
        }
    }

    /**
     * Retrieves the user's current location and performs reverse geocoding to obtain the address.
     * Updates the address LiveData and saves the location address in local storage.
     */
    private fun getLocationAndAddress() {
        locationUtil?.getCurrentLocation(onLocationReceived = { latitude, longitude ->
            // Fetch home data based on the user's location
            getHomeResult(HomeRequest(latitude.toString(), longitude.toString()))
            locationUtil?.reverseGeocode(latitude, longitude, onAddressReceived = { address ->
                // Update the address LiveData and save it locally
                _addressMutableLiveData.value = address
                viewModelScope.launch {
                    saveAddress(
                        UserLocationAddress(
                            address, latitude.toString(), longitude.toString()
                        )
                    )
                }
            }, onError = {
                // Proceed with empty location data if reverse geocoding fails
                getHomeResult(HomeRequest("", ""))
            })
        }, onError = {
            // Proceed with empty location data if location fetch fails
            getHomeResult(HomeRequest("", ""))
        })
    }


    fun getPartners() {
        val flow = createPartnersResultRequest()
        apply(
            flow = flow,
            maxRetries = 2,
            onError = { error ->
                _partners.value =
                    Resource.Error("No Internet Connection")
            },
            onSuccess = { resource ->
                _partners.value = resource
            }
        )
    }

    private fun createPartnersResultRequest(): Flow<Resource<BaseResponse<PartnersResponse>>> =
        channelFlow {
            send(Resource.Loading())
            val locationResult = mainUseCase.homeUseCase.getPartners()
            locationResult.handleResponse(
                onSuccess = {
                    send(Resource.Success(locationResult))
                },
                onError = { message, error ->
                    send(Resource.Error(message, error))
                }, onUnauthenticated = {
                    send(Resource.UnAuthenticated())
                }
            )
        }




    fun updateDeviceToken(updateTokeRequest: UpdateTokeRequest) {
        val flow = createUpdateDeviceTokenResultRequest(updateTokeRequest)
        apply(
            flow = flow,
            maxRetries = 2,
            onError = { error ->
                _updateDeviceToken.value =
                    Resource.Error("No Internet Connection")
            },
            onSuccess = { resource ->
                _updateDeviceToken.value = resource
            }
        )
    }

    private fun createUpdateDeviceTokenResultRequest(updateTokeRequest: UpdateTokeRequest): Flow<Resource<BaseResponse<String>>> =
        channelFlow {
            send(Resource.Loading())
            val locationResult = mainUseCase.homeUseCase.updateDeviceToken(updateTokeRequest)
            locationResult.handleResponse(
                onSuccess = {
                    send(Resource.Success(locationResult))
                },
                onError = { message, error ->
                    send(Resource.Error(message, error))
                }, onUnauthenticated = {
                    send(Resource.UnAuthenticated())
                }
            )
        }

    suspend fun checkIfUserViewedRatingDialog(): Boolean {
        return mainUseCase.homeUseCase.checkIfUserViewedRatingDialog().first()
    }

    suspend fun saveUserRating(isUserRateApp: Boolean) {
        mainUseCase.homeUseCase.saveUserRating(isUserRateApp)
    }


}

