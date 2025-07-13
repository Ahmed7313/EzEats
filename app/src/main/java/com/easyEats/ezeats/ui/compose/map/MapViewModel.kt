package com.easyEats.ezeats.ui.compose.map

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easyEats.ezeats.domain.main.use_case.MainUseCase
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

//import com.google.android.libraries.maps.model.LatLng

@HiltViewModel
class MapViewModel  @Inject constructor(
    private val mainUseCase: MainUseCase,
): ViewModel() {

    private var _isNewLocationSelected = MutableLiveData(false)
    var isNewLocationSelected : MutableLiveData<Boolean> = _isNewLocationSelected

    private val _title = MutableLiveData<String>()
    val title: LiveData<String>
        get() = _title

    private val _description = MutableLiveData<String>()
    val description: LiveData<String>
        get() = _description

    private var _selectedLat = mutableStateOf(0.0)
    var selectedLat : MutableState<Double> = _selectedLat

    val latlangAdded = mutableStateOf(false)

    private var _selectedLng = mutableStateOf(0.0)
    var selectedLng : MutableState<Double> = _selectedLng


    private var _userCurrentLng = mutableStateOf(0.0)
    var userCurrentLng : MutableState<Double> = _userCurrentLng

    private var _userCurrentLat = mutableStateOf(0.0)
    var userCurrentLat : MutableState<Double> = _userCurrentLat

    val pickUp = LatLng(userCurrentLat.value, userCurrentLng.value)

    private var _locationPermissionGranted = MutableLiveData(false)
    var locationPermissionGranted : LiveData<Boolean> = _locationPermissionGranted


    private val _progressLocation = MutableSharedFlow<Boolean>()
    val progressLocation = _progressLocation.asSharedFlow()

    suspend fun showProgress(){
        _progressLocation.emit(true)
    }
    suspend fun hideProgress(){
        _progressLocation.emit(false)
    }
}