package com.easyEats.ezeats.data.local.data_store


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.easyEats.ezeats.domain.main.model.UserLocationAddress
import com.easyEats.ezeats.utils.Constants.ADDRESS_KEY
import com.easyEats.ezeats.utils.Constants.IS_VERIFIED
import com.easyEats.ezeats.utils.Constants.MOBILE_NUMBER_KEY
import com.easyEats.ezeats.utils.Constants.NAME_KEY
import com.easyEats.ezeats.utils.Constants.ONBOARD_KEY
import com.easyEats.ezeats.utils.Constants.RATING_KEY
import com.easyEats.ezeats.utils.Constants.TOKEN_KEY
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStorePreference @Inject constructor(private val preferences: DataStore<Preferences>) {

    suspend fun userWatchOnBoardingScreen(userWatchOnBoardingScreen: Boolean) {
        preferences.edit {
            it[booleanPreferencesKey(ONBOARD_KEY)] = userWatchOnBoardingScreen
        }
    }

    fun isUserWatchOnBoardingScreen() = preferences.data.map {
        it[booleanPreferencesKey(ONBOARD_KEY)] ?: false

    }

    suspend fun saveAddress(userLocationAddress: UserLocationAddress) {
        val gson = Gson()
        val userLocationAddressGson = gson.toJson(userLocationAddress)
        preferences.edit { preferences ->
            preferences[stringPreferencesKey(ADDRESS_KEY)] = userLocationAddressGson
        }
    }


    suspend fun setUserVerification(isVerified: Boolean) {
        preferences.edit {
            it[booleanPreferencesKey(IS_VERIFIED)] = isVerified
        }
    }


    suspend fun saveMobileNumber(mobileNumber: String) {
        preferences.edit {
            it[stringPreferencesKey(MOBILE_NUMBER_KEY)] = mobileNumber
        }
    }

    suspend fun saveToken(token: String) {
        preferences.edit {
            it[stringPreferencesKey(TOKEN_KEY)] = token
        }
    }

    fun fetchToken() = preferences.data.map {
        it[stringPreferencesKey(TOKEN_KEY)] ?: ""
    }

    fun isUserVerified() = preferences.data.map {
        it[booleanPreferencesKey(IS_VERIFIED)] ?: false
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
            val userLocationAddressGson = preferences[stringPreferencesKey(ADDRESS_KEY)]
            userLocationAddressGson?.let { gson ->
                Gson().fromJson(gson, UserLocationAddress::class.java)
            }
        }


    fun fetchMobileNumber() = preferences.data.map {
        it[stringPreferencesKey(MOBILE_NUMBER_KEY)] ?: ""
    }

    suspend fun saveName(name: String) {
        preferences.edit {
            it[stringPreferencesKey(NAME_KEY)] = name
        }
    }

    fun fetchName() = preferences.data.map {
        it[stringPreferencesKey(NAME_KEY)] ?: ""
    }

    suspend fun saveUserRating(isUserViewedRatingDialog: Boolean) {
        preferences.edit {
            it[booleanPreferencesKey(RATING_KEY)] = isUserViewedRatingDialog
        }
    }

    fun checkIfUserViewedRatingDialog() = preferences.data.map {
        it[booleanPreferencesKey(RATING_KEY)] ?: false
    }


}