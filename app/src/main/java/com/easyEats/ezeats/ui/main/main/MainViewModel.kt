package com.easyEats.ezeats.ui.main.main

import android.content.IntentFilter
import android.location.LocationManager
import android.net.ConnectivityManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easyEats.ezeats.MyApplication.Companion.baseApplication
import com.easyEats.ezeats.R
import com.easyEats.ezeats.domain.main.model.UserLocationAddress
import com.easyEats.ezeats.domain.main.model.response.compound.CompoundResult
import com.easyEats.ezeats.domain.main.use_case.MainUseCase
import com.easyEats.ezeats.utils.reciveirs.LocationReceiver
import com.easyEats.ezeats.utils.reciveirs.NetworkChangeReceiver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.pow

@HiltViewModel
open class MainViewModel @Inject constructor(
    private val mainUseCase: MainUseCase
) : ViewModel() {

    // LiveData to expose network connectivity status
    private val _isConnected = MutableLiveData(true)
    val isConnected: LiveData<Boolean> get() = _isConnected
    private val _saveAddressResult = MutableLiveData<Boolean>()
    val saveAddressResult: LiveData<Boolean> = _saveAddressResult

    var compoundId = ""
    var compoundName = ""


    private val _compoundIdValue =
        MutableLiveData(baseApplication.getString(R.string.select_location))
    val compoundIdValue: LiveData<String> = _compoundIdValue

    fun clearCompound(){
        compoundId = ""
        _compoundIdValue.value = ""
    }

    // LiveData to expose location permission status
    val isLocationGranted = MutableLiveData<Boolean>()

    val isOpenShareLink = MutableLiveData<HashMap<String, Any>?>()

    // NetworkChangeReceiver to listen for connectivity changes
    private val networkChangeReceiver = NetworkChangeReceiver { isConnected ->
        _isConnected.postValue(isConnected)
    }
    private val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)

    // NetworkChangeReceiver to listen for connectivity changes
    private val locationChangeReceiver = LocationReceiver { isConnected ->
        isLocationGranted.postValue(isConnected)
    }
    private val intentFilter_ = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)

    val userLocationAddressMutableLiveData = MutableStateFlow<UserLocationAddress?>(null)

    private val _compoundList = MutableStateFlow<List<CompoundResult>>(arrayListOf())
    val compoundList = _compoundList.asStateFlow()

    /**
     * Initializes the ViewModel.
     * Registers the NetworkChangeReceiver to listen for connectivity changes.
     */
    init {
        baseApplication.registerReceiver(networkChangeReceiver, intentFilter)
        baseApplication.registerReceiver(locationChangeReceiver, intentFilter_)
        getCompoundLocal()
    }

    fun getCompoundLocal(){
        viewModelScope.launch {
            mainUseCase.mainUseCase.getCompound().collect {
                if(it != null){
                    _compoundList.value = arrayListOf()
                    _compoundList.value = it
                }
            }
        }
    }

    fun fetchCurrentAddress(): StateFlow<UserLocationAddress?> {
        viewModelScope.launch {
            mainUseCase.mainUseCase.getCurrentAddress()
                .collect { address ->
                    userLocationAddressMutableLiveData.value = address
                }
        }
        return userLocationAddressMutableLiveData
    }

    /**
     * Called when the ViewModel is being cleared.
     * Unregisters the NetworkChangeReceiver to prevent memory leaks.
     */
    override fun onCleared() {
        super.onCleared()
        baseApplication.unregisterReceiver(networkChangeReceiver)
        baseApplication.unregisterReceiver(locationChangeReceiver)
    }

    protected fun <T> apply(
        flow: Flow<T>,
        maxRetries: Int = 2,
        delayFactor: Double = 2.0,
        onError: (Throwable) -> Unit,
        onSuccess: (T) -> Unit
    ) {
        viewModelScope.launch {
            flow.retryWhen { cause, attempt ->
                val retryCount = attempt.toInt()
                val delayMillis = (delayFactor.pow(retryCount.toDouble()) * 1000).toLong()
                if (retryCount < maxRetries) {
                    delay(delayMillis)
                    true
                } else {
                    false
                }
            }
                .catch { e ->
                    onError(e)
                }
                .collect { result ->
                    onSuccess(result)
                }
        }
    }

    fun saveAddress(userLocationAddress: UserLocationAddress) {
        viewModelScope.launch {
            // Perform the save operation (background thread)
            mainUseCase.mainUseCase.saveAddress(userLocationAddress)

            // Once the address is saved, post the result (switch to Main thread automatically)
            _saveAddressResult.postValue(true)
        }
    }


    fun saveToke(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            mainUseCase.mainUseCase.saveToken(token)
            mainUseCase.mainUseCase.setUserVerification(false)

            //logout osama
        }
    }

    fun setUserVerification(isUserVerified: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            mainUseCase.mainUseCase.setUserVerification(isUserVerified)
        }
    }

    suspend fun isUserVerified(): Flow<Boolean> {
        return mainUseCase.mainUseCase.isUserVerified()
    }

    fun getToken(): Flow<String> {
        return mainUseCase.userInfoUseCase.getToken()
    }


    fun getAddress() = mainUseCase.mainUseCase.getCurrentAddress()


}
