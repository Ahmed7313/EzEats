package com.easyEats.ezeats.ui.main.main

import android.content.IntentFilter
import android.location.LocationManager
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easyEats.ezeats.MyApplication.Companion.TAG
import com.easyEats.ezeats.MyApplication.Companion.baseApplication
import com.easyEats.ezeats.domain.main.model.UserLocationAddress
import com.easyEats.ezeats.domain.main.use_case.main_use_case.MainActivityUseCase
import com.easyEats.ezeats.utils.reciveirs.LocationReceiver
import com.easyEats.ezeats.utils.reciveirs.NetworkChangeReceiver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.pow

@HiltViewModel
open class MainViewModel @Inject constructor(
    private val mainActivityUseCase: MainActivityUseCase
) : ViewModel() {

    // LiveData to expose network connectivity status
    private val _isConnected = MutableLiveData(true)
    val isConnected: LiveData<Boolean> get() = _isConnected

    // LiveData to expose location permission status
    val isLocationGranted = MutableLiveData<Boolean>()

    // NetworkChangeReceiver to listen for connectivity changes
    private val networkChangeReceiver = NetworkChangeReceiver { isConnected ->
        _isConnected.postValue(isConnected)
    }
    private val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)

    // NetworkChangeReceiver to listen for connectivity changes
    private val locationChangeReceiver = LocationReceiver { isConnected ->
        isLocationGranted.postValue(isConnected)
        Log.d(TAG, "isLocationGranted: $isConnected")
    }
    private val intentFilter_ = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)

    val userLocationAddressMutableLiveData = MutableLiveData<UserLocationAddress>()

    /**
     * Initializes the ViewModel.
     * Registers the NetworkChangeReceiver to listen for connectivity changes.
     */
    init {
        baseApplication.registerReceiver(networkChangeReceiver, intentFilter)
        baseApplication.registerReceiver(locationChangeReceiver, intentFilter_)
        fetchCurrentAddress()

    }

    private fun fetchCurrentAddress() {
        viewModelScope.launch {
            mainActivityUseCase.getCurrentAddress().collect { address ->
                userLocationAddressMutableLiveData.value = address
            }
        }
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
            mainActivityUseCase.saveAddress(userLocationAddress)
        }
    }

}
