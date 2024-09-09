package com.easyEats.ezeats.ui.auth.main

import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easyEats.ezeats.MyApplication.Companion.baseApplication
import com.easyEats.ezeats.utils.reciveirs.NetworkChangeReceiver
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.launch
import kotlin.math.pow


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

    // LiveData to expose network connectivity status
    private val _isConnected = MutableLiveData<Boolean>()
    val isConnected: LiveData<Boolean> get() = _isConnected

    private val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)

    private val _isOtpValid = MutableLiveData<Boolean>()
    val isOtpValid: LiveData<Boolean> get() = _isOtpValid

    var isOtpSent = false



    /**
     * Validates the phone number using a regex pattern.
     *
     * @param phoneNumber The phone number to validate
     */
    fun validatePhoneNumber(phoneNumber: String) {
        _isPhoneNumberValid.value = phoneNumberPattern.matches(phoneNumber)
    }

    fun validateOtp(otp: String) {
        _isOtpValid.value = otp.length == 4
    }

    // NetworkChangeReceiver to listen for connectivity changes
    private val networkChangeReceiver = NetworkChangeReceiver { isConnected ->
        _isConnected.postValue(isConnected)
    }

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

    /**
     * Applies retry logic with exponential backoff and error catching to a Flow.
     *
     * @param flow The Flow to apply retry and catch logic to.
     * @param maxRetries The maximum number of retry attempts.
     * @param delayFactor The factor to calculate the delay for exponential backoff.
     * @param onError A lambda to handle errors.
     * @param onSuccess A lambda to handle successful results.
     */
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

    fun isOtpSent(_isOtpSent: Boolean) {
        isOtpSent = _isOtpSent
    }
}

