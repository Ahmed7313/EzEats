package com.easyEats.ezeats.data.local.data_store


import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
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


    suspend fun saveMobileNumber(user: String) {
        preferences.edit {
            it[preferencesKey<String>(MOBILE_NUMBER_KEY)] = user
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


    companion object {
        const val ONBOARD_KEY = "onBoard"
        const val MOBILE_NUMBER_KEY = "mobile"
        const val TOKEN_KEY = "token"
        const val NAME_KEY = "name"
    }
}