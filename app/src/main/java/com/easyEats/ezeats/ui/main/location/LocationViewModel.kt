package com.easyEats.ezeats.ui.main.location

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
import com.easyEats.ezeats.domain.main.model.response.location_response.Location
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
class LocationViewModel @Inject constructor(private val mainUseCase: MainUseCase) :
    MainViewModel(mainUseCase) {
    private val _getLocationsResultMutableLiveData =
        MutableLiveData<Resource<BaseResponse<List<Location>>>>()
    val getLocationsResultLiveData: LiveData<Resource<BaseResponse<List<Location>>>> get() = _getLocationsResultMutableLiveData


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

    fun getLocation() {
        val flow = createLocationResultRequest()
        apply(
            flow = flow,
            maxRetries = 2,
            onError = { error ->
                _getLocationsResultMutableLiveData.value =
                    Resource.Error("No Internet Connection")
            },
            onSuccess = { resource ->
                _getLocationsResultMutableLiveData.value = resource
            }
        )
    }

    private fun createLocationResultRequest(): Flow<Resource<BaseResponse<List<Location>>>> =
        channelFlow {
            send(Resource.Loading())
            val locationResult = mainUseCase.locationUseCase.getLocations()
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

    /**
     * Checks if location permission is granted. If granted, it fetches the user's location and address.
     * If not granted, it requests the user to grant location access and proceeds accordingly.
     */
    fun checkLocationPermission() {
        Log.d(TAG, "checkLocationPermission: ")
        locationUtil.askUserForLocationPermission(onPermissionDenied = {
            getLocationAndAddress()
        }, onPermissionGranted = {
            getLocationAndAddress()
        })
    }

    /**
     * Retrieves the user's current location and reverse geocodes it to obtain the address.
     * The location and address data are used to update the home screen content.
     */
    private fun getLocationAndAddress() {
        locationUtil.getCurrentLocation(onLocationReceived = { latitude, longitude ->
            locationUtil.reverseGeocode(latitude, longitude, onAddressReceived = { address ->
                _addressMutableLiveData.value = address
                Log.d(TAG, "getLocationAndAddress:$address ")
                viewModelScope.launch { saveAddress(
                    UserLocationAddress(
                        address, latitude.toString(), longitude.toString()
                    )
                ) }
            }, onError = {
                Log.d(TAG, "getLocationAndAddress error 1:${it} ")
            })
        }, onError = {
            Log.d(TAG, "getLocationAndAddress error 2:${it} ")

        })
    }
}