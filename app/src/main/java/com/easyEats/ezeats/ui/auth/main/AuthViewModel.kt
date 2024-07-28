package com.easyEats.ezeats.ui.auth.main

import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.easyEats.ezeats.MyApplication.Companion.baseApplication
import com.easyEats.ezeats.utils.NetworkChangeReceiver

/**
 * AuthViewModel is a ViewModel that handles authentication-related logic, including phone number validation and
 * observing network connectivity status.
 *
 * This ViewModel exposes LiveData objects to observe phone number validity and network connectivity status.
 */
open class AuthViewModel : ViewModel() {

    // LiveData to expose phone number validity
    private val _isPhoneNumberValid = MutableLiveData(false)
    val isPhoneNumberValid: LiveData<Boolean> get() = _isPhoneNumberValid

    // Egyptian phone number regex pattern
    private val phoneNumberPattern = Regex("^01[0-2,5]{1}[0-9]{8}$")

    /**
     * Validates the phone number using a regex pattern.
     *
     * @param phoneNumber The phone number to validate
     */
    fun validatePhoneNumber(phoneNumber: String) {
        _isPhoneNumberValid.value = phoneNumberPattern.matches(phoneNumber)
    }

    private val _isOtpValid = MutableLiveData<Boolean>()
    val isOtpValid: LiveData<Boolean> get() = _isOtpValid

    fun validateOtp(otp: String) {
        _isOtpValid.value = otp.length == 4
    }

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
        val context = baseApplication.applicationContext
        context.registerReceiver(networkChangeReceiver, intentFilter)
    }

    /**
     * Called when the ViewModel is being cleared.
     * Unregisters the NetworkChangeReceiver to prevent memory leaks.
     */
    override fun onCleared() {
        super.onCleared()
        val context = baseApplication.applicationContext
        context.unregisterReceiver(networkChangeReceiver)
    }
}

