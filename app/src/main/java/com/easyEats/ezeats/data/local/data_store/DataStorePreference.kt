package com.easyEats.ezeats.data.local.data_store


import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import com.easyEats.ezeats.utils.Constants.ADDRESS_KEY
import com.easyEats.ezeats.utils.Constants.IS_VERIFIED
import com.easyEats.ezeats.utils.Constants.MOBILE_NUMBER_KEY
import com.easyEats.ezeats.utils.Constants.NAME_KEY
import com.easyEats.ezeats.utils.Constants.ONBOARD_KEY
import com.easyEats.ezeats.utils.Constants.TOKEN_KEY
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStorePreference @Inject constructor(private val preferences: DataStore<Preferences>) {

    suspend fun userWatchOnBoardingScreen(userWatchOnBoardingScreen: Boolean) {
        preferences.edit {
            it[preferencesKey<Boolean>(ONBOARD_KEY)] = userWatchOnBoardingScreen
        }
    }

    fun isUserWatchOnBoardingScreen() = preferences.data.map {
        it[preferencesKey<Boolean>(ONBOARD_KEY)] ?: false
    }

    suspend fun saveAddress(address: String) {
        preferences.edit {
            it[preferencesKey<String>(ADDRESS_KEY)] = address
        }
    }

    suspend fun setUserVerification(isVerified: Boolean) {
        preferences.edit {
            it[preferencesKey<Boolean>(IS_VERIFIED)] = isVerified
        }
    }


    suspend fun saveMobileNumber(mobileNumber: String) {
        preferences.edit {
            it[preferencesKey<String>(MOBILE_NUMBER_KEY)] = mobileNumber
        }
    }

    suspend fun saveToken(token: String) {
        preferences.edit {
            it[preferencesKey<String>(TOKEN_KEY)] = token
        }
    }

    fun fetchToken() = preferences.data.map {
        it[preferencesKey<String>(TOKEN_KEY)] ?: ""
    }

    fun isUserVerified() = preferences.data.map {
        it[preferencesKey<Boolean>(IS_VERIFIED)] ?: false
    }

    fun fetchAddress() = preferences.data.map {
        it[preferencesKey<String>(ADDRESS_KEY)] ?: ""
    }

    fun fetchMobileNumber() = preferences.data.map {
        it[preferencesKey<String>(MOBILE_NUMBER_KEY)] ?: ""
    }

    suspend fun saveName(name: String) {
        preferences.edit {
            it[preferencesKey<String>(NAME_KEY)] = name
        }
    }

    fun fetchName() = preferences.data.map {
        it[preferencesKey<String>(NAME_KEY)] ?: ""
    }


}