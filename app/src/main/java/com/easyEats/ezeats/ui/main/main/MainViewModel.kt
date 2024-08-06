package com.easyEats.ezeats.ui.main.main

import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.easyEats.ezeats.MyApplication.Companion.baseApplication
import com.easyEats.ezeats.utils.reciveirs.NetworkChangeReceiver
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    // LiveData to expose network connectivity status
    private val _isConnected = MutableLiveData<Boolean>()
    val isConnected: LiveData<Boolean> get() = _isConnected

    // NetworkChangeReceiver to listen for connectivity changes
    private val networkChangeReceiver = NetworkChangeReceiver { isConnected ->
        _isConnected.postValue(isConnected)
    }
    private val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)

    /**
     * Initializes the ViewModel.
     * Registers the NetworkChangeReceiver to listen for connectivity changes.
     */
    init {
        baseApplication.registerReceiver(networkChangeReceiver, intentFilter)
    }

    /**
     * Called when the ViewModel is being cleared.
     * Unregisters the NetworkChangeReceiver to prevent memory leaks.
     */
    override fun onCleared() {
        super.onCleared()
        baseApplication.unregisterReceiver(networkChangeReceiver)
    }
}