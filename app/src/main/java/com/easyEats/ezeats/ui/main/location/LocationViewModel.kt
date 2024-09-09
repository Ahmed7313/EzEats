package com.easyEats.ezeats.ui.main.location

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.easyEats.ezeats.MyApplication.Companion.TAG
import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.domain.main.model.response.location_response.Location
import com.easyEats.ezeats.domain.main.model.response.search_response.SearchScreenResponse
import com.easyEats.ezeats.domain.main.use_case.MainUseCase
import com.easyEats.ezeats.ui.main.main.MainViewModel
import com.easyEats.ezeats.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(private val mainUseCase: MainUseCase) :
    MainViewModel(mainUseCase) {
    private val _getLocationsResultMutableLiveData =
        MutableLiveData<Resource<BaseResponse<List<Location>>>>()
    val getLocationsResultLiveData: LiveData<Resource<BaseResponse<List<Location>>>> get() = _getLocationsResultMutableLiveData


    fun getLocation() {
        val flow = createLocationResultRequest()
        apply(
            flow = flow,
            maxRetries = 2,
            onError = { error ->
                _getLocationsResultMutableLiveData.value =
                    Resource.Error(error.localizedMessage ?: "Unknown error")
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
}