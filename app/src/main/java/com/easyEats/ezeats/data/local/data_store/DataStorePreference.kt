package com.easyEats.ezeats.data.local.data_store


import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.easyEats.ezeats.domain.main.model.UserLocationAddress
import com.easyEats.ezeats.domain.main.model.response.compound.CompoundResult
import com.easyEats.ezeats.utils.Constants.ADDRESS_KEY
import com.easyEats.ezeats.utils.Constants.COMPOUND
import com.easyEats.ezeats.utils.Constants.IS_VERIFIED
import com.easyEats.ezeats.utils.Constants.MOBILE_NUMBER_KEY
import com.easyEats.ezeats.utils.Constants.NAME_KEY
import com.easyEats.ezeats.utils.Constants.ONBOARD_KEY
import com.easyEats.ezeats.utils.Constants.RATING_KEY
import com.easyEats.ezeats.utils.Constants.TOKEN_KEY
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import javax.inject.Inject
import kotlin.collections.remove
import kotlin.text.clear

class DataStorePreference @Inject constructor(private val preferences: DataStore<Preferences>) {

    suspend fun userWatchOnBoardingScreen(userWatchOnBoardingScreen: Boolean) {
        preferences.edit {
            it[booleanPreferencesKey(ONBOARD_KEY)] = userWatchOnBoardingScreen
        }
    }

    fun isUserWatchOnBoardingScreen() = preferences.data.map {
        it[booleanPreferencesKey(ONBOARD_KEY)] ?: false

    }

    suspend fun clearUserData() {
        preferences.edit { preferences ->
            preferences.clear() // This clears all data
            // Or if you want to clear specific keys:
            preferences.remove(stringPreferencesKey(TOKEN_KEY))
            preferences.remove(stringPreferencesKey(MOBILE_NUMBER_KEY))
            preferences.remove(stringPreferencesKey(NAME_KEY))
            preferences.remove(booleanPreferencesKey(IS_VERIFIED))
            preferences.remove(stringPreferencesKey(ADDRESS_KEY))
        }
    }


    suspend fun saveAddress(userLocationAddress: UserLocationAddress) {
        val gson = Gson()
        val userLocationAddressGson = gson.toJson(userLocationAddress)
        preferences.edit { preferences ->
            preferences[stringPreferencesKey(ADDRESS_KEY)] = userLocationAddressGson
        }
    }


    suspend fun setUserVerification(isVerified: Boolean) {
        Log.d("setUserVerification", "setUserVerification: $isVerified")
        preferences.edit {
            it[booleanPreferencesKey(IS_VERIFIED)] = isVerified
        }
    }

    suspend fun saveCompound(compound: List<CompoundResult>) {
        preferences.edit {
            val json = Json.encodeToString(compound)
            it[stringPreferencesKey(COMPOUND)] = json
        }
    }

    val userLocationAddressFlow: Flow<UserLocationAddress?> = preferences.data
        .map { preferences ->
            val userLocationAddressGson = preferences[stringPreferencesKey(ADDRESS_KEY)]
            userLocationAddressGson?.let { gson ->
                Gson().fromJson(gson, UserLocationAddress::class.java)
            }
        }

    val getCompound: Flow<List<CompoundResult>?> = preferences.data
        .map { preference ->
            val json = preference[stringPreferencesKey(COMPOUND)]
            json?.let {
                try {
                    Json.decodeFromString<List<CompoundResult>>(it)
                } catch (e: Exception) {
                    null // handle parse error gracefully
                }
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