package com.easyEats.ezeats.data.local.data_store


import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import com.easyEats.ezeats.domain.main.model.UserLocationAddress
import com.easyEats.ezeats.utils.Constants.ADDRESS_KEY
import com.easyEats.ezeats.utils.Constants.IS_VERIFIED
import com.easyEats.ezeats.utils.Constants.MOBILE_NUMBER_KEY
import com.easyEats.ezeats.utils.Constants.NAME_KEY
import com.easyEats.ezeats.utils.Constants.ONBOARD_KEY
import com.easyEats.ezeats.utils.Constants.TOKEN_KEY
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
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

    suspend fun saveAddress(userLocationAddress: UserLocationAddress) {
        val gson = Gson()
        val userLocationAddressGson = gson.toJson(userLocationAddress)
        preferences.edit { preferences ->
            preferences[preferencesKey<String>(ADDRESS_KEY)] = userLocationAddressGson
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

//    suspend fun fetchAddress(): UserLocationAddress? {
//        val gson = Gson()
//        val userLocationAddressJson = preferences.data
//            .map { preferences ->
//                preferences[preferencesKey<String>(ADDRESS_KEY)] ?: ""
//            }.firstOrNull()  // This ensures you get the first emitted value
//        return if (userLocationAddressJson.isNullOrEmpty()) {
//            null
//        } else {
//            gson.fromJson(userLocationAddressJson, UserLocationAddress::class.java)
//        }
//    }

    val userLocationAddressFlow: Flow<UserLocationAddress?> = preferences.data
        .map { preferences ->
            val userLocationAddressGson = preferences[preferencesKey<String>(ADDRESS_KEY)]
            userLocationAddressGson?.let { gson ->
                Gson().fromJson(gson, UserLocationAddress::class.java)
            }
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