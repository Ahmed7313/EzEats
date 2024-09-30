package com.easyEats.ezeats.ui.main.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.easyEats.ezeats.MyApplication.Companion.TAG
import com.easyEats.ezeats.MyApplication.Companion.baseApplication
import com.easyEats.ezeats.R
import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.domain.main.model.UserLocationAddress
import com.easyEats.ezeats.domain.main.model.request.home.HomeRequest
import com.easyEats.ezeats.domain.main.model.response.home_response.FeaturedOffer
import com.easyEats.ezeats.domain.main.model.response.home_response.HomeResponse
import com.easyEats.ezeats.domain.main.model.response.home_response.HotDeal
import com.easyEats.ezeats.domain.main.use_case.MainUseCase
import com.easyEats.ezeats.ui.main.main.MainViewModel
import com.easyEats.ezeats.utils.Resource
import com.easyEats.ezeats.utils.location.LocationUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val mainUseCase: MainUseCase) :
    MainViewModel(mainUseCase) {

    private val _homeResultMutableLiveData = MutableLiveData<Resource<BaseResponse<HomeResponse>>>()
    val homeResultLiveData: LiveData<Resource<BaseResponse<HomeResponse>>> get() = _homeResultMutableLiveData

    private val _featuredOffers = MutableLiveData<List<FeaturedOffer>>()
    val featuredOffers: LiveData<List<FeaturedOffer>> = _featuredOffers

    private val _hotDealsOffers = MutableLiveData<List<HotDeal>>()
    val hotDealsOffers: LiveData<List<HotDeal>> = _hotDealsOffers

    private val _showShimmer = MutableLiveData(true)
    val showShimmer: LiveData<Boolean> = _showShimmer

    private val _addressMutableLiveData =
        MutableLiveData(baseApplication.getString(R.string.select_location))
    val addressLiveData: LiveData<String> = _addressMutableLiveData

    private lateinit var locationUtil: LocationUtil

    /**
     * Set the LocationUtil instance used for handling location-related operations.
     */
    fun setLocationUtil(myLocationUtil: LocationUtil) {
        locationUtil = myLocationUtil
    }

    /**
     * Fetches the home screen data based on the provided [HomeRequest].
     * Updates [homeResultLiveData] with the result, including featured offers and hot deals.
     */
    private fun getHomeResult(homeRequest: HomeRequest) {
        val flow = createHomeResultRequest(homeRequest)
        apply(flow = flow, maxRetries = 2, onError = { error ->
            _homeResultMutableLiveData.value =
                Resource.Error("No Internet Connection")
        }, onSuccess = { resource ->
            _homeResultMutableLiveData.value = resource
        })
    }


    /**
     * Creates a flow to request the home screen data, handling the response and errors.
     */
    private fun createHomeResultRequest(homeRequest: HomeRequest): Flow<Resource<BaseResponse<HomeResponse>>> =
        channelFlow {
            send(Resource.Loading())
            val homeResult = mainUseCase.homeUseCase.getHomeDetails(homeRequest)
            homeResult.handleResponse(onSuccess = {
                send(Resource.Success(homeResult))
                _featuredOffers.value = homeResult.result.data?.featuredOffers
                _hotDealsOffers.value = homeResult.result.data?.hotDeals
                _showShimmer.value = false
            }, onError = { message, error ->
                send(Resource.Error(message, error))
            }, onUnauthenticated = {
                send(Resource.UnAuthenticated())
            })
        }

    /**
     * Checks if location permission is granted. If granted, it fetches the user's location and address.
     * If not granted, it requests the user to grant location access and proceeds accordingly.
     */
    fun checkLocationPermission() {
        if (userLocationAddressMutableLiveData.value?.lat.isNullOrEmpty().not()) {
            getHomeResult(
                HomeRequest(
                    userLat = userLocationAddressMutableLiveData.value?.lat.toString(),
                    userLon = userLocationAddressMutableLiveData.value?.lng.toString()
                )
            )
            isLocationGranted.value = true
        } else {
            locationUtil.askUserForLocationPermission(onPermissionDenied = {
                isLocationGranted.value = false
                getHomeResult(HomeRequest("", ""))
            }, onPermissionGranted = {
                isLocationGranted.value = true
                getLocationAndAddress()
            })
        }


    }

    /**
     * Retrieves the user's current location and reverse geocodes it to obtain the address.
     * The location and address data are used to update the home screen content.
     */
    private fun getLocationAndAddress() {
        locationUtil.getCurrentLocation(onLocationReceived = { latitude, longitude ->
            getHomeResult(HomeRequest(latitude.toString(), longitude.toString()))
            locationUtil.reverseGeocode(latitude, longitude, onAddressReceived = { address ->
                _addressMutableLiveData.value = address
                saveAddress(
                    UserLocationAddress(
                        address, latitude.toString(), longitude.toString()
                    )
                )
            }, onError = {
                getHomeResult(HomeRequest(
                    userLat = "",
                    userLon = ""
                ))
            })
        }, onError = {
            getHomeResult(HomeRequest(
                userLat = "",
                userLon = ""
            ))
        })
    }
}
